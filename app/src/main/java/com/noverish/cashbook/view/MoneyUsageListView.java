package com.noverish.cashbook.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.activity.ExpenseStatisticsActivity;

import java.util.Calendar;

/**
 * Created by Noverish on 2016-04-14.
 */
public class MoneyUsageListView extends FrameLayout {
    private Context context;
    private MoneyUsageListPerMonth list;
    private FrameLayout contentLayout;
    private int year, month;

    private final String TAG = getClass().getSimpleName();

    public MoneyUsageListView(Context context) {
        super(context);

        if(!isInEditMode())
            init(context);
    }

    public MoneyUsageListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        this.year = Calendar.getInstance().get(Calendar.YEAR);
        this.month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.money_usage_list_view, this, true);


        contentLayout = (FrameLayout) findViewById(R.id.money_usage_list_view_content_layout);
        list = new MoneyUsageListPerMonth(context, year, month);
        contentLayout.addView(list);

        Button prevMonth = (Button) findViewById(R.id.money_usage_list_view_prev_month_button);
        prevMonth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseMonth();
            }
        });
        Button nextMonth = (Button) findViewById(R.id.money_usage_list_view_next_month_button);
        nextMonth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseMonth();
            }
        });
        View statisticsButton = findViewById(R.id.money_usage_list_view_statistics_button);
        statisticsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startStatisticsActivity();
            }
        });

        TextView yearTextView = (TextView) findViewById(R.id.money_usage_list_view_year);
        yearTextView.setText(String.valueOf(year));

        TextView monthTextView = (TextView) findViewById(R.id.money_usage_list_view_month);
        monthTextView.setText(String.valueOf(month));

    }

    public void refresh() {
        list.refresh();

        TextView yearTextView = (TextView) findViewById(R.id.money_usage_list_view_year);
        yearTextView.setText(String.valueOf(year));

        TextView monthTextView = (TextView) findViewById(R.id.money_usage_list_view_month);
        monthTextView.setText(String.valueOf(month));
    }

    public void increaseMonth() {
        month++;
        
        if(month == 13) {
            year++;
            month = 1;
        }

        contentLayout.removeAllViews();
        list = new MoneyUsageListPerMonth(context, year, month);
        contentLayout.addView(list);

        refresh();
    }

    public void decreaseMonth() {
        month --;

        if(month == 0) {
            year--;
            month = 12;
        }

        contentLayout.removeAllViews();
        list = new MoneyUsageListPerMonth(context, year, month);
        contentLayout.addView(list);

        refresh();
    }

    public void startStatisticsActivity() {
        Intent intent = new Intent(context.getApplicationContext(), ExpenseStatisticsActivity.class);
        intent.putExtra("year",year);
        intent.putExtra("month",month);
        context.startActivity(intent);

    }
}
