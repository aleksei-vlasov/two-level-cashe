package com.willey.avlasov.cache.impl;

/**
 * Created by A.N Vlasov on 13.10.15.
 */

import com.willey.avlasov.cache.api.AbstractCache;

import java.io.*;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileSystemCache<K extends Serializable, V extends Serializable> implements AbstractCache<K, V> {

    private FileSystemCacheProperties p = new FileSystemCacheProperties();
    private int size = 0;

    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private FileEntry<K, V> writingEntry;

    public FileSystemCache() {
    }

    public FileSystemCache(FileSystemCacheProperties p) {
        this.p = p;
    }

    public V get(K key) {
        if (rwl.readLock().tryLock()) {
            try {
                return getEntry(key).getValue();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                rwl.readLock().unlock();
            }
        } else {
            if (writingEntry.equals(getEntry(key)))
                return writingEntry.getValue();
            else
                return getEntry(key).getValue();
        }
    }

    private Entry<K, V> getEntry(K key) {
        try {
            File[] files = new File(p.getCacheFolder()).listFiles();
            for (File f : files) {
                Entry<K, V> entry = deserialize(f);
                if (entry.getKey().equals(key)) {
                    return entry;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean put(K key, V value) {
        writingEntry = new FileEntry<K, V>(key, value);
        rwl.writeLock().lock();
        try {
            if (size == p.getMaxSize())
                return false;
            String hash = String.valueOf(key.hashCode());
            File dir = new File(p.getCacheFolder() + File.separator + hash);
            File fileEntry;
            if (!dir.exists())
                dir.mkdirs();
            fileEntry = lookup(dir, key);
            if (fileEntry == null)
                fileEntry = new File(dir + File.separator + UUID.randomUUID().toString());
            serialize(key, value, fileEntry);
            size++;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            rwl.writeLock().unlock();
            writingEntry = null;
        }
        return true;
    }

    @Override
    public boolean contains(K key) {
        String hash = String.valueOf(key.hashCode());
        File dir = new File(p.getCacheFolder() + File.separator + hash);
        return lookup(dir, key) != null;
    }


    @Override
    public void delete(K key) {
        String hash = String.valueOf(key.hashCode());
        File dir = new File(p.getCacheFolder() + File.separator + hash);
        if (dir.exists()) {
            File fileEntry = lookup(dir, key);
            if (fileEntry != null) {
                fileEntry.delete();
                size--;
            }
        }
    }

    @Override
    public void clear() {
        clearEntry(new File(p.getCacheFolder()));
    }

    private void clearEntry(File file) {
        for (File inner : file.listFiles()) {
            if (inner.isDirectory()) {
                clearEntry(inner);
            } else {
                boolean deleted = inner.delete();
                assert deleted;
            }
        }
    }

    @Override
    public int size() {
        return size;
    }


    private void serialize(K key, V value, File file) throws IOException {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            out.writeObject(key);
            out.writeObject(value);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            out.close();
        }

    }

    public Entry<K, V> deserialize(File file) throws IOException, ClassNotFoundException {
        Entry<K, V> result = null;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
            @SuppressWarnings("unchecked")
            K key = (K) in.readObject();
            @SuppressWarnings("unchecked")
            V value = (V) in.readObject();
            result = new FileEntry<K, V>(key, value);
        } finally {
            in.close();
        }

        return result;
    }

    private File lookup(File dir, K key) {
        File[] files = dir.listFiles();
        try {
            for (File f : files) {
                Entry<K, V> entry = deserialize(f);
                if (entry.getKey().equals(key)) {
                    return f;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private static class FileEntry<K, V> implements Entry<K, V> {
        private final K key;
        private final V value;

        private FileEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null)
                return false;
            if (!(o instanceof FileEntry))
                return false;
            FileEntry entry = (FileEntry) o;
            if (this.key == entry.getKey()
                    && this.value == entry.getValue())
                return true;
            return false;
        }
    }

}