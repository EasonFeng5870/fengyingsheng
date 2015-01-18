package com.iassistent.server.cache.sessionCache;

import com.iassistent.server.user.bean.GateWayUser;
import com.iassistent.server.user.bean.Session;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public interface SessionService {

    /**
     * Get {@link com.iassistent.server.user.bean.Session} object.
     * @param token
     * @return {@link com.iassistent.server.user.bean.Session} object.
     */
    Session getSession(String token);

    /**
     * Create {@link Session} object
     * @param user {@link com.iassistent.server.user.bean.GateWayUser}.
     * @param apiKey api key used by caller.
     * @param authority {@link org.springframework.security.core.GrantedAuthority}.
     * @return {@link Session} object.
     */
    Session create(GateWayUser user, String apiKey, final GrantedAuthority authority);

    /**
     * Remove or invalidate the {@link Session} object from session.
     * @param token
     * @return <code>true</code> if it successfully removed, otherwise <code>false</code>
     */
    boolean invalidate(String token);

    /**
     * Updated session referenced by token with provided list of capabilities.
     * @param token
     * @param capabilities
     */
    public void updateSessionWithCapabilities(String token, List<String> capabilities);

    /**
     * Update Session object in cache using token as reference key
     * @param token
     * @param session
     */
    public void updateSessionInCache(String token, Session session);

    /**
     * Compute and return the expiry time.
     * @return expiry time in millis.
     */
    long getExpirationInMillis();

}
