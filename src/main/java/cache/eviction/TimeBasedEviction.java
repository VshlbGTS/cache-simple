package cache.eviction;

import cache.model.CacheEntry;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;

public class TimeBasedEviction<K> implements TimeEvictionPolicy<K, CacheEntry> {
    private final long evictionThresholdMillis;

    public TimeBasedEviction(long evictionThresholdMillis) {
        this.evictionThresholdMillis = evictionThresholdMillis;
    }

    @Override
    public void evictOnTime(Map<K, CacheEntry> cache) {
        Instant now = Instant.now();
        Iterator<Map.Entry<K, CacheEntry>> iterator = cache.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<K, CacheEntry> entry = iterator.next();
            CacheEntry cacheEntry = entry.getValue();

            if (now.toEpochMilli() - cacheEntry.getLastAccessedTime().toEpochMilli() > evictionThresholdMillis) {
                iterator.remove(); // Remove stale entry
            }
        }
    }
}