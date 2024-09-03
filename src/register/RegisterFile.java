package register;

public class RegisterFile {
    private int[] registers;

    public RegisterFile(int[] initialValues) {
        this.registers = initialValues;
    }

    public int read(int index) {
        if (index < 0 || index >= registers.length) {
            throw new ArrayIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return registers[index];
    }
}
