package test;

/**
 * Created by A.N Vlasov on 13.10.15.
 */

import com.willey.avlasov.cache.impl.MemoryCache;
import org.junit.BeforeClass;
import org.junit.Test;

public class MemoryTestCache {

    static MemoryCache<String, String> cache;

    @BeforeClass
    public static void init() {
        cache = new MemoryCache<String, String>();
    }

    @Test
    public void putValues() {
        cache.put("k1", "v1");
        cache.put("k2", "v2");
        cache.put("k3", "v3");
        cache.put("k4", "v4");
        cache.put("k5", "v5");
    }



}
