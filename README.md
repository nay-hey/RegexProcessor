# DFA Processor and Locality Analysis

This project simulates a Deterministic Finite Automaton (DFA) and analyzes cache locality performance based on different configurations. 

The project includes:
1. **DFA State Transition Definition**: Defines states and transitions in a CSV format.
2. **Trace Generator**: Generates a sequence of state transitions (trace).
3. **DFA Processor**: Simulates the DFA processing of the trace.
4. **Cache Locality Analysis**: Assesses static and dynamic locality based on DFA processing.

---

## 1. DFA State Transition CSV

The DFA structure is defined in a CSV file where:
   - Each row represents a state (e.g., `I1`, `S2`, `F3`).
   - Each column represents a transition on a specific input symbol (e.g., `p1`, `p2`).
   - Each cell shows the next state for a specific state-input pair.

Example CSV file:

```csv
states,p1,p2,p3,p1_p2,p1_p3,p2_p3,p1_p2_p3
I1,S2,S2,S2,F3,S2,S2,S2
S2,I1,I1,I1,I1,F3,I1,I1
F3,F3,F3,F3,F3,F3,F3,F3
```

---

## 2. Trace Generator

The **TraceGenerator** generates a random sequence of state transitions for cache analysis. 

## How to Run the Program

### Step 1: Compile the Java Program

1. Compile `TraceGenerator.java` to generate the trace:
   ```sh
   javac -d bin TraceGenerator.java  
   ```

2. Run `TraceGenerator` by providing the CSV file and the desired length of the trace:
   ```sh
   java -cp bin TraceGenerator <csv_file_path> <trace_length> <output_file_path> <debug>
   ```
   - **Example**:
     ```sh
     java -cp bin TraceGenerator dfa_states.csv 1000 output_trace.txt 2 true
     ```
   - This will output a trace string of symbols, e.g., 2,3,8,1,1,8,8,7,7,4 with 1000 transitions based on `dfa_states.csv`, saving the trace to `output_trace.txt`.

### Step 2: Compile and Run the DFA Processor

1. Compile the DFA-related Java classes (Main, Processor, and CSVParser) into the bin directory:
   ```sh
   javac -d bin src/processor/Main.java src/processor/Processor.java src/processor/CSVParser.java
   ```

2. Run the Main class in the processor package to simulate DFA processing. Pass the CSV file and the generated trace:
   ```sh
   java -cp bin processor.Main <csv_file_path> <trace_file_path> <output_file_path> <edge_size> [maxLength] [debug]
   ```
   - **Example**:
     ```sh
     java -cp bin processor.Main dfa_states.csv output_trace.txt output_trace.din 2 true
     ```

   **Note**:  
   The program expects at least 4 arguments. If fewer than 4 are provided, it will print the following usage message:
   ```sh
   Usage: java processor.Main <csvFilePath> <traceFilePath> <outputFilePath> <edgeSize> [maxLength] [debug]
   ```

   **Parameters**:
   - `<csv_file_path>`: Path to the CSV file defining the DFA transitions.
   - `<trace_file_path>`: Path to the trace file generated by `TraceGenerator`.
   - `<output_file_path>`: Path where the output of DFA processing will be saved.
   - `<edge_size>`: Memory required for each DFA transition.
   - `[maxLength]`: Optional. Maximum length of the trace (defaults to `1000` if not provided).
   - `[debug]`: Optional. Enable detailed logs (`true` or `false`).

   **Output**:
   - The DFA simulation generates:
     - **State Visit Sequence** based on DFA transitions.
     - **Address Access Sequence** saved to `<output_file_path>`.

### Note: Debug Mode
- **Enabling Debug Mode**: Use `true` for detailed logs such as state transitions, address access, and symbol processing.
- **Disabling Debug Mode**: Set `<debug>` to `false` or omit the flag.

---

## Locality Analysis with Dinero

After generating the **Address Access Sequence** file (`output_trace.din`), run **DineroIV** to analyze cache locality.

### Run the DineroIV Simulation

   ```sh
   ./dineroIV -l1-usize 4 -l1-ubsize 4 -l1-uassoc 1 -l1-urepl l -informat d < output_trace.din > results.txt
   ```
   - **Parameters**:
     - `-l1-usize`: Cache size (e.g., `16K`)
     - `-l1-ubsize`: Block size (e.g., `4`, `8`, `16` bytes)
     - `-l1-uassoc`: Cache associativity (`1` for direct-mapped, etc.)
     - `-l1-urepl`: Replacement policy (`l` for LRU)

---

## Example Output

Sample output for running the simulation:

```plaintext
Transition Table:
S1: 1 -> S2, 0 -> S3
S2: 1 -> S2, 0 -> S3

Provided trace: 2,3,8,1,1,8,8,7,7,4
Address Access Sequence:
0 0x00000004
0 0x00000008
0 0x00000010

State Visit Sequence: [S1, S2, F1]
Address access sequence saved to: output_trace.din
```

---

## Cache Locality Analysis

This analysis assesses the DFA's cache locality performance based on:
- **Edge Size**: The memory required for each transition.
- **Trace Pattern**: The sequence of transitions affects how well cache performs.

Analyze locality by:
1. **Adjusting Edge Size** to simulate different memory requirements.
2. **Comparing Static vs. Dynamic Locality** based on DFA processing results.

---

## Documentation

Refer to the [Project Documentation](https://docs.google.com/document/d/1cPBO43YiXDs8ulJmK5JPkgwBo_k5As54sy0AdXT05PE/edit?usp=sharing) for a full project description.

## Acknowledgments

Special thanks to **Mark D. Hill** for developing **DineroIV**.

