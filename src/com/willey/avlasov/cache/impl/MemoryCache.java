package com.willey.avlasov.cache.impl;

/**
 * Created by A.N Vlasov on 13.10.15.
 */

import com.willey.avlasov.cache.api.AbstractCache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MemoryCache<K, V> implements AbstractCache<K, V> {

    private final ConcurrentMap<K, V> map = new ConcurrentHashMap<K, V>();

    private MemoryCacheProperties p = new MemoryCacheProperties();

    public MemoryCache() {
    }

    public MemoryCache(MemoryCacheProperties p) {
        this.p = p;
    }

    @Override
    public boolean put(K key, V value) {
        if(size() == p.getMaxSize())
            return false;
        map.putIfAbsent(key, value);
        return true;
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public boolean contains(K key) {
        return map.containsKey(key);
    }

    @Override
    public void delete(K key) {
        map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

}