import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class generates the motif but including Shannon Entropy
 */
public class EntropyMotif {

    private ArrayList<String> chains;
    private int sSize;
    private double treshold;

    private long startTime;
    private long finishTime;
    private long executionTime;
    
    public long getExecutionTime() {
        return executionTime;
    }

    public EntropyMotif(ArrayList<String> chains, int sSize, double treshold) {
        this.chains = chains;
        this.sSize = sSize;
        this.treshold = treshold;
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
     * This method calculate shannon Entropy
     * @param chain
     * @return
     */
    public double calculateShannonEntropy(String chain) {
        HashMap<Character, Integer> frequencyMap = new HashMap<>();
        for (char base : chain.toCharArray()) {
            frequencyMap.put(base, frequencyMap.getOrDefault(base, 0) + 1);
        }

        double entropy = 0.0;
        int length = chain.length();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            double p = (double) entry.getValue() / length; 
            entropy -= p * (Math.log(p) / Math.log(2));    
        }

        return entropy; 
    }

    /**
     * This method with all before method generates the motif, filtering chains, return the motif and his frequence
     * @param chains
     * @param sSize
     * @return
     */    
    public Map.Entry<String, Integer> mofitMultiThread() {
        fitro();

        Map.Entry<String, Integer> mostFrequent;
        int nThreads = chains.size();
        HashMap<String, Integer> globalMotifMap = new HashMap<>();
        ArrayList<Thread> threads = new ArrayList<>();

        startTime=System.nanoTime();
        
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

        finishTime = System.nanoTime();

        
        executionTime = finishTime - startTime;

        if(!globalMotifMap.isEmpty())mostFrequent = getMostFrequentMotif(globalMotifMap);
        else return null;

        return mostFrequent;
    }

    private void fitro(){
        ArrayList<String> filtered = new ArrayList<>();
        for(String i : chains){
            double ent = calculateShannonEntropy(i);
            if(ent>treshold) filtered.add(i);
        }
        this.chains=filtered;
    }

}
