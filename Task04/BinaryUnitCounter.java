import java.io.*;
import java.util.Scanner;

public class BinaryUnitCounter implements Serializable{
    private static final long serialVersionUID = 1L;
    private double[] args;
    private double[] results;
    private transient int binaryUnitCount; // transient поле
    private int lastBinaryUnitCount;

    public static String createTable(double[] args, double[] results, int binaryUnitCount) {
        StringBuilder sb = new StringBuilder();
        // Add column headers
        sb.append(String.format("%-10s %-10s\n", "Arg", "Result"));

        // Add rows with data
        for (int i = 0; i < args.length; i++) {
            sb.append(String.format("%-10.2f %-10.2f\n", args[i], results[i]));
        }
        // Add footer with binary unit count
        sb.append(String.format("\nBinary Unit Count: %d", binaryUnitCount));
        return sb.toString();
    }
    
    public BinaryUnitCounter(double[] args) {
        this.args = args;
        this.results = new double[args.length];
        this.binaryUnitCount = 0;
        this.lastBinaryUnitCount = 0;
    }

    public void calculateAndUndo() {
        calculate();
        undo();
    }

    public void undo() {
        binaryUnitCount = lastBinaryUnitCount;
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

        ResultsStorage rs = new ResultsStorage(results);
        try {
            rs.save("results.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        
        Fabricatable fabricator = new BinaryUnitCounterFabricator(arguments);
        BinaryUnitCounter buc = fabricator.fabricate();
        System.out.println(buc);

        int binaryUnitCount = buc.getBinaryUnitCount();

        Display binaryDisplay = DisplayFactory.createDisplay("binary", binaryUnitCount);
        binaryDisplay.display();

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

        ResultsStorage rs = ResultsStorage.load("results.dat");
        buc.results = rs.getResults();

        System.out.println(buc);
                
                // Створення об’єкту, що реалізує інтерфейс ResultsDisplay
                ResultsDisplay resultsDisplay = new ConsoleResultsDisplay();
                
                // Для відображення результатів обчислення використовуємо метод displayResults
                resultsDisplay.displayResults(counter.getBinaryUnitCount(), counter.toString());
                
                String table = BinaryUnitCounter.createTable(counter.args, counter.results, counter.binaryUnitCount);
                System.out.println(table);
            }
        }
        
        // Інтерфейс для відображення результатів
        interface ResultsDisplay {
            void displayResults(int binaryUnitCount, String resultsString);
        }
        
        // Реалізація ResultsDisplay, яка відображає результати на консолі
        class ConsoleResultsDisplay implements ResultsDisplay {
            public void displayResults(int binaryUnitCount, String resultsString) {
                System.out.println("Binary Unit Count: " + binaryUnitCount);
                System.out.println("Results: ");
                System.out.println(resultsString);
            }
    } 
