package com.willey.avlasov.cache.impl;

/**
 * Created by A.N Vlasov on 13.10.15.
 */

import java.io.Serializable;
import com.willey.avlasov.cache.api.AbstractCache;

public class TwoLevelCashe<K extends Serializable, V extends Serializable> implements AbstractCache<K, V> {

    MemoryCache<K, V> msCache;
    FileSystemCache<K, V> fsCache;

    public TwoLevelCashe() {
        msCache = new MemoryCache<K, V>();
        fsCache = new FileSystemCache<K, V>();
    }

    public TwoLevelCashe(MemoryCacheProperties mProps, FileSystemCacheProperties fProps) {
        msCache = new MemoryCache<K, V>(mProps);
        fsCache = new FileSystemCache<K, V>(fProps);
    }

    @Override
    public boolean put(K key, V value) {
        boolean isReturn = msCache.put(key, value);
        if(!isReturn)
            isReturn = fsCache.put(key, value);
        return isReturn;
    }

    @Override
    public V get(K key) {
        V value = null;
        value = msCache.get(key);
        if(value == null)
            value = fsCache.get(key);
        return value;
    }

    @Override
    public boolean contains(K key) {
        return msCache.contains(key) || fsCache.contains(key);
    }

    @Override
    public void clear() {
        msCache.clear();
        fsCache.clear();
    }

    @Override
    public void delete(K key) {
        msCache.delete(key);
        fsCache.delete(key);
    }

    @Override
    public int size() {
        return msCache.size() + fsCache.size();
    }

}
