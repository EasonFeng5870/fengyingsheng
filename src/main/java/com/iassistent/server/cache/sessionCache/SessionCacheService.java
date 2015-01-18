package com.iassistent.server.cache.sessionCache;

import com.iassistent.server.user.bean.Session;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public interface SessionCacheService<E extends Session> {

    /**
     * Set cache name.
     * @param cacheName
     */
    void setCacheName(String cacheName);

    /**
     * Set time-to-live value.
     * @param ttlMinutes - time-to-live
     */
    void setCacheTtlMinutes(long ttlMinutes);

    /**
     * Return cache expiration in milliseconds.
     * @return cache expiration in milliseconds.
     */
    long getExpiration();

    /**
     * Return cache size.
     * @return cache size.
     */
    int cacheSize();

    /**
     * Set data in cache.
     * @param key - unique identifier of cache data.
     * @param value
     */
    void set(String key, E value);

    /**
     * Retrieve data
     * @param key - unique identifier of cache data.
     * @return
     */
    E get(String key);

    /**
     * Remove data from cache.
     * @param key - unique identifier of cache data.
     * @return
     */
    boolean remove(String key);

    /**
     * Remove all data from cache.
     */
    void  removeAll();

    /**
     * Get expiration time of specific key. Key must be present or exception will be thrown.
     * @param key
     * @return
     */
    long getEntryExpiration(String key);

}
