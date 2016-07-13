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
public class CardSelectSpinner extends Spinner {
    private Context context;
    private AccountDBManager manager;

    private ArrayList<Integer> cardIDList;

    public CardSelectSpinner(Context context) {
        super(context);
        set(context);
    }

    public CardSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        set(context);
    }

    public CardSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        set(context);
    }

    private void set(Context context) {
        this.context = context;

        manager = AccountDBManager.getAccountManager(context);

        cardIDList = manager.getAllCardID();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.support_simple_spinner_dropdown_item);
        for(int i = 0;i<cardIDList.size();i++) {
            String tmp[] = manager.getBankAccountCardSet(cardIDList.get(i));
            adapter.add(tmp[0] + " - " + tmp[1] + " - " + tmp[2]);
        }
        setAdapter(adapter);
    }

    public int getSelectedCardId() {
        int position = getSelectedItemPosition();
        return cardIDList.get(position);
    }

    public void setSelectionByCardId(int id) {
        int position = 0;

        for(int i = 0;i<cardIDList.size();i++)
            if(cardIDList.get(i) == id) {
                position = i;
                break;
            }

        setSelection(position);
    }
}
