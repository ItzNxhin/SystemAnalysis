import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;


/**
 * Thsi class generates the motif the database
 */
public class Motif {

    private  ArrayList<String> chains;
    private int sSize;
    public Motif(ArrayList<String> chains, int sSize){
        this.chains = chains;
        this.sSize= sSize;
    }
    /**
     * This method generates all the combinations in a sequence
     * @param chain
     * @param sSize
     * @return
     */
    public ArrayList<String> combinations(String chain) {
        ArrayList<String> cmb = new ArrayList<>();
        for (int i = 0; i <= chain.length() - sSize; i++) {
            String motif = chain.substring(i, i + sSize);
            if (!cmb.contains(motif)) {
                cmb.add(motif);
            }
        }
        return cmb;
    }

    /**
     * This method count how many times are a combination in a sequence
     * @param motif
     * @param chain
     * @return
     */
    public int countMotif(String motif, String chain) {
        int count = 0;
        int index = 0;
        while ((index = chain.indexOf(motif, index)) != -1) {
            count++;
            index += motif.length();
        }
        return count;
    }

    /**
     * This method generates HashMap(Synchronized for each sequence) with all combiantions and frequence
     * @param chain
     * @param sSize
     * @param globalMotifMap
     */
    public synchronized void accumulateMotifs(String chain, HashMap<String, Integer> globalMotifMap) {
        ArrayList<String> motifs = combinations(chain);
        for (String motif : motifs) {
            int count = countMotif(motif, chain);
            
            synchronized (globalMotifMap) {
                globalMotifMap.put(motif, globalMotifMap.getOrDefault(motif, 0) + count);
            }
        }
    }

    /**
     * This method return the combiantion with more frecuence sorting the hashmap
     * @param listMotif
     * @return
     */
    public Map.Entry<String, Integer> getMostFrequentMotif(HashMap<String, Integer> listMotif) {
        List<Map.Entry<String, Integer>> sortedMotifs = new ArrayList<>(listMotif.entrySet());

        Collections.sort(sortedMotifs, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        return sortedMotifs.get(0);
    }

    /**
     * This method with all before method generates the motif, return the motif and his frequence
     * @param chains
     * @param sSize
     * @return
     */
    public Map.Entry<String, Integer> mofitMultiThread() {
        int nThreads = chains.size();
        HashMap<String, Integer> globalMotifMap = new HashMap<>();
        ArrayList<Thread> threads = new ArrayList<>();

        // Thread per chain
        for (int i = 0; i < nThreads; i++) {
            final String chain = chains.get(i);

            Thread t = new Thread(() -> {
                accumulateMotifs(chain, globalMotifMap);
            });

            threads.add(t);
            t.start();
        }

        // Wait all theards
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Map.Entry<String, Integer> mostFrequent = getMostFrequentMotif(globalMotifMap);

        return mostFrequent;
    }
}