package com.noverish.cashbook.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.CashBookDBManager;
import com.noverish.cashbook.database.CategoryDBManager;
import com.noverish.cashbook.other.IconManager;
import com.noverish.cashbook.other.MoneyUsageItem;
import com.noverish.cashbook.view.MoneyUsageView;
import com.noverish.cashbook.view.StatisticsPerMonthPieChart;

import java.util.HashMap;

/**
 * Created by Noverish on 2016-05-14.
 */
public class ExpenseStatisticsActivity extends AppCompatActivity {
    private StatisticsPerMonthPieChart pieChart;
    private LinearLayout mainLayout;

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.expense_statistics);

        pieChart = (StatisticsPerMonthPieChart) findViewById(R.id.expense_statics_pie_chart);
        mainLayout = (LinearLayout) findViewById(R.id.expense_statistics_main_layout);

        Intent intent = getIntent();
        int year = intent.getIntExtra("year", -1);
        int month = intent.getIntExtra("month", -1);

        pieChart.setPieChartData(year, month);

        CashBookDBManager cashBookDBManager = CashBookDBManager.getCashBookDBManager(this);
        CategoryDBManager categoryDBManager = CategoryDBManager.getCategoryManager(this);

        HashMap<Integer, Long> totalAmount = cashBookDBManager.getAmountOfBigCategory(MoneyUsageItem.EXPENDITURE, year, month);

        boolean first = true;
        for(int bigCategoryId : totalAmount.keySet()) {
            Log.e(TAG,"asdf");

            MoneyUsageItem item = new MoneyUsageItem(0,
                    MoneyUsageItem.EXPENDITURE,
                    totalAmount.get(bigCategoryId),
                    categoryDBManager.getBigCategoryNameById(MoneyUsageItem.EXPENDITURE, bigCategoryId),
                    "",1,1,"");
            MoneyUsageView view = new MoneyUsageView(this, bigCategoryId, item);
            Drawable drawable = IconManager.getIconManager(this).getDrawable(categoryDBManager.getBigCategoryById(MoneyUsageItem.EXPENDITURE, bigCategoryId).icon);
            view.setIcon(drawable);

            if (first) {
                view.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.money_usage_view_top_border, null));
                first = false;
            }

            mainLayout.addView(view);
        }
    }
}
