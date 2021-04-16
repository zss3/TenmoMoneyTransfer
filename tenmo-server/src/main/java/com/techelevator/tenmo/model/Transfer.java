package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private Long transferId;
    private String transferType;
    private String transferStatus;
    private String accountFromName;
    private String accountToName;
    private BigDecimal amount;

    public Transfer() { }

    public Transfer(Long transferId, String transferType, String transferStatus, String accountFromId, String accountToId, BigDecimal amount) {
       this.transferId = transferId;
       this.transferType = transferType;
       this.transferStatus = transferStatus;
       this.accountFromName = accountFromId;
       this.accountToName = accountToId;
       this.amount = amount;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getAccountFromName() {
        return accountFromName;
    }

    public void setAccountFromName(String accountFromName) {
        this.accountFromName = accountFromName;
    }

    public String getAccountToName() {
        return accountToName;
    }

    public void setAccountToName(String accountToName) {
        this.accountToName = accountToName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


}
