package cache.service;

import cache.eviction.SizeEvictionPolicy;
import cache.eviction.TimeEvictionPolicy;
import cache.listener.RemovalListener;
import cache.model.CacheEntry;
import cache.stats.CacheStats;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheService<K, V> {
    private final int maxSize;
    private final Map<K, CacheEntry> cache;
    private final SizeEvictionPolicy<K, CacheEntry> sizeEvictionPolicy;
    private final TimeEvictionPolicy<K, CacheEntry> timeEvictionPolicy;
    private final RemovalListener<K, V> removalListener;
    private final CacheStats cacheStats;

    public CacheService(int maxSize,
                        SizeEvictionPolicy<K, CacheEntry> sizeEvictionPolicy,
                        TimeEvictionPolicy<K, CacheEntry> timeEvictionPolicy,
                        RemovalListener<K, V> removalListener) {
        this.maxSize = maxSize;
        this.sizeEvictionPolicy = sizeEvictionPolicy;
        this.timeEvictionPolicy = timeEvictionPolicy;
        this.removalListener = removalListener;
        this.cache = new ConcurrentHashMap<>();
        this.cacheStats = new CacheStats();
    }

    public V get(K key) {
        CacheEntry entry = cache.get(key);
        if (entry != null) {
            if (sizeEvictionPolicy != null) {
                sizeEvictionPolicy.updateOnAccess(key);
            }
            entry.updateLastAccessedTime(); // Update last accessed timestamp
            return (V) entry.getValue();
        }
        return null; // Key not found
    }

    public void put(K key, V value) {
        long startTime = System.nanoTime();

        if (cache.containsKey(key)) {
            cache.get(key).setValue((String) value);
            cache.get(key).updateLastAccessedTime(); // Update access time
        } else {
            if (cache.size() >= maxSize && sizeEvictionPolicy != null) {
                sizeEvictionPolicy.evictOnSize(cache, maxSize); // Dynamically invoke size-based eviction
                cacheStats.incrementEvictions();
            }
            if (timeEvictionPolicy != null) {
                timeEvictionPolicy.evictOnTime(cache); // Dynamically invoke time-based eviction
            }
            cache.put(key, new CacheEntry((String) value));
        }

        long endTime = System.nanoTime();
        cacheStats.recordPutTime(endTime - startTime);
    }

    public void remove(K key) {
        CacheEntry removedEntry = cache.remove(key);
        if (removedEntry != null) {
            removalListener.onRemove(key, (V) removedEntry.getValue());
            cacheStats.incrementEvictions();
        }
    }

    public CacheStats getStats() {
        return cacheStats;
    }

    public int getSize() {
        return cache.size();
    }
}