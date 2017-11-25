package java.util.concurrent;

import io.vavr.concurrent.CurrentThreadExecutorService;

public class ForkJoinPool {
    public static ExecutorService commonPool() {
        return new CurrentThreadExecutorService();
    }
}