# DFA Processor and Locality Analysis

## Project Overview

This project involves creating a **Deterministic Finite Automaton (DFA)** and a **DFA Simulator** to process input patterns. Additionally, the project includes **locality analysis** using both static and dynamic locality concepts, integrated with the **DineroIV** cache simulation tool to evaluate performance.

## How to Run the Program

### Step 1: Compile the Java Program

1. Compile `TraceGenerator.java` to generate the trace:
   ```sh
   javac -d bin TraceGenerator.java  
2. Run `TraceGenerator` by providing the CSV file and the desired length of the trace:
```sh
java -cp bin TraceGenerator <csv_file_path> <trace_length> <output_file_path> <debug>
```
  - **Example**:
     ```sh
     java -cp bin TraceGenerator dfa_states.csv 1000 output_trace.txt 2 true
     ```
   - This will output a trace string of symbols, e.g., 2,3,8,1,1,8,8,7,7,4 with 1000 transitions based on `dfa_states.csv`, saving the trace to `output_trace.txt`

###  Step 2: Compile and Run the DFA Processor
1. Compile the DFA-related Java classes (Main, Processor, and CSVParser) into the bin directory:

```sh
javac -d bin src/processor/Main.java src/processor/Processor.java src/processor/CSVParser.java
```
2. Run the Main class in the processor package to simulate DFA processing. Pass the CSV file and the generated trace:

```sh
java -cp bin processor.Main <csv_file_path> <trace_file_path> <output_file_path> <edge_size> <debug>
```
  - **Example**:
     ```sh
     java -cp bin processor.Main dfa_states.csv output_trace.txt output_trace.din 2 true
     ```

   **Output**:
   - The DFA simulation generates:
     - **State Visit Sequence** based on DFA transitions.
     - **Address Access Sequence** saved to `output_trace.din`.

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

## Documentation

Refer to the [Project Documentation](https://docs.google.com/document/d/1cPBO43YiXDs8ulJmK5JPkgwBo_k5As54sy0AdXT05PE/edit?usp=sharing) for a full project description.

## Acknowledgments

Special thanks to **Mark D. Hill** for developing **DineroIV**.
