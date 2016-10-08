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

        if(banks == null) {
            setDefaultBank();
            Toast.makeText(context, "은행 데이터가 없습니다.",Toast.LENGTH_SHORT).show();
        }


        try {
            FileInputStream fis;
            fis = context.openFileInput(ACCOUNT_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            accounts = (HashMap<Integer, Account>) oi.readObject();
            oi.close();

        } catch (Exception e) {
            Log.e(TAG, "load ACCOUNT_DATABASE failed");
        }

        if(accounts == null) {
            setDefaultAccount();
            Toast.makeText(context, "계좌 데이터가 없습니다.",Toast.LENGTH_SHORT).show();
        }

        try {
            FileInputStream fis;
            fis = context.openFileInput(CARD_DATABASE);
            ObjectInputStream oi = new ObjectInputStream(fis);
            cards = (HashMap<Integer, Card>) oi.readObject();
            oi.close();

        } catch (Exception e) {
            Log.e(TAG, "load CARD_DATABASE failed");
        }

        if(cards == null) {
            setDefaultCard();
            Toast.makeText(context, "카드 데이터가 없습니다.",Toast.LENGTH_SHORT).show();
        }
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
}
