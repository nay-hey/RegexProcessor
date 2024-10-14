import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

class SymbolTransition {
    int symbol;
    String nextState;

    public SymbolTransition(int symbol, String nextState) {
        this.symbol = symbol;
        this.nextState = nextState;
    }
}

class Transition {
    String state;
    List<SymbolTransition> symbolTransitions;

    public Transition(String state) {
        this.state = state;
        this.symbolTransitions = new ArrayList<>();
    }
}

public class TraceGenerator {
    private final List<Transition> transitionTable;
    private final Set<String> finalStates;
    private String initialState;
    private static boolean debug = false;

    public TraceGenerator(List<Transition> transitionTable, Set<String> finalStates) {
        this.transitionTable = transitionTable;
        this.finalStates = finalStates;
        this.initialState = transitionTable.stream()
            .filter(transition -> transition.state.startsWith("I"))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No initial state found"))
            .state;
    }

    public String generateTrace(int length) {
        Random random = new Random();
        StringBuilder trace = new StringBuilder(length);
        String[] currentState = {initialState}; 
        int[] chosenSymbol = {-1}; 

        for (int i = 0; i < length; i++) {
            Transition currentTransition = transitionTable.stream()
                .filter(t -> t.state.equals(currentState[0])) 
                .findFirst()
                .orElse(null);

            if (currentTransition == null || currentTransition.symbolTransitions.isEmpty()) {
                break; 
            }

            List<SymbolTransition> transitions = currentTransition.symbolTransitions;
            String nextState = null;

            if (i == length - 1) {
                for (SymbolTransition symbolTransition : transitions) {
                    if (finalStates.contains(symbolTransition.nextState)) {
                        chosenSymbol[0] = symbolTransition.symbol; 
                        nextState = symbolTransition.nextState;
                        break;
                    }
                }

                if (nextState == null) {
                    chosenSymbol[0] = transitions.get(random.nextInt(transitions.size())).symbol;
                    nextState = transitions.stream()
                        .filter(st -> st.symbol == chosenSymbol[0]) 
                        .findFirst()
                        .orElse(null).nextState;
                }
            } else {
                do {
                    chosenSymbol[0] = transitions.get(random.nextInt(transitions.size())).symbol; 
                    nextState = transitions.stream()
                        .filter(st -> st.symbol == chosenSymbol[0]) 
                        .findFirst()
                        .orElse(null).nextState;
                } while (finalStates.contains(nextState));
            }

            if (chosenSymbol[0] != -1) {
                trace.append(chosenSymbol[0]).append(",");
            }
            currentState[0] = nextState; 
        }

        if (trace.length() > 0) {
            trace.setLength(trace.length() - 1); 
        }

        return trace.toString();
    }

    public static List<Transition> parseTransitionTable(String csvFilePath) {
        List<Transition> transitionTable = new ArrayList<>();
        String[] headers;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line = reader.readLine();
            if (line != null) {
                headers = line.split(","); 
            } else {
                return transitionTable;
            }

            HashMap<String, Integer> headerToInt = new HashMap<>();
            for (int i = 1; i < headers.length; i++) {
                headerToInt.put(headers[i].trim(), i);
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == headers.length) {
                    String state = parts[0].trim();
                    Transition transition = new Transition(state);

                    for (int i = 1; i < parts.length; i++) {
                        int symbol = headerToInt.get(headers[i].trim());
                        String nextState = parts[i].trim();
                        transition.symbolTransitions.add(new SymbolTransition(symbol, nextState));
                    }
                    transitionTable.add(transition);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transitionTable;
    }

    public static void main(String[] args) {

        if (args.length >= 4 && args[3].equals("debug")) {
            debug = true;  
        }


        if (args.length < 3) {
            System.out.println("Usage: java TraceGenerator <csvFilePath> <traceLength> <outputFilePath>");
            return;
        }

        String csvFilePath = args[0];
        int traceLength = Integer.parseInt(args[1]);
        String outputFilePath = args[2];

        List<Transition> transitionTable = parseTransitionTable(csvFilePath);

        Set<String> finalStates = new HashSet<>();
        for (Transition transition : transitionTable) {
            if (transition.state.startsWith("F")) {
                finalStates.add(transition.state);
            }
        }

        TraceGenerator generator = new TraceGenerator(transitionTable, finalStates);

        String trace = generator.generateTrace(traceLength);
        debugPrint("Generated Trace: " + trace);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(trace);
            System.out.println("Trace written to file: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void debugPrint(String message) {
        if (debug) {
            System.out.println("[DEBUG] " + message);
        }
    }
}
