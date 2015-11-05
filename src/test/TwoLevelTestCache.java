package test;

/**
 * Created by A.N Vlasov on 13.10.15.
 */

import com.willey.avlasov.cache.impl.FileSystemCacheProperties;
import com.willey.avlasov.cache.impl.MemoryCacheProperties;
import com.willey.avlasov.cache.impl.TwoLevelCashe;
import org.junit.BeforeClass;
import org.junit.Test;

public class TwoLevelTestCache {

    static TwoLevelCashe<String, String> cache;

    @BeforeClass
    public static  void init() {
        FileSystemCacheProperties fsp = new FileSystemCacheProperties();
        MemoryCacheProperties msp = new MemoryCacheProperties();
        msp.setMaxSize(5);
        cache = new TwoLevelCashe<String, String>(msp, fsp);
    }

    @Test
    public void putValues() {
        cache.put("k1", "v1");
        cache.put("k2", "v2");
        cache.put("k3", "v3");
        cache.put("k4", "v4");
        cache.put("k5", "v5");
        cache.put("k6", "v6");
        cache.put("k7", "v7");
        cache.put("k8", "v8");
    }


}
