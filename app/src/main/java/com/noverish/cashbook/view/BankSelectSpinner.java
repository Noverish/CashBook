package com.noverish.cashbook.view;

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
public class BankSelectSpinner extends Spinner {
    private Context context;
    private AccountDBManager manager;

    private ArrayList<Integer> bankIDList;

    public BankSelectSpinner(Context context) {
        super(context);
        if(!isInEditMode())
            init(context);
    }

    public BankSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode())
            init(context);
    }

    public BankSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        this.context = context;

        manager = AccountDBManager.getAccountManager(context);

        bankIDList = manager.getAllBankID();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.support_simple_spinner_dropdown_item);
        for(int i = 0;i< bankIDList.size();i++) {
            String name = manager.getBank(bankIDList.get(i)).getName();
            adapter.add(name);
        }
        setAdapter(adapter);
    }

    public int getSelectedBankId() {
        int position = getSelectedItemPosition();
        return bankIDList.get(position);
    }

    public void setSelectionByBankId(int id) {
        int position = 0;

        for(int i = 0;i<bankIDList.size();i++) {
            if (bankIDList.get(i) == id) {
                position = i;
                break;
            }
        }

        setSelection(position);
    }
}
