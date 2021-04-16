package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDAO {

    public String transferMoney (Long userFromId, Long userToId, BigDecimal amount);

    public List<Transfer> viewTransfers(Long userId);

    public Transfer viewTransfersByID(Long transferId);

}
