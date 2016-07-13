package com.noverish.cashbook.other;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.noverish.cashbook.R;
import com.noverish.cashbook.activity.MainActivity;
import com.noverish.cashbook.activity.NotificationListenerActivity;
import com.noverish.cashbook.database.AccountDBManager;
import com.noverish.cashbook.database.CashBookDBManager;
import com.noverish.cashbook.database.ContentToCategoryDatabase;

import java.util.Set;

public class NotificationsReadService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereceiver;
    private static NotificationsReadService instance;

    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereceiver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("LIST");
        registerReceiver(nlservicereceiver, filter);
        instance = this;

        Log.e(TAG, "onCreate");
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        startForeground(1, new Notification());

        Log.e(TAG, "onStartCommand");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e(TAG, "onDestroy");

        unregisterReceiver(nlservicereceiver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
/*
        Log.e(TAG, "**********onNotificationPosted**********");
        String[] strs = statusBarNotificationToString(sbn).split("\n");
        for(String str : strs)
            Log.e(TAG,str);*/

//        Toast.makeText(this, sbn.getNotification().extras.getString("android.text"), Toast.LENGTH_SHORT).show();

        //makeNotification(strs[4],strs[8], strs[3]);

        String title = (String) sbn.getNotification().extras.get("android.title");

        if(title != null && title.equals("KEB 하나은행")) {
            insertStatusBarToCashBookDB(sbn);
        }
/*
        NotificationDBManager dbManager = NotificationDBManager.getNotificationDBManager(this);

        String text = sbn.getNotification().extras.getString("android.text");

        NotificationItem item = new NotificationItem(title, 0, 0, text, 0, 0, "");
        dbManager.insert(item);
*/
/*
        Intent i = new Intent();
        i.putExtra("text",sbn.getNotification().extras.getString("android.text"));
        sendBroadcast(i);*/
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    private String statusBarNotificationToString(StatusBarNotification sbn) {
        String tmp = "\n";

        tmp += "PostTime : " + sbn.getPostTime() + "\n";
        tmp += "ID : " + sbn.getId() + "\n";
        tmp += "Package : " + sbn.getPackageName() + "\n";
        tmp += "tickerText : " + sbn.getNotification().tickerText + "\n";

        Set<String> keySet = sbn.getNotification().extras.keySet();
        for(String key : keySet) {
            tmp += key + " : " + sbn.getNotification().extras.get(key) + "\n";
        }

        tmp += "\n";

        return tmp;
    }

    private void insertStatusBarToCashBookDB(StatusBarNotification sbn) {
        AccountDBManager accountDBManager = AccountDBManager.getAccountManager(this);
        ContentToCategoryDatabase contentToCategoryDatabase = ContentToCategoryDatabase.getContentToCategoryDatabase(this);
        CashBookDBManager cashBookDBManager = CashBookDBManager.getCashBookDBManager(this);

        String text = (String) sbn.getNotification().extras.get("android.text");

        if(text != null) {
            String[] strings = text.split("/");

            long date = sbn.getPostTime();

            int classification;
            String statement = text.substring(1, 3);
            if (statement.equals("출금"))
                classification = 0;
            else if (statement.equals("입금"))
                classification = 1;
            else
                classification = 0;

            long amount = Long.parseLong(strings[0].replaceAll("[^\\d]+", ""));

            String content = strings[1];


            String accountName = accountDBManager.getFirstAccountNameOfBank(accountDBManager.getBankID("하나"));
            int accountID = -accountDBManager.getAccountID("하나", accountName);

            int categoryID = contentToCategoryDatabase.getCategoryIDFromContent(content);

            long balance = Long.parseLong(strings[3].replaceAll("[^\\d]+", ""));

            if (content.equals("")) {
                if(classification == 1) {
                    categoryID = -accountID;

                    accountName = accountDBManager.getFirstAccountNameOfBank(accountDBManager.getBankID("현금"));
                    accountID = -accountDBManager.getAccountID("현금", accountName);
                }

                classification = 2;
                content = "ATM 출금";
            }

            MoneyUsageItem item = new MoneyUsageItem(date, classification, amount, content, content, accountID, categoryID, "");

            cashBookDBManager.insert(item, true);

            makeNotification("하나카드 지출이 등록되었습니다",content + " - " + amount + "원", "");
        }
    }

    public void makeNotification(String title, String text, String ticker) {
        PendingIntent intent = PendingIntent.getActivity(this, 0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification noti = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(ticker)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .build();

        manager.notify(1, noti);
    }

    class NLServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getIntExtra("command",0) == NotificationListenerActivity.REQUEST_CODE_NOTIFICATION_LIST) {
/*
                try {
                    NotificationsReadService.this.getActiveNotifications();
                } catch (NullPointerException np) {
                    Intent intentToSetting = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(intentToSetting);
                    return;
                }
*/
                String tmp = "===== Notification List ====";

                for (StatusBarNotification sbn : NotificationsReadService.this.getActiveNotifications())
                    tmp += statusBarNotificationToString(sbn);
                tmp += "============================";

                Intent i = new Intent("com.noverish.cashbook.notification.NOTIFICATION_LISTENER_EXAMPLE");
                i.putExtra("command", intent.getIntExtra("command", 0));
                i.putExtra("notification_event", tmp);
                sendBroadcast(i);
            }
            /*else if(intent.getIntExtra("command",0) == 4321) {
                for (StatusBarNotification sbn : NotificationsReadService.this.getActiveNotifications()) {
                    String title = (String) sbn.getNotification().extras.get("android.title");

                    if (title != null && title.equals("KEB 하나은행")) {
                        NotificationItem item = statusBarNotificationToItem(sbn);
                        notificationDBManager.insert(item);
                    }

                    Intent i = new Intent("com.noverish.cashbook.notification.NOTIFICATION_LISTENER_EXAMPLE");
                    i.putExtra("command", intent.getIntExtra("command", 0));
                    sendBroadcast(i);
                }
            }*/
        }
    }

    public static void checkNotificationAccess(Context context) {
        if(instance == null) {
            Toast.makeText(context, "NotificationsReadService did not create!", Toast.LENGTH_LONG).show();
        } else {
            try {
                instance.getActiveNotifications();
                Log.e("checkNotificationAccess", "NotificationsReadService working well");
            } catch (Exception ex) {
                Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Toast.makeText(context, "NotificationsReadService does not work!", Toast.LENGTH_LONG).show();
            }
        }


        /*
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                    Log.e("asdf","ffff");

                    try {
                        instance.getActiveNotifications();
                    } catch (Exception ex) {
                        Log.e("checkNotificationAccess", "getActiveNotifications is null!");

                    PendingIntent intent = PendingIntent.getActivity(this, 0,
                            new Intent(context.getApplicationContext(), MainActivity.class),
                            PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        Notification noti = new NotificationCompat.Builder(context.getApplicationContext())
                                .setContentTitle("title")
                                .setContentText("text")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setTicker("ticker")
                                        //.setContentIntent(intent)
                                .setAutoCancel(true)
                                .build();

                        manager.notify(1, noti);


                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    }
                }
            }
        });

        thread.start();*/
    }



    /*
    private NotificationItem statusBarNotificationToItem(StatusBarNotification sbn) {
        AccountDBManager accountDBManager = AccountDBManager.getAccountManager(getApplicationContext());
        NotificationItem item = null;

        String text = (String) sbn.getNotification().extras.get("android.text");

        if(text != null) {
            String[] strings = text.split("/");

            long date = sbn.getPostTime();
            String statement = text.substring(1, 3);
            long amount = Long.parseLong(strings[0].replaceAll("[^\\d]+", ""));
            String content = strings[1];

            String accountName = accountDBManager.getFirstAccountNameOfBank(accountDBManager.getBankID("하나")).get(0);
            int accountID = accountDBManager.getAccountID("하나", accountName);

            long balance = Long.parseLong(strings[3].replaceAll("[^\\d]+", ""));

            if(content.equals(""))
                statement = "이체";

            item = new NotificationItem(statement, date, amount, content, accountID, balance, statusBarNotificationToString(sbn));
        }

        return item;
    }*/
}