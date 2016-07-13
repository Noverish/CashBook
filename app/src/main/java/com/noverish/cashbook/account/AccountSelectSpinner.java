package com.noverish.cashbook.account;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.AccountDBManager;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-02-09.
 */
public class AccountSelectSpinner extends Spinner {
    private Context context;
    private AccountDBManager manager;

    private ArrayList<Integer> accountIDList;

    public AccountSelectSpinner(Context context) {
        super(context);
        if(!isInEditMode())
            init(context);
    }

    public AccountSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode())
            init(context);
    }

    public AccountSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        this.context = context;

        manager = AccountDBManager.getAccountManager(context);

        accountIDList = manager.getAllAccountID();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.support_simple_spinner_dropdown_item);
        for(int i = 0;i< accountIDList.size();i++) {
            String bankName = manager.getAccount(accountIDList.get(i)).getBank().getName();
            String accountName = manager.getAccount(accountIDList.get(i)).getName();
            adapter.add(bankName + " - " + accountName);
        }
        setAdapter(adapter);
    }

    public int getSelectedAccountId() {
        return accountIDList.get(getSelectedItemPosition());
    }

    public void setSelectionByAccountId(int id) {
        int position = 0;

        for(int i = 0;i<accountIDList.size();i++)
            if(accountIDList.get(i) == id) {
                position = i;
                break;
            }

        setSelection(position);
    }
}
