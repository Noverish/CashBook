package com.noverish.cashbook.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.AccountDBManager;
import com.noverish.cashbook.database.CashBookDBManager;
import com.noverish.cashbook.database.CategoryDBManager;
import com.noverish.cashbook.database.ContentToCategoryDatabase;
import com.noverish.cashbook.other.MoneyUsageItem;
import com.noverish.cashbook.account.AccountSelectSpinner;
import com.noverish.cashbook.view.CashCardAccountSelectView;
import com.noverish.cashbook.view.CategorySelectView;
import com.noverish.cashbook.view.DateTimeSelector;

import java.util.Calendar;

public class CashBookAddActivity extends AppCompatActivity {
    private CashBookDBManager manager;

    private Button expense;
    private Button income;
    private Button transfer;
    private EditText amountString;
    private EditText content;
    private EditText place;
    private EditText memo;

    private DateTimeSelector dateTimeSelector = null;
    private CashCardAccountSelectView cashCardAccountSelectView = null;
    private CategorySelectView categorySelectView = null;
    private AccountSelectSpinner transferToAccountSpinner = null;

    private int classification;
    private Context context;

    private String TAG = this.getClass().getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashbook_add);
        context = this;

        manager = CashBookDBManager.getCashBookDBManager(this);

        final int id = getIntent().getIntExtra("id", 0);

        Button saveButton = (Button) findViewById(R.id.cashbook_add_save);
        Button deleteButton = (Button) findViewById(R.id.cashbook_add_delete);
        Button saveWithoutChangeButton = (Button) findViewById(R.id.cashbook_add_save_without_change_balance);
        Button deleteWithoutChangeButton = (Button) findViewById(R.id.cashbook_add_delete_without_change_balance);

        expense = (Button) findViewById(R.id.cashbook_add_button_expenditure);
        income = (Button) findViewById(R.id.cashbook_add_button_income);
        transfer = (Button) findViewById(R.id.cashbook_add_button_transfer);

        amountString = (EditText) findViewById(R.id.cashbook_add_amount);
        content = (EditText) findViewById(R.id.cashbook_add_content);
        place = (EditText) findViewById(R.id.cashbook_add_place);
        memo = (EditText) findViewById(R.id.cashbook_add_memo);

        dateTimeSelector = (DateTimeSelector) findViewById(R.id.cashbook_add_date_time_selector);
        cashCardAccountSelectView = (CashCardAccountSelectView) findViewById(R.id.cashbook_add_account_select);
        categorySelectView = (CategorySelectView) findViewById(R.id.cashbook_add_category_select);
        transferToAccountSpinner = (AccountSelectSpinner) findViewById(R.id.cashbook_add_transfer_to_account_spinner);

        setClassificationButton();

        if(id != 0) {
            setData(manager.getItem(id));

            saveButton.setText(getResources().getString(R.string.modify));
            saveButton.setOnClickListener(new modifyOnClickListener(id, true));

            saveWithoutChangeButton.setVisibility(View.INVISIBLE);

            deleteButton.setText(getResources().getString(R.string.remove));
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.delete(id, true);

                    setResult(MainActivity.RESULT_CODE_CASHBOOK_DB_MODIFIED);
                    finish();
                }
            });

            deleteWithoutChangeButton.setText(getResources().getString(R.string.cashbook_add_delete_without_change_balance));
            deleteWithoutChangeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manager.delete(id, false);

                    setResult(MainActivity.RESULT_CODE_CASHBOOK_DB_MODIFIED);
                    finish();
                }
            });
        } else {
            setData(new MoneyUsageItem(Calendar.getInstance().getTimeInMillis(), MoneyUsageItem.EXPENDITURE, 0,"","",-AccountDBManager.DEFAULT_ACCOUNT_ID, CategoryDBManager.DEFAULT_CATEOGRY_ID, ""));

            saveButton.setText(getResources().getString(R.string.add));
            saveButton.setOnClickListener(new saveOnClickListener(true));

            saveWithoutChangeButton.setOnClickListener(new saveOnClickListener(false));

            deleteButton.setText(getResources().getString(R.string.cancel));
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            deleteWithoutChangeButton.setVisibility(View.INVISIBLE);
        }

        content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String usageContent = content.getText().toString();

                    int categoryId = ContentToCategoryDatabase.getContentToCategoryDatabase(context).getCategoryIDFromContent(usageContent);
                    categorySelectView.setData(classification, categoryId);

                    // code to execute when EditText loses focus
                }
            }
        });
    }

    private class saveOnClickListener implements View.OnClickListener {
        private boolean changeBalance;

        private saveOnClickListener(boolean changeBalance) {
            this.changeBalance = changeBalance;
        }

        @Override
        public void onClick(View v) {
            if (amountString.getText().toString().equals("")) {
                Toast.makeText(CashBookAddActivity.this, getResources().getString(R.string.cashbook_add_empty_amount), Toast.LENGTH_SHORT).show();
                return;
            }

            if (content.getText().toString().equals("")) {
                Toast.makeText(CashBookAddActivity.this, getResources().getString(R.string.cashbook_add_empty_content), Toast.LENGTH_SHORT).show();
                return;
            }

            long date = dateTimeSelector.getTimeInMillis();
            long amount = Integer.parseInt(amountString.getText().toString());
            String usageContent = content.getText().toString();
            String usagePlace = place.getText().toString();
            int accountId = cashCardAccountSelectView.getNowAccountID();

            int categoryId;
            if (classification == MoneyUsageItem.TRANSFER)
                categoryId = transferToAccountSpinner.getSelectedAccountId();
            else
                categoryId = categorySelectView.getNowCategoryID();

            String usageMemo = memo.getText().toString();

            MoneyUsageItem moneyUsageItem = new MoneyUsageItem(date, classification, amount, usageContent, usagePlace, accountId, categoryId, usageMemo);

            manager.insert(moneyUsageItem, changeBalance);
            ContentToCategoryDatabase.getContentToCategoryDatabase(context).putContentCategory(usageContent, categoryId);

            setResult(MainActivity.RESULT_CODE_CASHBOOK_DB_MODIFIED);
            finish();
        }
    }

    private class modifyOnClickListener implements View.OnClickListener {
        private int id;
        private boolean changeBalance;

        private modifyOnClickListener(int id, boolean changeBalance) {
            this.changeBalance = changeBalance;
            this.id = id;
        }

        @Override
        public void onClick(View v) {

            if (amountString.getText().toString().equals("")) {
                Toast.makeText(CashBookAddActivity.this, getResources().getString(R.string.cashbook_add_empty_amount), Toast.LENGTH_SHORT).show();
                return;
            }

            if (content.getText().toString().equals("")) {
                Toast.makeText(CashBookAddActivity.this, getResources().getString(R.string.cashbook_add_empty_content), Toast.LENGTH_SHORT).show();
                return;
            }

            long date = dateTimeSelector.getTimeInMillis();
            long amount = Integer.parseInt(amountString.getText().toString());
            String usageContent = content.getText().toString();
            String usagePlace = place.getText().toString();
            String usageMemo = memo.getText().toString();
            int accountId = cashCardAccountSelectView.getNowAccountID();
            int categoryId;

            if (classification == MoneyUsageItem.TRANSFER)
                categoryId = transferToAccountSpinner.getSelectedAccountId();
            else
                categoryId = categorySelectView.getNowCategoryID();

            MoneyUsageItem moneyUsageItem = new MoneyUsageItem(date, classification, amount, usageContent, usagePlace, accountId, categoryId, usageMemo);

            manager.modify(id, moneyUsageItem, changeBalance);
            ContentToCategoryDatabase.getContentToCategoryDatabase(context).putContentCategory(usageContent, categoryId);

            setResult(MainActivity.RESULT_CODE_CASHBOOK_DB_MODIFIED);
            finish();
        }
    }

    private void setData(MoneyUsageItem item) {
        Log.e(TAG, item.toString());



        dateTimeSelector.setTimeInMillis(item.getDate());
        if(item.getAmount() != 0)
            amountString.setText(String.valueOf(item.getAmount()));
        content.setText(item.getContent());
        place.setText(item.getPlace());
        memo.setText(item.getMemo());
        categorySelectView.setData(classification, item.getCategoryIdOrToAccountID());

        cashCardAccountSelectView.setData(item.getAccountId());
        switch (item.getClassification()) {
            case MoneyUsageItem.EXPENDITURE : {
                expense.performClick();
                break;
            }
            case MoneyUsageItem.INCOME : {
                income.performClick();
                break;
            }
            case MoneyUsageItem.TRANSFER : {
                transfer.performClick();
                break;
            }
        }

    }

    private void setClassificationButton() {
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense.setBackgroundResource(R.drawable.expense_selected);
                income.setBackgroundResource(R.drawable.income);
                transfer.setBackgroundResource(R.drawable.transfer);

                classification = MoneyUsageItem.EXPENDITURE;

                categorySelectView.setClassification(classification);
                categorySelectView.setVisibility(View.VISIBLE);
                transferToAccountSpinner.setVisibility(View.INVISIBLE);
            }
        });

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense.setBackgroundResource(R.drawable.expense);
                income.setBackgroundResource(R.drawable.income_selected);
                transfer.setBackgroundResource(R.drawable.transfer);

                classification = MoneyUsageItem.INCOME;

                categorySelectView.setClassification(classification);
                categorySelectView.setVisibility(View.VISIBLE);
                transferToAccountSpinner.setVisibility(View.INVISIBLE);
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense.setBackgroundResource(R.drawable.expense);
                income.setBackgroundResource(R.drawable.income);
                transfer.setBackgroundResource(R.drawable.transfer_selected);

                classification = MoneyUsageItem.TRANSFER;

                categorySelectView.setClassification(classification);
                categorySelectView.setVisibility(View.INVISIBLE);
                transferToAccountSpinner.setVisibility(View.VISIBLE);
            }
        });
    }
}
