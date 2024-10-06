from collections import defaultdict
import matplotlib.pyplot as plt

access_sequence = [1, 8, 1, 6, 1, 9, 1, 9, 1, 3, 1, 7, 1, 3, 1, 6, 1, 6, 1, 5]

access_times = defaultdict(list)

for i, address in enumerate(access_sequence):
    access_times[address].append(i)

temporal_gaps = []

for address, times in access_times.items():
    if len(times) > 1: 
        temporal_gaps += [times[i+1] - times[i] for i in range(len(times)-1)]

spatial_gaps = [abs(access_sequence[i+1] - access_sequence[i]) for i in range(len(access_sequence)-1)]


plt.figure(figsize=(14, 6))

plt.subplot(1, 2, 1)
if temporal_gaps:
    plt.hist(temporal_gaps, bins=range(1, max(temporal_gaps)+2), color='blue', alpha=0.7, rwidth=0.8)
    plt.title('Temporal Locality: Time Between Revisits')
    plt.xlabel('Time Between Accesses (in terms of access order)')
    plt.ylabel('Frequency')
    plt.grid(True)
else:
    plt.text(0.5, 0.5, 'No temporal gaps detected.', horizontalalignment='center', verticalalignment='center')
    plt.title('Temporal Locality: Time Between Revisits')
    plt.xlabel('Time Between Accesses')
    plt.ylabel('Frequency')

plt.subplot(1, 2, 2)
if spatial_gaps:
    plt.hist(spatial_gaps, bins=range(1, max(spatial_gaps)+2), color='green', alpha=0.7, rwidth=0.8)
    plt.title('Spatial Locality: Address Differences')
    plt.xlabel('Difference Between Consecutive Addresses')
    plt.ylabel('Frequency')
    plt.grid(True)
else:
    plt.text(0.5, 0.5, 'No spatial gaps detected.', horizontalalignment='center', verticalalignment='center')
    plt.title('Spatial Locality: Address Differences')
    plt.xlabel('Difference Between Consecutive Addresses')
    plt.ylabel('Frequency')

plt.tight_layout()
plt.show()
