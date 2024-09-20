package processor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java processor.Main <csvFilePath> <traceLength>");
            return;
        }

        String csvFilePath = args[0];
        int traceLength = Integer.parseInt(args[1]);

        Map<String, Map<Integer, String>> transitionTable = CSVParser.parseTransitionTable(csvFilePath);
        if (transitionTable.isEmpty()) {
            System.err.println("Error parsing CSV file.");
            return;
        }

        printTransitionTable(transitionTable);

        // Determine final states
        Set<String> finalStates = new HashSet<>();
        for (String state : transitionTable.keySet()) {
            if (state.startsWith("F")) {
                finalStates.add(state);
            }
        }

        TraceGenerator traceGenerator = new TraceGenerator(transitionTable, finalStates);
        String trace = traceGenerator.generateTrace(traceLength);
        System.out.println("Generated trace: " + trace);

        Processor processor = new Processor(transitionTable);

        boolean result = processor.processInput(trace);

        if (result) {
            System.out.println("String accepted, reached final state.");
        } else {
            System.out.println("String rejected, did not reach final state.");
        }

        Map<String, Integer> stateAccessCounts = processor.getStateAccessCounts();
        System.out.println("State Access Counts: " + stateAccessCounts);

        Map<String, Integer> edgeAccessCounts = processor.getEdgeAccessCounts();
        System.out.println("Edge Access Counts: " + edgeAccessCounts);
    }

    private static void printTransitionTable(Map<String, Map<Integer, String>> transitionTable) {
        System.out.println("Transition Table:");
        for (Map.Entry<String, Map<Integer, String>> entry : transitionTable.entrySet()) {
            String state = entry.getKey();
            Map<Integer, String> transitions = entry.getValue();
            System.out.print(state + ": ");
            for (Map.Entry<Integer, String> transition : transitions.entrySet()) {
                System.out.print(transition.getKey() + " -> " + transition.getValue() + ", ");
            }
            System.out.println();
        }
    }
}
