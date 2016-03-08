package io.github.ztmark;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Author: Mark
 * Date  : 16/3/8.
 */
public class BloomFilterTest {


    @Test
    public void testCreateBloomFilter() {
        BloomFilter<String> bloomFilter = new BloomFilter<>(100, 10);
        assertNotNull(bloomFilter);
        assertEquals(100, bloomFilter.getBitSize());
        assertEquals(10, bloomFilter.getExpectedNumberOfElements());
        assertEquals(7, bloomFilter.getNumberOfHashFunction());
        assertEquals(0, bloomFilter.getAddedNumberOfElements());
    }

    @Test
    public void testAddBloomFilter() {
        BloomFilter<String> bloomFilter = new BloomFilter<>(100, 10);
        assertNotNull(bloomFilter);
        assertEquals(0, bloomFilter.getAddedNumberOfElements());
        bloomFilter.add("hello");
        assertEquals(1, bloomFilter.getAddedNumberOfElements());
    }

    @Test
    public void testContains() {
        BloomFilter<String> bloomFilter = new BloomFilter<>(100, 10);
        assertNotNull(bloomFilter);
        bloomFilter.add("hello");
        assertTrue(bloomFilter.contains("hello"));
        assertFalse(bloomFilter.contains("world"));
    }


}