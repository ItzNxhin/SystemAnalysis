import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {

    private BufferedReader reader;

    public Reader() {
        try {
            this.reader = new BufferedReader(new FileReader("database.txt"));
        } catch (IOException e) {
            
        }
    }

    public ArrayList<String> readData() {
        ArrayList<String> lines = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if(!line.isBlank()) lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } 
        return lines;
    }
}   