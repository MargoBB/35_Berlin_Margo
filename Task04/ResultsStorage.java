import java.io.*;

public class ResultsStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    private double[] results;

    public ResultsStorage(double[] results) {
        this.results = results;
    }

    public double[] getResults() {
        return results;
    }

    public void setResults(double[] results) {
        this.results = results;
    }

    public void save(String filename) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
        oos.writeObject(this);
        oos.close();
    }

    public static ResultsStorage load(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
        ResultsStorage rs = (ResultsStorage) ois.readObject();
        ois.close();
        return rs;
    }
}