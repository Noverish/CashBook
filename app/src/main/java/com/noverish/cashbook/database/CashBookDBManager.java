package com.noverish.cashbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.noverish.cashbook.other.MoneyUsageItem;

import java.util.Calendar;
import java.util.HashMap;

public class CashBookDBManager {
    private AccountDBManager accountDBManager;
    private CategoryDBManager categoryDBManager;
    private SQLiteDatabase database = null;

    public static final String DATABASE_NAME = "cashbook.db";
    public static final String DATABASE_TABLE_NAME = "MoneyTable";

    public static final String DATE_COLUMN = "date";
    public static final String CLASSIFICATION_COLUMN = "classification";
    public static final String AMOUNT_COLUMN = "amount";
    public static final String CONTENT_COLUMN = "content";
    public static final String PLACE_COLUMN = "place";
    public static final String ACCOUNT_ID_COLUMN = "accountId";
    public static final String CATEGORY_ID_COLUMN = "categoryId";
    public static final String MEMO_COLUMN = "memo";

    private Context context = null;
    private final String TAG = this.getClass().getSimpleName();

    public static CashBookDBManager cashBookDBManager = null;
    public static CashBookDBManager getCashBookDBManager(Context context) {
        if(cashBookDBManager == null) {
            cashBookDBManager = new CashBookDBManager(context);
        }
        return cashBookDBManager;
    }

    private CashBookDBManager(Context context) {
        this.context = context;
        this.accountDBManager = AccountDBManager.getAccountManager(context);
        this.categoryDBManager = CategoryDBManager.getCategoryManager(context);
        //context.deleteDatabase(DATABASE_NAME);

        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        database.execSQL("create table if not exists " + DATABASE_TABLE_NAME + "(" +
                " _id integer PRIMARY KEY autoincrement, " +
                DATE_COLUMN + " integer, " +
                CLASSIFICATION_COLUMN + " integer, " +
                AMOUNT_COLUMN + " integer, " +
                CONTENT_COLUMN + " text, " +
                PLACE_COLUMN + " text, " +
                ACCOUNT_ID_COLUMN + " int, " +
                CATEGORY_ID_COLUMN + " int, " +
                MEMO_COLUMN + " text);");

        //printAllDatabase();

    }

    public int insert(MoneyUsageItem item, boolean changeBalance) {
        if(changeBalance)
            realizeMoneyUsageItem(item);

        ContentValues recordValues = moneyUsageItemToContentValues(item);

        Log.e(TAG, "insert " + item.toString());

        int retur =  (int) database.insert(DATABASE_TABLE_NAME, null, recordValues);

        return retur;
    }

    public void modify(int id, MoneyUsageItem changeItem, boolean changeBalance) {
        if(changeBalance) {
            MoneyUsageItem item = getItem(id);
            item.setAmount(-item.getAmount());
            realizeMoneyUsageItem(item);
            realizeMoneyUsageItem(changeItem);
        }

        ContentValues cv = moneyUsageItemToContentValues(changeItem);

        database.update(DATABASE_TABLE_NAME, cv, "_id=" + id, null);
    }

    public boolean delete(int id, boolean changeBalance) {
        if(changeBalance) {
            MoneyUsageItem item = getItem(id);
            item.setAmount(-item.getAmount());
            realizeMoneyUsageItem(item);
        }

        Log.e(TAG, "delete " + getItem(id).toString());

        boolean bool =  database.delete(DATABASE_TABLE_NAME, "_id = " + id, null) > 0;

        return bool;
    }


    private void realizeMoneyUsageItem(MoneyUsageItem item) {
        if(item.getClassification() == MoneyUsageItem.TRANSFER) {
            int fromAccountID, toAccountID = item.getCategoryIdOrToAccountID();
            long amount = item.getAmount();

            if(item.getAccountId() < 0)
                fromAccountID = -item.getAccountId();
            else {
                String[] tmp = accountDBManager.getBankAccountCardSet(item.getAccountId());
                fromAccountID = accountDBManager.getAccountID(tmp[0], tmp[1]);
            }

            accountDBManager.addBalance(fromAccountID, -amount);
            accountDBManager.addBalance(toAccountID, amount);

            Log.e(TAG, "FromAccountID is " + fromAccountID + ", amount is " + -amount);
            Log.e(TAG, "ToAccountID is " + toAccountID + ", amount is " + amount);
        } else if(item.getClassification() == MoneyUsageItem.EXPENDITURE) {
            int accountID;
            long amount = item.getAmount();

            if(item.getAccountId() < 0) {
                accountID = -item.getAccountId();
            } else {
                String[] tmp = accountDBManager.getBankAccountCardSet(item.getAccountId());
                accountID = accountDBManager.getAccountID(tmp[0], tmp[1]);
            }

            Log.e(TAG,"account ID : " + accountID + " account name : " + accountDBManager.getAccountName(accountID) + " amount : " + amount);
            Log.e(TAG,"before balance : " + accountDBManager.getBalance(accountID));

            accountDBManager.addBalance(accountID, -amount);

            Log.e(TAG, "after balance : " + accountDBManager.getBalance(accountID));
        } else {
            int accountID;
            long amount = item.getAmount();

            if(item.getAccountId() < 0) {
                accountID = -item.getAccountId();
            } else {
                String[] tmp = accountDBManager.getBankAccountCardSet(item.getAccountId());
                accountID = accountDBManager.getAccountID(tmp[0], tmp[1]);
            }

            Log.e(TAG,"account ID : " + accountID + " account name : " + accountDBManager.getAccountName(accountID) + " amount : " + amount);
            Log.e(TAG,"before balance : " + accountDBManager.getBalance(accountID));

            accountDBManager.addBalance(accountID, amount);

            Log.e(TAG, "after balance : " + accountDBManager.getBalance(accountID));
        }
    }


    public int getID(MoneyUsageItem item) {
        long milli = item.getDate();

        String query = "SELECT * FROM " + DATABASE_TABLE_NAME + " WHERE date = " + milli;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0) {
            cursor.close();
            Log.e("NameNotFoundException", "There is no " + milli + " in CashBookDatabase!");
            printAllDatabase();
            return 0;
        }

        cursor.moveToNext();
        int itemID = cursor.getInt(0);
        cursor.close();

        return itemID;
    }

    public MoneyUsageItem getItem(int id) {
        String query = "SELECT  * FROM " + DATABASE_TABLE_NAME + " WHERE _id = " + id ;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0) {
            cursor.close();
            Log.e("getItem","There is no " + id + "in CashBookDatabase!");
            printAllDatabase();
            return null;
        }

        cursor.moveToNext();

        long date = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_COLUMN));
        int classification = cursor.getInt(cursor.getColumnIndexOrThrow(CLASSIFICATION_COLUMN));
        int amount = cursor.getInt(cursor.getColumnIndexOrThrow(AMOUNT_COLUMN));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT_COLUMN));
        String place = cursor.getString(cursor.getColumnIndexOrThrow(PLACE_COLUMN));
        int accountId = cursor.getInt(cursor.getColumnIndexOrThrow(ACCOUNT_ID_COLUMN));
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORY_ID_COLUMN));
        String memo = cursor.getString(cursor.getColumnIndexOrThrow(MEMO_COLUMN));

        return new MoneyUsageItem(date, classification, amount, content, place, accountId, categoryId, memo);
    }


    public HashMap<Integer, Long> getAmountOfBigCategory(int classificationPara, int year, int month) {
        HashMap<Integer, Long> ans = new HashMap<>();
        for(int id : categoryDBManager.getAllBigCategoryId(classificationPara))
            ans.put(id, 0L);

        Cursor cursor = getItemWithMonth(year, month);

        int size = cursor.getCount();
        for(int i = 0;i < size;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_COLUMN));
            int classification = cursor.getInt(cursor.getColumnIndexOrThrow(CLASSIFICATION_COLUMN));
            long amount = cursor.getLong(cursor.getColumnIndexOrThrow(AMOUNT_COLUMN));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT_COLUMN));
            String place = cursor.getString(cursor.getColumnIndexOrThrow(PLACE_COLUMN));
            int accountId = cursor.getInt(cursor.getColumnIndexOrThrow(ACCOUNT_ID_COLUMN));
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORY_ID_COLUMN));
            String memo = cursor.getString(cursor.getColumnIndexOrThrow(MEMO_COLUMN));

            if(classification == classificationPara) {
                int bigCategoryId = categoryDBManager.getBigCategoryIdFromSmallCategoryId(classification, categoryId);

                ans.put(bigCategoryId, ans.get(bigCategoryId) + amount);
            }
        }

        return ans;
    }

    public Cursor getItemWithDay(int year, int month, int day) {
        HashMap<Integer, MoneyUsageItem> ans = new HashMap<>();

        month -= 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        long start = calendar.getTimeInMillis();
        calendar.set(year, month, day, 23, 59, 59);
        long end = calendar.getTimeInMillis();

        String query = "SELECT * FROM " + DATABASE_TABLE_NAME + " WHERE " + DATE_COLUMN + " BETWEEN " + start + " AND " + end + " ORDER BY " + DATE_COLUMN + " ASC;";
        return database.rawQuery(query, null);
/*
        int size = cursor.getCount();
        for(int i = 0;i < size;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_COLUMN));
            int classification = cursor.getInt(cursor.getColumnIndexOrThrow(CLASSIFICATION_COLUMN));
            int amount = cursor.getInt(cursor.getColumnIndexOrThrow(AMOUNT_COLUMN));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT_COLUMN));
            String place = cursor.getString(cursor.getColumnIndexOrThrow(PLACE_COLUMN));
            int accountId = cursor.getInt(cursor.getColumnIndexOrThrow(ACCOUNT_ID_COLUMN));
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORY_ID_COLUMN));
            String memo = cursor.getString(cursor.getColumnIndexOrThrow(MEMO_COLUMN));

            MoneyUsageItem item = new MoneyUsageItem(date, classification, amount, content, place, accountId, categoryId, memo);
            ans.put(id, item);
        }

        return ans;*/
    }

    public Cursor getItemWithMonth(int year, int month) {
        month -= 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 0, 0, 0);
        long start = calendar.getTimeInMillis();

        int maximumDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.set(year, month, maximumDayOfMonth, 23, 59, 59);
        long end = calendar.getTimeInMillis();

        String query = "SELECT * FROM " + DATABASE_TABLE_NAME + " WHERE " + DATE_COLUMN + " BETWEEN " + start + " AND " + end + " ORDER BY " + DATE_COLUMN + " ASC;";
        return database.rawQuery(query, null);
    }

    public Cursor getItemWithCategoryAndMonth(int bigCategoryId, int year, int month) {
        return null;
    }

    private ContentValues moneyUsageItemToContentValues(MoneyUsageItem item) {
        ContentValues recordValues = new ContentValues();

        recordValues.put(DATE_COLUMN, item.getDate());
        recordValues.put(CLASSIFICATION_COLUMN, item.getClassification());
        recordValues.put(AMOUNT_COLUMN, item.getAmount());
        recordValues.put(CONTENT_COLUMN, item.getContent());
        recordValues.put(PLACE_COLUMN, item.getPlace());
        recordValues.put(ACCOUNT_ID_COLUMN, item.getAccountId());
        recordValues.put(CATEGORY_ID_COLUMN, item.getCategoryIdOrToAccountID());
        recordValues.put(MEMO_COLUMN, item.getMemo());

        return recordValues;
    }


    public boolean isItAdded(MoneyUsageItem item) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.getDate());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Cursor cursor = getItemWithDay(year, month, day);

        int count = cursor.getCount();
        for(int i = 0;i < count;i++) {
            cursor.moveToNext();

            long date = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_COLUMN));
            Calendar dbCalendar = Calendar.getInstance();
            dbCalendar.setTimeInMillis(date);

            if(dbCalendar.get(Calendar.HOUR_OF_DAY) == calendar.get(Calendar.HOUR_OF_DAY))
                if(dbCalendar.get(Calendar.MINUTE) == calendar.get(Calendar.MINUTE))
                    if(cursor.getLong(cursor.getColumnIndexOrThrow(AMOUNT_COLUMN)) == item.getAmount())
                        return true;

        }
        return false;
    }

    public int printAllDatabase() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_NAME;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        for(int i = 0;i < cnt;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            long date = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_COLUMN));
            int classification = cursor.getInt(cursor.getColumnIndexOrThrow(CLASSIFICATION_COLUMN));
            int amount = cursor.getInt(cursor.getColumnIndexOrThrow(AMOUNT_COLUMN));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT_COLUMN));
            String place = cursor.getString(cursor.getColumnIndexOrThrow(PLACE_COLUMN));
            int accountId = cursor.getInt(cursor.getColumnIndexOrThrow(ACCOUNT_ID_COLUMN));
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(CATEGORY_ID_COLUMN));
            String memo = cursor.getString(cursor.getColumnIndexOrThrow(MEMO_COLUMN));

            MoneyUsageItem item = new MoneyUsageItem(date, classification, amount, content, place, accountId, categoryId, memo);
            Log.e(TAG,"ID = " + id + ", " + item.toString());
        }

        cursor.close();
        return cnt;
    }

    public void decreaseAllCategory() {
        for(int id = 0;id<500;id++) {

            MoneyUsageItem item = getItem(id);
            if(item != null) {
                if(item.getClassification() < 2) {
                    item.setCategoryIdOrToAccountID(item.getCategoryIdOrToAccountID() - 1);
                    ContentValues cv = moneyUsageItemToContentValues(item);
                    database.update(DATABASE_TABLE_NAME, cv, "_id=" + id, null);

                    Log.e(TAG,"update cashbook id : " + id + " category : " + item.getCategoryIdOrToAccountID());
                }
            }
        }
    }

    private void comment() {
        /*public MoneyUsageItem execute(int id) {
        Log.e("CashBookDBManager","execute " + id + " database");

        String SQL = "select date, classification, amount, content, place, bigCategory, smallCategory "
                + " from " + DATABASE_ACCOUNT_TABLE_NAME
                + " where _id = ?";
        String[] args = {String.valueOf(id)};

        Cursor cursor = database.rawQuery(SQL, args);

        cursor.moveToNext();

        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(cursor.getInt(0));
        int classification = cursor.getInt(1);
        int amount = cursor.getInt(2);
        String content = cursor.getString(3);
        String place = cursor.getString(4);
        String bigCategroy = cursor.getString(5);
        String smallCategory = cursor.getString(6);

        cursor.close();

        return new MoneyUsageItem(id, date, classification, amount, content, place, bigCategroy, smallCategory);

    }*/

    /*public void insertRecord(MoneyUsageItem item) {
        database.execSQL("insertSmallCategory into" + DATABASE_ACCOUNT_TABLE_NAME +
                "(date, classification, amount, content, place, bigCategory, smallCategory) values (" +
                item.getDate().getTimeInMillis() + ", " +
                item.getClassification() + ", " +
                item.getAmount() + ", " +
                "'" + item.getContent() + "', " +
                "'" + item.getPlace() + "', " +
                "'" + item.getCategoryIdOrToAccountID() + "', " +
                "'" + item.getSmallCategoryId() + "');");
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs,
                        String groupBy, String having, String orderBy) {
        return database.query(DATABASE_ACCOUNT_TABLE_NAME,
                columns, selection, selectionArgs, groupBy, having, orderBy);
    }




    private boolean openDatabase() {
        helper = new CashBookDBHelper(this);
        database = helper.getWritableDatabase();

        return true;
    }

    private void executeRawQuery() {
        Cursor c1 = database.rawQuery("select count(*) as Total from " + DATABASE_ACCOUNT_TABLE_NAME, null);

        c1.moveToNext();
        c1.close();
    }

    private void executeRawQueryParam() {
        String SQL = "select content "
                + " from " + DATABASE_ACCOUNT_TABLE_NAME
                + " where classification > ?";
        String[] args = {"0"};

        Cursor c1 = database.rawQuery(SQL, args);
        int recordCount = c1.getCount();

        for(int i = 0; i < recordCount; i++) {
            c1.moveToNext();
            String content = c1.getString(0);

            Log.e("asdf", content);
        }

        c1.close();
    }*/

/*
    package com.noverish.cashbook.Other;

    public class CashBookDBHelper {extends SQLiteOpenHelper

    {

    public CashBookDBHelper(Context context) {
        super(context, CashBookDBManager.DATABASE_NAME, null, CashBookDBManager.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String DROP_SQL = "drop table if exists " + CashBookDBManager.DATABASE_ACCOUNT_TABLE_NAME;
            db.execSQL(DROP_SQL);
        } catch (Exception ex) {
            Log.e("CashBookDBHelper","Exception in DROP_SQL");
        }

        try {
            String CREATE_SQL = "create table" + CashBookDBManager.DATABASE_ACCOUNT_TABLE_NAME + "(" +
                    " _id integer PRIMARY KEY autoincrement, " +
                    "date integer, " +
                    "classification integer, " +
                    "amount integer, " +
                    "content text, " +
                    "place text, " +
                    "bigCategory text, " +
                    "smallCategory text);";
            db.execSQL(CREATE_SQL);
        } catch (Exception ex) {
            Log.e("CashBookDBHelper","Exception in CREATE_SQL");
        }
    }

    public void onOpen(SQLiteDatabase db) {
        Log.e("CashBookDBHelper","opened database [" + CashBookDBManager.DATABASE_NAME + "].");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("CashBookDBHelper", "Upgrading database from version" + oldVersion + " to " + newVersion)
    }
    }

*/
    }
}
