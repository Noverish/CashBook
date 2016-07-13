package com.noverish.cashbook.z_deprecated;

/**
 * Created by Noverish on 2016-02-28.
 */
public class NotificationItem {
    public static String WITHDRAW = "출금";
    public static String DEPOSIT = "입금";
    public static String TRANSFER = "이체";

    private String bankStatement;
    private long date;
    private long amount;
    private String content;
    private int accountId;
    private long balance;
    private String allText;

    public NotificationItem(String bankStatement, long date, long amount, String content, int accountId, long balance, String allText) {
        this.bankStatement = bankStatement;
        this.date = date;
        this.amount = amount;
        this.content = content;
        this.accountId = accountId;
        this.balance = balance;
        this.allText = allText;
    }

    public String getBankStatement() {
        return bankStatement;
    }

    public long getDate() {
        return date;
    }

    public long getAmount() {
        return amount;
    }

    public String getContent() {
        return content;
    }

    public int getAccountId() {
        return accountId;
    }

    public long getBalance() {
        return balance;
    }

    public String getAllText() {
        return allText;
    }


    public void setBankStatement(String bankStatement) {
        this.bankStatement = bankStatement;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void setAllText(String allText) {
        this.allText = allText;
    }


    @Override
    public String toString() {
        return "NotificationItem{" +
                "bankStatement='" + bankStatement + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", content='" + content + '\'' +
                ", accountId=" + accountId +
                ", balance=" + balance +
                ", allText='" + allText + '\'' +
                '}';
    }
}
