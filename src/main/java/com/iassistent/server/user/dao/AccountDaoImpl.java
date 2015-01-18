package com.iassistent.server.user.dao;

import com.iassistent.server.user.bean.Account;
import com.iassistent.server.util.dao.BaseDao;
import com.iassistent.server.util.dao.BaseDaoImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by cwr.yingsheng.feng on 2015.1.2 0002.
 */
public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {



    @Override
    public Account selectAccount(final Account account) throws Exception {
        final String selectSQL = "select * from users where username=? and password=?";
        Account myAccount = new Account();
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1,account.getUsername());
                preparedStatement.setString(2,account.getPassword());
            }
        };
        ResultSetExtractor<Account> res = new ResultSetExtractor<Account>() {
            @Override
            public Account extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Account account1 = new Account();
                while(resultSet.next()){
                    account1.setUsername(resultSet.getString("username"));
                    account1.setPassword(resultSet.getString("password"));
                }
                return account1;
            }
        };
        myAccount = (Account)this.getJdbcTemplate().query(selectSQL, pss, res);
        System.out.println(myAccount.getUsername()+"|"+myAccount.getPassword());
        return myAccount;
    }

    @Override
    public int createAccount(final Account account) throws Exception {
        final String insertSQL = "insert into table users ( username, password, email, fullname) values (?,?,?,?)";
        PreparedStatementSetter pss = new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, account.getUsername());
                preparedStatement.setString(2, account.getPassword());
                preparedStatement.setString(3, account.getEmail());
                preparedStatement.setString(4, account.getFullname());
            }
        };
        return this.getJdbcTemplate().update(insertSQL,pss);
    }
}
