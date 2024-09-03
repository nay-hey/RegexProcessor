package register;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Check if a file path argument is provided
        if (args.length < 1) {
            System.err.println("Usage: java register.Main <registerFilePath>");
            return;
        }

        String registerFilePath = args[0];
        int[] registerValues = readRegisterValues(registerFilePath);

        if (registerValues == null) {
            System.err.println("Error reading register values.");
            return;
        }

        Processor processor = new Processor(registerValues);

        // Generate a random input string of 1000 characters
        String input = generateRandomString(1005);
        int count = 0;
        System.out.println("Generated input: " + input);

        boolean result = processor.processInput(input, count);

        if (result) {
            System.out.println("String accepted, reached final state.");
        } else {
            System.out.println("String rejected, did not reach final state.");
        }
    }

    private static int[] readRegisterValues(String fileName) {
        int[] registerValues = new int[24]; // Adjust size if needed

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int index = 0;

            // Skip the header line
            reader.readLine();

            while ((line = reader.readLine()) != null && index < registerValues.length) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String valuePart = parts[1].trim();
                    try {
                        registerValues[index] = Integer.parseInt(valuePart);
                        index++;
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return registerValues;
    }

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        String characters = "abcdefgh"; // Define valid characters

        for (int i = 0; i < length; i++) {
            char randomChar = characters.charAt(random.nextInt(characters.length()));
            sb.append(randomChar);
        }

        sb.append("d");

        return sb.toString();
    }
}
