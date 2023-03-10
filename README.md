# 35_Berlin_Margo

## Завдання №1 - Класи та об'єкти  

## Умова індивідуального завдання
**1.** Визначити кількість одиниць у двійковому поданні цілої частини
середнього арифметичного значення функції 1000*sin(α) для чотирьох
довільних аргументів.

1. Код програми **"BinaryUnitCounter"** - 

```
import java.io.*;

public class BinaryUnitCounter implements Serializable {
    private static final long serialVersionUID = 1L;
    private double[] args;
    private double[] results;
    private transient int binaryUnitCount; // transient поле
    
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
    
    public static void main(String[] args) throws Exception {
        double[] arguments = {0.1, 0.2, 0.3, 0.4};
        
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
```

2. Результати працюючого коду -

<img src="https://github.com/MargoBB/35_Berlin_Margo/blob/main/src/1.png">

## Завдання №2 - Спадкування

## Умова завдання

**1.** Як основа використовувати вихідний текст проекту попередньої лабораторної роботи. Забезпечити розміщення результатів обчислень уколекції з можливістю збереження/відновлення.
**2.** Використовуючи шаблон проектування Factory Method (Virtual Constructor), розробити ієрархію, що передбачає розширення рахунок додавання
нових відображуваних класів.
**3.** Розширити ієрархію інтерфейсом "фабрикованих" об'єктів, що представляє набір методів для відображення результатів обчислень.
**4.** Реалізувати ці методи виведення результатів у текстовому стані.
**5.** Розробити та реалізувати інтерфейс для "фабрикуючого" методу.

1. Код програми **"BinaryUnitCounter"** -

```
import java.io.*;

public class BinaryUnitCounter implements Serializable{
    private static final long serialVersionUID = 1L;
    private double[] args;
    private double[] results;
    private transient int binaryUnitCount; // transient поле
    
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

                // Створення об’єкту BinaryUnitCounter
                BinaryUnitCounter counter = new BinaryUnitCounter(arguments);
                counter.calculate();
                
                // Створення об’єкту, що реалізує інтерфейс ResultsDisplay
                ResultsDisplay resultsDisplay = new ConsoleResultsDisplay();
                
                // Для відображення результатів обчислення використовуємо метод displayResults
                resultsDisplay.displayResults(counter.getBinaryUnitCount(), counter.toString());
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
```

2. Інші реалізовані класи для цієї програми -

<img src="https://github.com/MargoBB/35_Berlin_Margo/blob/main/src/2.png">

## Завдання №3 - Поліморфізм

## Умова завдання

**1.** За основу використовувати вихідний текст проекту попередньої лабораторної роботи Використовуючи шаблон проектування Factory Method (Virtual Constructor), розширити ієрархію похідними класами, реалізують методи для подання результатів у вигляді текстової таблиці. Параметри відображення таблиці мають визначатися користувачем.
**2.** Продемонструвати заміщення (перевизначення, overriding), поєднання (перевантаження, overloading), динамічне призначення методів (Пізнє зв'язування, поліморфізм, dynamic method dispatch).
**3.** Забезпечити діалоговий інтерфейс із користувачем.
**4.** Розробити клас для тестування основної функціональності.
**5.** Використати коментарі для автоматичної генерації документації засобами javadoc.

1. Код програми **"BinaryUnitCounter"** -

```
import java.io.*;

public class BinaryUnitCounter implements Serializable{
    private static final long serialVersionUID = 1L;
    private double[] args;
    private double[] results;
    private transient int binaryUnitCount; // transient поле

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

                // Створення об’єкту BinaryUnitCounter
                BinaryUnitCounter counter = new BinaryUnitCounter(arguments);
                counter.calculate();
                
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
```

2. Результат працюючої програми -

<img src="https://github.com/MargoBB/35_Berlin_Margo/blob/main/src/3.png">

## Завдання №4 - Обробка колекцій

## Умова завдання

**1.** Реалізувати можливість скасування (undo) операцій (команд).
**2.** Продемонструвати поняття "макрокоманда"
**3.** При розробці програми використовувати шаблон Singletone.
**4.** Забезпечити діалоговий інтерфейс із користувачем.
**5.** Розробити клас для тестування функціональності програми.

1. Код програми **"BinaryUnitCounter"** -

```
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
```

2. Результат працюючої програми -

<img src="https://github.com/MargoBB/35_Berlin_Margo/blob/main/src/4.png">

## Завдання №6 - Паралельне виконання

## Умова завдання

**1.** Продемонструвати можливість паралельної обробки елементів колекції (пошук мінімуму, максимуму, обчислення середнього значення, відбір за критерієм, статистична обробка тощо).
**2.** Управління чергою завдань (команд) реалізувати за допомогою шаблону Worker Thread.

1. Код програми **"BinaryUnitCounter"** -

```
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.*;

public class BinaryUnitCounter implements Serializable, Runnable {
    private static final long serialVersionUID = 1L;
    private double[] args;
    private double[] results;
    private transient int binaryUnitCount; // transient поле
    private int lastBinaryUnitCount;

    public static String createTable(double[] args, double[] results, int binaryUnitCount) {
        StringBuilder sb = new StringBuilder();
        // Додавання заголовків стовпців
        sb.append(String.format("%-10s %-10s\n", "Arg", "Result"));

        // Додавання рядків з даними
        for (int i = 0; i < args.length; i++) {
            sb.append(String.format("%-10.2f %-10.2f\n", args[i], results[i]));
        }
        // Додавання нижнього колонтитулу із кількістю двійкових одиниць
        sb.append(String.format("\nBinary Unit Count: %d", binaryUnitCount));
        return sb.toString();
    }
    
    public void run() {
        // Знаходження мінімального значення
        double min = Double.MAX_VALUE;
        for (int i = 0; i < args.length; i++) {
            if (args[i] < min) {
                min = args[i];
            }
        }
        results[0] = min;
    
        // Знаходження максимального значення
        double max = Double.MIN_VALUE;
        for (int i = 0; i < args.length; i++) {
            if (args[i] > max) {
                max = args[i];
             }
        }
        results[1] = max;
        
        // Обчислення середнього значення
        double sum = 0;
        for (int i = 0; i < args.length; i++) {
            sum += args[i];
        }
        double mean = sum / args.length;
        results[2] = mean;
        
        
        // Встановлення кількість двійкових одиниць
        String binaryStr = Integer.toBinaryString((int)mean);
        binaryUnitCount = binaryStr.length();
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

    public double[] getResults() {
        return results;
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
        
        BinaryUnitCounter buc = new BinaryUnitCounter(arguments);
        
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Future<?> task1 = executor.submit(buc);
        Future<?> task2 = executor.submit(buc);
        Future<?> task3 = executor.submit(buc);

        task1.get();
        task2.get();
        task3.get();

        double[] results = buc.getResults();

        System.out.println("Minimum value: " + results[0]);
        System.out.println("Maximum value: " + results[1]);
        System.out.println("Average value: " + results[2]);
        System.out.println("Binary unit count: " + buc.getBinaryUnitCount());

        executor.shutdown();

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

        int binaryUnitCount = buc.getBinaryUnitCount();

        Display binaryDisplay = DisplayFactory.createDisplay("binary", binaryUnitCount);
        binaryDisplay.display();

        // Серіалізація та десеріалізація
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("buc.ser"));
        oos.writeObject(buc);
        oos.close();
        
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("buc.ser"));
        BinaryUnitCounter restoredBuc = (BinaryUnitCounter) ois.readObject();
        ois.close();
        
        System.out.println(restoredBuc);
        
        // Перевірка правильності обчислень та серіалізації/десеріалізації
        if (buc.getBinaryUnitCount() == restoredBuc.getBinaryUnitCount()) {
            System.out.println("Serialization/Deserialization Test Passed!");
        } else {
            System.out.println("Serialization/Deserialization Test Failed!");
        }

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
```

2. Результат працюючої програми -

<img src="https://github.com/MargoBB/35_Berlin_Margo/blob/main/src/5.png">