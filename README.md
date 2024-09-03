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
   javac -d bin src/register/Main.java src/register/Processor.java src/register/RegisterFile.java
2. **Run the Java Program**
   After compiling, run the program using the java command. Replace path/to/register.asm with the path to your register file.

   ```sh
   java -cp bin register.Main path/to/register.asm
