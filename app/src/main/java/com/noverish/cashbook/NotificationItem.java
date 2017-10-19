package com.noverish.cashbook;

import android.service.notification.StatusBarNotification;

import com.noverish.cashbook.database.DatabaseClient;

import java.util.Calendar;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Noverish on 2017-10-19.
 */

public class NotificationItem extends RealmObject {
    @PrimaryKey
    private long id;
    private String packageName;
    private String title;
    private String text;
    private String subText;
    private String infoText;
    private String ticker;
    private long postTime;

    public NotificationItem() {}

    public NotificationItem(StatusBarNotification sbn) {
        this.id = DatabaseClient.getPrimaryKey(this.getClass());
        this.packageName = sbn.getPackageName();
        this.title = sbn.getNotification().extras.getString("android.title");
        this.text = sbn.getNotification().extras.getString("android.text");
        this.text = sbn.getNotification().extras.getString("android.subText");
        this.text = sbn.getNotification().extras.getString("android.infoText");
        this.ticker = sbn.getNotification().tickerText.toString();
        this.postTime = sbn.getPostTime();
    }

    @Override
    public String toString() {
        return "NotificationItem{" +
                "id=" + id +
                ", \npackageName='" + packageName + '\'' +
                ", \ntitle='" + title + '\'' +
                ", \ntext='" + text + '\'' +
                ", \nsubText='" + subText + '\'' +
                ", \ninfoText='" + infoText + '\'' +
                ", \nticker='" + ticker + '\'' +
                ", \npostTime=" + postTime +
                "} ";
    }

    public long getId() {
        return id;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getSubText() {
        return subText;
    }

    public String getInfoText() {
        return infoText;
    }

    public String getTicker() {
        return ticker;
    }

    public Calendar getPostTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(postTime);
        return calendar;
    }
}
