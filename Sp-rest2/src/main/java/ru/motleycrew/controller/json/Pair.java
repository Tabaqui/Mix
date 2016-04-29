package ru.motleycrew.controller.json;

/**
 * Created by User on 28.04.2016.
 */
public class Pair<K, V> {

    private K first;
    private V second;

    public Pair() {
    }

    public Pair(K key, V value) {
        this.first = key;
        this.second = value;
    }

    public void setFirst(K first) {
        this.first = first;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }
}
