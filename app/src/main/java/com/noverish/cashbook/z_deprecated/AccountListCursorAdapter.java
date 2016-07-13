package com.noverish.cashbook.z_deprecated;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.AccountDBManager;

public class AccountListCursorAdapter extends CursorAdapter {
    public AccountListCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.account_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AccountDBManager manager = AccountDBManager.getAccountManager(context);

        TextView bank = (TextView) view.findViewById(R.id.account_item_bank);
        TextView account = (TextView) view.findViewById(R.id.account_item_account);
        TextView amount = (TextView) view.findViewById(R.id.account_item_amount);
/*
        int bankID = cursor.getInt(cursor.getColumnIndexOrThrow(AccountDBManager.BANK_ID_COLUMN));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(AccountDBManager.NAME_COLUMN));
        long balance = cursor.getLong(cursor.getColumnIndexOrThrow(AccountDBManager.BALANCE_COLUMN));


        bank.setText(manager.getBankName(bankID));
        account.setText(name);
        amount.setText(MoneyEditText.numberWithComma(balance));*/
    }
}


