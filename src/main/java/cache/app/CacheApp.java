package cache.app;

import cache.eviction.LFUEviction;
import cache.eviction.TimeBasedEviction;
import cache.listener.RemovalListener;
import cache.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheApp {
    private static final Logger logger = LoggerFactory.getLogger(CacheApp.class);

    public static void main(String[] args) {
        logger.info("Cache Service Testing:");

        // 1. Initialize Time-Based Eviction Policy
        logger.info("\n1. Initialize Time-Based Eviction Policy:");
        TimeBasedEviction<String> timeEvictionPolicy = new TimeBasedEviction<>(5000); // 5 seconds for time-based eviction

        // 2. Initialize LFU Eviction Policy
        logger.info("\n2. Initialize LFU Eviction Policy:");
        LFUEviction<String> lfuEvictionPolicy = new LFUEviction<>();

        // 3. Initialize Removal Listener
        logger.info("\n3. Initialize Removal Listener:");
        RemovalListener<String, String> removalListener = new RemovalListener<>();

        // 4. Initialize Cache Service
        logger.info("\n4. Initialize Cache Service:");
        CacheService<String, String> cacheService = new CacheService<>(
                100_000, // Maximum size
                lfuEvictionPolicy, // LFU eviction policy
                timeEvictionPolicy, // Time-based eviction policy
                removalListener // Removal listener
        );

        // 5. Add Entries to Cache (Basic Put Operations)
        logger.info("\n5. Add Entries to Cache:");
        cacheService.put("key1", "value1");
        cacheService.put("key2", "value2");
        cacheService.put("key3", "value3");
        logger.info("Added key1, key2, and key3 to the cache.");

        // 6. Access Entries to Simulate Usage
        logger.info("\n6. Access Entries to Simulate Usage:");
        logger.info("Accessing key1: {}", cacheService.get("key1"));
        logger.info("Accessing key2: {}", cacheService.get("key2"));
        logger.info("Accessing key2 again: {}", cacheService.get("key2")); // Simulate repeated access for LFU

        // 7. Wait for Time-Based Eviction
        logger.info("\n7. Wait for Time-Based Eviction (5 seconds sleep):");
        try {
            Thread.sleep(6000); // Sleep for 6 seconds to let time-based eviction trigger
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Thread was interrupted during sleep.");
        }
        logger.info("Waited 6 seconds.");

        // 8. Add New Entry and Trigger Eviction
        logger.info("\n8. Add New Entry and Trigger Eviction:");
        cacheService.put("key4", "value4"); // Adding a new entry triggers eviction
        logger.info("Added key4 to cache.");
        logger.info("Trigger eviction logic by adding more keys.");
        logger.info("Cache size after eviction: {}", cacheService.getSize());

        // 9. Check Statistics
        logger.info("\n9. Check Statistics:");
        logger.info("Eviction Count: {}", cacheService.getStats().getEvictionCount());
        logger.info("Average Put Time (ms): {}", cacheService.getStats().getAveragePutTimeMillis());
        logger.info("Total Put Operations: {}", cacheService.getStats().getPutOperationCount());

        // 10. Access Entries After Evictions
        logger.info("\n10. Access Entries After Evictions:");
        logger.info("Accessing key1: {}", cacheService.get("key1")); // Likely evicted by time-based eviction
        logger.info("Accessing key4: {}", cacheService.get("key4")); // Should remain in the cache

        // 11. Trigger LFU Eviction
        logger.info("\n11. Trigger LFU Eviction:");
        for (int i = 5; i <= 10; i++) {
            cacheService.put("key" + i, "value" + i); // Add enough entries to exceed max size if configured
            logger.info("Added key{} to the cache.", i);
        }
        logger.info("Cache size after LFU eviction: {}", cacheService.getSize());
        logger.info("Eviction Count After LFU: {}", cacheService.getStats().getEvictionCount());

        // 12. Validate LFU Eviction
        logger.info("\n12. Validate LFU Eviction:");
        logger.info("Accessing key2: {}", cacheService.get("key2")); // Key2 should still exist due to repeated access earlier
        logger.info("Accessing key5: {}", cacheService.get("key5")); // May be evicted if it's the least frequently used
    }
}