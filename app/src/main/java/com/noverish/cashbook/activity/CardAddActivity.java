package com.noverish.cashbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.AccountDBManager;
import com.noverish.cashbook.account.AccountSelectSpinner;

import java.util.ArrayList;


public class CardAddActivity extends AppCompatActivity {
    private AccountDBManager manager;

    private EditText nameEditText;
    private AccountSelectSpinner accountSelectSpinner;
    private Button cardAddButton;
    private Button cardDeleteButton;

    private ArrayList<String> accountArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_add);

        manager = AccountDBManager.getAccountManager(this);

        nameEditText = (EditText) findViewById(R.id.card_add_name);
        accountSelectSpinner = (AccountSelectSpinner) findViewById(R.id.card_add_account_select_spinner);
        cardAddButton = (Button) findViewById(R.id.card_add_add_button);
        cardDeleteButton = (Button) findViewById(R.id.card_add_delete_button);

        Intent intent = getIntent();
        final int cardID = intent.getIntExtra("id", 0);

        if(cardID == 0) {
            setData("", 0);

            setButtonAddStatus();
        } else {
            AccountDBManager.Card card = manager.getCard(cardID);
            String cardName = card.getName();
            String accountName = card.getAccount().getName();
            String bankName = card.getAccount().getBank().getName();

            setData(cardName, manager.getAccountID(bankName, accountName));

            setButtonModifyStatus(cardID);
        }
    }

    private void setData(String cardName, int accountID) {
        nameEditText.setText(cardName);
        accountSelectSpinner.setSelectionByAccountId(accountID);
    }

    private void setButtonAddStatus() {
        cardAddButton.setText(getResources().getString(R.string.add));
        cardAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = nameEditText.getText().toString();
                int accountID = accountSelectSpinner.getSelectedAccountId();

                if (cardName.equals("")) {
                    Toast.makeText(CardAddActivity.this, getResources().getString(R.string.card_add_empty_account_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    manager.insertCard(accountID, cardName);
                } catch (AccountDBManager.AlreadyExistException exist) {
                    Toast.makeText(CardAddActivity.this, getResources().getString(R.string.card_add_already_exist_account), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(CardAddActivity.this, getResources().getString(R.string.card_add_saved), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        cardDeleteButton.setText(getResources().getString(R.string.cancel));
        cardDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CardAddActivity.this, getResources().getString(R.string.card_add_canceled), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setButtonModifyStatus(final int cardID) {
        cardAddButton.setText(getResources().getString(R.string.modify));
        cardAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = nameEditText.getText().toString();
                int accountID = accountSelectSpinner.getSelectedAccountId();

                if (cardName.equals("")) {
                    Toast.makeText(CardAddActivity.this, getResources().getString(R.string.card_add_empty_account_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                manager.modifyCard(cardID, accountID, cardName);

                Toast.makeText(CardAddActivity.this, getResources().getString(R.string.card_add_modified), Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        cardDeleteButton.setText(getResources().getString(R.string.remove));
        cardDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.deleteCard(cardID);
                Toast.makeText(CardAddActivity.this, getResources().getString(R.string.card_add_removed), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
