package cache.eviction;

import java.util.Map;

public interface SizeEvictionPolicy<K, V> {
    void evictOnSize(Map<K, V> cache, int maxSize);
    void updateOnAccess(K key);
}