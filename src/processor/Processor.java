package processor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
class AddressAccess {
    int address;

    AddressAccess(String operation, int address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("0 0x%08X", address);
    }
}

public class Processor {
    private final List<Transition> transitionTable;
    private String currentState;
    private final List<String> stateVisitSequence;
    private final HashMap<String, Integer> addressMap;
    private final List<AddressAccess> addressAccessSequence; 

    private final int EDGE_SIZE; 
    private boolean debug;

    public Processor(List<Transition> transitionTable, int edgeSize) {
        this(transitionTable, edgeSize, false); 
    }

    public Processor(List<Transition> transitionTable, int edgeSize, boolean debug) {
        this.transitionTable = transitionTable;
        this.EDGE_SIZE = edgeSize; 
        this.debug = debug;

        this.currentState = transitionTable.stream()
            .filter(transition -> transition.state.startsWith("I"))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No initial state found"))
            .state;

        this.stateVisitSequence = new ArrayList<>(); 
        this.addressMap = new HashMap<>();
        this.addressAccessSequence = new ArrayList<>();
        int currentAddress = 0;
        for (Transition transition : transitionTable) {
            addressMap.put(transition.state, currentAddress); 
           
            for (SymbolTransition st : transition.symbolTransitions) {
                String edgeKey = transition.state + "," + st.symbol + "->" + st.nextState;
                addressMap.put(edgeKey, currentAddress);
                currentAddress += EDGE_SIZE; 
            }
        }
    }
    public void saveAddressAccessSequenceToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (AddressAccess access : addressAccessSequence) {
                writer.write(access.toString() + "\n"); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean processInput(String input) {
        String[] symbols = input.split(",");

        for (int i = 0; i < symbols.length; i++) {
            int ch = Integer.parseInt(symbols[i]);
            Transition currentTransition = transitionTable.stream()
                .filter(t -> t.state.equals(currentState))
                .findFirst()
                .orElse(null);

            
            if (currentTransition != null) {
                SymbolTransition nextTransition = currentTransition.symbolTransitions.stream()
                    .filter(st -> st.symbol == ch)
                    .findFirst()
                    .orElse(null);

                if (nextTransition != null) {
                    String edgeKey = currentState + "," + ch + "->" + nextTransition.nextState;
                    int edgeAddress = addressMap.get(edgeKey);
                    String operationType = ""; 
                    addressAccessSequence.add(new AddressAccess(operationType, edgeAddress));

                    currentState = nextTransition.nextState;
                    stateVisitSequence.add(currentState); 
                    if (debug) {
                        System.out.println("Current state after processing '" + ch + "': " + currentState);
                    }
                } else {
                    if (debug) {
                        System.out.println("Transition not found for state '" + currentState + "' and character '" + ch + "'");
                    }
                    return false;
                }
            }

        }

        return currentState.startsWith("F");
    }

    public List<String> getStateVisitSequence() {
        return stateVisitSequence;
    }

    private static void debugPrint(String message, boolean debug) {
        if (debug) {
            System.out.println("[DEBUG] " + message);
        }
    }

    public void printAddressAccessSequence(boolean debug) {
        debugPrint("Address Access Sequence: ", debug);
        for (AddressAccess access : addressAccessSequence) {
            debugPrint(access.toString(), debug);
        }
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
