# DFA Processor and Locality Analysis

## Project Overview

This project involves the creation of a **Deterministic Finite Automaton (DFA)** and a **DFA Simulator** for processing input strings. Additionally, **locality analysis** is performed on the DFA using both static and dynamic locality concepts. This analysis is integrated with **DineroIV**, a cache simulation tool, to evaluate locality performance.

### Main Project Objectives

### 1. DFA Conversion
- **Manual State Diagram Coding:** Implement the Deterministic Finite Automaton (DFA) by hand-coding the state diagram.
- **Automated DFA Compilation:** Develop a tool or script to automate the creation and compilation of the DFA state diagram.

### 2. Simulator
- **Input Handling:** Process input patterns (strings) using the DFA.
  - **Automaton Processing:** Use the DFA to simulate the processing of the input.
  - **Trace Generation:** Generate and display a trace of symbols, including up to approximately 1000 states, before reaching the final state.

### 3. Locality Analysis
   - **Static Locality:** Evaluates locality using static analysis of DFA states.
   - **Dynamic Locality:** Evaluates dynamic locality by running the trace through the DFA, analyzing real-time state transitions.
   - **Dinero Cache Simulation:** Results from locality analysis are further analyzed using **DineroIV** to measure the cache performance.

## How to Run the Program

### Step 1: Compile the Java Program

1. Compile `TraceGenerator.java` to generate the trace:
   ```sh
   javac -d bin TraceGenerator.java  
2. Run `TraceGenerator` by providing the CSV file and the desired length of the trace:
```sh
java -cp bin TraceGenerator <csv_file_name> <length>
```
This will output a trace string of symbols, e.g., 2,3,8,1,1,8,8,7,7,4.

###  Step 2: Compile and Run the DFA Processor
1. Compile the DFA-related Java classes (Main, Processor, and CSVParser) into the bin directory:

```sh
javac -d bin src/processor/Main.java src/processor/Processor.java src/processor/CSVParser.java
```
2. Run the Main class in the processor package to simulate DFA processing. Pass the CSV file and the generated trace:

```sh
java -cp bin processor.Main <csv_file> <trace>
```
Example trace input:
2,3,8,1,1,8,8,7,7,4

### Note: 
The `<debug>` argument is optional and can be `true` or `false`. If `true`, detailed debugging information will be printed (such as state transitions and address access sequences). If `false`, debugging outputs will be disabled.

3. **Output**:
   - The DFA simulation will generate a **State Visit Sequence** and an **Address Access Sequence** based on the transitions taken by the automaton.
   - The **Address Access Sequence** will be saved to the specified output file.

### Debug Mode

- **Enabling Debug Mode**: Set the `<debug>` argument to `true` when running the program to print detailed logs such as:
   - Transitions processed by the DFA.
   - Address access at each transition step.
   - Symbol-by-symbol processing of the input trace.
  
- **Disabling Debug Mode**: Set the `<debug>` argument to `false` (or omit the argument) to run the simulation without detailed logs.

---

## Locality Analysis with Dinero

### 1. **Run the DineroIV Simulation**:
   - After generating the address access sequence file (e.g., `output_trace.txt`), the data can be fed into **DineroIV** for cache locality analysis.

   - Command to run DineroIV with the generated access sequence:
     ```sh
     dineroIV -l1-usize 16K -informat d < output_trace.txt
     ```
   - This runs a cache simulation and provides cache hit/miss statistics, allowing you to analyze the locality of the DFA in terms of cache performance.

### 2. **Cache Parameters**:
   - You can modify cache parameters in the **DineroIV** command. For example:
     - `-l1-usize`: Level 1 cache size (e.g., 16K)
     - `-informat d`: Data file input format

---

## Debugging and Logging

If you want to enable or disable debugging information such as state transitions and address access sequences:
- **Debug Mode**: Controlled by the `<debug>` argument in the command line. When debug mode is enabled (`true`), it will print additional details such as:
   - Each transition processed.
   - Address access for each state transition.
   - Symbol-by-symbol processing.

---

## Sample Output

Here’s what you can expect when running the simulation:

```plaintext
Transition Table:
S1: 1 -> S2, 0 -> S3, 
S2: 1 -> S2, 0 -> S3, 
...

Provided trace: 2,3,8,1,1,8,8,7,7,4
Address Access Sequence:
0 0x00000004
0 0x00000008
0 0x00000010
...

State Visit Sequence: [S1, S2, F1]
Address access sequence saved to: output_trace.txt
String accepted, reached final state.
```

---

## Documentation

For detailed information about the project, including the DFA structure, simulator functionality, and locality analysis, please refer to the project documentation available at the following Google Docs link:

[Project Documentation](https://docs.google.com/document/d/1cPBO43YiXDs8ulJmK5JPkgwBo_k5As54sy0AdXT05PE/edit?usp=sharing)
