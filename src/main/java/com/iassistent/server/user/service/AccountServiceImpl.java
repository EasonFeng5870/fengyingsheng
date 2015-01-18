package com.iassistent.server.user.service;

import com.iassistent.server.user.bean.Account;
import com.iassistent.server.user.dao.AccountDao;

/**
 * Created by cwr.yingsheng.feng on 2015.1.2 0002.
 */
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public int createAccount(Account account) throws Exception {
        return accountDao.createAccount(account);
    }

    @Override
    public Account selectAccount(Account account) throws Exception {
        return accountDao.selectAccount(account);
    }
}
