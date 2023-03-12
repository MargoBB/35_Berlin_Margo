import java.io.*;
import java.util.Scanner;

public class BinaryUnitCounter implements Serializable {
    private static final long serialVersionUID = 1L;
    private double[] args;
    private double[] results;
    private transient int binaryUnitCount; // transient поле
    private int lastBinaryUnitCount;
    
    public BinaryUnitCounter(double[] args) {
        this.args = args;
        this.results = new double[args.length];
        this.binaryUnitCount = 0;
        this.lastBinaryUnitCount = 0;
    }
    
    public void calculate() {
        double sum = 0;
        for (int i = 0; i < args.length; i++) {
            results[i] = 1000 * Math.sin(args[i]);
            sum += results[i];
        }
        double mean = sum / args.length;
        String binaryStr = Integer.toBinaryString((int)mean);
        binaryUnitCount = binaryStr.length();
    }

    public void calculateAndUndo() {
        calculate();
        undo();
    }
    
    public int getBinaryUnitCount() {
        return binaryUnitCount;
    }

    public void undo() {
        binaryUnitCount = lastBinaryUnitCount;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nArgs: ");
        for (double arg : args) {
            sb.append(arg).append(" ");
        }
        sb.append("\nResults: ");
        for (double result : results) {
            sb.append(result).append(" ");
        }
        sb.append("\nBinary Unit Count: ").append(binaryUnitCount);
        return sb.toString();
    }
    
    public static void main(String[] args) throws Exception {
        double[] arguments = {0.1, 0.2, 0.3, 0.4};

        BinaryUnitCounter counter = new BinaryUnitCounter(arguments);
        Scanner scanner = new Scanner(System.in);

        boolean done = false;
        while (!done) {
            System.out.print("Enter command (c=calculate, u=undo, q=quit): ");
            String command = scanner.nextLine();
            
            switch (command) {
                case "c":
                    counter.calculate();
                    System.out.println("Binary unit count: " + counter.getBinaryUnitCount());
                    counter.lastBinaryUnitCount = counter.getBinaryUnitCount();
                    break;
                case "u":
                    counter.undo();
                    System.out.println("Undone. Binary unit count: " + counter.getBinaryUnitCount());
                    break;
                case "q":
                    done = true;
                    break;
                default:
                    System.out.println("Invalid command. Try again.");
                    break;
            }
        }
    
        counter.calculateAndUndo();
        
        BinaryUnitCounter buc = new BinaryUnitCounter(arguments);
        buc.calculate();
        System.out.println(buc);
        
        // серіалізація та десеріалізація
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("buc.ser"));
        oos.writeObject(buc);
        oos.close();
        
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("buc.ser"));
        BinaryUnitCounter restoredBuc = (BinaryUnitCounter) ois.readObject();
        ois.close();
        
        System.out.println(restoredBuc);
        
        // перевірка правильності обчислень та серіалізації/десеріалізації
        if (buc.getBinaryUnitCount() == restoredBuc.getBinaryUnitCount()) {
            System.out.println("Serialization/Deserialization Test Passed!");
        } else {
            System.out.println("Serialization/Deserialization Test Failed!");
        }
    }
}