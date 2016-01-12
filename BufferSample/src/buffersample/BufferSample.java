/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buffersample;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vnikolaev
 */
public class BufferSample {

    /**
     * @param args the command line arguments
     */
    public static void bean(String[] args) {
        final Buffer<Integer> buff = new Buffer<>(50);
        Thread source1 = new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < 1_000_00; i++) {
                        buff.putToTail(i);
                        System.out.println(" " + " 1>   " + i);
                    }
                } catch (InterruptedException ignore) { /* NOP */ }
            }
        };
        Thread source2 = new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < 1_000_00; i++) {
                        buff.putToTail(i);
                        System.out.println(" " + " 2>   " + i);
                    }
                } catch (InterruptedException ignore) { /* NOP */ }
            }
        };
        Thread source3 = new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < 1_000_00; i++) {
                        buff.putToTail(i);
                        System.out.println(" " + " 3>   " + i);
                    }
                } catch (InterruptedException ignore) { /* NOP */ }
            }
        };
        Thread target1 = new Thread() {
            public void run() {
                try {
                    Integer result;
                    while ((result = buff.peakFromHead()) != null) {
                        System.out.println(" " + " 1< " + result);
                    }
                } catch (InterruptedException ignore) { /* NOP */ }
            }
        };
        Thread target2 = new Thread() {
            public void run() {
                try {
                    Integer result;
                    while ((result = buff.peakFromHead()) != null) {
                        System.out.println(" " + " 2< " + result);
                    }
                } catch (InterruptedException ignore) { /* NOP */ }
            }
        };
        Map<String, Point> m = new HashMap<>();
        m.put("Uno", new Point(1, 1));
        m.put("Due", new Point(2, 2));
        Delegating d = new Delegating(m);
        d.setLocations("Uno", 3, 3);
        System.out.println(d.getLocations().toString());
//        source2.start();
//        target1.start();
//        source1.start();
//        source3.start();
//        target2.start();
    }

}
