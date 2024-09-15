import java.util.Map;
public class App {
    public static void main(String[] args) throws Exception {
        //Percents
        double perA = 0.25;
        double perC = 0.25;
        double perG = 0.25;
        double perT = 0.25;
        //Database path
        String filepath = "data/database.txt";
        //Sizes
        int nSequences = 1000;  //Number of sequences
        int size = 5;           //Sequence size
        int sSize = 5;          //Motif size
        GeneratorBD z = new GeneratorBD(filepath, size, nSequences, perA, perC, perG, perT);
        z.generateDTB();  

        Reader r = new Reader(filepath);

        Motif m = new Motif(r.readData(), sSize);
        Map.Entry<String, Integer> motif =m.mofitMultiThread();
        System.out.println("Motif : " +motif.getKey());
        System.out.println("Frecuency : " +motif.getValue());
    }
}
