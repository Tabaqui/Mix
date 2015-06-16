/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buffersample;

/**
 *
 * @author vnikolaev
 */
public class BufferSample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final Buffer<Integer> buff = new Buffer<>();
        Thread source1 = new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < 1_000_000; i++) {
                        buff.put(i);
//                        System.out.println("1>" + i);
                    }
                } catch (InterruptedException ignore) { /* NOP */ }
            }
        };
        Thread source2 = new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < 1_000_000; i++) {
                        buff.put(i);
//                        System.out.println("2>" + i);
                    }
                } catch (InterruptedException ignore) { /* NOP */ }
            }
        };
        Thread target1 = new Thread() {
            public void run() {
                try {
                    Integer result;
                    while ((result = buff.pop()) != null) {
                        System.out.println("1< " + result);
                    }
                } catch (InterruptedException ignore) { /* NOP */ }
            }
        };
        Thread target2 = new Thread() {
            public void run() {
                try {
                    Integer result;
                    while ((result = buff.pop()) != null) {
                        System.out.println("2< " + result);
                    }
                } catch (InterruptedException ignore) { /* NOP */ }
            }
        };
        source2.start();
        target1.start();
        source1.start();
        target2.start();
    }

}
