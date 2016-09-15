package com.noverish.cashbook.other;

import android.graphics.drawable.Drawable;

import com.noverish.cashbook.view.DateTimeSelector;

/**
 * Created by Noverish on 2016-01-10.
 */
public class MoneyUsageItem {
    public static final int EXPENDITURE = 0;
    public static final int INCOME = 1;
    public static final int TRANSFER = 2;

    private Drawable icon = null;
    private long date;
    private int classification;
    private long amount;
    private String content;
    private String place;
    private int accountId;
    private int categoryIdOrToAccountID;
    private String memo;
    private int transferFee;
    private String geoCode;


    public MoneyUsageItem(long date, int classification, long amount, String content, String place, int accountId, int categoryId, String memo, int transferFee, String geoCode) {
        this.date = date;
        this.classification = classification;
        this.amount = amount;
        this.content = content;
        this.place = place;
        this.accountId = accountId;
        this.categoryIdOrToAccountID = categoryId;
        this.memo = memo;
        this.transferFee = transferFee;
        this.geoCode = geoCode;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setClassification(int classification) {
        this.classification = classification;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setCategoryIdOrToAccountID(int categoryIdOrToAccountID) {
        this.categoryIdOrToAccountID = categoryIdOrToAccountID;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setTransferFee(int transferFee) {
        this.transferFee = transferFee;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public Drawable getIcon() {
        return icon;
    }

    public long getDate() {
        return date;
    }

    public int getClassification() {
        return classification;
    }

    public long getAmount() {
        return amount;
    }

    public String getContent() {
        return content;
    }

    public String getPlace() {
        return place;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getCategoryIdOrToAccountID() {
        return categoryIdOrToAccountID;
    }

    public String getMemo() {
        return memo;
    }

    public int getTransferFee() {
        return transferFee;
    }

    public String getGeoCode() {
        return geoCode;
    }


    @Override
    public String toString() {
        return "MoneyUsageItem{" +
                ", date=" + date +
                ", classification=" + classification +
                ", amount=" + amount +
                ", content='" + content + '\'' +
                ", place='" + place + '\'' +
                ", accountId=" + accountId +
                ", categoryIdOrToAccountID=" + categoryIdOrToAccountID +
                ", memo='" + memo + '\'' +
                ", transferFee=" + transferFee +
                ", geoCode='" + geoCode + '\'' +
                '}';
    }

    public String toReadableString() {
        String day = DateTimeSelector.millisToDateString(date);
        String time = DateTimeSelector.millisToTimeString(date);

        return day + " - " + time + "\n" +
                "금액 : " + Essentials.numberWithComma(amount) + "\n" +
                "내역 : " + content + "\n" +
                "장소 : " + place + "\n";
    }
}
