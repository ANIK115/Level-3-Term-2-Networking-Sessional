set ns [new Simulator]

set val(nn) [lindex $argv 0]
set val(nf) [lindex $argv 1]
set val(np) [lindex $argv 2]

puts "Number of nodes: $val(nn)"
puts "Number of flows: $val(nf)"
puts "Number of packets: $val(np)"

set nam_file [open [lindex $argv 4] w]
$ns namtrace-all $nam_file

set trace_file [open [lindex $argv 3] w]
$ns trace-all $trace_file

for {set i 0} {$i < $val(nn)} {incr i} {
    set node($i) [$ns node]
    puts "Node $i"
}

#                 \                              /
#                  \                            /
#                   \                          /  
#                    \                        /
#                 ---- 0 ----------------$val(nn)/2 ----    
#                    /                        \
#                   /                          \
#                  /                            \
#                 /                              \  
# # setup bottle neck link
$ns duplex-link $node(0) $node([expr $val(nn)/2]) 2Mb 10ms DropTail
$ns queue-limit $node(0) $node([expr $val(nn)/2]) 50
$ns queue-limit $node([expr $val(nn)/2]) $node(0) 50
puts "Link 0 -> [expr $val(nn)/2]"
# setup other links
for {set i 1} {$i < [expr $val(nn)/2]} {incr i} {
    $ns duplex-link $node([expr $i]) $node(0) 10Mb 3ms DropTail
    puts "Link1 $i -> 0"
}
for {set i [expr $val(nn)/2 +1]} {$i < $val(nn)} {incr i} {
    $ns duplex-link $node([expr $val(nn)/2]) $node([expr $i]) 10Mb 2ms DropTail
    puts "Link2 $i -> [expr $val(nn)/2]"
}

#setup the seed
expr {srand(61)}

# setup flows
for {set i 0} {$i < $val(nf)} {incr i} {
    set src [expr int(rand()*$val(nn)/2)]
    set dest [expr int(rand()*$val(nn)/2)+$val(nn)/2]
    while {$dest == $src} {
        set dest [expr int(rand()*$val(nn)/2)+$val(nn)/2]
    }
    set tcp_($i) [new Agent/TCP]
    set sink_($i) [new Agent/TCPSink]
    $ns attach-agent $node($src) $tcp_($i)
    $ns attach-agent $node($dest) $sink_($i)

    $tcp_($i) set packetSize_ 1000
    $tcp_($i) set window_ [expr 10 *($val(np) / 100)]

    $ns connect $tcp_($i) $sink_($i)
    $tcp_($i) set fid_ $i

    set ftp_($i) [new Application/FTP]
    $ftp_($i) attach-agent $tcp_($i)


    $ns at 0.0 "$ftp_($i) start"
    $ns at 40.0 "$ftp_($i) stop"
    puts "Flow $i: $src -> $dest"
}

$ns at 50.0 "finish"
proc finish {} {
    global ns trace_file nam_file
    $ns flush-trace  
    close $nam_file
    close $trace_file
    exit 0
}

$ns run
