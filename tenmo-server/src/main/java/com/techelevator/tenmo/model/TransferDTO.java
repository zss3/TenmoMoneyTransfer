package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferDTO {

    @NotNull(message = "The field `userFromId` should not be blank.")
    private Long userFromId;
    @NotNull(message = "The field `userToId` should not be blank.")
    private Long userToId;
   @DecimalMin( value = "1.0", message = "The field `current bid` should be greater than 0.")
    private BigDecimal amount;



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
