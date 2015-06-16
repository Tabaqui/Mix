/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buffersample;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author vnikolaev
 */
public class Buffer<T> {

    private T buffer;
    private final Lock lock;
    private final Condition readerCondition;
    private final Condition writerCondition;

    public Buffer() {
        this.buffer = null;
        this.lock = new ReentrantLock();
        readerCondition = lock.newCondition();
        writerCondition = lock.newCondition();
    }

    public void put(T pack) throws InterruptedException {
        lock.lock();
        try {
            while (buffer != null) {
                writerCondition.await();
            }
            buffer = pack;
            readerCondition.signal();
        } finally {
            lock.unlock();
        }

    }

    public T pop() throws InterruptedException {
        lock.lock();
        try {
            while (buffer == null) {
                readerCondition.await();
            }
            T local = buffer;
            buffer = null;
            writerCondition.signal();
            return local;
        } finally {
            lock.unlock();
        }
    }

}
