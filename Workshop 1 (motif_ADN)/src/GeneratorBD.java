import java.io.PrintWriter;
import java.util.Random;
/**
 * This class generates artificial database to save sequences
 */
public class GeneratorBD {

    //Atributes
    private PrintWriter out;
    private int sizeChain;
    private int nSequences;
    private double probA; 
    private double probC;
    private double probG;
    private  double probT;

    /**
     * Contructor 
     * @param sizeChain
     * @param nSequences
     * @param probA
     * @param probC
     * @param probG
     * @param probT
     */
    public GeneratorBD(int sizeChain, int nSequences, double probA, double probC, double probG, double probT){
        this.nSequences = nSequences;
        this.sizeChain = sizeChain;
        this.probA = probA;
        this.probC = probC;
        this.probG = probG;
        this.probT = probT;
        try {
            this.out = new PrintWriter("database.txt");
        } catch (Exception e) {
        }
    }
    
    /**
     * This class genarate and save line per line each sequence (Calling respective method)
     */
    public void generateDTB(){
        for (int i = 0; i < nSequences; i++) {
            String x = generateChain();
            saveString(x, out);
        }
        out.close();
    }

    /**
     * This class generate a secuence with the defined parameters
     * @return
     */
    private String generateChain(){
        double[] percent = posibilities(); 
        String chain =  "";
        Random r = new Random();
        for (int i = 0; i < sizeChain; i++) {
            Double n = r.nextDouble();  //Radom double
            //Where is the random doble in posibiliies and add nucleotide to chain
            if(n<percent[0]) chain+="A";
            else if(n<percent[1]) chain+="C";
            else if(n<percent[2]) chain+="G";
            else chain+="T";
        }
        
        return chain;
    }

    /**
     * This class saves in file a line
     * @param str
     * @param arch
     */
    private void saveString(String str, PrintWriter arch){
        arch.println(str+"\n");
    }

    /**
     * This class convert the posbilites to a double array
     * @return
     */
    private double[] posibilities(){
        double[] p = new double[4];
        p[0]=(probA);
        p[1]=(probA+probC);
        p[2]=(probA+probC+probG);
        p[3]=(probA+probC+probG+probT);
        return p;
    }

}
