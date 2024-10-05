package processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java processor.Main <csvFilePath> <traceLength>");
            return;
        }

        String csvFilePath = args[0];
        int traceLength = Integer.parseInt(args[1]);

        List<Transition> transitionTable = CSVParser.parseTransitionTable(csvFilePath);
        if (transitionTable.isEmpty()) {
            System.err.println("Error parsing CSV file.");
            return;
        }

        printTransitionTable(transitionTable);

        // Determine final states
        Set<String> finalStates = new HashSet<>();
        for (Transition transition : transitionTable) {
            if (transition.state.startsWith("F")) {
                finalStates.add(transition.state);
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

        System.out.println("State Access Counts: " + processor.getStateAccessCounts());
        System.out.println("Edge Access Counts: " + processor.getEdgeAccessCounts());
        System.out.println("State Visit Sequence: " + processor.getStateVisitSequence());
        System.out.println("Analysis: " + processor.analyzeTemporalLocality());
    }

    private static void printTransitionTable(List<Transition> transitionTable) {
        System.out.println("Transition Table:");
        for (Transition transition : transitionTable) {
            System.out.print(transition.state + ": ");
            for (SymbolTransition symbolTransition : transition.symbolTransitions) {
                System.out.print(symbolTransition.symbol + " -> " + symbolTransition.nextState + ", ");
            }
            System.out.println();
        }
    }
}

class Transition {
    String state;
    List<SymbolTransition> symbolTransitions;

    Transition(String state) {
        this.state = state;
        this.symbolTransitions = new ArrayList<>();
    }
}

class SymbolTransition {
    int symbol;
    String nextState;

    SymbolTransition(int symbol, String nextState) {
        this.symbol = symbol;
        this.nextState = nextState;
    }
}
