package com.noverish.cashbook.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.AccountDBManager;
import com.noverish.cashbook.database.CashBookDBManager;
import com.noverish.cashbook.database.CategoryDBManager;
import com.noverish.cashbook.other.MoneyUsageItem;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-05-10.
 */
public class ExtractSMSActivity extends AppCompatActivity implements View.OnClickListener{
    private CashBookDBManager cashBookDBManager;

    private Context context;
    private LinearLayout layout;
    private Button addButton;

    private ArrayList<MoneyUsageItem> items = new ArrayList<>();

    public static final String INBOX = "content://sms/inbox";
    public static final String SENT = "content://sms/sent";
    public static final String DRAFT = "content://sms/draft";

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_extract_sms);
        context = this;

        cashBookDBManager = CashBookDBManager.getCashBookDBManager(this);
        layout = (LinearLayout) findViewById(R.id.extract_sms_activity_layout);
        addButton = (Button) findViewById(R.id.activity_extract_sms_add);
        addButton.setOnClickListener(this);

        getMessage();

    }

    private void getMessage() {
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if(cursor != null) {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(cursor.getCount());
            progressDialog.setMessage("로딩중입니다...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            if (cursor.moveToFirst()) { // must check the result to prevent exception
                do {
                    MoneyUsageItem item = processMessage(cursor);

                    if(item != null) {
                        CustomCheckbox checkBox = new CustomCheckbox(context, item);

                        layout.addView(checkBox);
                    }

                    progressDialog.incrementProgressBy(1);
                    if(progressDialog.getProgress() == progressDialog.getMax())
                        progressDialog.dismiss();

                } while (cursor.moveToNext());
            } else {
                Log.e(TAG, "There is no SMS");
            }
        } else {
            Log.e(TAG, "SMS cursor null PointerException");
        }
    }

    private MoneyUsageItem processMessage(Cursor cursor) {
        long date = cursor.getLong(cursor.getColumnIndexOrThrow("date"));
        String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));

        //국민은행
        if(body.contains("[KB]")) {
            try {
                String[] strs = body.split("\n");

                int classification = (body.contains("입금")) ? 1 : 0;
                String content = strs[3];

                AccountDBManager accountDBManager = AccountDBManager.getAccountManager(this);
                String accountName = accountDBManager.getFirstAccountNameOfBank(accountDBManager.getBankID("국민"));
                int accountID = -accountDBManager.getAccountID("국민", accountName);

                long amount; int categoryId;
                if(body.contains("ATM출금")) {
                    amount = Long.parseLong(strs[4].replaceAll("[^\\d]", ""));
                    String accountNameOfCash = accountDBManager.getFirstAccountNameOfBank(accountDBManager.getBankID("현금"));
                    categoryId = accountDBManager.getAccountID("현금", accountNameOfCash);
                    classification = MoneyUsageItem.TRANSFER;
                } else {
                    amount = Long.parseLong(strs[5].replaceAll("[^\\d]", ""));
                    categoryId = CategoryDBManager.DEFAULT_CATEOGRY_ID;
                }

                return new MoneyUsageItem(date, classification, amount, content, content, accountID, categoryId, "");
            } catch (Exception ex) {
                Log.e(TAG,body);
                ex.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        int count = 0;

        for(MoneyUsageItem item : items) {
            cashBookDBManager.insert(item, true);
            count++;
        }

        Toast.makeText(this, count + "개 저장이 완료되었습니다", Toast.LENGTH_LONG).show();

        setResult(MainActivity.RESULT_CODE_CASHBOOK_DB_MODIFIED);
        finish();
    }

    private class CustomCheckbox extends CheckBox implements CompoundButton.OnCheckedChangeListener{
        private MoneyUsageItem item;

        public CustomCheckbox(Context context, MoneyUsageItem item) {
            super(context);
            this.item = item;

            if(cashBookDBManager.isItAdded(item)) {
                this.setText("추가됨\n" + item.toReadableString());
                this.setTextColor(ContextCompat.getColor(context, R.color.pastel_green));
            }
            else
                this.setText(item.toReadableString());

            this.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CustomCheckbox customCheckbox = (CustomCheckbox) buttonView;
            if(isChecked) {
                items.add(customCheckbox.item);
            } else {
                items.remove(customCheckbox.item);
            }
        }

    }
}
