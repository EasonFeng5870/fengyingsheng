package com.iassistent.server.user.dao;

import com.iassistent.server.user.bean.Account;

/**
 * Created by cwr.yingsheng.feng on 2015.1.2 0002.
 */
public interface AccountDao {

    public Account selectAccount(Account account) throws Exception;

    public int createAccount(Account account) throws Exception;
}
