# Project Objectives

## 1. DFA Conversion
- **Manual State Diagram Coding:** Implement the Deterministic Finite Automaton (DFA) by hand-coding the state diagram.
- **Automated DFA Compilation:** Develop a tool or script to automate the creation and compilation of the DFA state diagram.

## 2. Simulator
- **Input Handling:** Process input patterns (strings) using the DFA.
  - **Automaton Processing:** Use the DFA to simulate the processing of the input.
  - **Trace Generation:** Generate and display a trace of symbols, including up to approximately 1000 states, before reaching the final state.

## 3. Locality Analysis
- **Static Locality:** Analyze the static locality of the DFA.
- **Dynamic Locality:** Examine the dynamic locality of the DFA.

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


## Documentation

For detailed information about the project, including the DFA structure, simulator functionality, and locality analysis, please refer to the project documentation available at the following Google Docs link:

[Project Documentation](https://docs.google.com/document/d/1cPBO43YiXDs8ulJmK5JPkgwBo_k5As54sy0AdXT05PE/edit?usp=sharing)
