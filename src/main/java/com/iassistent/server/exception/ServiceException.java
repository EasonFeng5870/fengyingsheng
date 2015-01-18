package com.iassistent.server.exception;

/**
 * Created by cwr.yingsheng.feng on 2015.1.5 0005.
 */
public class ServiceException extends Exception {

    public ServiceException(String message){
        super(message);
    }

    public ServiceException(String message, Throwable t){
        super(message,t);
    }

    public ServiceException(Throwable t){
        super(t);
    }
}
