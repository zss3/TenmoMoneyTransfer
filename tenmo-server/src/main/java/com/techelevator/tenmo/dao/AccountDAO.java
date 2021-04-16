package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDAO {

    BigDecimal getBalance(Long userId);

    Long getUserIdFromAccountId(Long accountId);

    Account getAccountByUserId(Long userId);


}
