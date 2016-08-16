package com.noverish.cashbook.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.AccountDBManager;


public class CardListCursorAdapter extends CursorAdapter {
    public CardListCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AccountDBManager manager = AccountDBManager.getAccountManager(context);

        TextView account = (TextView) view.findViewById(R.id.card_item_account);
        TextView card = (TextView) view.findViewById(R.id.card_item_card);
    }
}
