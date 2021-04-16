package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDAO implements AccountDAO{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getBalance(Long userId) {
        return getAccountByUserId(userId).getBalance();
    }

    @Override
    public Long getUserIdFromAccountId(Long accountId) {
        Account account = null;
        String sql = "SELECT balance, account_id, user_id " +
                "FROM accounts " +
                "WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);

        while (results.next()){
            account = mapRowToAccount(results);
        }
        return account.getUserId();
    }

    @Override
    public Account getAccountByUserId(Long userId) {
        Account account = null;
        String sql = "SELECT balance, account_id, user_id " +
                "FROM accounts " +
                "WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()){
            account = mapRowToAccount(results);
        }
        return account;
    }



    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getLong("account_id"));
        account.setUserId(rs.getLong("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));

        return account;
    }

}
