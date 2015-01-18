package com.iassistent.server.exception;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public class SessionException extends RuntimeException {

    public SessionException(String message){
        super(message);
    }

    public SessionException(Throwable cause){
        super(cause);
    }

}
