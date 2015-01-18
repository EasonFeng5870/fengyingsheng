package com.iassistent.server.user.bean;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.*;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public class Session implements Serializable {

    private static final long serialVersionUID = -3474395522860793731L;

    private String username;
    private String password;
    private String apiKey;
    private Date createTime;
    private Date lastAccessTime;
    private Authentication authentication;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    Session(SessionBuilder sb) {
        this.username = sb.username;
        this.password = sb.password;
        this.apiKey = sb.apiKey;
        this.createTime = sb.createTime;
        this.lastAccessTime = sb.lastAccessTime;

        // create authentication object during session creation
        Authentication auth = new Authentication();
        auth.accountId = sb.accountId;
        auth.endpointId = sb.endpointId;
        auth.endpointIdSet = sb.endpointIdSet;
        auth.domainName = sb.domainName;
        auth.authToken = sb.authToken;
        auth.principal = sb.principal;
        auth.authority = sb.authority;
        auth.authorities = new ArrayList<GrantedAuthority>();
        auth.authorities.add(sb.authority);
        auth.setAuthenticated(true);

        this.authentication = auth;
    }

    public final class Authentication implements Serializable, org.springframework.security.core.Authentication  {

        private static final long serialVersionUID = 4128926681612861327L;
        private String accountId;
        private String endpointId;
        private Set<String> endpointIdSet;
        private String domainName;
        private String authToken;
        private String principal;
        private GrantedAuthority authority;
        private List<String> capabilities;
        private String details="";
        private String credentials="";
        private boolean authenticated;
        private List <GrantedAuthority>authorities;


        /**
         * Made it non-instantiable outside of this class.
         */
        private Authentication() {}

        public String getAccountId() {
            return accountId;
        }

        public String getEndpointId() {
            return endpointId;
        }

        public Set<String> getEndpointIdSet() {
            if (endpointIdSet==null){
                return Collections.emptySet();
            }
            else {
                return endpointIdSet;
            }
        }

        public void setCurrentSelectedEndpoint(String requestedId){
            this.endpointId=requestedId;
        }

        public String getDomainName() {
            return domainName;
        }

        public String getAuthToken() {
            return authToken;
        }

        public String getPrincipal() {
            return principal;
        }

        public GrantedAuthority getAuthority() {
            return authority;
        }
        @Override
        public Collection<GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getName() {
            return getPrincipal();
        }

        @Override
        public Object getCredentials() {
            return credentials;
        }

        @Override
        public Object getDetails() {
            return details;
        }

        @Override
        public boolean isAuthenticated() {
            return this.authenticated;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            this.authenticated = isAuthenticated;
        }

        public List<String> getCapabilities() {
            return capabilities;
        }

        public void setCapabilities(List<String> capabilities) {
            this.capabilities = capabilities;
        }

    }


    public static final class SessionBuilder {
        private String username;
        private String password;
        private String apiKey;
        private Date createTime;
        private Date lastAccessTime;
        private String accountId;
        private String endpointId;
        private Set<String> endpointIdSet;
        private String domainName;
        private String authToken;
        private String principal;
        private GrantedAuthority authority;

        public SessionBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public SessionBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public SessionBuilder setApiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public SessionBuilder setCreateTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public SessionBuilder setLastAccessTime(Date lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
            return this;
        }

        public SessionBuilder setAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public SessionBuilder setEndpointId(String endpointId) {
            this.endpointId = endpointId;
            return this;
        }

        public SessionBuilder setEndpointIdSet(Set<String> ids) {
            this.endpointIdSet = ids;
            return this;
        }

        public SessionBuilder setDomainName(String domainName) {
            this.domainName = domainName;
            return this;
        }

        public SessionBuilder setAuthToken(String authToken) {
            this.authToken = authToken;
            return this;
        }

        public SessionBuilder setPrincipal(String principal) {
            this.principal = principal;
            return this;
        }

        public SessionBuilder setAuthority(GrantedAuthority authority) {
            this.authority = authority;
            return this;
        }

        public Session build() {
            return new Session(this);
        }
    }

}
