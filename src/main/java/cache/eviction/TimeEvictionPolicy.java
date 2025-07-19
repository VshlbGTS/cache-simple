package cache.eviction;

import java.util.Map;

public interface TimeEvictionPolicy<K, V> {
    void evictOnTime(Map<K, V> cache);
}