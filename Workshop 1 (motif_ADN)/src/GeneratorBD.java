import java.io.PrintWriter;
import java.util.Random;

public class GeneratorBD {

    private PrintWriter out;

    public GeneratorBD(){
        try {
            this.out = new PrintWriter("database.txt");
        } catch (Exception e) {
        }
    }
    
    public void generateDTB(double[] percent, int sizeChain, int nSequences){
        for (int i = 0; i < nSequences; i++) {
            String x = generateChain(percent, sizeChain);
            saveString(x, out);
        }
        out.close();
    }

    public String generateChain(double[] percent, int size){
        String chain =  "";
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            Double n = r.nextDouble();
            if(n<percent[0]) chain+="A";
            else if(n<percent[1]) chain+="C";
            else if(n<percent[2]) chain+="G";
            else chain+="T";
        }
        
        return chain;
    }

    public void saveString(String str, PrintWriter arch){
        arch.println(str+"\n");
    }

}
