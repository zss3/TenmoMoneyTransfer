package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransferDAO {

    private JdbcTemplate jdbcTemplate;
    private AccountDAO accountDAO;
    private static final String TRANSFER_INFO_QUERY = "SELECT DISTINCT transfers.transfer_id, amount, transfer_statuses.transfer_status_desc, transfer_types.transfer_type_desc, " +
            "(SELECT username FROM users WHERE users.user_id = (SELECT user_id FROM accounts  WHERE users.user_id = accounts.user_id AND accounts.account_id = transfers.account_from)) AS from_username, "+
            "(SELECT username FROM users WHERE users.user_id = (SELECT user_id FROM accounts  WHERE users.user_id = accounts.user_id AND accounts.account_id = transfers.account_to)) AS to_username "+
            "FROM transfers " +
            "INNER JOIN accounts ON accounts.account_id = transfers.account_to OR accounts.account_id = transfers.account_from " +
            "INNER JOIN users ON accounts.user_id = users.user_id "+
            "INNER JOIN transfer_types ON transfers.transfer_type_id = transfer_types.transfer_type_id "+
            "INNER JOIN transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id ";
    public JdbcTransferDAO(JdbcTemplate jdbcTemplate, AccountDAO accountDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDAO = accountDAO;
    }


    @Override
    public String transferMoney(Long userFromId, Long userToId, BigDecimal amount) {
        String sql = "SELECT balance " +
                "FROM accounts " +
                "WHERE user_id = ?;";
        SqlRowSet startingBalance = jdbcTemplate.queryForRowSet(sql, userFromId);
        BigDecimal accountFromBalance = BigDecimal.ZERO;
        while (startingBalance.next()) {
            accountFromBalance = new BigDecimal(startingBalance.getDouble("balance"));
        }

         if (accountFromBalance.compareTo(amount) >= 0) {
//subtract money from account below
            String sqlTransfer = "START TRANSACTION; "+
                    "UPDATE accounts " +
                    "SET balance = (balance - ?) " +
                    "WHERE user_id = ?; "+
//Add money to account below
                     "UPDATE accounts " +
                    "SET balance = (balance + ?) " +
                    "WHERE user_id = ?;"+
                    "COMMIT;";
            jdbcTemplate.update(sqlTransfer, amount, userFromId, amount, userToId);
//add transfer to transfers table
            addTransfer(2, 2, userFromId, userToId, amount);

            return "Approved";
        } else {
            //add transfer to transfers table
            addTransfer(2, 3, userFromId, userToId, amount);
            return "Not approved. Insufficient funds.";
        }
    }

    private void addTransfer(int transferTypeId, int transferStatusId, Long userFromId, Long userToId, BigDecimal amount) {
        //get account id from user id
        Long userToAccount = 0L;
        Long userFromAccount = 0L;

        userFromAccount = accountDAO.getAccountByUserId(userFromId).getAccountId();
        userToAccount = accountDAO.getAccountByUserId(userToId).getAccountId();

        //add new transfer data to transfers table
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) ;";
        jdbcTemplate.update(sql, transferTypeId, transferStatusId, userFromAccount, userToAccount, amount);
    }


    @Override
    public List<Transfer> viewTransfers(Long userId) {
        Transfer transfer = null;
        List<Transfer> listOfTransfers = new ArrayList<>();
        String sql = TRANSFER_INFO_QUERY +
                "WHERE account_from IN (SELECT account_id FROM accounts WHERE user_id = ?) OR account_to IN (SELECT account_id FROM accounts WHERE user_id = ?) ;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            transfer = mapRowToTransfer(results);
            listOfTransfers.add(transfer);
        }
        return listOfTransfers;
    }

    @Override
    public Transfer viewTransfersByID(Long transferId) {
        Transfer transfer = null;
        String sql = TRANSFER_INFO_QUERY +
                "WHERE transfers.transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        while (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getLong("transfer_id"));
        transfer.setTransferType(rs.getString("transfer_type_desc"));
        transfer.setTransferStatus(rs.getString("transfer_status_desc"));
        transfer.setAccountFromName(rs.getString("from_username"));
        transfer.setAccountToName(rs.getString("to_username"));
        transfer.setAmount(rs.getBigDecimal("amount"));

        return transfer;
    }
}