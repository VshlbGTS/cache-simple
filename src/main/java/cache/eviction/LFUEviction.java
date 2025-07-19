package cache.eviction;

import cache.model.CacheEntry;

import java.util.Map;
import java.util.HashMap;

public class LFUEviction<K> implements SizeEvictionPolicy<K, CacheEntry> {
    private final Map<K, Integer> usageFrequency;

    public LFUEviction() {
        this.usageFrequency = new HashMap<>();
    }

    @Override
    public void evictOnSize(Map<K, CacheEntry> cache, int maxSize) {
        if (cache.isEmpty()) {
            return;
        }

        while (cache.size() > maxSize) {
            K leastUsedKey = null;
            int minFrequency = Integer.MAX_VALUE;

            for (Map.Entry<K, Integer> entry : usageFrequency.entrySet()) {
                K key = entry.getKey();
                int frequency = entry.getValue();

                if (cache.containsKey(key) && frequency < minFrequency) {
                    minFrequency = frequency;
                    leastUsedKey = key;
                }
            }

            if (leastUsedKey != null) {
                cache.remove(leastUsedKey);
                usageFrequency.remove(leastUsedKey);
            }
        }
    }

    @Override
    public void updateOnAccess(K key) {
        usageFrequency.put(key, usageFrequency.getOrDefault(key, 0) + 1);
    }
}