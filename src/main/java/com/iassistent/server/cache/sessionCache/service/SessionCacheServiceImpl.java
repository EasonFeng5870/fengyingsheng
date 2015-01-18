package com.iassistent.server.cache.sessionCache.service;

import com.iassistent.server.cache.service.impl.HazelcastGlobalNamedExpiringCacheService;
import com.iassistent.server.user.bean.Session;
import com.iassistent.server.cache.sessionCache.SessionCacheService;
import com.iassistent.server.exception.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public class SessionCacheServiceImpl implements SessionCacheService<Session> {

    private static final Logger log = Logger.getLogger(SessionCacheServiceImpl.class);

    private HazelcastGlobalNamedExpiringCacheService expiringCacheService;

    /** Default cache name. **/
    private String cacheName = "RESTSessionCache";

    /** Default time-to-live in minutes. **/
    private long cacheTtlMinutes = 240;

    public void setExpiringCacheService(HazelcastGlobalNamedExpiringCacheService<Session> expiringCacheService) {
        this.expiringCacheService = expiringCacheService;
    }

    @Override
    public void setCacheName(String cacheName) {
        if(StringUtils.isNotBlank(cacheName)){
            this.cacheName = cacheName;
        }
    }

    @Override
    public void setCacheTtlMinutes(long ttlMinutes) {
        this.cacheTtlMinutes = ttlMinutes;
    }

    @Override
    public long getExpiration() {
        return cacheTtlMinutes*60*1000;
    }

    @Override
    public int cacheSize() {
        try {
            return expiringCacheService.cacheSize(cacheName);
        } catch (ServiceException e) {
            //throw new ServiceException(e);
            log.error("The cache size method is down!", e);
        }
        return 0;
    }

    @Override
    public void set(String key, Session value) {
        try {
            expiringCacheService.setCachedValue(cacheName,key,value,cacheTtlMinutes, TimeUnit.MINUTES);
        } catch (ServiceException e) {
            log.error("The set method is down!", e);
        }
    }

    @Override
    public Session get(String key) {
        try {
            return (Session)expiringCacheService.retrieveCachedValue(cacheName,key);
        } catch (ServiceException e) {
            log.error("The set method is down!", e);
        }
        return null;
    }

    @Override
    public boolean remove(String key) {
        try {
            return expiringCacheService.clearCachedValue(cacheName, key);
        } catch (ServiceException e) {
            log.error("The remove method is down!", e);
        }
        return false;
    }

    @Override
    public void removeAll() {
        try {
            expiringCacheService.clearAll(cacheName);
        } catch (ServiceException e) {
            log.error("The remove method is down!", e);
        }
    }

    @Override
    public long getEntryExpiration(String key) {
        try {
            return expiringCacheService.getEntryExpiration(cacheName, key);
        }
        catch (ServiceException e){
            log.error("The remove method is down!", e);
        }
        return 0;
    }
}
