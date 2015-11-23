
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SAP {

    private Digraph localG;

    public SAP(Digraph G) {
        localG = new Digraph(G);
    }

    public int length(int v, int w) {
        List<Integer> commonAncestors = getCommonAncestor(v, w);
        BreadthFirstDirectedPaths pathsV = new BreadthFirstDirectedPaths(localG, v);
        BreadthFirstDirectedPaths pathsW = new BreadthFirstDirectedPaths(localG, w);
        int length = Integer.MAX_VALUE;
        for (Integer cmnAncestor : commonAncestors) {
            if (length > pathsV.distTo(cmnAncestor) + pathsW.distTo(cmnAncestor)) {
                length = pathsV.distTo(cmnAncestor) + pathsW.distTo(cmnAncestor);
            }
        }
        if (length == Integer.MAX_VALUE) {
            length = -1;
        }
        return length;
    }

    public int ancestor(int v, int w) {
        List<Integer> commonAncestors = getCommonAncestor(v, w);
        BreadthFirstDirectedPaths pathsV = new BreadthFirstDirectedPaths(localG, v);
        BreadthFirstDirectedPaths pathsW = new BreadthFirstDirectedPaths(localG, w);
        int ancestor = -1;
        int length = Integer.MAX_VALUE;
        for (Integer cmnAncestor : commonAncestors) {
            if (length > pathsV.distTo(cmnAncestor) + pathsW.distTo(cmnAncestor)) {
                length = pathsV.distTo(cmnAncestor) + pathsW.distTo(cmnAncestor);
                ancestor = cmnAncestor;
            }
        }
        return ancestor;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int length = Integer.MAX_VALUE;
        Set<Integer> commonAcestors = getCommonAncestors(v, w);
        BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(localG, v);
        BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(localG, w);
        for (Integer ancestor : commonAcestors) {
            int mayBeLen = vPaths.distTo(ancestor) + wPaths.distTo(ancestor);
            if (mayBeLen < length) {
                length = mayBeLen;
            }
        }

        if (Integer.MAX_VALUE == length) {
            return -1;
        }
        return length;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        Set<Integer> commonAcestors = getCommonAncestors(v, w);
        BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(localG, v);
        BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(localG, w);
        for (Integer cmnAncestor : commonAcestors) {
            int mayBeLen = vPaths.distTo(cmnAncestor) + wPaths.distTo(cmnAncestor);
            if (mayBeLen < length) {
                length = mayBeLen;
                ancestor = cmnAncestor;
            }
        }
        return ancestor;
    }

    private Set<Integer> getAdjs(int v) {
        Set<Integer> result = getAdjs0(v, new TreeSet<Integer>());
        return result;
    }

    private Set<Integer> getAdjs0(int v, Set<Integer> adjs) {
        for (Integer vAdj : localG.adj(v)) {
            if (!adjs.contains(vAdj)) {
                adjs.add(vAdj);
                adjs.addAll(getAdjs(vAdj));
            }
        }
        return adjs;
    }

    private Set<Integer> getCommonAncestors(Iterable<Integer> v, Iterable<Integer> w) {
        Set<Integer> vAdjs = new TreeSet<>();
        for (Integer V : v) {
            vAdjs.add(V);
            vAdjs.addAll(getAdjs(V));
        }
        
        Set<Integer> wAdjs = new TreeSet<>();
        for (Integer W : w) {
            wAdjs.add(W);
            wAdjs.addAll(getAdjs(W));
        }
        Set<Integer> commonAncestors = new TreeSet<>();
        for (Integer vAdj : vAdjs) {
            for (Integer wAdj : wAdjs) {
                if (vAdj.equals(wAdj)) {
                    commonAncestors.add(vAdj);
                }
            }
        }
//        for (Integer vAdj : vAdjs) {
//            for (Integer W : w) {
//                if (vAdj.equals(W)) {
//                    commonAncestors.add(W);
//                }
//            }
//        }
//        for (Integer wAdj : wAdjs) {
//            for (Integer V : v) {
//                if (wAdj.equals(V)) {
//                    commonAncestors.add(V);
//                }
//            }
//        }

        return commonAncestors;
    }

    private List<Integer> getCommonAncestor(int v, int w) {
        Iterable<Integer> vAdjs = getAdjs(v);
        Iterable<Integer> wAdjs = getAdjs(w);
        List<Integer> commonAncestors = new LinkedList<>();

        for (Integer vAdj : vAdjs) {
            for (Integer wAdj : wAdjs) {
                if (vAdj.equals(wAdj)) {
                    commonAncestors.add(vAdj);
                }
            }
        }
        for (Integer vAdj : vAdjs) {
            if (vAdj == w) {
                commonAncestors.add(w);
            }
        }
        for (Integer wAdj : wAdjs) {
            if (wAdj == v) {
                commonAncestors.add(v);
            }
        }
        return commonAncestors;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        testSAP(args[0]);
//        testParse(args[0]);
    }

    private static void testParse(String arg) {
        In in = new In(arg);
        List<String> hypernyms = new ArrayList<>();
        int size = 0;
        while (in.hasNextLine()) {
            String line = in.readLine();
            hypernyms.add(line);
            int mayBeSize = Integer.parseInt(line.split(",")[0]);
            if (mayBeSize > size) {
                size = Integer.parseInt(line.split(",")[0]);
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

        System.out.println(hyps.toString());
    }

    private static void testSAP(String arg) {
        In in = new In(arg);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            List<Integer> V = new LinkedList<>();
            int v = StdIn.readInt();
            V.add(v);
            v = StdIn.readInt();
            V.add(v);
//            v = StdIn.readInt();
//            V.add(v);
            List<Integer> W = new LinkedList<>();
            int w = StdIn.readInt();
            W.add(w);
            w = StdIn.readInt();
            W.add(w);
//            w = StdIn.readInt();
//            W.add(w);
            int length = sap.length(V, W);
            int ancestor = sap.ancestor(V, W);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
