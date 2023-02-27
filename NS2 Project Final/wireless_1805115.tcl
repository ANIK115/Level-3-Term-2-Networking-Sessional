# simulator
set ns [new Simulator]


# ======================================================================
# Define options

set val(chan)         Channel/WirelessChannel  ;# channel type
set val(prop)         Propagation/TwoRayGround ;# radio-propagation model
set val(ant)          Antenna/OmniAntenna      ;# Antenna type
set val(ll)           LL                       ;# Link layer type
set val(ifq)          Queue/DropTail/PriQueue  ;# Interface queue type
set val(ifqlen)       50                       ;# max packet in ifq
set val(netif)        Phy/WirelessPhy          ;# network interface type
set val(mac)          Mac/802_11               ;# MAC type
set val(rp)           DSDV                     ;# DSDV routing protocol 
set val(nn)           [expr [lindex $argv 0]]  ;# number of mobilenodes
# =======================================================================


#=======================Energy Model=====================================

set val(energymodel_11)         EnergyModel		;
set val(initialenergy_11)       1000            ;# Initial energy in Joules

set val(idlepower_11)			869.4e-3		;#LEAP (802.11g)
set val(rxpower_11)				1560.6e-3		;#LEAP (802.11g)
set val(txpower_11)				1679.4e-3		;#LEAP (802.11g)
set val(sleeppower_11)			37.8e-3			;#LEAP (802.11g)
set val(transitionpower_11)		176.695e-3		;#LEAP (802.11g)
set val(transitiontime_11)		2.36			;#LEAP (802.11g)

#========================================================================


#================================ Others ================================
Mac/802_11 set dataRate_ 11Mb
Mac/802_11 set syncFlag_ 1
Mac/802_11 set dutyCycle_ tcp_interval

#set area dimension
set dimension 500

#set packet rate
set tcp_pckt_rate [lindex $argv 2]
puts "$tcp_pckt_rate"

#set speed
set speed [lindex $argv 3]

# trace file
set trace_file [open [lindex $argv 4] w]
$ns trace-all $trace_file

# nam file
set nam_file [open [lindex $argv 5] w]
$ns namtrace-all-wireless $nam_file $dimension $dimension

# topology: to keep track of node movements
set topo [new Topography]
$topo load_flatgrid $dimension $dimension ;# 500m x 500m area


# general operation director for mobilenodes
create-god $val(nn)


# node configs
# ======================================================================

# $ns node-config -addressingType flat or hierarchical or expanded
#                  -adhocRouting   DSDV or DSR or TORA
#                  -llType	   LL
#                  -macType	   Mac/802_15_4
#                  -propType	   "Propagation/TwoRayGround"
#                  -ifqType	   "Queue/DropTail/PriQueue"
#                  -ifqLen	   50
#                  -phyType	   "Phy/WirelessPhy"
#                  -antType	   "Antenna/OmniAntenna"
#                  -channelType    "Channel/WirelessChannel"
#                  -topoInstance   $topo
#                  -energyModel    "EnergyModel"
#                  -initialEnergy  (in Joules)
#                  -rxPower        (in W)
#                  -txPower        (in W)
#                  -agentTrace     ON or OFF
#                  -routerTrace    ON or OFF
#                  -macTrace       ON or OFF
#                  -movementTrace  ON or OFF

# ======================================================================

$ns node-config -adhocRouting $val(rp) \
                -llType $val(ll) \
                -macType $val(mac) \
                -ifqType $val(ifq) \
                -ifqLen $val(ifqlen) \
                -antType $val(ant) \
                -propType $val(prop) \
                -phyType $val(netif) \
                -topoInstance $topo \
                -channelType $val(chan) \
                -agentTrace ON \
                -routerTrace ON \
                -macTrace OFF \
                -movementTrace OFF \
			    -energyModel $val(energymodel_11) \
			    -idlePower $val(idlepower_11) \
			    -rxPower $val(rxpower_11) \
			    -txPower $val(txpower_11) \
          		-sleepPower $val(sleeppower_11) \
          		-transitionPower $val(transitionpower_11) \
			    -transitionTime $val(transitiontime_11) \
			    -initialEnergy $val(initialenergy_11)

expr {srand(63)}

# create nodes
for {set i 0} {$i < $val(nn) } {incr i} {
    set node($i) [$ns node]
    $node($i) random-motion 0       ;# disable random motion

    $node($i) set X_ [expr {int(rand()*($dimension-1))+1}]
    $node($i) set Y_ [expr {int(rand()*($dimension-1))+1}]
    $node($i) set Z_ 0

    $ns initial_node_pos $node($i) 20

    set Xdest [expr {int(rand()*($dimension-1))+1}]
    while {$Xdest == $dimension} {
        set Xdest [expr {int(rand()*($dimension-1))+1}]
    }
    
    set Ydest [expr {int(rand()*($dimension-1))+1}]
    while {$Ydest == $dimension} {
        set Ydest [expr {int(rand()*($dimension-1))+1}]

    }
    # set speed [expr {int(rand()*5)+1}]

    $ns at 1.0 "$node($i) setdest $Xdest $Ydest $speed" 
}


set val(nf) [expr [lindex $argv 1]]

#Traffic

#random source, random sink

for {set index 0} {$index < $val(nf)} {incr index} {
    set src [expr {int(rand()*$val(nn))}]
    set dest [expr {int(rand()*$val(nn))}]

    while {$src == $dest} {
        set src [expr {int(rand()*$val(nn))}]
    }

    #Create Agent
    set tcp [new Agent/TCP]
    set tcp_sink [new Agent/TCPSink]

    #Attach to nodes
    $ns attach-agent $node($src) $tcp
    $ns attach-agent $node($dest) $tcp_sink

    $tcp set maxseq_ $tcp_pckt_rate

    #connect agents
    $ns connect $tcp $tcp_sink
    $tcp set fid_ $index

    #Traffic generator
    set ftp [new Application/FTP]

    #attach to agent
    $ftp attach-agent $tcp

    #start traffic generation
    $ns at 1.0 "$ftp start"
}


# End Simulation

# Stop nodes
for {set i 0} {$i < $val(nn)} {incr i} {
    $ns at 50.0 "$node($i) reset"
}

# call final function
proc finish {} {
    global ns trace_file nam_file
    $ns flush-trace
    close $trace_file
    close $nam_file
}

proc halt_simulation {} {
    global ns
    puts "Simulation ending"
    $ns halt
}

$ns at 50.0001 "finish"
$ns at 50.0002 "halt_simulation"



# Run simulation
puts "Simulation starting"
$ns run

