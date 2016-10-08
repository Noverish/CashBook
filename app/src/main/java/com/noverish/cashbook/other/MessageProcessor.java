package com.noverish.cashbook.other;

import android.content.Context;
import android.service.notification.StatusBarNotification;

import com.noverish.cashbook.database.AccountDBManager;
import com.noverish.cashbook.database.ContentToCategoryDatabase;

/**
 * Created by Noverish on 2016-10-08.
 */

public class MessageProcessor {
    public static MoneyUsageItem notificationToItem(Context context, StatusBarNotification sbn) {
        AccountDBManager accountDBManager = AccountDBManager.getAccountManager(context);
        ContentToCategoryDatabase contentToCategoryDatabase = ContentToCategoryDatabase.getContentToCategoryDatabase(context);

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

            return new MoneyUsageItem(date, classification, amount, content, content, accountID, categoryID, "", 0, "");
        }
        return null;
    }
}
