package com.iassistent.server.cache.service;

import com.iassistent.server.exception.ServiceException;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public interface NamedExpiringCacheService<T extends Serializable> {

    /**
     * Add a key/value pair to the cache. If the key already exists in the
     * cache, then this call will overwrite the cached value with the value
     * passed in.
     *
     * @param key
     * @param value
     */
    public void setCachedValue(String cacheName, String key, T value, long ttlValue, TimeUnit timeUnit) throws ServiceException;

    /**
     * Retrieve a value from the cache with the specified key. Returns null if
     * the key doesn't exist in the cache.
     * @param cacheName
     * @param key
     * @return
     * @throws ServiceException
     */
    public T retrieveCachedValue(String cacheName, String key) throws ServiceException;

    /**
     * Removes an entry from the cache.
     * @param cacheName
     * @param key
     * @return true if something was actually removed from the cache, false it
     *         the item was not present
     * @throws ServiceException
     */
    public boolean clearCachedValue(String cacheName, String key) throws ServiceException;

    /**
     * Clear all the entries from the cache. Note that this may not be supported
     * by all implementations.
     *
     * @param cacheName
     * @throws ServiceException
     */
    public void clearAll(String cacheName) throws ServiceException;

    /**
     * Get the current size of the cache. This is the number of keys currently
     * stored in the specified cache.
     *
     * @param cacheName
     * @return
     * @throws ServiceException
     */
    public int cacheSize(String cacheName) throws ServiceException;
}
