package com.willey.avlasov.cache.api;

/**
 * Created by A.N Vlasov on 13.10.15.
 */

public interface AbstractCache<K, V> {

    boolean put(K key, V value);

    V get(K key);

    boolean contains(K key);

    void delete(K key);

    void clear();

    int size();
}