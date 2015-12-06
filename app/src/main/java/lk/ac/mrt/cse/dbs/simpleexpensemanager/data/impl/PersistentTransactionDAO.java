package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Chanaka on 6/12/2015.
 */
public class PersistentTransactionDAO implements TransactionDAO {
    private DatabaseHandler dbHandler;
    public PersistentTransactionDAO(DatabaseHandler dbHandler){
        this.dbHandler=dbHandler;
    }
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        dbHandler.storeTransaction(new Transaction(date,accountNo,expenseType,amount));
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return dbHandler.getTransactions();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions=dbHandler.getTransactions();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
}
