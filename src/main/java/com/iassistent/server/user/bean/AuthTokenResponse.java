package com.iassistent.server.user.bean;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public class AuthTokenResponse {

    private String authToken;
    private String accountId;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "AuthTokenResponse [authToken=" + authToken + ", accountId="
                + accountId + "]";
    }
}
