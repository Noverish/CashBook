package com.noverish.cashbook.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.AccountDBManager;

import java.util.ArrayList;


public class CardManageActivity extends AppCompatActivity {
    public static final int CARD_ADD = 1001;
    public static final int CARD_MODIFY = 1002;

    private AccountDBManager dbManager;
    private ListView cardList;
    private CardListCursorAdapter adapter;

    private Button cardAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_manage);
        dbManager = AccountDBManager.getAccountManager(this);

        adapter = new CardListCursorAdapter(this, dbManager.getAllCard());
        cardList = (ListView) findViewById(R.id.card_manage_list);
        cardList.setAdapter(adapter);
        cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CardManageActivity.this, CardAddActivity.class);

                intent.putExtra("id", (int)id);
                startActivityForResult(intent, CARD_MODIFY);
            }
        });

        cardAddButton = (Button) findViewById(R.id.card_manage_add);
        cardAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CardAddActivity.class);
                startActivityForResult(intent, CARD_ADD);
            }
        });

        dbManager.printCardDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        dbManager.printCardDatabase();

        adapter.clear();
        adapter.addAll(dbManager.getAllCard());
        adapter.notifyDataSetChanged();
    }

    class CardListCursorAdapter extends ArrayAdapter<AccountDBManager.Card> {
        private Context context;

        public CardListCursorAdapter(Context context, ArrayList<AccountDBManager.Card> accounts) {
            super(context, 0, accounts);

            this.context = context;
        }

        public long getItemId(int position) {
            AccountDBManager.Card card = getItem(position);
            return AccountDBManager.getAccountManager(context).getCardID(card.getAccount().getBank().getName(), card.getAccount().getName(), card.getName());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            AccountDBManager.Card card = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
            }

            TextView accountTextView = (TextView) convertView.findViewById(R.id.card_item_account);
            TextView cardTextView = (TextView) convertView.findViewById(R.id.card_item_card);

            AccountDBManager.Account account = card.getAccount();
            String name = card.getName();

            accountTextView.setText(account.getName());
            cardTextView.setText(name);

            return convertView;
        }


    }
}
