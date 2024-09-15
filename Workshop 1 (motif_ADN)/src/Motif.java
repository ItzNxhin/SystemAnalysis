import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

public class Motif {

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

    // Método para procesar todas las cadenas en paralelo y acumular los motivos
    public Map.Entry<String, Integer> processChainsInParallel(ArrayList<String> chains, int sSize) {
        int nThreads = chains.size();
        HashMap<String, Integer> globalMotifMap = new HashMap<>();
        ArrayList<Thread> threads = new ArrayList<>();

        // Crear un hilo para procesar cada cadena y acumular los motivos en el mapa global
        for (int i = 0; i < nThreads; i++) {
            final String chain = chains.get(i);

            Thread t = new Thread(() -> {
                accumulateMotifs(chain, sSize, globalMotifMap);
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
        Map.Entry<String, Integer> mostFrequent = getMostFrequentMotif(globalMotifMap);

        return mostFrequent;
    }
}