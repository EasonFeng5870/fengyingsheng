package com.iassistent.server.user.model;

/**
 * Created by cwr.yingsheng.feng on 2015.1.16 0016.
 */
public class UserAttempts {

    private int id;
    private String username;
    private int attempts;
    private String lastModified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
