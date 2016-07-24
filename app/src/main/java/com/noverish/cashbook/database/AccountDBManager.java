package com.noverish.cashbook.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.noverish.cashbook.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class AccountDBManager {
    private Context context;
    private HashMap<Integer, Bank> banks;
    private HashMap<Integer, Account> accounts;
    private HashMap<Integer, Card> cards;

    private static final String BANK_DATABASE = "bank";
    private static final String ACCOUNT_DATABASE = "account";
    private static final String CARD_DATABASE = "card";

    private static AccountDBManager accountDBManager;
    public static AccountDBManager getAccountManager(Context context) {
        if(accountDBManager == null) {
            accountDBManager = new AccountDBManager(context);
        }
        return accountDBManager;
    }

    private final String TAG = getClass().getSimpleName();
    public static final int DEFAULT_ACCOUNT_ID = 1;

    private AccountDBManager(Context context) {
        this.context = context;

        /*context.deleteFile(BANK_DATABASE);
        context.deleteFile(ACCOUNT_DATABASE);
        context.deleteFile(CARD_DATABASE);
*/
        loadDatabase();
    }


    private void saveDatabase() {
        context.deleteFile(BANK_DATABASE);
        try {
            FileOutputStream fos = context.openFileOutput(BANK_DATABASE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(banks);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e(TAG, "save banks failed");
        }

        context.deleteFile(ACCOUNT_DATABASE);
        try {
            FileOutputStream fos = context.openFileOutput(ACCOUNT_DATABASE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(accounts);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e(TAG, "save accounts failed");
        }

        context.deleteFile(CARD_DATABASE);
        try {
            FileOutputStream fos = context.openFileOutput(CARD_DATABASE, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(cards);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e(TAG, "save cards failed");
        }
    }

    private void loadDatabase() {
        try {
            FileInputStream fis;
            fis = context.openFileInput(BANK_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            banks = (HashMap<Integer, Bank>) oi.readObject();
            oi.close();

        } catch (Exception e) {
            Log.e(TAG, "load BANK_DATABASE failed");
        }

        if(banks == null)
            setDefaultBank();
        printBankDatabase();


        try {
            FileInputStream fis;
            fis = context.openFileInput(ACCOUNT_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            accounts = (HashMap<Integer, Account>) oi.readObject();
            oi.close();

        } catch (Exception e) {
            Log.e(TAG, "load ACCOUNT_DATABASE failed");
        }

        if(accounts == null)
            setDefaultAccount();
        printAccountDatabase();

        try {
            FileInputStream fis;
            fis = context.openFileInput(CARD_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            cards = (HashMap<Integer, Card>) oi.readObject();
            oi.close();

        } catch (Exception e) {
            Log.e(TAG, "load CARD_DATABASE failed");
        }

        if(cards == null)
            setDefaultCard();
        printCardDatabase();
    }

    private void resetDatabase() {
        context.deleteFile(BANK_DATABASE);
        context.deleteFile(ACCOUNT_DATABASE);
        context.deleteFile(CARD_DATABASE);

        setDefaultBank();
        setDefaultAccount();
        setDefaultCard();
    }


    private void setDefaultBank() {
        banks = new HashMap<>();

        try {
            insertBank("현금");
        } catch (AlreadyExistException ex) {
            ex.printStackTrace();
        }

        try {
            insertBank("하나");
        } catch (AlreadyExistException ex) {
            ex.printStackTrace();
        }

        try {
            insertBank("국민");
        } catch (AlreadyExistException ex) {
            ex.printStackTrace();
        }

        try {
            insertBank("농협");
        } catch (AlreadyExistException ex) {
            ex.printStackTrace();
        }

        try {
            insertBank("신한");
        } catch (AlreadyExistException ex) {
            ex.printStackTrace();
        }

        try {
            insertBank("우리");
        } catch (AlreadyExistException ex) {
            ex.printStackTrace();
        }
    }

    private void setDefaultAccount() {
        accounts = new HashMap<>();

        try {
            insertAccount(getBankID("현금"), "현금", 0);
            insertAccount(getBankID("하나"), "용돈 관리 통장", 0);
            insertAccount(getBankID("국민"), "예금 통장", 0);
        } catch (AlreadyExistException ex) {

        }
    }

    private void setDefaultCard() {
        cards = new HashMap<>();

        try {
            insertCard(getAccountID("하나", "용돈 관리 통장"), "VIVA G");
        } catch (AlreadyExistException ex) {
            ex.printStackTrace();
        }
    }


    private void insertBank(String name) throws AlreadyExistException {
        if(getBankID(name) == -1) {
            int newID;
            try {
                newID = Collections.max(banks.keySet()) + 1;
            } catch (NoSuchElementException ex) {
                newID = 1;
            }

            banks.put(newID, new Bank(name));
            saveDatabase();
        } else {
            throw new AlreadyExistException();
        }
    }

    public void insertAccount(int bankID, String name, long balance) throws AlreadyExistException {
        if(getAccountID(bankID, name) == -1) {
            int newID;
            try {
                newID = Collections.max(accounts.keySet()) + 1;
            } catch (NoSuchElementException ex) {
                newID = 1;
            }

            accounts.put(newID, new Account(getBank(bankID), name, balance));
            saveDatabase();
        } else {
            throw new AlreadyExistException();
        }
    }

    public void insertCard(int accountID, String name) throws AlreadyExistException {
        if(getCardID(accountID, name) == -1) {
            int newID;
            try {
                newID = Collections.max(cards.keySet()) + 1;
            } catch (NoSuchElementException ex) {
                newID = 1;
            }

            cards.put(newID, new Card(getAccount(accountID), name));
            saveDatabase();
        } else {
            throw new AlreadyExistException();
        }
    }


    public void modifyAccount(int accountID, int bankID, String name, long amount) {
        Log.e("modifyAccount", accountID + " " + bankID + " " + name + " " + amount);

        if(accounts.keySet().contains(accountID)) {
            Account account = accounts.get(accountID);
            account.bank = getBank(bankID);
            account.name = name;
            account.balance = amount;
            accounts.put(accountID, account);
            saveDatabase();
        } else {
            Log.e("IDNotFoundException", "modifyAccount id = " + accountID);
        }
    }

    public void modifyCard(int cardID, int accountID, String name) {
        Log.e("modifyCard", cardID + " " + accountID + " " + name);

        if(cards.keySet().contains(cardID)) {
            Card card = cards.get(cardID);
            card.account = getAccount(accountID);
            card.name = name;
            cards.put(cardID, card);
        } else {
            Log.e("IDNotFoundException", "modifyCard id = " + cardID);
        }
    }


    public void deleteAccount(int accountID) {
        accounts.remove(accountID);
        saveDatabase();
    }

    public void deleteCard(int cardID) {
        accounts.remove(cardID);
        saveDatabase();
    }


    public void setBalance(int accountID, long amount) {
        if(accounts.keySet().contains(accountID)) {
            accounts.get(accountID).balance = amount;
            saveDatabase();
        } else {
            Log.e("IDNotFoundException", "setBalance");
        }
    }

    public void addBalance(int accountID, long amount) {
        if(accounts.keySet().contains(accountID)) {
            accounts.get(accountID).balance += amount;
            saveDatabase();
        } else {
            Log.e("IDNotFoundException", "setBalance");
        }
    }

    public long getBalance(int accountID) {
        if(accounts.keySet().contains(accountID)) {
            return accounts.get(accountID).balance;
        } else {
            Log.e("IDNotFoundException", "setBalance");
            return -1;
        }
    }


    public Bank getBank(int bankID) {
        return banks.get(bankID);
    }

    public Account getAccount(int accountID) {
        return accounts.get(accountID);
    }

    public Card getCard(int cardID) {
        return cards.get(cardID);
    }


    public int getBankID(String bankName) {
        for(int id : banks.keySet()) {
            if(banks.get(id).name.equals(bankName))
                return id;
        }

        return -1;
    }

    public int getAccountID(String bankName, String accountName) {
        for(int id : accounts.keySet()) {
            if(accounts.get(id).bank.name.equals(bankName) && accounts.get(id).name.equals(accountName)) {
                return id;
            }
        }

        return -1;
    }

    public int getAccountID(int bankID, String accountName) {
        return getAccountID(getBank(bankID).name, accountName);
    }

    public int getCardID(String bankName, String accountName, String cardName) {
        for(int id : cards.keySet()) {
            if(cards.get(id).account.bank.name.equals(bankName)
                    && cards.get(id).account.name.equals(accountName)
                    && cards.get(id).name.equals(cardName))
                return id;
        }

        return -1;
    }

    public int getCardID(int bankID, String accountName, String cardName) {
        return getCardID(getBank(bankID).name, accountName, cardName);
    }

    public int getCardID(int accountID, String cardName) {
        return getCardID(getAccount(accountID).bank.name, getAccount(accountID).name, cardName);
    }


    public String getBankName(int bankID) {
        return banks.get(bankID).name;
    }

    public String getAccountName(int accountID) {
        return accounts.get(accountID).name;
    }

    public String getCardName(int cardID) {
        return cards.get(cardID).name;
    }

    public String[] getBankAccountSet(int id) {
        String[] str = new String[2];
        Account account = getAccount(id);

        str[0] = account.bank.name;
        str[1] = account.name;

        return str;

    }

    public String[] getBankAccountCardSet(int id) {
        String[] str = new String[3];
        Card card = getCard(id);

        str[0] = card.account.bank.name;
        str[1] = card.account.name;
        str[2] = card.name;

        return str;
    }

    public String getFirstAccountNameOfBank(int bankID) {
        for(Account account : accounts.values()) {
            if(account.bank.name.equals(getBankName(bankID)))
                return account.name;
        }
        return null;
    }


    public ArrayList<Bank> getAllBank() {
        return new ArrayList<>(banks.values());
    }

    public ArrayList<Account> getAllAccount() {
        return new ArrayList<>(accounts.values());
    }

    public ArrayList<Card> getAllCard() {
        return new ArrayList<>(cards.values());
    }

    public ArrayList<Integer> getAllBankID() {
        return new ArrayList<>(banks.keySet());
    }

    public ArrayList<Integer> getAllAccountID() {
        return new ArrayList<>(accounts.keySet());
    }

    public ArrayList<Integer> getAllCardID() {
        return new ArrayList<>(cards.keySet());
    }

    public HashMap<Integer, Bank> getAllIDAndBank() {
        return banks;
    }

    public HashMap<Integer, Account> getAllIDAndAccount() {
        return accounts;
    }

    public HashMap<Integer, Card> getAllIDAndCard() {
        return cards;
    }


    public void printBankDatabase() {
        for(Bank bank : banks.values()) {
            Log.e(TAG,bank.toString());
        }
    }

    public void printAccountDatabase() {
        for(Account account : accounts.values()) {
            Log.e(TAG,account.toString());
        }
    }

    public void printCardDatabase() {
        for(Card card : cards.values()) {
            Log.e(TAG,card.toString());
        }
    }

    public void printAllDatabase() {
        printBankDatabase();
        printAccountDatabase();
        printCardDatabase();
    }


    public static class Bank implements Serializable {
        public String name;
        int frequency;

        public Bank(String name) {
            this.name = name;
            this.frequency = 0;
        }


        public String getName() {
            return name;
        }

        public int getFrequency() {
            return frequency;
        }


        public void setName(String name) {
            this.name = name;
        }


        public void increaseFrequency() {
            this.frequency++;
        }

        @Override
        public String toString() {
            return "Bank{" +
                    "name='" + name + '\'' +
                    ", frequency=" + frequency +
                    '}';
        }
    }

    public static class Account implements Serializable {
        Bank bank;
        String name;
        long balance;
        int frequency;

        public Account(Bank bank, String name, long balance) {
            this.bank = bank;
            this.name = name;
            this.balance = balance;
            this.frequency = 0;
        }


        public Bank getBank() {
            return bank;
        }

        public String getName() {
            return name;
        }

        public long getBalance() {
            return balance;
        }

        public int getFrequency() {
            return frequency;
        }


        public void setBank(Bank bank) {
            this.bank = bank;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setBalance(long balance) {
            this.balance = balance;
        }


        public void increaseFrequency() {
            this.frequency++;
        }

        @Override
        public String toString() {
            return "Account{" +
                    "bank=" + bank +
                    ", name='" + name + '\'' +
                    ", balance=" + balance +
                    ", frequency=" + frequency +
                    '}';
        }
    }

    public static class Card implements Serializable {
        Account account;
        String name;
        int frequency;

        public Card(Account account, String name) {
            this.account = account;
            this.name = name;
        }


        public Account getAccount() {
            return account;
        }

        public String getName() {
            return name;
        }

        public int getFrequency() {
            return frequency;
        }


        public void setAccount(Account account) {
            this.account = account;
        }

        public void setName(String name) {
            this.name = name;
        }


        public void increaseFrequency() {
            this.frequency++;
        }

        @Override
        public String toString() {
            return "Card{" +
                    "account=" + account +
                    ", name='" + name + '\'' +
                    ", frequency=" + frequency +
                    '}';
        }
    }


    public class AlreadyExistException extends Exception {
        public void showAlearyExistBank(Context context) {
            Toast.makeText(context, context.getResources().getString(R.string.account_db_manager_already_exist_bank), Toast.LENGTH_LONG).show();
        }


        public void showAlearyExistAccount(Context context) {
            Toast.makeText(context, context.getResources().getString(R.string.account_db_manager_already_exist_account), Toast.LENGTH_LONG).show();
        }


        public void showAlearyExistCard(Context context) {
            Toast.makeText(context, context.getResources().getString(R.string.account_db_manager_already_exist_card), Toast.LENGTH_LONG).show();
        }
    }

    private void comment() {





    /*
    private static AccountDBManager accountDBManager = null;
    public static AccountDBManager getAccountManager(Context context) {
        if(accountDBManager == null) {
            accountDBManager = new AccountDBManager(context);
        }
        return accountDBManager;
    }

    private SQLiteDatabase database = null;

    public static final String DATABASE_NAME = "account.db";
    public static final String BANK_TABLE = "bank";
    public static final String ACCOUNT_TABLE = "account";
    public static final String CARD_TABLE = "card";

    public static final String NAME_COLUMN = "name";
    public static final String FREQUENCY_COLUMN = "frequency";
    public static final String BALANCE_COLUMN = "amount";
    public static final String BANK_ID_COLUMN = "bank_id";
    public static final String ACCOUNT_ID_COLUMN = "account_id";

    public static final int DEFAULT_BANK_ID = 1, DEFAULT_ACCOUNT_ID = 1;

    private final String TAG = getClass().getSimpleName();

    private AccountDBManager(Context context) {
        //context.deleteDatabase(DATABASE_NAME);

        database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

        database.execSQL("create table if not exists " + BANK_TABLE + "(" +
                "_id integer PRIMARY KEY autoincrement, " +
                NAME_COLUMN + " text, " +
                FREQUENCY_COLUMN + " integer);");

        database.execSQL("create table if not exists " + ACCOUNT_TABLE + "(" +
                "_id integer PRIMARY KEY autoincrement, " +
                BANK_ID_COLUMN + " integer, " +
                NAME_COLUMN + " text, " +
                BALANCE_COLUMN + " integer, " +
                FREQUENCY_COLUMN + " integer);");

        database.execSQL("create table if not exists " + CARD_TABLE + "(" +
                "_id integer PRIMARY KEY autoincrement, " +
                ACCOUNT_ID_COLUMN + " integer, " +
                NAME_COLUMN + " text, " +
                FREQUENCY_COLUMN + " integer);");

        //setDefaultCategory();

        printAllDatabase();
    }

    private void setDefaultCategory() {
        try {
            insertBank("현금");
            insertBank("하나");
            insertBank("국민");
            insertBank("농협");
            insertBank("신한");
            insertBank("우리");

            insertAccount(getBankID("현금"), "지갑 속 현금");
            insertAccount(getBankID("하나"), "용돈 관리 통장");
            insertAccount(getBankID("국민"), "예금 통장");

            insertCard(getAccountID("하나", "용돈 관리 통장"), "VIVA G");
        } catch (AlreadyExistException al) {

        }
    }


    public int insertBank(String name) throws AlreadyExistException {
        if(getBankID(name) == 0) {
            ContentValues values = new ContentValues();
            values.put(NAME_COLUMN, name);

            return (int) database.insert(BANK_TABLE, null, values);
        }

        throw new AlreadyExistException();
    }

    public int insertAccount(int bankID, String name) throws AlreadyExistException {
        return insertAccount(bankID, name, 0);
    }

    public int insertAccount(int bankID, String name, long balance) throws AlreadyExistException {
        if(getAccountID(bankID, name) == 0) {
            ContentValues recordValues = new ContentValues();
            recordValues.put(BANK_ID_COLUMN, bankID);
            recordValues.put(NAME_COLUMN, name);
            recordValues.put(BALANCE_COLUMN, balance);

            return (int) database.insert(ACCOUNT_TABLE, null, recordValues);
        }

        throw new AlreadyExistException();
    }

    public int insertCard(int accountID, String name) throws AlreadyExistException {
        if(getCardID(accountID, name) == 0) {
            ContentValues recordValues = new ContentValues();
            recordValues.put(ACCOUNT_ID_COLUMN, accountID);
            recordValues.put(NAME_COLUMN, name);

            return (int) database.insert(CARD_TABLE, null, recordValues);
        }

        throw new AlreadyExistException();
    }



    public void modifyAccount(int accountID, int bankID, String name, long amount) {
        Log.e("modifyAccount", accountID + " " + bankID + " " + name + " " + amount);

        if(existID(ACCOUNT_TABLE, accountID)) {
            ContentValues cv = new ContentValues();
            cv.put(BANK_ID_COLUMN, bankID);
            cv.put(NAME_COLUMN, name);
            cv.put(BALANCE_COLUMN, amount);
            database.update(ACCOUNT_TABLE, cv, "_id=" + accountID, null);
        } else {
            Log.e("IDNotFoundException", "modifyAccount id = " + accountID);
        }
    }

    public void modifyCard(int cardID, int accountID, String name) {
        Log.e("modifyCard", cardID + " " + accountID + " " + name);

        if(existID(CARD_TABLE, cardID)) {
            ContentValues cv = new ContentValues();
            cv.put(ACCOUNT_ID_COLUMN, accountID);
            cv.put(NAME_COLUMN, name);
            database.update(CARD_TABLE, cv, "_id=" + accountID, null);
        } else {
            Log.e("IDNotFoundException", "modifyCard id = " + cardID);
        }
    }


    public boolean deleteBank(String bankName) {
        return database.delete(BANK_TABLE, NAME_COLUMN + " = '" + bankName + "'", null) > 0;
    }

    public boolean deleteBank(int bankID) {
        return database.delete(BANK_TABLE, "_id" + " = " + bankID, null) > 0;
    }

    public boolean deleteAccount(int bankID, String accountName) {
        return database.delete(ACCOUNT_TABLE, BANK_ID_COLUMN + " = " + bankID + " AND " + NAME_COLUMN + " = '" + accountName + "'", null) > 0;
    }

    public boolean deleteAccount(int accountID) {
        return database.delete(ACCOUNT_TABLE, "_id" + " = " + accountID, null) > 0;
    }

    public boolean deleteCard(int AccountID, String cardName) {
        return database.delete(CARD_TABLE, ACCOUNT_ID_COLUMN + " = " + AccountID + " AND " + NAME_COLUMN + " = '" + cardName + "'", null) > 0;
    }

    public boolean deleteCard(int cardID) {
        return database.delete(CARD_TABLE, "_id" + " = " + cardID, null) > 0;
    }





    public void setBalance(int accountID, long amount) {
        if(existID(ACCOUNT_TABLE, accountID)) {
            ContentValues cv = new ContentValues();
            cv.put(BALANCE_COLUMN, amount);
            database.update(ACCOUNT_TABLE, cv, "_id=" + accountID, null);
        } else {
            Log.e("IDNotFoundException", "setBalance");
        }
    }

    public void addBalance(int accountID, long amount) {
        String query = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE _id = " + accountID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0) {
            Log.e("IDNotFoundException", "addBalance");
            return;
        }

        cursor.moveToNext();
        int balance = cursor.getInt(cursor.getColumnIndexOrThrow(BALANCE_COLUMN));
        cursor.close();

        Log.e(TAG, getAccountName(accountID) + "'s balance is " + getBalance(accountID));

        ContentValues cv = new ContentValues();
        cv.put(BALANCE_COLUMN, (balance + amount));
        database.update(ACCOUNT_TABLE, cv, "_id = " + accountID, null);

        Log.e(TAG,getAccountName(accountID) + "'s balance is " + getBalance(accountID));
    }

    public long getBalance(int accountID) {
        String query = "SELECT amount FROM " + ACCOUNT_TABLE + " WHERE _id = " + accountID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0) {
            Log.e("IDNotFoundException", "getBalance");
            return Long.MAX_VALUE;
        }

        cursor.moveToNext();
        int balance = cursor.getInt(cursor.getColumnIndexOrThrow(BALANCE_COLUMN));
        cursor.close();

        return balance;
    }


    public String getBankName(int bankID) {
        String query = "SELECT * FROM " + BANK_TABLE + " WHERE _id = " + bankID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0)
            return "getBankName - NameNotFoundException";

        cursor.moveToNext();
        String bankName = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
        cursor.close();

        return bankName;
    }

    public String getAccountName(int accountID) {
        String query = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE _id = " + accountID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0)
            return "getAccountName - NameNotFoundException";

        cursor.moveToNext();
        String accountName = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
        cursor.close();

        return accountName;
    }

    public String getCardName(int cardID) {
        String query = "SELECT * FROM " + CARD_TABLE + " WHERE _id = " + cardID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0)
            return "getCardName - NameNotFoundException";

        cursor.moveToNext();
        String cardName = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
        cursor.close();

        return cardName;
    }

    public String[] getBankAccountCardSet(int cardID) {
        String[] set = new String[3];

        String query = "SELECT * FROM " + CARD_TABLE + " WHERE _id = " + cardID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0)
            return new String[]{"getBankAccountCardSet - Exception", "null", "null"};

        cursor.moveToNext();
        int accountID = cursor.getInt(cursor.getColumnIndexOrThrow(ACCOUNT_ID_COLUMN));
        set[2] = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
        cursor.close();

        query = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE _id = " + accountID;
        cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0)
            return new String[]{"null", "getBankAccountCardSet - Exception", "null"};

        cursor.moveToNext();
        int bankID = cursor.getInt(cursor.getColumnIndexOrThrow(BANK_ID_COLUMN));
        set[1] = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
        cursor.close();

        query = "SELECT * FROM " + BANK_TABLE + " WHERE _id = " + bankID;
        cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0)
            return new String[]{"null", "null", "getBankAccountCardSet - Exception"};

        cursor.moveToNext();
        set[0] = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
        cursor.close();

        return set;
    }

    public String[] getBankAccountSet(int accountID) {
        String[] set = new String[2];

        String query = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE _id = " + accountID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0)
            return new String[]{"getBankAccountSet - Exception", ""};

        cursor.moveToNext();
        int bankID = cursor.getInt(1);
        set[1] = cursor.getString(2);
        cursor.close();

        query = "SELECT * FROM " + BANK_TABLE + " WHERE _id = " + bankID;
        cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0)
            return new String[]{"", "getBankAccountSet - Exception"};

        cursor.moveToNext();
        set[0] = cursor.getString(1);
        cursor.close();

        return set;
    }

    public ArrayList<String> getFirstAccountNameOfBank(int bankID) {
        ArrayList<String> str = new ArrayList<>();

        String query = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE " + BANK_ID_COLUMN + " = " + bankID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0)
            return str;

        int cnt = cursor.getCount();

        for(int i = 0;i<cnt;i++) {
            cursor.moveToNext();

            str.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN)));
        }
        cursor.close();

        return str;
    }


    public int getBankID(String bankName) {
        String query = "SELECT * FROM " + BANK_TABLE + " WHERE name = '" + bankName + "'";
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0) {
            cursor.close();
            Log.e("NameNotFoundException", "There is no " + bankName + " in BankDatabase!");
            printBankDatabase();
            return 0;
        }

        cursor.moveToNext();
        int bankID = cursor.getInt(0);
        cursor.close();

        return bankID;
    }

    public int getAccountID(String bankName, String accountName) {
        String query = "SELECT * FROM " + BANK_TABLE + " WHERE name = '" + bankName + "'";
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0) {
            cursor.close();
            Log.e("getAccountID","There is no " + bankName + " in BankDatabase!");
            printBankDatabase();
            return 0;
        }

        cursor.moveToNext();
        int bankID = cursor.getInt(0);
        cursor.close();

        return getAccountID(bankID, accountName);
    }

    public int getAccountID(int bankID, String accountName) {
        String query = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE name = '" + accountName + "' AND bank_id = " + bankID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0) {
            cursor.close();
            Log.e("getAccountID","There is no " + accountName + " in AccountDatabase!");
            printAccountDatabase();
            return 0;
        }

        cursor.moveToNext();
        int accountID = cursor.getInt(0);
        cursor.close();

        return accountID;
    }

    public int getCardID(String bankName, String accountName, String cardName) {
        String query = "SELECT * FROM " + BANK_TABLE + " WHERE name = '" + bankName + "'";
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0) {
            cursor.close();
            Log.e("getCardID","There is no " + bankName + "in BankDatabase!");
            printBankDatabase();
            return 0;
        }

        cursor.moveToNext();
        int bankID = cursor.getInt(0);
        cursor.close();

        return getCardID(bankID, accountName, cardName);
    }

    public int getCardID(int bankID, String accountName, String cardName) {
        String query = "SELECT * FROM " + ACCOUNT_TABLE + " WHERE name = '" + accountName + "' AND bank_id = " + bankID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0) {
            cursor.close();
            Log.e("getCardID","There is no " + accountName + "in AccountDatabase!");
            printAccountDatabase();
            return 0;
        }

        cursor.moveToNext();
        int accountID = cursor.getInt(0);
        cursor.close();

        return getCardID(accountID, cardName);
    }

    public int getCardID(int accountID, String cardName) {
        String query = "SELECT * FROM " + CARD_TABLE + " WHERE name = '" + cardName + "' AND account_id = " + accountID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0) {
            cursor.close();
            Log.e("getCardID","There is no " + cardName + "in CardDatabase!");
            printCardDatabase();
            return 0;
        }

        cursor.moveToNext();
        int cardID = cursor.getInt(0);
        cursor.close();

        return cardID;
    }


    public HashMap<Integer, String> getAllBankNameAndID() {
        HashMap<Integer, String> ans = new HashMap<>();

        String query = "SELECT * FROM " + BANK_TABLE;
        Cursor cursor = database.rawQuery(query, null);

        int cnt = cursor.getCount();

        for(int i = 0;i<cnt;i++) {
            cursor.moveToNext();

            ans.put(cursor.getInt(0), cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN)));
        }
        cursor.close();

        return ans;
    }

    public HashMap<Integer, String> getAllAccountNameAndID() {
        HashMap<Integer, String> ans = new HashMap<>();

        String query = "SELECT * FROM " + ACCOUNT_TABLE;
        Cursor cursor = database.rawQuery(query, null);

        int cnt = cursor.getCount();

        for(int i = 0;i<cnt;i++) {
            cursor.moveToNext();

            ans.put(cursor.getInt(0), cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN)));
        }
        cursor.close();

        return ans;
    }

    public HashMap<Integer, String> getAllCardNameAndID() {
        HashMap<Integer, String> ans = new HashMap<>();

        String query = "SELECT * FROM " + CARD_TABLE;
        Cursor cursor = database.rawQuery(query, null);

        int cnt = cursor.getCount();

        for(int i = 0;i<cnt;i++) {
            cursor.moveToNext();

            ans.put(cursor.getInt(0), cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN)));
        }
        cursor.close();

        return ans;
    }


    public ArrayList<CustomDatabase.Bank> getAllBank() {
        ArrayList<CustomDatabase.Bank> ans = new ArrayList<>();

        String query = "SELECT * FROM " + BANK_TABLE;
        Cursor cursor = database.rawQuery(query, null);

        int cnt = cursor.getCount();

        for(int i = 0;i<cnt;i++) {
            cursor.moveToNext();

            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));

            ans.add(new CustomDatabase.Bank(name));
        }
        cursor.close();

        return ans;
    }

    public ArrayList<CustomDatabase.Account> getAllAccount() {
        ArrayList<CustomDatabase.Account> ans = new ArrayList<>();

        String query = "SELECT * FROM " + ACCOUNT_TABLE;
        Cursor cursor = database.rawQuery(query, null);

        int cnt = cursor.getCount();

        for(int i = 0;i<cnt;i++) {
            cursor.moveToNext();

            CustomDatabase.Bank bank = new CustomDatabase.Bank("은행");
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
            long balence = cursor.getLong(cursor.getColumnIndexOrThrow(BALANCE_COLUMN));

            ans.add(new CustomDatabase.Account(bank, name, balence));
        }
        cursor.close();

        return ans;
    }

    public ArrayList<CustomDatabase.Card> getAllCard() {
        ArrayList<CustomDatabase.Card> ans = new ArrayList<>();

        String query = "SELECT * FROM " + CARD_TABLE;
        Cursor cursor = database.rawQuery(query, null);

        int cnt = cursor.getCount();

        for(int i = 0;i<cnt;i++) {
            cursor.moveToNext();

            CustomDatabase.Bank bank = new CustomDatabase.Bank("뱅크");
            CustomDatabase.Account account = new CustomDatabase.Account(bank, "계1좌", 0);
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));

            ans.add(new CustomDatabase.Card(account, name));
        }
        cursor.close();

        return ans;
    }



    public void printBankDatabase() {
        String countQuery = "SELECT * FROM " + BANK_TABLE;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        for(int i = 0;i < cnt;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
            int frequency = cursor.getInt(cursor.getColumnIndexOrThrow(FREQUENCY_COLUMN));

            Log.e("printBankDatabase", "ID : " + id + " name : " + name + " frequency : " + frequency);
        }

        cursor.close();
    }

    public void printAccountDatabase() {
        String countQuery = "SELECT * FROM " + ACCOUNT_TABLE;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        for(int i = 0;i < cnt;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            int bankID = cursor.getInt(cursor.getColumnIndexOrThrow(BANK_ID_COLUMN));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
            int balance = cursor.getInt(cursor.getColumnIndexOrThrow(BALANCE_COLUMN));
            int frequency = cursor.getInt(cursor.getColumnIndexOrThrow(FREQUENCY_COLUMN));

            Log.e("printAccountDatabase", "ID : " + id + " bankID : " + bankID + " name : " + name + " balance : " + balance + " frequency : " + frequency);
        }

        cursor.close();
    }

    public void printCardDatabase() {
        String countQuery = "SELECT * FROM " + CARD_TABLE;
        Cursor cursor = database.rawQuery(countQuery, null);
        int cnt = cursor.getCount();

        for(int i = 0;i < cnt;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            int accountID = cursor.getInt(cursor.getColumnIndexOrThrow(ACCOUNT_ID_COLUMN));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN));
            int frequency = cursor.getInt(cursor.getColumnIndexOrThrow(FREQUENCY_COLUMN));

            Log.e("printCardDatabase",  "ID : " + id + " accountID : " + accountID + " name : " + name + " frequency : " + frequency);
        }

        cursor.close();
    }

    public void printAllDatabase() {
        printBankDatabase();
        printAccountDatabase();
        printCardDatabase();
    }


    public Cursor getCursor(String tableName) {
        return database.rawQuery("SELECT  * FROM " + tableName, null);
    }

    private boolean existID(String tableName, int ID) {
        boolean ans;

        String query = "SELECT * FROM " + tableName + " WHERE _id = " + ID;
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() == 0)
            ans = false;
        else
            ans = true;

        cursor.close();

        return ans;
    }

    public class AlreadyExistException extends Exception {

    }

    */
    }
}
