package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransactionController {

    private UserDAO userDAO;
    private AccountDAO accountDAO;
    private TransferDAO transferDAO;

    public TransactionController(UserDAO userDAO, AccountDAO accountDAO, TransferDAO transferDAO) {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.transferDAO = transferDAO;
    }

    //get the balance of logged in user
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/balance")
    public BigDecimal getBalance(Principal principal) {
        Long userId = getCurrentUserId(principal);
        return accountDAO.getBalance(userId);
    }

    //list users
    @PreAuthorize("hasRole('USER')")
    @RequestMapping (path = "/users", method = RequestMethod.GET)
    public List<User> listAllUsers(){
        return userDAO.findAll();
    }

    //make a transfer. send money.
    @PreAuthorize("hasRole('USER')")
    @RequestMapping (path = "/transfer", method = RequestMethod.POST)
    public String transfer(@Valid @RequestBody TransferDTO transferDTO){
        Long userFromId = transferDTO.getUserFromId();
        Long userToId = transferDTO.getUserToId();
        BigDecimal amount = transferDTO.getAmount();
        return transferDAO.transferMoney(userFromId, userToId, amount);
    }

    //view all transfers for logged in user.
    @PreAuthorize("hasRole('USER')")
    @RequestMapping (path = "/transfers/view", method = RequestMethod.GET)
    public List<Transfer> viewMyTransfers(Principal principal){
        Long userId = getCurrentUserId(principal);
        return transferDAO.viewTransfers(userId);
    }

    //view transfer details.
    @PreAuthorize("hasRole('USER')")
    @RequestMapping (path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer viewDetailsOfTransfer(@PathVariable Long id){
        return transferDAO.viewTransfersByID(id);
    }



    //get current logged in user
    private Long getCurrentUserId(Principal principal) {
        User user = userDAO.findByUsername(principal.getName());
        Long userId = user.getId();
        return userId;
    }

}