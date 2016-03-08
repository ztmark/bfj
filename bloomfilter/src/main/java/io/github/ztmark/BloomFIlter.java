package io.github.ztmark;

import com.google.common.collect.ImmutableList;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * Author: Mark
 * Date  : 16/3/8.
 */
public class BloomFilter<T> {

    private final BitSet bitSet;
    private final int bitSize;
    private final int expectedNumberOfElements;
    private final ImmutableList<HashFunction> hashFunctions;
    private final int k;
    private int addedNumberOfElements;



    public BloomFilter(int bitSize, int expectedNumberOfElements) {
        if (bitSize <= 0 || expectedNumberOfElements <= 0) {
            throw new RuntimeException("wrong parameters bitSize and expectedNumberOfElements should greater than zero.");
        }
        this.bitSize = bitSize;
        bitSet = new BitSet(bitSize);
        this.expectedNumberOfElements = expectedNumberOfElements;
        // k = m / n * ln2
        k = (int) Math.round(1.0 * bitSize / expectedNumberOfElements * Math.log(2));
        hashFunctions = HashFunctionGenerator.generateKHashFunctions(k);
        addedNumberOfElements = 0;
    }

    public void add(T elem) {
        for (HashFunction function : hashFunctions) {
            bitSet.set(Math.abs(function.hashInt(elem.hashCode()).asInt() / bitSize));
        }
        addedNumberOfElements++;
    }

    public boolean contains(T elem) {
        for (HashFunction function : hashFunctions) {
            if (!bitSet.get(Math.abs(function.hashInt(elem.hashCode()).asInt() / bitSize))) {
                return false;
            }
        }
        return true;
    }

    public double getFalsePositiveProbability() {
        // p = (1 - e^(-k * n / m)) ^ k
        return Math.pow((1 - Math.exp(-k * 1.0 * addedNumberOfElements / bitSize)), k);

    }

    public int getAddedNumberOfElements() {
        return addedNumberOfElements;
    }

    public int getBitSize() {
        return bitSize;
    }

    public int getExpectedNumberOfElements() {
        return expectedNumberOfElements;
    }

    public int getNumberOfHashFunction() {
        return k;
    }

    private static final class HashFunctionGenerator {

        public static HashFunction generateAHashFunction() {
            return Hashing.murmur3_128(new Random(System.currentTimeMillis()).nextInt());
        }

        public static ImmutableList<HashFunction> generateKHashFunctions(int k) {
            Random random = new Random(System.currentTimeMillis());
            List<HashFunction> hashFunctions = new ArrayList<HashFunction>();
            for (int i = 0; i < k; i++) {
                hashFunctions.add(Hashing.murmur3_128(random.nextInt()));
            }
            return ImmutableList.copyOf(hashFunctions);
        }

    }


}
