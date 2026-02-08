package ru.nsu.ksadov.find;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utility class for checking primality of numbers and
 * detecting composite numbers in an array using
 * sequential and parallel approaches.
 */
public class Primes {

    /**
     * Checks whether a given number is prime.
     */
    public static boolean isPrime(long n) {
        if (n < 2) {
            return false;
        }
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sequentially checks whether the given array contains
     * at least one composite number.
     */
    public static boolean isAnyCompositeSequential(long[] nums) {
        for (long n : nums) {
            if (!isPrime(n)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks whether the given array contains at least one
     * composite number using multiple threads.
     * The array is divided into equal chunks processed
     * by separate threads. The computation stops early
     * if a composite number is found.
     */
    public static boolean isAnyCompositeThreads(long[] nums, int threadCount)
            throws InterruptedException {
        AtomicBoolean foundComposite = new AtomicBoolean(false);
        Thread[] threads = new Thread[threadCount];
        int chunkSize = (int) Math.ceil((double) nums.length / threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, nums.length);

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (foundComposite.get()) {
                        return;
                    }
                    if (!isPrime(nums[j])) {
                        foundComposite.set(true);
                        return;
                    };
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }
        return foundComposite.get();
    }


    /**
     * Checks whether the given array contains at least one
     * composite number using a parallel stream.
     */
    public static boolean isAnyCompositeStream(long[] nums) {
        return Arrays.stream(nums).parallel().anyMatch(n -> !isPrime(n));
    }
}
