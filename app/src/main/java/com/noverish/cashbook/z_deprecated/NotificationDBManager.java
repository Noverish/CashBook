package com.noverish.cashbook.z_deprecated;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.noverish.cashbook.database.CashBookDBManager;
import com.noverish.cashbook.other.MoneyUsageItem;

public class NotificationDBManager {
    private CashBookDBManager cashBookDBManager;
    private static NotificationDBManager notificationDBManager = null;
    public static NotificationDBManager getNotificationDBManager(Context context) {
        if(notificationDBManager == null) {
            notificationDBManager = new NotificationDBManager(context);
        }
        return notificationDBManager;
    }

    public SQLiteDatabase database = null;

    public static final String DATABASE_NAME = "notification.db";
    public static final String DATABASE_TABLE_NAME = "notification_table";

    public static final String STATEMENT_COLUMN = "statement";
    public static final String DATE_COLUMN = "date";
    public static final String AMOUNT_COLUMN = "amount";
    public static final String CONTENT_COLUMN = "content";
    public static final String ACCOUNT_NUMBER_COLUMN = "account";
    public static final String BALANCE_COLUMN = "balance";
    public static final String ALL_TEXT_COLUMN = "all_text";

    private String TAG = this.getClass().getSimpleName();

    private NotificationDBManager(Context context) {
        //context.deleteDatabase(DATABASE_NAME);


        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        database.execSQL("create table if not exists " + DATABASE_TABLE_NAME + "(" +
                " _id integer PRIMARY KEY autoincrement, " +
                STATEMENT_COLUMN + " text, " +
                DATE_COLUMN + " integer, " +
                AMOUNT_COLUMN + " integer, " +
                CONTENT_COLUMN + " text, " +
                ACCOUNT_NUMBER_COLUMN + " text, " +
                BALANCE_COLUMN + " integer, " +
                ALL_TEXT_COLUMN + " text);");

        printAllDatabase();

        cashBookDBManager = CashBookDBManager.getCashBookDBManager(context);
    }


    public int insert(NotificationItem item) {
        Log.e(TAG, "insertSmallCategory : " + item.toString());

        ContentValues recordValues = notificationItemToContentValues(item);

        return (int) database.insert(DATABASE_TABLE_NAME, null, recordValues);
    }

    public void modify(int id, NotificationItem item) {
        Log.e(TAG,"ID : " + id + " - modify : " + item.toString());

        ContentValues recordValues = notificationItemToContentValues(item);

        database.update(DATABASE_TABLE_NAME, recordValues, " _id = " + id, null);
    }

    public boolean delete(int id) {
        Log.e(TAG, "ID : " + id + " - delete");

        return database.delete(DATABASE_TABLE_NAME, " _id = " + id, null) > 0;
    }


    public void saveNotificationToMoneyUsage() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_NAME;
        Cursor cursor = database.rawQuery(countQuery, null);

        int cnt = cursor.getCount();

        for (int i = 0;i < cnt;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String statement = cursor.getString(cursor.getColumnIndexOrThrow(STATEMENT_COLUMN));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_COLUMN));
            long amount = cursor.getLong(cursor.getColumnIndexOrThrow(AMOUNT_COLUMN));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT_COLUMN));
            int accountId = cursor.getInt(cursor.getColumnIndexOrThrow(ACCOUNT_NUMBER_COLUMN));
            long balance = cursor.getLong(cursor.getColumnIndexOrThrow(BALANCE_COLUMN));
            String allText = cursor.getString(cursor.getColumnIndexOrThrow(ALL_TEXT_COLUMN));

            int classification;

            if(statement.equals(NotificationItem.WITHDRAW))
                classification = MoneyUsageItem.EXPENDITURE;
            else if(statement.equals(NotificationItem.DEPOSIT))
                classification = MoneyUsageItem.INCOME;
            else {
                classification = MoneyUsageItem.TRANSFER;
            }

            MoneyUsageItem moneyUsageItem = new MoneyUsageItem(date, classification, amount, content, content, -accountId, 1, "");
            NotificationItem notificationItem = new NotificationItem(statement, date, amount, content, accountId, balance, allText);

            cashBookDBManager.insert(moneyUsageItem, true);

            Log.e(TAG, "insert " + moneyUsageItem);
            Log.e(TAG,"delete " + notificationItem);

            delete(id);
        }

        cursor.close();
    }

    public int printAllDatabase() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_NAME;
        Cursor cursor = database.rawQuery(countQuery, null);

        int cnt = cursor.getCount();

        for (int i = 0;i < cnt;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String statement = cursor.getString(cursor.getColumnIndexOrThrow(STATEMENT_COLUMN));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_COLUMN));
            long amount = cursor.getLong(cursor.getColumnIndexOrThrow(AMOUNT_COLUMN));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT_COLUMN));
            int accountId = cursor.getInt(cursor.getColumnIndexOrThrow(ACCOUNT_NUMBER_COLUMN));
            long balance = cursor.getLong(cursor.getColumnIndexOrThrow(BALANCE_COLUMN));
            String allText = cursor.getString(cursor.getColumnIndexOrThrow(ALL_TEXT_COLUMN));

            String tmp = (new NotificationItem(statement, date, amount, content, accountId, balance, allText)).toString();
            Log.e(TAG, "ID = " + id + ", " + tmp);
        }

        if(cnt == 0)
            Log.e(TAG,"DATABASE is EMPTY!!");

        cursor.close();
        return cnt;
    }

    public void resetDatabase() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_NAME;
        Cursor cursor = database.rawQuery(countQuery, null);

        int cnt = cursor.getCount();

        for (int i = 0;i < cnt;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

            delete(id);
        }

        cursor.close();
    }


    private ContentValues notificationItemToContentValues(NotificationItem item) {
        ContentValues recordValues = new ContentValues();

        recordValues.put(STATEMENT_COLUMN, item.getBankStatement());
        recordValues.put(DATE_COLUMN, item.getDate());
        recordValues.put(AMOUNT_COLUMN, item.getAmount());
        recordValues.put(CONTENT_COLUMN, item.getContent());
        recordValues.put(ACCOUNT_NUMBER_COLUMN, item.getAccountId());
        recordValues.put(BALANCE_COLUMN, item.getBalance());
        recordValues.put(ALL_TEXT_COLUMN, item.getAllText());

        return recordValues;
    }

}
