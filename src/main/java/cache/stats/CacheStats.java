package cache.stats;

import java.util.concurrent.atomic.AtomicLong;

public class CacheStats {
    private final AtomicLong evictionCount;
    private final AtomicLong totalPutTimeNano;
    private final AtomicLong putOperationCount;

    public CacheStats() {
        this.evictionCount = new AtomicLong(0);
        this.totalPutTimeNano = new AtomicLong(0);
        this.putOperationCount = new AtomicLong(0);
    }

    public void incrementEvictions() {
        evictionCount.incrementAndGet();
    }

    public void recordPutTime(long durationNano) {
        totalPutTimeNano.addAndGet(durationNano);
        putOperationCount.incrementAndGet();
    }

    public long getEvictionCount() {
        return evictionCount.get();
    }

    public double getAveragePutTimeMillis() {
        if (putOperationCount.get() == 0) {
            return 0.0;
        }
        return (totalPutTimeNano.get() / 1_000_000.0) / putOperationCount.get();
    }

    public long getPutOperationCount() {
        return putOperationCount.get();
    }

    @Override
    public String toString() {
        return "CacheStats{" +
                "evictionCount=" + evictionCount +
                ", totalPutTimeNano=" + totalPutTimeNano +
                ", putOperationCount=" + putOperationCount +
                ", averagePutTimeMillis=" + getAveragePutTimeMillis() +
                '}';
    }
}