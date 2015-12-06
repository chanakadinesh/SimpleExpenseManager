package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Chanaka on 6/12/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {



    public static final String DATABASE_NAME = "130051J.db";

    //Table accounts
    public static final String ACCOUNTS_TABLE_NAME = "accounts";
    public static final String COLUMN_ACCOUNT_NO = "accountNo";
    public static final String COLUMN_BANK_NAME = "bankName";
    public static final String COLUMN_ACCOUNT_HOLDER = "accountHolder";
    public static final String COLUMN_ACCOUNT_BALANCE = "accountBalance";

    //Table Transactions
    public static final String TRANSACTIONS_TABLE_NAME = "transactions";
    //public static final String COLUMN_ACCOUNT_NO = "accountNo";
    public static final String COLUMN_EXPENSE_TYPE = "expenseType";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_AMOUNT = "amount";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 3);
        Log.v(null, "database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+ACCOUNTS_TABLE_NAME+
                        " ("+
                        COLUMN_ACCOUNT_NO+" text primary key, "+
                        COLUMN_BANK_NAME+" text, "+
                        COLUMN_ACCOUNT_HOLDER+" text, "+
                        COLUMN_ACCOUNT_BALANCE+" real)"
        );
        db.execSQL(
                "CREATE TABLE " + TRANSACTIONS_TABLE_NAME + " (" +
                        "id integer primary key autoincrement," +
                        COLUMN_DATE + " text," +
                        COLUMN_ACCOUNT_NO + " text," +
                        COLUMN_EXPENSE_TYPE + " text," +
                        COLUMN_AMOUNT + " real," +
                        "FOREIGN KEY (" + COLUMN_ACCOUNT_NO + ") REFERENCES " + ACCOUNTS_TABLE_NAME + "(" + COLUMN_ACCOUNT_NO + "))"
        );
        Log.i(null, "Successfully tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS_TABLE_NAME);

        onCreate(db);
    }

    //Methods for transaction table
    public boolean storeTransaction(Transaction transaction){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_ACCOUNT_NO,transaction.getAccountNo());
        values.put(COLUMN_DATE,transaction.getDate().toString());
        values.put(COLUMN_EXPENSE_TYPE, transaction.getExpenseType().toString());
        values.put(COLUMN_AMOUNT, transaction.getAmount());
        db.insert(TRANSACTIONS_TABLE_NAME, null, values);
        db.close();
        Log.i(null, "Adding Treansactions Success");
        return true;
    }

    public List<Transaction> getTransactions(){
        List<Transaction> transactions =new LinkedList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TRANSACTIONS_TABLE_NAME,null);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if(cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                String accountNo = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NO));
                ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_TYPE)));
                double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                Date date=null;
                try {
                    date = sdf.parse(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                } catch (ParseException e) {
                }
                Transaction temp = new Transaction(date, accountNo, expenseType, amount);
                transactions.add(temp);
                cursor.moveToNext();
            }
            db.close();
        }else{
            Log.d(null,"Getting Transactions failed");
        }

        return transactions;
    }

    //Methods for accounts table
    public boolean addAccount(Account account){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_ACCOUNT_NO,account.getAccountNo());
        values.put(COLUMN_BANK_NAME,account.getBankName());
        values.put(COLUMN_ACCOUNT_HOLDER, account.getAccountHolderName());
        values.put(COLUMN_ACCOUNT_BALANCE,account.getBalance());
        db.insert(ACCOUNTS_TABLE_NAME, null, values);
        db.close();
        Log.i(null, "Adding accounts success");
        return true;
    }

    public List<String> getAccountNumbers(){
        List<String> accountNames=new ArrayList<String>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select "+COLUMN_ACCOUNT_NO+" from "+ACCOUNTS_TABLE_NAME,null);
        if(cursor.moveToFirst()){
            do{
                accountNames.add(cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NO)));
            }while (cursor.moveToNext());
        }else{
            Log.d(null,"Getting account numbers failed");
        }
        db.close();
        return accountNames;
    }

    public List<Account> getAccounts(){
        List<Account> accountNames=new ArrayList<Account>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select * from "+ACCOUNTS_TABLE_NAME,null);
        if(cursor.moveToFirst()){
            do{
                String accNo=cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NO));
                String bankName=cursor.getString(cursor.getColumnIndex(COLUMN_BANK_NAME));
                String accHolder=cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_HOLDER));
                Double balance=cursor.getDouble(cursor.getColumnIndex(COLUMN_ACCOUNT_BALANCE));
                accountNames.add(new Account(accNo,bankName,accHolder,balance));
            }while (cursor.moveToNext());
        }else{
            Log.d(null, "Getting accounts failed");
        }
        db.close();
        return accountNames;
    }

    public Account getAccount(String accNum){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select * from "+ACCOUNTS_TABLE_NAME+" where "+COLUMN_ACCOUNT_NO+"="+accNum,null);
        if(cursor==null || cursor.getCount()<=0){
            Log.d(null,"Getting account failed");
            return null;}
        String accNo=cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NO));
        String bankName=cursor.getString(cursor.getColumnIndex(COLUMN_BANK_NAME));
        String accHolder=cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_HOLDER));
        Double balance=cursor.getDouble(cursor.getColumnIndex(COLUMN_ACCOUNT_BALANCE));
        db.close();
        return new Account(accNo,bankName,accHolder,balance);
    }

    public boolean removeAccount(String accNum){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            return db.delete(ACCOUNTS_TABLE_NAME, COLUMN_ACCOUNT_NO + " = ? ", new String[]{accNum+""})>0;
        }catch (Exception e){return false;}
    }

    public boolean update(String accNum,ExpenseType expenseType, Double amount){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT " + COLUMN_ACCOUNT_BALANCE + " FROM " + ACCOUNTS_TABLE_NAME + " WHERE " + COLUMN_ACCOUNT_NO + "=?", new String[]{accNum + ""});
        if(cursor==null || cursor.getCount()<=0){
            Log.d("chanaka", "Updating balance failed");
            return false;
        }
        cursor.moveToFirst();
        double balance=Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_BALANCE)));

        //db.close();
        SQLiteDatabase db1=this.getWritableDatabase();

        switch (expenseType){
            case EXPENSE:
                balance=balance-amount;
                break;
            case INCOME:
                balance=balance+amount;
                break;
            default:
                break;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ACCOUNT_BALANCE,balance);
        boolean result=db1.update(ACCOUNTS_TABLE_NAME,contentValues, COLUMN_ACCOUNT_NO + " = ?",new String[]{accNum+""})>0;
        return true;
    }



}
