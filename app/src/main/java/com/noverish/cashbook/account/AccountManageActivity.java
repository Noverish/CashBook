package com.noverish.cashbook.account;

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
import com.noverish.cashbook.other.Essentials;

import java.util.ArrayList;


public class AccountManageActivity extends AppCompatActivity {
    public static final int ACCOUNT_ADD = 1001;
    public static final int ACCOUNT_CHANGE = 1002;

    private AccountDBManager dbManager;
    private ListView accountList;
    private AccountListCursorAdapter adapter;

    private Button accountAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_manage);
        dbManager = AccountDBManager.getAccountManager(this);

        adapter = new AccountListCursorAdapter(this, dbManager.getAllAccount());
        accountList = (ListView) findViewById(R.id.account_manage_list);
        accountList.setAdapter(adapter);
        accountList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AccountManageActivity.this, AccountAddActivity.class);

                intent.putExtra("id", (int)id);
                startActivityForResult(intent, ACCOUNT_CHANGE);
            }
        });

        accountAddButton = (Button) findViewById(R.id.account_manage_add);
        accountAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AccountAddActivity.class);
                startActivityForResult(intent, ACCOUNT_ADD);
            }
        });

        dbManager.printAccountDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        dbManager.printAccountDatabase();

        adapter.clear();
        adapter.addAll(dbManager.getAllAccount());
        adapter.notifyDataSetChanged();
    }

    class AccountListCursorAdapter extends ArrayAdapter<AccountDBManager.Account> {
        private Context context;

        public AccountListCursorAdapter(Context context, ArrayList<AccountDBManager.Account> accounts) {
            super(context, 0, accounts);

            this.context = context;
        }

        public long getItemId(int position) {
            AccountDBManager.Account account = getItem(position);
            return AccountDBManager.getAccountManager(context).getAccountID(account.getBank().name, account.getName());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            AccountDBManager.Account account = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_item, parent, false);;
            }

            TextView bankTextView = (TextView) convertView.findViewById(R.id.account_item_bank);
            TextView accountTextView = (TextView) convertView.findViewById(R.id.account_item_account);
            TextView amountTextView = (TextView) convertView.findViewById(R.id.account_item_amount);

            AccountDBManager.Bank bank = account.getBank();
            String name = account.getName();
            long balance = account.getBalance();

            bankTextView.setText(bank.getName());
            accountTextView.setText(name);
            amountTextView.setText(Essentials.numberWithComma(balance));

            return convertView;
        }
    }
}
