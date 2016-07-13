package com.noverish.cashbook.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.activity.CashBookAddActivity;
import com.noverish.cashbook.activity.MainActivity;
import com.noverish.cashbook.database.CashBookDBManager;
import com.noverish.cashbook.other.Essentials;
import com.noverish.cashbook.other.MoneyUsageItem;

import java.util.Calendar;

/**
 * Created by Noverish on 2016-04-13.
 */
public class MoneyUsageListPerDay extends RelativeLayout{

    private CashBookDBManager cashBookDBManager;
    private Context context;

    private Cursor cursor;
    private TextView dayTextView, totalAmountIncome, totalAmountExpense;
    private LinearLayout contentLayout;
    private int year = 1970, month = 1, day = 1;
    private long income, expense;
    private String dayOfWeek;

    private final String TAG = getClass().getSimpleName();

    public MoneyUsageListPerDay(Context context, Cursor cursor) {
        super(context);

        cursor.moveToNext();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(CashBookDBManager.DATE_COLUMN)));
        cursor.moveToPrevious();

        String[] days = {"일", "월", "화", "수", "목", "금", "토"};

        this.context = context;
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.dayOfWeek = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        this.cursor = cursor;
        this.cashBookDBManager = CashBookDBManager.getCashBookDBManager(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.money_usage_list_per_day, this, true);

        dayTextView = (TextView) findViewById(R.id.cashbook_list_per_day_day);
        totalAmountIncome = (TextView) findViewById(R.id.money_usage_list_per_day_total_amount_income);
        totalAmountExpense = (TextView) findViewById(R.id.money_usage_list_per_day_total_amount_expenditure);
        contentLayout = (LinearLayout) findViewById(R.id.cashbook_list_per_day_content_layout);

        init(context);
    }

    private void init(Context context) {
        contentLayout.removeAllViews();
        expense = income = 0;

        dayTextView.setText(year + "년 " + month + "월 " + day + "일 " + dayOfWeek + "요일");

        boolean first = true;
        int count = cursor.getCount();
        for(int i = 0;i<count;i++) {
            cursor.moveToNext();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

            MoneyUsageView view = new MoneyUsageView(context, id);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), CashBookAddActivity.class);
                    intent.putExtra("id", ((MoneyUsageView) v).getMoneyUsageItemId());
                    ((Activity) getContext()).startActivityForResult(intent, MainActivity.REQUEST_CODE_MONEY_USAGE_MODIFY);
                }
            });

            if (first) {
                view.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.money_usage_view_top_border, null));
                first = false;
            }

            contentLayout.addView(view);

            MoneyUsageItem item = view.getMoneyUsageItem();

            if (item.getClassification() == MoneyUsageItem.EXPENDITURE) {
                expense += item.getAmount();
            } else if (item.getClassification() == MoneyUsageItem.INCOME) {
                income += item.getAmount();
            }
        }


        totalAmountExpense.setText(Essentials.numberWithComma(expense));
        totalAmountIncome.setText(Essentials.numberWithComma(income));
    }

    public long getTotalExpense() {
        return expense;
    }

    public long getTotalIncome() {
        return income;
    }

    public void refresh() {
        cursor = cashBookDBManager.getItemWithDay(year, month, day);;

        init(context);
    }
}
