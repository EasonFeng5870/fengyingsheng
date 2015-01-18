package com.iassistent.server.cache.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapEntry;
import com.iassistent.server.cache.service.NamedExpiringCacheService;
import com.iassistent.server.common.Context;
import com.iassistent.server.exception.ServiceException;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public class HazelcastContextAwareNamedExpiringCacheService<T extends Serializable> implements NamedExpiringCacheService<T> {

    protected HazelcastInstance f_hazelcastInstance;

    public void setHazelcastInstance( HazelcastInstance hzInstance )
    {
        f_hazelcastInstance = hzInstance;
    }

    protected String getGlobalCacheName(String localName){
        String globalPrefix = "Global";
        Context ctx = Context.getCurrentContext();
        if (ctx != null && ctx.has(Context.CONTEXT_ID)){
            globalPrefix = ctx.getString(Context.CONTEXT_ID);
        }
        String realCacheName = globalPrefix + localName;
        return realCacheName;
    }

    private IMap<String, T> getGlobalMap(String localCacheName){
        String globalName = getGlobalCacheName(localCacheName);
        IMap<String, T> map = f_hazelcastInstance.getMap(globalName);
        return map;
    }

    @Override
    public void setCachedValue(String cacheName, String key, T value, long ttlValue, TimeUnit timeUnit) throws ServiceException {
        IMap map = getGlobalMap(key);
        if(ttlValue > 0){
            map.put(key, value,ttlValue,timeUnit);
        }else{
            map.put(key, value);
        }
    }

    @Override
    public T retrieveCachedValue(String cacheName, String key) throws ServiceException {
        IMap<String, T> map = getGlobalMap(cacheName);
        T val = map.get(key);
        return val;
    }

    @Override
    public boolean clearCachedValue(String cacheName, String key) throws ServiceException {
        IMap<String, T> map = getGlobalMap(cacheName);
        T obj = map.remove(key);
        return (obj != null);
    }

    @Override
    public void clearAll(String cacheName) throws ServiceException {
        IMap<String, T> map = getGlobalMap(cacheName);
        map.clear();
    }

    @Override
    public int cacheSize(String cacheName) throws ServiceException {
        IMap<String, T> map = getGlobalMap(cacheName);
        int nElems = 0;
        if(map != null){
            nElems = map.size();
        }
        return nElems;
    }

    public long getEntryExpiration( String cacheName, String key ) throws ServiceException
    {
        IMap<String, T> map = getGlobalMap(cacheName);

        MapEntry mapEntry;
        try
        {
            mapEntry = map.getMapEntry(key);
        }
        catch (NullPointerException e)
        {
            throw new ServiceException("Key [" + key + "] not found in cache [" + cacheName + "]");
        }

        if (mapEntry == null)
        {
            throw new ServiceException("Key [" + key + "] not found in cache [" + cacheName + "]");
        }

        return mapEntry.getExpirationTime();

    }
}
