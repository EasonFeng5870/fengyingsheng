package com.iassistent.server.user.bean;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public class GateWayUser extends User {

    private String accountId;
    private String endpointId;
    private Set<String> endpointIdSet;
    private String domainId;
    private String domainName;
    private String role;
    private int permissions;
    private boolean accountInvalid;

    GateWayUser(String username,
                String password,
                boolean enabled,
                boolean accountNonExpired,
                boolean credentialsNonExpired,
                boolean accountNonLocked,
                Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    GateWayUser(Builder b) {
        this(b.username,
                b.password,
                b.enabled,
                b.accountNonExpired,
                b.credentialsNonExpired,
                b.accountNonLocked,
                AuthorityUtils.commaSeparatedStringToAuthorityList(b.role));

        this.accountId = b.accountId;
        this.endpointId = b.endpointId;
        this.endpointIdSet = b.endpointIdSet;
        this.domainId = b.domainId;
        this.domainName = b.domainName;
        this.role = b.role;
        this.permissions = b.permissions;
        this.accountInvalid = b.accountInvalid;
    }

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

    public void setCurrentSelectedEndpoint (String requestedId){
        if (this.endpointIdSet.contains(requestedId)){
            this.endpointId = requestedId;
        }
    }

    public String getDomainId() {
        return domainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getRole() {
        return role;
    }

    public int getPermissions() {
        return permissions;
    }

    public boolean isAccountInvalid() {
        return accountInvalid;
    }

    public static final class Builder {
        private String username;
        private boolean enabled;
        private boolean accountNonExpired;
        private boolean credentialsNonExpired;
        private boolean accountNonLocked;
        private String accountId;
        private String endpointId;
        public Set<String> endpointIdSet;
        private String domainId;
        private String domainName;
        private String password;
        private String role;
        private int permissions;
        private boolean accountInvalid;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder setAccountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
            return this;
        }

        public Builder setDomainName(String domainName) {
            this.domainName = domainName;
            return this;
        }

        public Builder setCredentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
            return this;
        }

        public Builder setAccountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
            return this;
        }

        public Builder setAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder setEndpointId(String endpointId) {
            this.endpointId = endpointId;
            return this;
        }

        public Builder setDomainId(String domainId) {
            this.domainId = domainId;
            return this;
        }

        public Builder setEndpointIdSet(Set<String> ids){
            this.endpointIdSet = Collections.synchronizedSet(new HashSet<String>(ids));
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setRole(String role) {
            this.role = role;
            return this;
        }

        public Builder setPermissions(int permissions) {
            this.permissions = permissions;
            return this;
        }

        public Builder setAccountInvalid(boolean accountInvalid) {
            this.accountInvalid = accountInvalid;
            return this;
        }

        public GateWayUser build() {
            return new GateWayUser(this);
        }
    }
}
