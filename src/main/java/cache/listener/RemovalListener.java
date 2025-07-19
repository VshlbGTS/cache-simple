package cache.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemovalListener<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(RemovalListener.class);

    public void onRemove(K key, V value) {
        logger.info("Entry removed from cache: key={}, value={}", key, value);
    }
}