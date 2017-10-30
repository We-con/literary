/*
 * @(#)PersistentDiskCache.class $version 2014. 5. 1.
 *
 * Copyright 2014 Naver Corp. All rights Reserved.
 * NAVER PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.reader.readingManagement.network;

import com.navercorp.volleyextensions.cache.disk.DiskBasedCache;

import java.io.File;

/**
 * 이미지 파일에 대해 영속적으로 캐싱을 하는 디스크캐시의 구현.
 *
 */
public class PersistentImageDiskCache extends DiskBasedCache {
    public PersistentImageDiskCache(File rootDirectory, int maxCacheSizeInBytes) {
        super(rootDirectory, maxCacheSizeInBytes);
    }

    @Override
    public synchronized Entry get(String key) {
        Entry entry = super.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.data == null || entry.data.length == 0) {
            return null;
        }
        if (entry.responseHeaders != null) {
            String contentType = entry.responseHeaders.get("Content-Type");
            if (contentType != null && contentType.startsWith("image")) {
                return new PersistentCacheEntry(entry);
            }
        }
        return entry;
    }

    static class PersistentCacheEntry extends Entry {
        PersistentCacheEntry(Entry entry) {
            this.data = entry.data;
            this.etag = entry.etag;
            this.serverDate = entry.serverDate;
            this.ttl = entry.ttl;
            this.softTtl = entry.softTtl;
            this.responseHeaders = entry.responseHeaders;
        }

        @Override
        public boolean isExpired() {
            return false;
        }

        @Override
        public boolean refreshNeeded() {
            return false;
        }
    }
}
