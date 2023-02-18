#!/bin/bash
echo "Area Size, Number of Nodes, Number of flows, Sent Packets, Dropped Packets, Received Packets, Network throughput, End-to-end Delay, Packet delivery ratio, Packet drop ratio" > varyArea.csv
# area dimension, nodes, flows, trace file, nam file
ns offline.tcl 250 40 20 traceArea250.tr animationArea250.nam
awk -v dim=250 -v nodes=40 -v flows=20 -f parse.awk traceArea250.tr >> varyArea.csv

ns offline.tcl 500 40 20 traceArea500.tr animationArea500.nam
awk -v dim=500 -v nodes=40 -v flows=20 -f parse.awk traceArea500.tr >> varyArea.csv

ns offline.tcl 750 40 20 traceArea750.tr animationArea750.nam
awk -v dim=750 -v nodes=40 -v flows=20 -f parse.awk traceArea750.tr >> varyArea.csv

ns offline.tcl 1000 40 20 traceArea1000.tr animationArea1000.nam
awk -v dim=1000 -v nodes=40 -v flows=20 -f parse.awk traceArea1000.tr >> varyArea.csv

ns offline.tcl 1250 40 20 traceArea1250.tr animationArea1250.nam
awk -v dim=1250 -v nodes=40 -v flows=20 -f parse.awk traceArea1250.tr >> varyArea.csv

echo "Area Size, Number of Nodes, Number of flows, Sent Packets, Dropped Packets, Received Packets, Network throughput, End-to-end Delay, Packet delivery ratio, Packet drop ratio" > varyNodes.csv

ns offline.tcl 500 20 20 traceNodes20.tr animationNodes20.nam
awk -v dim=500 -v nodes=20 -v flows=20 -f parse.awk traceNodes20.tr >> varyNodes.csv

ns offline.tcl 500 40 20 traceNodes40.tr animationNodes40.nam
awk -v dim=500 -v nodes=40 -v flows=20 -f parse.awk traceNodes40.tr >> varyNodes.csv

ns offline.tcl 500 60 20 traceNodes60.tr animationNodes60.nam
awk -v dim=500 -v nodes=60 -v flows=20 -f parse.awk traceNodes60.tr >> varyNodes.csv

ns offline.tcl 500 80 20 traceNodes80.tr animationNodes80.nam
awk -v dim=500 -v nodes=80 -v flows=20 -f parse.awk traceNodes80.tr >> varyNodes.csv

ns offline.tcl 500 100 20 traceNodes100.tr animationNodes100.nam
awk -v dim=500 -v nodes=100 -v flows=20 -f parse.awk traceNodes100.tr >> varyNodes.csv

echo "Area Size, Number of Nodes, Number of flows, Sent Packets, Dropped Packets, Received Packets, Network throughput, End-to-end Delay, Packet delivery ratio, Packet drop ratio" > varyFlows.csv

ns offline.tcl 500 40 10 traceFlows10.tr animationFlows10.nam
awk -v dim=500 -v nodes=40 -v flows=10 -f parse.awk traceFlows10.tr >> varyFlows.csv

ns offline.tcl 500 40 20 traceFlows20.tr animationFlows20.nam
awk -v dim=500 -v nodes=40 -v flows=20 -f parse.awk traceFlows20.tr >> varyFlows.csv

ns offline.tcl 500 40 30 traceFlows30.tr animationFlows30.nam
awk -v dim=500 -v nodes=40 -v flows=30 -f parse.awk traceFlows30.tr >> varyFlows.csv

ns offline.tcl 500 40 40 traceFlows40.tr animationFlows40.nam
awk -v dim=500 -v nodes=40 -v flows=40 -f parse.awk traceFlows40.tr >> varyFlows.csv

ns offline.tcl 500 40 50 traceFlows50.tr animationFlows50.nam
awk -v dim=500 -v nodes=40 -v flows=50 -f parse.awk traceFlows50.tr >> varyFlows.csv

