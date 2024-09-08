package processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java processor.Main <csvFilePath> <traceLength>");
            return;
        }

        String csvFilePath = args[0];
        int traceLength = Integer.parseInt(args[1]);

        Map<String, Map<Character, String>> transitionTable = CSVParser.parseTransitionTable(csvFilePath);
        if (transitionTable.isEmpty()) {
            System.err.println("Error parsing CSV file.");
            return;
        }

        printTransitionTable(transitionTable);

        Set<String> finalStates = new HashSet<>();
        for (String state : transitionTable.keySet()) {
            if (state.startsWith("F")) {
                finalStates.add(state);
            }
        }

        TraceGenerator traceGenerator = new TraceGenerator(transitionTable, finalStates);
        String trace = traceGenerator.generateTrace(traceLength);
        System.out.println("Generated trace: " + trace);

        Processor processor = new Processor(transitionTable, finalStates);

        boolean result = processor.processInput(trace);

        if (result) {
            System.out.println("String accepted, reached final state.");
        } else {
            System.out.println("String rejected, did not reach final state.");
        }
    }

    private static void printTransitionTable(Map<String, Map<Character, String>> transitionTable) {
        System.out.println("Transition Table:");
        for (Map.Entry<String, Map<Character, String>> entry : transitionTable.entrySet()) {
            String state = entry.getKey();
            Map<Character, String> transitions = entry.getValue();
            System.out.print(state + ": ");
            for (Map.Entry<Character, String> transition : transitions.entrySet()) {
                System.out.print(transition.getKey() + " -> " + transition.getValue() + ", ");
            }
            System.out.println();
        }
    }
}
