import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryUnitCounter implements Serializable {
    private List<Integer> calculationResults;
    private static final long serialVersionUID = 1L;
    private double[] args;
    private double[] results;
    private transient int binaryUnitCount; // transient поле
    
    public BinaryUnitCounter() {
        this.calculationResults = new ArrayList<>();
    }

    public List<Integer> getCalculationResults() {
        return calculationResults;
    }

    public void saveResults() {
        // код для збереження результатів обчислень у файл
    }

    public void restoreResults() {
        // код для відновлення результатів обчислення з файлу
    }

    public interface ResultDisplay {
        void displayResult(int result);
    }

    public class TextResultDisplay implements ResultDisplay {
        @Override
        public void displayResult(int result) {
            System.out.println("Number of units: " + result);
        }
    }

    public ResultDisplay fabricateResultDisplay() {
        return new TextResultDisplay();
    }

    public BinaryUnitCounter(double[] args) {
        this.args = args;
        this.results = new double[args.length];
        this.binaryUnitCount = 0;
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
    
    public int getBinaryUnitCount() {
        return binaryUnitCount;
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
    
    // Factory Method
    public static BinaryUnitCounter create(double[] args) {
        BinaryUnitCounter buc = new BinaryUnitCounter(args);
        buc.calculate();
        return buc;
    }
    
    public static void main(String[] args) throws Exception {
        double[] arguments = {0.1, 0.2, 0.3, 0.4};
        
        BinaryUnitCounter buc = BinaryUnitCounter.create(arguments);
        System.out.println(buc);
        
        // серіалізація та десеріалізація
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("buc.ser"));
        oos.writeObject(buc);
        oos.close();
        
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("buc.ser"));
        BinaryUnitCounter restoredBuc = (BinaryUnitCounter) ois.readObject();
        ois.close();
        
        System.out.println(restoredBuc);
        
        // перевірка правильності обчислень і серіалізації/десеріалізації
        if (buc.getBinaryUnitCount() == restoredBuc.getBinaryUnitCount()) {
            System.out.println("Serialization/Deserialization Test Passed!");
        } else {
            System.out.println("Serialization/Deserialization Test Failed!");
        }
    }
}