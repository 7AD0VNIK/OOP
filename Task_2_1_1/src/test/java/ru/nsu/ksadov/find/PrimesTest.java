package ru.nsu.ksadov.find;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Primes tests.
 */
class PrimesTest {

    @Test
    void testExampleWithComposite() throws InterruptedException {
        long[] data = {6, 8, 7, 13, 5, 9, 4};

        assertTrue(Primes.isAnyCompositeSequential(data));
        assertTrue(Primes.isAnyCompositeThreads(data, 4));
        assertTrue(Primes.isAnyCompositeStream(data));
    }

    @Test
    void testExampleAllPrime() throws InterruptedException {
        long[] data = {
                20319251L, 6997901L, 6997927L, 6997937L,
                17858849L, 6997967L, 6998009L, 6998029L,
                6998039L, 20165149L, 6998051L, 6998053L
        };

        assertFalse(Primes.isAnyCompositeSequential(data));
        assertFalse(Primes.isAnyCompositeThreads(data, 4));
        assertFalse(Primes.isAnyCompositeStream(data));
    }

    @Test
    void testSingleElement() throws InterruptedException {
        assertTrue(Primes.isAnyCompositeSequential(new long[]{1}));
        assertFalse(Primes.isAnyCompositeSequential(new long[]{2}));
    }

    @Test
    void testNegativeAndZero() throws InterruptedException {
        long[] data = {-5, 0, 2, 3};

        assertTrue(Primes.isAnyCompositeSequential(data));
        assertTrue(Primes.isAnyCompositeThreads(data, 2));
        assertTrue(Primes.isAnyCompositeStream(data));
    }

    @Test
    void testEmptyArray() throws InterruptedException {
        long[] data = {};

        assertFalse(Primes.isAnyCompositeSequential(data));
        assertFalse(Primes.isAnyCompositeThreads(data, 2));
        assertFalse(Primes.isAnyCompositeStream(data));
    }
}
