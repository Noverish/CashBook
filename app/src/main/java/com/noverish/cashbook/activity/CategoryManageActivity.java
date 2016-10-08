package com.noverish.cashbook.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.CategoryDBManager;
import com.noverish.cashbook.other.MoneyUsageItem;
import com.noverish.cashbook.view.BigCategoryView;
import com.noverish.cashbook.view.SmallCategoryView;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-10-08.
 */

public class CategoryManageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manage);

        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_category_manage);

        CategoryDBManager manager = CategoryDBManager.getCategoryManager(this);
        ArrayList<String> expenseBigCategory = manager.getAllBigCategory(MoneyUsageItem.EXPENDITURE);
        ArrayList<String> incomeBigCategory = manager.getAllBigCategory(MoneyUsageItem.INCOME);

        for(String bigCate : expenseBigCategory) {
            layout.addView(new BigCategoryView(this, bigCate, ContextCompat.getColor(this, R.color.expense)));

            ArrayList<String> expenseSmallCategory = manager.getAllSmallCategory(MoneyUsageItem.EXPENDITURE, bigCate);
            for(String smallCate : expenseSmallCategory)
                layout.addView(new SmallCategoryView(this, smallCate));
        }

        for(String bigCate : incomeBigCategory) {
            layout.addView(new BigCategoryView(this, bigCate, ContextCompat.getColor(this, R.color.income)));

            ArrayList<String> incomeSmallCategory = manager.getAllSmallCategory(MoneyUsageItem.INCOME, bigCate);
            for(String smallCate : incomeSmallCategory)
                layout.addView(new SmallCategoryView(this, smallCate));
        }
    }
}
