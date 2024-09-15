import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CSVWriter {

    // Método para guardar datos en un archivo CSV con encabezados dinámicos, permite anexar datos
    public void writeCSV(String filename, List<Map<String, Object>> data, boolean append) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, append))) {
            if (!append) {
                // Escribir el encabezado solo si el archivo no existe o estamos creando uno nuevo
                writer.println("Number of sequence,Size of sequence,Probability of bases(A-C-G-T),Motif,Motif Size,Motif Occurrence,With Shannon Entropy,Time to find motif(ms)");
            }

            // Escribir cada entrada en el CSV
            for (Map<String, Object> entry : data) {
                writer.printf(
                        "%d,%d,%s,%s,%d,%d,%b,%d%n",
                        (int) entry.get("nS"),                 // Número de secuencia
                        (int) entry.get("sS"),                 // Tamaño de la secuencia
                        entry.get("pr").toString(),            // Probabilidades de bases
                        entry.get("motif").toString(),         // Motivo
                        (int) entry.get("mSize"),              // Tamaño del motivo
                        (int) entry.get("mOcurrence"),         // Ocurrencias del motivo
                        (boolean) entry.get("wEntropy"),        // Entropía de Shannon
                        (long) entry.get("time")               // Tiempo para encontrar el motivo
                );
            }

        } catch (IOException e) {
            System.err.println("Error al guardar los datos en el archivo CSV.");
            e.printStackTrace();
        }
    }
}


