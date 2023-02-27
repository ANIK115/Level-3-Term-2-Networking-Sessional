#!/bin/bash
echo "Nodes, Flows, Packets Per Second, Speed, Sent Packets, Dropped Packets, Received Packets, Network throughput, End-to-end Delay, Packet delivery ratio, Packet drop ratio, Energy consumption" > varyNodes.csv

ns wireless_1805115.tcl 20 20 200 10 traceNodes20.tr animationNodes20.nam
awk -v nodes=20 -v flows=20 -v packets=200 -v speed=10 -f parse.awk traceNodes20.tr >> varyNodes.csv

ns wireless_1805115.tcl 40 20 200 10 traceNodes40.tr animationNodes40.nam
awk -v nodes=40 -v flows=20 -v packets=200 -v speed=10 -f parse.awk traceNodes40.tr >> varyNodes.csv

ns wireless_1805115.tcl 60 20 200 10 traceNodes60.tr animationNodes60.nam
awk -v nodes=60 -v flows=20 -v packets=200 -v speed=10 -f parse.awk traceNodes60.tr >> varyNodes.csv

ns wireless_1805115.tcl 80 20 200 10 traceNodes80.tr animationNodes80.nam
awk -v nodes=80 -v flows=20 -v packets=200 -v speed=10 -f parse.awk traceNodes80.tr >> varyNodes.csv

ns wireless_1805115.tcl 100 20 200 10 traceNodes100.tr animationNodes100.nam
awk -v nodes=100 -v flows=20 -v packets=200 -v speed=10 -f parse.awk traceNodes100.tr >> varyNodes.csv

echo "Nodes, Flows, Packets Per Second, Speed, Sent Packets, Dropped Packets, Received Packets, Network throughput, End-to-end Delay, Packet delivery ratio, Packet drop ratio, Energy consumption" > varyFlows.csv

ns wireless_1805115.tcl 40 10 200 10 traceFlows10.tr animationFlows10.nam
awk -v nodes=40 -v flows=10 -v packets=200 -v speed=10 -f parse.awk traceFlows10.tr >> varyFlows.csv

ns wireless_1805115.tcl 40 20 200 10 traceFlows20.tr animationFlows20.nam
awk -v nodes=40 -v flows=20 -v packets=200 -v speed=10 -f parse.awk traceFlows20.tr >> varyFlows.csv

ns wireless_1805115.tcl 40 30 200 10 traceFlows30.tr animationFlows30.nam
awk -v nodes=40 -v flows=30 -v packets=200 -v speed=10 -f parse.awk traceFlows30.tr >> varyFlows.csv

ns wireless_1805115.tcl 40 40 200 10 traceFlows40.tr animationFlows40.nam
awk -v nodes=40 -v flows=40 -v packets=200 -v speed=10 -f parse.awk traceFlows40.tr >> varyFlows.csv

ns wireless_1805115.tcl 40 50 200 10 traceFlows50.tr animationFlows50.nam
awk -v nodes=40 -v flows=50 -v packets=200 -v speed=10 -f parse.awk traceFlows50.tr >> varyFlows.csv


echo "Nodes, Flows, Packets Per Second, Speed, Sent Packets, Dropped Packets, Received Packets, Network throughput, End-to-end Delay, Packet delivery ratio, Packet drop ratio, Energy consumption" > varyPackets.csv

ns wireless_1805115.tcl 40 20 100 10 tracePackets100.tr animationPackets100.nam
awk -v nodes=40 -v flows=20 -v packets=100 -v speed=10 -f parse.awk tracePackets100.tr >> varyPackets.csv

ns wireless_1805115.tcl 40 20 200 10 tracePackets200.tr animationPackets200.nam
awk -v nodes=40 -v flows=20 -v packets=200 -v speed=10 -f parse.awk tracePackets200.tr >> varyPackets.csv

ns wireless_1805115.tcl 40 20 300 10 tracePackets300.tr animationPackets300.nam
awk -v nodes=40 -v flows=20 -v packets=300 -v speed=10 -f parse.awk tracePackets300.tr >> varyPackets.csv

ns wireless_1805115.tcl 40 20 400 10 tracePackets400.tr animationPackets400.nam
awk -v nodes=40 -v flows=20 -v packets=400 -v speed=10 -f parse.awk tracePackets400.tr >> varyPackets.csv

ns wireless_1805115.tcl 40 20 500 10 tracePackets500.tr animationPackets500.nam
awk -v nodes=40 -v flows=20 -v packets=500 -v speed=10 -f parse.awk tracePackets500.tr >> varyPackets.csv


echo "Nodes, Flows, Packets Per Second, Speed, Sent Packets, Dropped Packets, Received Packets, Network throughput, End-to-end Delay, Packet delivery ratio, Packet drop ratio, Energy consumption" > varySpeed.csv

ns wireless_1805115.tcl 40 20 200 5 traceSpeed5.tr animationSpeed5.nam
awk -v nodes=40 -v flows=20 -v packets=200 -v speed=5 -f parse.awk traceSpeed5.tr >> varySpeed.csv

ns wireless_1805115.tcl 40 20 200 10 traceSpeed10.tr animationSpeed10.nam
awk -v nodes=40 -v flows=20 -v packets=200 -v speed=10 -f parse.awk traceSpeed10.tr >> varySpeed.csv

ns wireless_1805115.tcl 40 20 200 15 traceSpeed15.tr animationSpeed15.nam
awk -v nodes=40 -v flows=20 -v packets=200 -v speed=15 -f parse.awk traceSpeed15.tr >> varySpeed.csv

ns wireless_1805115.tcl 40 20 200 20 traceSpeed20.tr animationSpeed20.nam
awk -v nodes=40 -v flows=20 -v packets=200 -v speed=20 -f parse.awk traceSpeed20.tr >> varySpeed.csv

ns wireless_1805115.tcl 40 20 200 25 traceSpeed25.tr animationSpeed25.nam
awk -v nodes=40 -v flows=20 -v packets=200 -v speed=25 -f parse.awk traceSpeed25.tr >> varySpeed.csv

