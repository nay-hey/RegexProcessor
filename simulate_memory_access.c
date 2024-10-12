#include <stdio.h>
#include <stdlib.h>

int main() {
    #define MAX_SIZE 256
    int addresses[MAX_SIZE];
    int num_addresses = 0;
    int max_address = -1;

    FILE *file = fopen("address_access_sequence.txt", "r");
    if (!file) {
        fprintf(stderr, "Could not open file\n");
        return 1;
    }

    while (fscanf(file, "%d", &addresses[num_addresses]) != EOF && num_addresses < MAX_SIZE) {
        if (addresses[num_addresses] > max_address) {
            max_address = addresses[num_addresses]; 
        }
        num_addresses++;
    }
    fclose(file);

    if (max_address < 0) {
        fprintf(stderr, "No valid addresses found in the file\n");
        return 1;
    }

    int *memory = (int *)malloc((MAX_SIZE + 1) * sizeof(int));
    if (!memory) {
        fprintf(stderr, "Memory allocation failed\n");
        return 1;
    }
    memory[0]=0;
    
    for (int i = 0; i <num_addresses; i++) {
        memory[addresses[i]] = i;
        volatile int temp = memory[addresses[i]];    
    }
    free(memory);

    return 0;
}
