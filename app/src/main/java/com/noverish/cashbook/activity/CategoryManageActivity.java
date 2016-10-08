package com.noverish.cashbook.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.CategoryDBManager;
import com.noverish.cashbook.other.MoneyUsageItem;
import com.noverish.cashbook.view.BigCategoryView;
import com.noverish.cashbook.view.SmallCategoryView;

import java.util.ArrayList;

import static com.noverish.cashbook.database.CategoryDBManager.getCategoryManager;

/**
 * Created by Noverish on 2016-10-08.
 */

public class CategoryManageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manage);

        Button defaultButton = (Button) findViewById(R.id.activity_category_manage_default);
        defaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryDBManager.getCategoryManager(CategoryManageActivity.this).resetDatabase();
                Toast.makeText(CategoryManageActivity.this, "기본값으로 재 설정 되었습니다", Toast.LENGTH_SHORT).show();
                refresh();
                setResult(MainActivity.RESULT_CODE_CASHBOOK_DB_MODIFIED);
            }
        });

        refresh();
    }

    private void refresh() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.activity_category_manage);

        CategoryDBManager manager = getCategoryManager(this);
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
