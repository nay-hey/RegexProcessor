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

1. **Compile the Java Files:**
   First, compile the Java source files using the following command. This will place the compiled `.class` files in the `bin` directory.

   ```sh
   javac -d bin src/processor/Main.java src/processor/Processor.java src/processor/CSVParser.java src/processor/TraceGenerator.java
2. **Run the Java Program**
   After compiling, run the program using the java command. Replace path/to/register.asm with the path to your register file.

   ```sh
   java -cp bin processor.Main path/to/dfa.csv length of the trace to be generated

## Documentation

For detailed information about the project, including the DFA structure, simulator functionality, and locality analysis, please refer to the project documentation available at the following Google Docs link:

[Project Documentation](https://docs.google.com/document/d/1cPBO43YiXDs8ulJmK5JPkgwBo_k5As54sy0AdXT05PE/edit?usp=sharing)
