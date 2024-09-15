import java.util.Map;
public class App {
    public static void main(String[] args) throws Exception {
        double[] per = {0.25,0.5,0.75,1};
        GeneratorBD z = new GeneratorBD();
        z.generateDTB(per, 100,10);  
        Reader r = new Reader();

        Motif m = new Motif();
        Map.Entry<String, Integer> motif =m.processChainsInParallel(r.readData(), 4);
        System.out.println("Motif : " +motif.getKey());
        System.out.println("Frecuency : " +motif.getValue());
    }
}
