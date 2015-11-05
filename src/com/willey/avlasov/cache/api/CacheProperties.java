package com.willey.avlasov.cache.api;

/**
 * Created by A.N Vlasov on 13.10.15.
 */


public abstract class CacheProperties {

    public enum CacheStrategy {SERIAL, FREQUENCY, FILE, MEMORY}

    public int DEFAULT_CACHE_SIZE = 10000;

    private int maxSize = DEFAULT_CACHE_SIZE;
    private CacheStrategy strategy = CacheStrategy.SERIAL;

    public CacheStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(CacheStrategy strategy) {
        this.strategy = strategy;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
