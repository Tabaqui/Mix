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

    private final T[] cyclicBuffer;
    private final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    private int head, tail, len;

    public Buffer(int size) {
        this.cyclicBuffer = (T[]) new Object[size];
        this.lock = new ReentrantLock();
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
    }
    
    public void putToTail(T pack) throws InterruptedException {
        lock.lock();
        try {
            while (len == cyclicBuffer.length) {
                notFull.await();
            }
            cyclicBuffer[tail] = pack;
            if (++tail == cyclicBuffer.length) {
                tail = 0;
            }
            ++len;
            notEmpty.signal();            
        } finally {
            lock.unlock();
        }
    }
    
    public T peakFromHead() throws InterruptedException {
        lock.lock();
        try {
            while(len == 0) {
                notEmpty.await();
            }
            T result = cyclicBuffer[head];
            cyclicBuffer[head] = null;
            if (++head == cyclicBuffer.length) {
                head = 0;
            }
            --len;
            notFull.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }
}
