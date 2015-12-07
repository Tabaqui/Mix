/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buffersample;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author vnikolaev
 */
public class BufferSample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final Buffer<Integer> buff = new Buffer<>(5000);
        Thread source1 = new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < 1_0000_0; i++) {
                        buff.putToTail(i);
                        System.out.println(" " + " 1>   " + i);
                    }
                } catch (InterruptedException ignore) { /* NOP */ }
            }
        };
        Thread source2 = new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < 1_0000_0; i++) {
                        buff.putToTail(i);
                        System.out.println(" " + " 2>   " + i);
                    }
                } catch (InterruptedException ignore) { /* NOP */ }
            }
        };
        Thread source3 = new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < 1_0000_0; i++) {
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
        source2.start();
        target1.start();
//        source1.start();
//        source3.start();
//        target2.start();
        
        RWLockCollection<String> collection = new RWLockCollection();
        collection.
    }

}
