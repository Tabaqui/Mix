
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WordNet {

    private SAP localSap;
    private Set<String> searchList;
    private Map<String, Set<Integer>> synsets;

    private WordNet() {

    }

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new NullPointerException();
        }
        In in = new In(hypernyms);
        createDigraph(in);
        in.close();
        in = new In(synsets);
        createSearchList(in);
        in.close();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return searchList;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException();
        }
        return searchList.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateInput(nounA, nounB);
        Set<Integer> aIds = synsets.get(nounA);
        Set<Integer> bIds = synsets.get(nounB);
        return localSap.length(aIds, bIds);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateInput(nounA, nounB);
        Set<Integer> aIds = synsets.get(nounA);
        Set<Integer> bIds = synsets.get(nounB);
        String result = null;
        int ancestorId = localSap.ancestor(aIds, bIds);
        for (Entry<String, Set<Integer>> e : synsets.entrySet()) {
            if (e.getValue().contains(ancestorId)) {
                result = e.getKey();
            }
        }
        return result;
    }

    private void validateInput(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new NullPointerException();
        }
        if (!searchList.contains(nounA) || !searchList.contains(nounB)) {
            throw new IllegalArgumentException();
        }
    }

    private void createDigraph(In in) {
        List<String> hypernyms = new ArrayList<>();
        int size = 0;
        while (in.hasNextLine()) {
            String line = in.readLine();
            hypernyms.add(line);
            int mayBeSize = getMax(line.split(","));
            if (mayBeSize > size) {
                size = mayBeSize;
            }
        }
        in.close();
        Digraph hyps = new Digraph(++size);
        for (String hypernym : hypernyms) {
            String[] lineItems = hypernym.split(",");
            for (int i = 1; i < lineItems.length; i++) {
                hyps.addEdge(Integer.parseInt(lineItems[0]), Integer.parseInt(lineItems[i]));
            }
        }
        localSap = new SAP(hyps);
    }

    private int getMax(String[] hypernyms) {
        int max = 0;
        for (String item : hypernyms) {
            int mayBeSize = Integer.parseInt(item);
            if (mayBeSize > max) {
                max = mayBeSize;
            }
        }
        return max;
    }

    private void createSearchList(In in) {
        synsets = new HashMap<>();
        searchList = new HashSet<>();
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            Integer id = Integer.parseInt(line[0]);
            String[] nouns = line[1].split(" ");
            for (String noun : nouns) {
                if (synsets.containsKey(noun)) {
                    synsets.get(noun).add(id);
                } else {
                    synsets.put(noun, new HashSet<>(Arrays.asList(id)));
                }
            }
            searchList.addAll(Arrays.asList(nouns));
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        WordNet wn = new WordNet();
        wn.createDigraph(in);
    }
}
