
public class Outcast {

    private WordNet wordNet;

    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns) {
        int maxLength = -1;
        String result = null;
        for (String nounOuter : nouns) {
            for (String nounInner : nouns) {
                if (nounInner.equals(nounOuter)) {
                    continue;
                }
                int distance = wordNet.distance(nounInner, nounOuter);
                if (distance > maxLength) {
                    maxLength = distance;
                    result = nounInner;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
    }
}
