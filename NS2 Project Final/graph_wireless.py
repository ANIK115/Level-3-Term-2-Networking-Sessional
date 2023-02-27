import pandas as pd
import matplotlib.pyplot as plt
import sys
baseline_data = pd.read_csv(sys.argv[2])
modified_data = pd.read_csv(sys.argv[3])
x_axis_label = ''
red_type = ''
# if sys.argv[2]=='Modified_RED_results.csv':
#     red_type = 'Modified'
# else:
#     red_type = 'Baseline'
if sys.argv[1]=='nn':
    x_axis_label = 'Number of Nodes'
    title_prefix = 'Number of Nodes'
    X = baseline_data.iloc[:5, 0].values
    baseline_throughput = baseline_data.iloc[:5, 7].values
    baseline_delay = baseline_data.iloc[:5, 8].values
    baseline_delivery_ratio = baseline_data.iloc[:5, 9].values
    baseline_drop_ratio = baseline_data.iloc[:5, 10].values
    baseline_energy = baseline_data.iloc[:5, 11].values
    modified_throughput = modified_data.iloc[:5, 7].values
    modified_delay = modified_data.iloc[:5, 8].values
    modified_delivery_ratio = modified_data.iloc[:5, 9].values
    modified_drop_ratio = modified_data.iloc[:5, 10].values
    modified_energy = modified_data.iloc[:5, 11].values
elif sys.argv[1]=='nf':
    x_axis_label = 'Number of Flows'
    title_prefix = 'Number of Flows'
    X = baseline_data.iloc[:5, 1].values
    baseline_throughput = baseline_data.iloc[:5, 7].values
    baseline_delay = baseline_data.iloc[:5, 8].values
    baseline_delivery_ratio = baseline_data.iloc[:5, 9].values
    baseline_drop_ratio = baseline_data.iloc[:5, 10].values
    baseline_energy = baseline_data.iloc[:5, 11].values
    modified_throughput = modified_data.iloc[:5, 7].values
    modified_delay = modified_data.iloc[:5, 8].values
    modified_delivery_ratio = modified_data.iloc[:5, 9].values
    modified_drop_ratio = modified_data.iloc[:5, 10].values
    modified_energy = modified_data.iloc[:5, 11].values
    
elif sys.argv[1]=='np':
    x_axis_label = 'Number of Packets'
    title_prefix = 'Number of Packets'
    X = baseline_data.iloc[:5, 2].values
    baseline_throughput = baseline_data.iloc[:5, 7].values
    baseline_delay = baseline_data.iloc[:5, 8].values
    baseline_delivery_ratio = baseline_data.iloc[:5, 9].values
    baseline_drop_ratio = baseline_data.iloc[:5, 10].values
    baseline_energy = baseline_data.iloc[:5, 11].values
    modified_throughput = modified_data.iloc[:5, 7].values
    modified_delay = modified_data.iloc[:5, 8].values
    modified_delivery_ratio = modified_data.iloc[:5, 9].values
    modified_drop_ratio = modified_data.iloc[:5, 10].values
    modified_energy = modified_data.iloc[:5, 11].values

elif sys.argv[1]=='sp':
    x_axis_label = 'Speed'
    title_prefix = 'Speed'
    X = baseline_data.iloc[:5, 3].values
    baseline_throughput = baseline_data.iloc[:5, 7].values
    baseline_delay = baseline_data.iloc[:5, 8].values
    baseline_delivery_ratio = baseline_data.iloc[:5, 9].values
    baseline_drop_ratio = baseline_data.iloc[:5, 10].values
    baseline_energy = baseline_data.iloc[:5, 11].values
    modified_throughput = modified_data.iloc[:5, 7].values
    modified_delay = modified_data.iloc[:5, 8].values
    modified_delivery_ratio = modified_data.iloc[:5, 9].values
    modified_drop_ratio = modified_data.iloc[:5, 10].values
    modified_energy = modified_data.iloc[:5, 11].values

# plot throughput
plt.scatter(X, baseline_throughput, color='red')
plt.plot(X, baseline_throughput, color='red', label='TCP')
plt.scatter(X, modified_throughput, color='blue')
plt.plot(X, modified_throughput, color='blue', label='TCP INVS')
plt.xlabel(x_axis_label)
plt.ylabel('Throughput(kbits/sec)')
plt.title('Throughput vs ' + title_prefix )
plt.legend()
plt.savefig(red_type + x_axis_label+'_vs_throughput')
plt.close()
#plot average delay
plt.scatter(X, baseline_delay,color='red')
plt.plot(X, baseline_delay, color='red', label='TCP')
plt.scatter(X, modified_delay, color='blue')
plt.plot(X, modified_delay, color='blue', label='TCP INVS')
plt.xlabel(x_axis_label)
plt.ylabel('End to end avg delay(s)')
plt.title('End to end avg delay vs '+title_prefix )
plt.legend()

plt.savefig(red_type + x_axis_label+'_vs_delay')
plt.close()
#plot delivery ratio
plt.scatter(X, baseline_delivery_ratio, color='red')
plt.plot(X, baseline_delivery_ratio, color='red', label='TCP')
plt.scatter(X, modified_delivery_ratio, color='blue')
plt.plot(X, modified_delivery_ratio, color='blue', label='TCP INVS')
plt.xlabel(x_axis_label)
plt.ylabel('Delivery Ratio(%)')
plt.title('Delivery Ratio vs ' + title_prefix )
plt.legend()
plt.savefig(red_type + x_axis_label+'_vs_delivery_ratio')
plt.close()
#plot drop delay
plt.scatter(X, baseline_drop_ratio, color='red')
plt.plot(X, baseline_drop_ratio, color='red', label='TCP')
plt.scatter(X, modified_drop_ratio, color='blue')
plt.plot(X, modified_drop_ratio, color='blue', label='TCP INVS')
plt.xlabel(x_axis_label)
plt.ylabel('Drop Ratio(%)')
plt.title('Drop Ratio vs ' + title_prefix )
plt.legend()
plt.savefig(red_type + x_axis_label+'_vs_drop_ratio')
plt.close()

#plot Average Energy
plt.scatter(X, baseline_energy, color='red')
plt.plot(X, baseline_energy, color='red', label='TCP')
plt.scatter(X, modified_energy, color='blue')
plt.plot(X, modified_energy, color='blue', label='TCP INVS')
plt.xlabel(x_axis_label)
plt.ylabel('Avg Energy Consumption')
plt.title('Avg Energy Consumption vs ' + title_prefix )
plt.legend()
plt.savefig(red_type + x_axis_label+'_vs_energy')
plt.close()

