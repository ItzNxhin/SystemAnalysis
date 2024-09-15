import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

public class EntropyMotif {

    // Método para generar todas las combinaciones (motivos) de tamaño sSize en la cadena
    public ArrayList<String> combinations(String chain, int sSize) {
        ArrayList<String> cmb = new ArrayList<>();
        for (int i = 0; i <= chain.length() - sSize; i++) {
            String motif = chain.substring(i, i + sSize);
            if (!cmb.contains(motif)) {
                cmb.add(motif);
            }
        }
        return cmb;
    }

    // Método para contar cuántas veces aparece un motivo en la cadena
    public int countMotif(String motif, String chain) {
        int count = 0;
        int index = 0;
        while ((index = chain.indexOf(motif, index)) != -1) {
            count++;
            index += motif.length();
        }
        return count;
    }

    // Método para calcular la entropía de Shannon de una cadena
    public double calculateShannonEntropy(String chain) {
        // Contar las frecuencias de A, C, G, T
        HashMap<Character, Integer> frequencyMap = new HashMap<>();
        for (char base : chain.toCharArray()) {
            frequencyMap.put(base, frequencyMap.getOrDefault(base, 0) + 1);
        }

        // Calcular la probabilidad de cada base
        double entropy = 0.0;
        int length = chain.length();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            double p = (double) entry.getValue() / length;  // p(x) = frecuencia / longitud total
            entropy -= p * (Math.log(p) / Math.log(2));     // Suma ponderada: -p(x) log2(p(x))
        }

        return entropy; // Retorna la entropía de Shannon
    }

    // Método para generar un HashMap con motivos y sus conteos a nivel global
    public synchronized void accumulateMotifs(String chain, int sSize, HashMap<String, Integer> globalMotifMap) {
        ArrayList<String> motifs = combinations(chain, sSize);
        for (String motif : motifs) {
            int count = countMotif(motif, chain);
            
            // Sincronización en la actualización del mapa global
            synchronized (globalMotifMap) {
                globalMotifMap.put(motif, globalMotifMap.getOrDefault(motif, 0) + count);
            }
        }
    }

    // Método para obtener el motivo más repetido de un HashMap
    public Map.Entry<String, Integer> getMostFrequentMotif(HashMap<String, Integer> listMotif) {
        List<Map.Entry<String, Integer>> sortedMotifs = new ArrayList<>(listMotif.entrySet());

        // Ordenar por valor (frecuencia) de mayor a menor
        Collections.sort(sortedMotifs, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Retornar solo el motivo más frecuente (primero en la lista ordenada)
        return sortedMotifs.get(0);
    }

    // Método para procesar todas las cadenas en paralelo, filtrar por entropía y acumular los motivos
    public void processChainsInParallel(ArrayList<String> chains, int sSize, double entropyThreshold) {
        int nThreads = chains.size();
        HashMap<String, Integer> globalMotifMap = new HashMap<>();
        ArrayList<Thread> threads = new ArrayList<>();

        // Crear un hilo para procesar cada cadena y acumular los motivos en el mapa global
        for (int i = 0; i < nThreads; i++) {
            final String chain = chains.get(i);

            Thread t = new Thread(() -> {
                double entropy = calculateShannonEntropy(chain);

                // Solo procesar cadenas con entropía mayor al umbral
                if (entropy > entropyThreshold) {
                    accumulateMotifs(chain, sSize, globalMotifMap);
                } else {
                    System.out.println("Cadena descartada por baja entropía: " + chain + " | Entropía: " + entropy);
                }
            });

            threads.add(t);
            t.start();
        }

        // Esperar a que todos los hilos terminen
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Obtener el motivo más frecuente globalmente
        if (!globalMotifMap.isEmpty()) {
            Map.Entry<String, Integer> mostFrequent = getMostFrequentMotif(globalMotifMap);
            // Imprimir el motivo más repetido
            System.out.println("Motivo más frecuente global:");
            System.out.println("Motivo: " + mostFrequent.getKey() + " | Frecuencia: " + mostFrequent.getValue());
        } else {
            System.out.println("No se encontraron motivos válidos después de aplicar el filtro de entropía.");
        }
    } 
}
