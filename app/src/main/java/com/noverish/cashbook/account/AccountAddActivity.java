package com.noverish.cashbook.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.AccountDBManager;
import com.noverish.cashbook.view.BankSelectSpinner;

public class AccountAddActivity extends AppCompatActivity {
    private AccountDBManager manager;

    private EditText nameEditText;
    private BankSelectSpinner bankSelectSpinner;
    private EditText balanceEditText;
    private Button accountAddButton;
    private Button accountRemoveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_add);

        manager = AccountDBManager.getAccountManager(this);

        nameEditText = (EditText) findViewById(R.id.account_add_name);
        bankSelectSpinner = (BankSelectSpinner) findViewById(R.id.account_add_bank_select_spinner);
        balanceEditText = (EditText) findViewById(R.id.account_add_amount);
        accountAddButton = (Button) findViewById(R.id.account_add_add_button);
        accountRemoveButton = (Button) findViewById(R.id.account_add_delete_button);

        Intent intent = getIntent();
        final int accountID = intent.getIntExtra("id", 0);

        if(accountID == 0) {
            setData("", 0, 0);

            setButtonAddStatus();
        } else {
            AccountDBManager.Account account = manager.getAccount(accountID);
            String accountName = account.getName();
            int bankId = manager.getBankID(account.getBank().getName());
            long balance = manager.getBalance(accountID);

            Log.e("bankID",bankId + " is bankID");

            setData(accountName, bankId, balance);

            setButtonModifyStatus(accountID);
        }
    }

    private void setData(String accountName, int bankId, long balance) {
        nameEditText.setText(accountName);
        bankSelectSpinner.setSelectionByBankId(bankId);
        balanceEditText.setText(String.valueOf(balance));
    }

    private void setButtonAddStatus() {
        accountAddButton.setText(getResources().getString(R.string.add));
        accountAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName = nameEditText.getText().toString();
                int bankID = bankSelectSpinner.getSelectedBankId();
                long startBalance = Long.parseLong(balanceEditText.getText().toString());

                if (accountName.equals("")) {
                    Toast.makeText(AccountAddActivity.this, getResources().getString(R.string.account_add_empty_account_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    manager.insertAccount(bankID, accountName, startBalance);
                } catch (AccountDBManager.AlreadyExistException exist) {
                    Toast.makeText(AccountAddActivity.this, getResources().getString(R.string.account_add_already_exist_account), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(AccountAddActivity.this, getResources().getString(R.string.account_add_saved), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        accountRemoveButton.setText(getResources().getString(R.string.cancel));
        accountRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AccountAddActivity.this, getResources().getString(R.string.account_add_canceled), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setButtonModifyStatus(final int accountID) {
        accountAddButton.setText(getResources().getString(R.string.modify));
        accountAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName = nameEditText.getText().toString();
                int bankID = bankSelectSpinner.getSelectedBankId();
                long startBalance = Long.parseLong(balanceEditText.getText().toString());

                if (accountName.equals("")) {
                    Toast.makeText(AccountAddActivity.this, getResources().getString(R.string.account_add_empty_account_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                manager.modifyAccount(accountID, bankID, accountName, startBalance);

                Toast.makeText(AccountAddActivity.this, getResources().getString(R.string.account_add_modified), Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        accountRemoveButton.setText(getResources().getString(R.string.remove));
        accountRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.deleteAccount(accountID);
                Toast.makeText(AccountAddActivity.this, getResources().getString(R.string.account_add_removed), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
