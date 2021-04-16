package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class TransferDTO {


    private Long userFromId;
    private Long userToId;
    private BigDecimal amount;

    public TransferDTO(Long userFromId, Long userToId, BigDecimal amount) {
        this.amount = amount;
        this.userFromId = userFromId;
        this.userToId = userToId;
    }


    public Long getUserFromId() {
        return userFromId;
    }

    public Long getUserToId() {
        return userToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setUserFromId(Long userFromId) {
        this.userFromId = userFromId;
    }

    public void setUserToId(Long userToId) {
        this.userToId = userToId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

