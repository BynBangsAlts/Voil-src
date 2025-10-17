package net.fabricmc.fabric.managers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class ThreadManager {
    private final ExecutorService executor;

    public ThreadManager(int threadCount) {
        this.executor = Executors.newFixedThreadPool(threadCount, new ThreadFactory(){
            private int count = 0;

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "VoilThread-" + this.count++);
                thread.setUncaughtExceptionHandler((t, e) -> {
                    System.err.println("Uncaught exception in thread " + t.getName() + ": " + e.getMessage());
                    e.printStackTrace();
                });
                return thread;
            }
        });
    }

    public ThreadManager() {
        this(4);
    }

    public Future<?> runAsync(Runnable task) {
        return this.executor.submit(task);
    }

    public void shutdown() {
        this.executor.shutdownNow();
    }

    public boolean isShutdown() {
        return this.executor.isShutdown();
    }

    public static ThreadManager singleThreaded() {
        return new ThreadManager(1);
    }

    public static ThreadManager multiThreaded(int numThreads) {
        return new ThreadManager(numThreads);
    }
}
