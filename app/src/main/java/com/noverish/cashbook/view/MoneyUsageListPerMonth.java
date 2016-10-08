package com.noverish.cashbook.view;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.CashBookDBManager;
import com.noverish.cashbook.other.Essentials;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Noverish on 2016-04-14.
 */
public class MoneyUsageListPerMonth extends RelativeLayout {

    private CashBookDBManager manager;
    private HashMap<Integer, MoneyUsageListPerDay> list = new HashMap<>();
    private android.os.Handler handler = new Handler();

    private LinearLayout scrollView;
    private TextView totalIncomeTextView, totalExpenseTextView;

    private Context context;
    private int year, month, maximumDayOfMonth;
    private long totalIncome, totalExpense;

    private final String TAG = getClass().getSimpleName();

    public MoneyUsageListPerMonth(Context context, int year, int month) {
        super(context);

        this.year = year;
        this.month = month;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        this.maximumDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        init(context);
    }

    private void init(Context contextPara) {
        LayoutInflater inflater = (LayoutInflater) contextPara.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.money_usage_list_per_month, this, true);

        manager = CashBookDBManager.getCashBookDBManager(contextPara);
        this.context = contextPara;

        scrollView = (LinearLayout) findViewById(R.id.money_usage_list_per_month_content_layout);
        totalExpenseTextView = (TextView) findViewById(R.id.money_usage_list_per_month_total_expense);
        totalIncomeTextView = (TextView) findViewById(R.id.money_usage_list_per_month_total_income);

        refresh();
    }


    public void refresh() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int day = maximumDayOfMonth ; day > 0 ; day--) {
                    Cursor cursor = manager.getItemWithDay(year, month, day);

                    if(cursor.getCount() != 0) {
                        MoneyUsageListPerDay usageListPerDay = new MoneyUsageListPerDay(context, cursor);
                        list.put(day, usageListPerDay);

                        handler.post(new AddViewRunnable(scrollView, usageListPerDay));

                        totalExpense += usageListPerDay.getTotalExpense();
                        totalIncome += usageListPerDay.getTotalIncome();
                    }
                }

                handler.post(new SetTextRunnable(totalExpenseTextView, Essentials.numberWithComma(totalExpense)));
                handler.post(new SetTextRunnable(totalIncomeTextView, Essentials.numberWithComma(totalIncome)));
            }
        });
        thread.start();
    }

    public HashMap<Integer, MoneyUsageListPerDay> getList() {
        return list;
    }

    private class AddViewRunnable implements Runnable {
        ViewGroup viewGroup;
        View view;

        private AddViewRunnable(ViewGroup viewGroup, View view) {
            this.viewGroup = viewGroup;
            this.view = view;
        }

        public void run() {
            viewGroup.addView(view);
        }
    }

    private class SetTextRunnable implements Runnable {
        TextView textView;
        String str;

        private SetTextRunnable(TextView textView, String str) {
            this.textView = textView;
            this.str = str;
        }

        public void run() {
            textView.setText(str);
        }
    }
}
