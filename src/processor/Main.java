package processor;

import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private static boolean debug = false;

    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Usage: java processor.Main <csvFilePath> <traceFilePath> <outputFilePath> <edgeSize> [maxLength] [debug]");
            return;
        }
    
        String csvFilePath = args[0];
        String traceFilePath = args[1];
        String outputFilePath = args[2];
        int edgeSize = Integer.parseInt(args[3]);
    
        int maxLength = -1; 
        boolean debug = false;
    
        if (args.length >= 5) {
            try {
                maxLength = Integer.parseInt(args[4]);
            } catch (NumberFormatException e) {
                if (args[4].equalsIgnoreCase("debug")) {
                    debug = true; 
                } else {
                    System.err.println("Invalid maxLength argument. It must be an integer or omitted.");
                    return;
                }
            }
        }
    
        if (args.length == 6 && args[5].equalsIgnoreCase("debug")) {
            debug = true;
        }
        List<Transition> transitionTable = CSVParser.parseTransitionTable(csvFilePath);
        if (transitionTable.isEmpty()) {
            System.err.println("Error parsing CSV file.");
            return;
        }
        
        printTransitionTable(transitionTable);

        String trace = readTraceFromFile(traceFilePath);
        if (trace == null || trace.isEmpty()) {
            System.err.println("Error reading trace from file or file is empty.");
            return;
        }

        debugPrint("Provided trace: " + trace);
        Set<String> finalStates = new HashSet<>();
        for (Transition transition : transitionTable) {
            if (transition.state.startsWith("F")) {
                finalStates.add(transition.state);
            }
        }

        Processor processor = new Processor(transitionTable, edgeSize, maxLength, debug);
        if (args.length >= 5 && args[args.length - 1].equalsIgnoreCase("debug")) {
            processor.setDebug(true);
        }
        
        boolean result = processor.processInput(trace);
        processor.printAddressAccessSequence(debug);
        
        processor.saveAddressAccessSequenceToFile(outputFilePath);

        System.out.println("Address access sequence saved to: " + outputFilePath);
        if (result) {
            System.out.println("String accepted, reached final state.");
        } else {
            System.out.println("String rejected, did not reach final state.");
        }

        debugPrint("State Visit Sequence: " + processor.getStateVisitSequence());
    }

    private static void debugPrint(String message) {
        if (debug) {
            System.out.println("[DEBUG] " + message);
        }
    }

    private static String readTraceFromFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath))).trim();
        } catch (IOException e) {
            System.err.println("Error reading trace from file: " + e.getMessage());
            return null;
        }
    }
    private static void printTransitionTable(List<Transition> transitionTable) {
        debugPrint("Transition Table:");
        for (Transition transition : transitionTable) {
            StringBuilder sb = new StringBuilder(transition.state + ": ");
            for (SymbolTransition symbolTransition : transition.symbolTransitions) {
                sb.append(symbolTransition.symbol)
                  .append(" -> ")
                  .append(symbolTransition.nextState)
                  .append(", ");
            }
            debugPrint(sb.toString());
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
