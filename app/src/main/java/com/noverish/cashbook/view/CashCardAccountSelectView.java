package com.noverish.cashbook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.AccountDBManager;

import java.util.ArrayList;

public class CashCardAccountSelectView extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener{
    private AccountDBManager manager;

    private Context context;

    private LinearLayout contentLayout;
    private ListView cardListView;
    private ListView accountListView;

    private final String TAG = getClass().getSimpleName();
    private final int DEFAULT_ACCOUNT_ID = -1;

    private int nowAccountID = DEFAULT_ACCOUNT_ID;

    boolean expanded = false;


    public CashCardAccountSelectView(Context context) {
        super(context);
        if(!isInEditMode())
            init(context);
    }

    public CashCardAccountSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode())
            init(context);
    }

    public CashCardAccountSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode())
            init(context);
    }

    public void init(Context context) {
        this.context = context;
        this.manager = AccountDBManager.getAccountManager(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cash_card_account_select, this, true);

        cardListView = (ListView) findViewById(R.id.account_select_layout_card_select_list);
        accountListView = (ListView) findViewById(R.id.account_select_layout_account_select_list);
        contentLayout = (LinearLayout) findViewById(R.id.account_select_layout_content_layout);

        setSelectSubTopButton();
        setListViews();
        resize(false);
    }

    public void setData(int ID) {
        nowAccountID = ID;

        Button category001 = (Button) findViewById(R.id.account_category001);
        Button category002 = (Button) findViewById(R.id.account_category002);
        Button category003 = (Button) findViewById(R.id.account_category003);

        if(ID < 0) {
            Log.e(TAG,"This AccountID is " + ID);

            String[] tmps = manager.getBankAccountSet(-ID);

            if(tmps[0].equals("현금"))
                category001.setText("현금");
            else
                category001.setText("계좌");

            showAccountLayout();

            category002.setText(tmps[0]);
            category003.setText(tmps[1]);

        } else if(ID > 0) {
            Log.e(TAG,"This cardID is " + ID);

            String[] tmps = manager.getBankAccountCardSet(ID);

            category001.setText("체크");
            category002.setText(tmps[0]);
            category003.setText(tmps[2]);

            showCardLayout();
        } else {
            Log.e(TAG,"Impossible - ID is 0");
        }

        category001.setOnClickListener(this);
        category002.setOnClickListener(this);
        category003.setOnClickListener(this);
    }

    private void setSelectSubTopButton() {
        Button cardSelectButton = (Button) findViewById(R.id.account_select_layout_card_select_button);
        cardSelectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardLayout();
                setFocusableInTouchMode(true);
                requestFocus();
            }
        });

        Button accountSelectButton = (Button) findViewById(R.id.account_select_layout_account_select_button);
        accountSelectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountLayout();
                setFocusableInTouchMode(true);
                requestFocus();
            }
        });
    }

    private void setListViews() {
        ArrayList<Integer> accountIDSet = manager.getAllAccountID();
        ArrayList<String> accountArrayList = new ArrayList<>();

        for(int accountID : accountIDSet) {
            String[] set = manager.getBankAccountSet(accountID);
            accountArrayList.add(set[0] + " - " + set[1]);
        }

        ArrayAdapter<String> accountAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, accountArrayList);
        accountListView.setAdapter(accountAdapter);
        accountListView.setOnItemClickListener(this);

        ArrayList<Integer> cardIDSet = manager.getAllCardID();
        ArrayList<String> cardArrayList = new ArrayList<>();

        for(int cardID : cardIDSet) {
            String[] set = manager.getBankAccountCardSet(cardID);
            cardArrayList.add(set[0] + " - " + set[1] + " - " + set[2]);
        }

        ArrayAdapter<String> cardAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, cardArrayList);
        cardListView.setAdapter(cardAdapter);
        cardListView.setOnItemClickListener(this);
    }


    private void showAccountLayout() {
        accountListView.setVisibility(VISIBLE);
        setTotalHeightofListView(accountListView);

        cardListView.setVisibility(INVISIBLE);
        FrameLayout.LayoutParams cardLP = (FrameLayout.LayoutParams) cardListView.getLayoutParams();
        cardLP.height = 0;
    }

    private void showCardLayout() {
        //accountListView.getAdapter().getView(0, null, null).getHeight();

        accountListView.setVisibility(INVISIBLE);
        FrameLayout.LayoutParams accountLP = (FrameLayout.LayoutParams) accountListView.getLayoutParams();
        accountLP.height = 0;

        cardListView.setVisibility(VISIBLE);
        setTotalHeightofListView(cardListView);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long idLong) {
        String[] names = ((String)parent.getItemAtPosition(position)).split(" - ");

        if(names.length == 2) {
            int id = manager.getAccountID(names[0], names[1]);

            nowAccountID = -id;

            setData(-id);
        } else if(names.length == 3) {
            int id = manager.getCardID(names[0], names[1], names[2]);

            nowAccountID = id;

            setData(id);
        } else {
            Log.e(TAG,"Impossible - onItemClick");
            //setData(0);
        }

        expanded = false;

        LayoutParams p2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        contentLayout.setLayoutParams(p2);
    }


    public int getNowAccountID() {
        return nowAccountID;
    }

    @Override
    public void onClick(View v) {
        resize();
    }

    private void resize(boolean expand) {
        if (expand) {
            LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            contentLayout.setLayoutParams(p);
        }
        else {
            LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            contentLayout.setLayoutParams(p);
        }

        this.expanded = expand;
    }

    private void resize() {
        resize(!expanded);
    }

    public static void setTotalHeightofListView(ListView listView) {

        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),

                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
}