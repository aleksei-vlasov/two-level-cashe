package com.willey.avlasov.cache.impl;

/**
 * Created by A.N Vlasov on 13.10.15.
 */

import com.willey.avlasov.cache.api.CacheProperties;

public class FileSystemCacheProperties extends CacheProperties {

    private String cacheFolder = this.getClass().getPackage().getName();

    public String getCacheFolder() {
        return cacheFolder;
    }

    public void setCacheFolder(String cacheFolder) {
        this.cacheFolder = cacheFolder;
    }
}
