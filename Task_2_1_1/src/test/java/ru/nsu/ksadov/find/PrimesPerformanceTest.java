package ru.nsu.ksadov.find;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

/**
 * Bench tests.
 */
public class PrimesPerformanceTest {

    private static final int SIZE = 2_000_000;
    private static final long PRIME = 6998053L;

    private long[] generateTestData() {
        long[] data = new long[SIZE];
        Arrays.fill(data, PRIME);
        return data;
    }

    private long measure(Runnable task) {
        long start = System.nanoTime();
        task.run();
        return (System.nanoTime() - start) / 1_000_000; // ms
    }

    @Test
    void performanceTest() throws InterruptedException {
        long[] data = generateTestData();

        System.out.println("Array size: " + SIZE);

        long seqTime = measure(() ->
                Primes.isAnyCompositeSequential(data));
        System.out.println("Sequential: " + seqTime + " ms");

        for (int threads = 1; threads <= 8; threads *= 2) {
            int t = threads;
            long time = measure(() -> {
                try {
                    Primes.isAnyCompositeThreads(data, t);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println("Threads (" + t + "): " + time + " ms");
        }

        long streamTime = measure(() ->
                Primes.isAnyCompositeStream(data));
        System.out.println("ParallelStream: " + streamTime + " ms");
    }
}

