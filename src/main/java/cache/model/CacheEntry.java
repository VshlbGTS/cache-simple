package cache.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheEntry {
    private String value;
    private Instant lastAccessedTime;

    public CacheEntry(String value) {
        this.value = value;
        this.lastAccessedTime = Instant.now();
    }

    public void updateLastAccessedTime() {
        this.lastAccessedTime = Instant.now();
    }
}