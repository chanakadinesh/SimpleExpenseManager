package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Chanaka on 6/12/2015.
 */
public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHandler dbHandler;
    public PersistentAccountDAO(DatabaseHandler dbHandler){
        this.dbHandler =dbHandler;
    }
    @Override
    public List<String> getAccountNumbersList() {
        return dbHandler.getAccountNumbers();
    }

    @Override
    public List<Account> getAccountsList() {
        return dbHandler.getAccounts();
    }


    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return dbHandler.getAccount(accountNo);
    }

    @Override
    public void addAccount(Account account) {
        dbHandler.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        dbHandler.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        dbHandler.update(accountNo,expenseType,amount);
    }
}
