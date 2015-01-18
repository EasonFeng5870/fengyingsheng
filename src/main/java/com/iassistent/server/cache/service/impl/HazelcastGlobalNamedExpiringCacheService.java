package com.iassistent.server.cache.service.impl;

import java.io.Serializable;

/**
 * This is like the HazelcastContextAwareNamedExpiringCacheService except that
 * it *removes* the context-awareness. This means that multiple integrations
 * that use this service with the same cache name will have their data mixed.
 *
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 *
 * @param <T>
 */
public class HazelcastGlobalNamedExpiringCacheService<T extends Serializable> extends  HazelcastContextAwareNamedExpiringCacheService<T> {

    @Override
    protected String getGlobalCacheName(String localCacheName){
        return localCacheName;
    }
}
