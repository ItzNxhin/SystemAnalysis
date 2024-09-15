import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        // Percents
        double perA = 0.2;
        double perC = 0.3;
        double perG = 0.3;
        double perT = 0.2;
        String pertString = perA + "-" + perC + "-" + perG + "-" + perT;
        // Database path
        String filepath = "data/database.txt";
        String csv = "data/results.csv";
        // Sizes
        int nSequences = 1000; // Number of sequences
        int size = 10; // Sequence size
        int sSize = 3; // Motif size
        // Treshold
        double trh = 1.3;
        GeneratorBD z = new GeneratorBD(filepath, size, nSequences, perA, perC, perG, perT);
        z.generateDTB();

        Reader r = new Reader(filepath);
        ArrayList<String> sequences = r.readData();
        Motif m = new Motif(sequences, sSize);
        EntropyMotif m2 = new EntropyMotif(sequences, sSize, trh);
        Map.Entry<String, Integer> motif = m.mofitMultiThread();
        Map.Entry<String, Integer> motif2 = m2.mofitMultiThread();

        System.out.println("Motif : " + motif.getKey());
        System.out.println("Frecuency : " + motif.getValue());
        if(motif2==null){
            motif2=Map.entry("Null",0);
        }
        System.out.println("Motif : " + motif2.getKey());
        System.out.println("Frecuency : " + motif2.getValue());

        List<Map<String, Object>> data = List.of( Map.of("nS", nSequences, "sS", size, "pr", pertString, "motif", motif.getKey(),
        "mSize", sSize, "mOcurrence", motif.getValue(), "wEntropy", false, "time", m.getExecutionTime()/ 1_000_000),
        Map.of("nS", nSequences, "sS", size, "pr", pertString, "motif", motif2.getKey(),
        "mSize", sSize, "mOcurrence", motif2.getValue(), "wEntropy", true, "time", m2.getExecutionTime()/ 1_000_000)
        );
    

        CSVWriter tableFile = new CSVWriter();
        tableFile.writeCSV(csv, data, true);
    }
}
