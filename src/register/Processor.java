package register;

public class Processor {
    private RegisterFile registerFile;
    private int pc;

    public Processor(int[] initialValues) {
        this.registerFile = new RegisterFile(initialValues);
        this.pc = 0;
    }

    public boolean processInput(String input, int count) {
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            int encoding = getEncoding(ch);
            pc = registerFile.read(pc + encoding);
            System.out.println("PC after processing '" + ch + "': " + pc);

            if (pc == 16 && i == input.length() - 1) {
            	System.out.println("States traversed " + count);
                return true;
            }
            count += 1;
        }
        return false;
    }

    private int getEncoding(char ch) {
        switch (ch) {
            case 'a': return 0;
            case 'b': return 1;
            case 'c': return 2;
            case 'd': return 3;
            case 'e': return 4;
            case 'f': return 5;
            case 'g': return 6;
            case 'h': return 7;
            default: throw new IllegalArgumentException("Invalid character: " + ch);
        }
    }
}
