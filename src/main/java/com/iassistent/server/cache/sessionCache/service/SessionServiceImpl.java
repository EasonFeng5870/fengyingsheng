package com.iassistent.server.cache.sessionCache.service;

import com.iassistent.server.cache.sessionCache.SessionCacheService;
import com.iassistent.server.user.bean.Session;
import com.iassistent.server.cache.sessionCache.SessionService;
import com.iassistent.server.user.bean.GateWayUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public class SessionServiceImpl implements SessionService {

    private SessionCacheService<Session> cacheService;

    @Override
    public Session getSession(String token) {
        return cacheService.get(token);
    }

    @Override
    public Session create(GateWayUser user, String apiKey, GrantedAuthority authority) {
        String token = UUID.randomUUID().toString();
        Session session = new Session.SessionBuilder().setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setAccountId(user.getAccountId())
                .setAuthToken(token)
                .setCreateTime(new Date())
                .setLastAccessTime(new Date())
                .setPrincipal(user.getUsername())
                .setAuthority(authority)
                .build();
        cacheService.set(token, session);
        return session;
    }

    @Override
    public boolean invalidate(String token) {
        return cacheService.remove(token);
    }

    @Override
    public void updateSessionWithCapabilities(String token, List<String> capabilities) {
        Session session = cacheService.get(token);

        Session.Authentication authentication = session.getAuthentication();
        authentication.setCapabilities(capabilities);

        cacheService.set(token, session);
    }

    @Override
    public void updateSessionInCache(String token, Session session) {
        cacheService.set(token, session);
    }

    @Override
    public long getExpirationInMillis() {
        return cacheService.getExpiration();
    }

    public void setCacheService(final SessionCacheService cacheService) {
        this.cacheService = cacheService;
    }
}
