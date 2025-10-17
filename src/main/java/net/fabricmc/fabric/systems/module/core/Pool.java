package net.fabricmc.fabric.systems.module.core;

import java.util.ArrayDeque;
import java.util.Queue;

public class Pool<T> {
    private final Queue<T> items = new ArrayDeque<T>();
    private final Producer<T> producer;

    public Pool(Producer<T> producer) {
        this.producer = producer;
    }

    public synchronized T get() {
        if (this.items.size() > 0) {
            return this.items.poll();
        }
        return this.producer.create();
    }

    public synchronized void free(T obj) {
        this.items.offer(obj);
    }

    public static interface Producer<T> {
        public T create();
    }
}
