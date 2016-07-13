package com.noverish.cashbook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.AccountDBManager;
import com.noverish.cashbook.other.Essentials;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-02-01.
 */
public class CurrentPropertyView extends LinearLayout {
    private AccountDBManager accountDBManager;

    private TextView livingCostTextView;
    private TextView totalPropertyTextView;

    private final String TAG = getClass().getSimpleName();

    public CurrentPropertyView(Context context) {
        super(context);
        if(!isInEditMode())
            init(context);
    }

    public CurrentPropertyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode())
            init(context);
    }

    public CurrentPropertyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode())
            init(context);
    }

    public void init(Context context) {
        accountDBManager = AccountDBManager.getAccountManager(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.current_property_layout, this, true);

        livingCostTextView = (TextView) findViewById(R.id.current_property_layout_living_cost_amount);
        totalPropertyTextView = (TextView) findViewById(R.id.current_property_layout_total_property_amount);

        refresh();
    }

    private long getTotalProperty() {
        long total = 0;

        ArrayList<Integer> allAccountID = accountDBManager.getAllAccountID();

        for(int id : allAccountID) {
            total += accountDBManager.getBalance(id);
        }

        return total;
    }

    private long getLivingCostProperty() {
        long amount = 0;

        ArrayList<Integer> allAccountID = accountDBManager.getAllAccountID();

        for(int id : allAccountID) {
            if(accountDBManager.getBankAccountSet(id)[0].equals("하나")) {
                amount += accountDBManager.getBalance(id);
            } else if(accountDBManager.getBankAccountSet(id)[0].equals("현금")) {
                amount += accountDBManager.getBalance(id);
            }
        }

        return amount;
    }

    public void refresh() {
        totalPropertyTextView.setText(Essentials.numberWithComma(getTotalProperty()));
        livingCostTextView.setText(Essentials.numberWithComma(getLivingCostProperty()));
    }
}
