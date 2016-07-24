package com.noverish.cashbook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.CategoryDBManager;
import com.noverish.cashbook.other.MoneyUsageItem;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-03-02.
 */
public class CategorySelectView extends LinearLayout {
    private static final int BIG_CATEGORY_BUTTON_FONT_SIZE = 13, SMALL_CATEGORY_BUTTON_FONT_SIZE = 10;

    private Context context;
    private CategoryDBManager manager;

    private TextView topBigCategoryButton, topSmallCategoryButton;
    private RelativeLayout totalLayout;

    private CategorySet nowCategorySet, expenditureCategorySet, incomeCategorySet;

    private boolean totalLayoutExpanded = false;
    private String nowBigCategoryName;
    private int nowClassification;
    private int nowCategoryID;

    private final String TAG = this.getClass().getSimpleName();

    public CategorySelectView(Context context) {
        super(context);
        if(!isInEditMode())
            init(context);
    }

    public CategorySelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode())
            init(context);
    }

    public CategorySelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode())
            init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.manager = CategoryDBManager.getCategoryManager(context);

        expenditureCategorySet = new CategorySet();
        incomeCategorySet = new CategorySet();
        nowCategorySet = expenditureCategorySet;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.category_select, this, true);

        topBigCategoryButton = (TextView) findViewById(R.id.big_category_button);
        topBigCategoryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resizeTotalLayout();
            }
        });

        topSmallCategoryButton = (TextView) findViewById(R.id.small_category_button);
        topSmallCategoryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resizeTotalLayout();
            }
        });

        totalLayout = (RelativeLayout) findViewById(R.id.category_select_total_layout);
        expenditureCategorySet.bigCategoryLayout1 = (LinearLayout) findViewById(R.id.category_select_expenditure_big_category1);
        expenditureCategorySet.bigCategoryLayout2 = (LinearLayout) findViewById(R.id.category_select_expenditure_big_category2);
        expenditureCategorySet.smallCategoryLayoutSet = (RelativeLayout) findViewById(R.id.category_select_expenditure_small_category);
        incomeCategorySet.bigCategoryLayout1 = (LinearLayout) findViewById(R.id.category_select_income_big_category1);
        incomeCategorySet.bigCategoryLayout2 = (LinearLayout) findViewById(R.id.category_select_income_big_category2);
        incomeCategorySet.smallCategoryLayoutSet = (RelativeLayout) findViewById(R.id.category_select_income_small_category);

        fillLayout(MoneyUsageItem.EXPENDITURE, expenditureCategorySet);
        fillLayout(MoneyUsageItem.INCOME, incomeCategorySet);

        resizeTotalLayout(false);
    }


    public void setData(int classification, int categoryID) {
        nowClassification = classification;
        nowCategoryID = categoryID;

        topBigCategoryButton.setText(manager.getCategoryNameSetFromID(nowClassification, categoryID)[0]);
        topSmallCategoryButton.setText(manager.getCategoryNameSetFromID(nowClassification, categoryID)[1]);
    }

    public void setClassification(int classification) {
        if(classification == MoneyUsageItem.EXPENDITURE) {
            nowCategorySet = expenditureCategorySet;

            expenditureCategorySet.bigCategoryLayout1.setVisibility(VISIBLE);
            expenditureCategorySet.bigCategoryLayout1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            expenditureCategorySet.bigCategoryLayout2.setVisibility(VISIBLE);
            expenditureCategorySet.bigCategoryLayout2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            expenditureCategorySet.smallCategoryLayoutSet.setVisibility(VISIBLE);
            expenditureCategorySet.smallCategoryLayoutSet.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            incomeCategorySet.bigCategoryLayout1.setVisibility(INVISIBLE);
            incomeCategorySet.bigCategoryLayout1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            incomeCategorySet.bigCategoryLayout2.setVisibility(INVISIBLE);
            incomeCategorySet.bigCategoryLayout2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            incomeCategorySet.smallCategoryLayoutSet.setVisibility(INVISIBLE);
            incomeCategorySet.smallCategoryLayoutSet.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

            nowClassification = MoneyUsageItem.EXPENDITURE;
        } else if(classification == MoneyUsageItem.INCOME) {
            nowCategorySet = incomeCategorySet;

            expenditureCategorySet.bigCategoryLayout1.setVisibility(INVISIBLE);
            expenditureCategorySet.bigCategoryLayout1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            expenditureCategorySet.bigCategoryLayout2.setVisibility(INVISIBLE);
            expenditureCategorySet.bigCategoryLayout2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            expenditureCategorySet.smallCategoryLayoutSet.setVisibility(INVISIBLE);
            expenditureCategorySet.smallCategoryLayoutSet.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

            incomeCategorySet.bigCategoryLayout1.setVisibility(VISIBLE);
            incomeCategorySet.bigCategoryLayout1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            incomeCategorySet.bigCategoryLayout2.setVisibility(VISIBLE);
            incomeCategorySet.bigCategoryLayout2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            incomeCategorySet.smallCategoryLayoutSet.setVisibility(VISIBLE);
            incomeCategorySet.smallCategoryLayoutSet.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            nowClassification = MoneyUsageItem.INCOME;
        } else if(classification == MoneyUsageItem.TRANSFER) {

            expenditureCategorySet.bigCategoryLayout1.setVisibility(INVISIBLE);
            expenditureCategorySet.bigCategoryLayout1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            expenditureCategorySet.bigCategoryLayout2.setVisibility(INVISIBLE);
            expenditureCategorySet.bigCategoryLayout2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            expenditureCategorySet.smallCategoryLayoutSet.setVisibility(INVISIBLE);
            expenditureCategorySet.smallCategoryLayoutSet.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

            incomeCategorySet.bigCategoryLayout1.setVisibility(VISIBLE);
            incomeCategorySet.bigCategoryLayout1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            incomeCategorySet.bigCategoryLayout2.setVisibility(VISIBLE);
            incomeCategorySet.bigCategoryLayout2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            incomeCategorySet.smallCategoryLayoutSet.setVisibility(VISIBLE);
            incomeCategorySet.smallCategoryLayoutSet.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

            nowClassification = MoneyUsageItem.TRANSFER;
        }
    }

    public int getNowCategoryID() {
        return nowCategoryID;
    }


    private void fillLayout(int classification, CategorySet categorySet) {
        categorySet.bigCategoryList = manager.getAllBigCategory(classification);

        OnClickListener bigOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                bigCategoryButtonClicked((Button) v);
                setFocusableInTouchMode(true);
                requestFocus();
            }
        };

        categorySet.bigCategoryLayout1.setVisibility(INVISIBLE);
        categorySet.bigCategoryLayout2.setVisibility(INVISIBLE);

        for(int i = 0;i<categorySet.bigCategoryList.size();i++) {
            LinearLayout smallCategoryLayout = new LinearLayout(context);
            smallCategoryLayout.setLayoutParams(new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            smallCategoryLayout.setOrientation(LinearLayout.VERTICAL);

            if(i % 2 == 0)
                fillButtonsOneLine(categorySet.bigCategoryList.get(i), categorySet.bigCategoryLayout1, BIG_CATEGORY_BUTTON_FONT_SIZE, bigOnClickListener);
            else
                fillButtonsOneLine(categorySet.bigCategoryList.get(i), categorySet.bigCategoryLayout2, BIG_CATEGORY_BUTTON_FONT_SIZE, bigOnClickListener);
            categorySet.smallCategoryLayoutSet.addView(smallCategoryLayout);

            OnClickListener smallOnClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    smallCategoryButtonClicked((Button) v);
                    setFocusableInTouchMode(true);
                    requestFocus();
                }
            };

            smallCategoryLayout.setVisibility(INVISIBLE);
            fillButtonsOneLine(manager.getAllSmallCategory(classification, i), smallCategoryLayout, SMALL_CATEGORY_BUTTON_FONT_SIZE, smallOnClickListener);
        }

    }

    private void fillButtonsOneLine(String content, LinearLayout layout, int fontSize, OnClickListener onClickListener) {
        ArrayList<String> list =  new ArrayList<>();
        list.add(content);

        fillButtonsOneLine(list, layout, fontSize, onClickListener);
    }

    private void fillButtonsOneLine(ArrayList<String> list, LinearLayout layout, int fontSize, OnClickListener onClickListener) {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);

        for(int i = 0;i<list.size();i++) {
            Button button = new Button(context);
            button.setLayoutParams(params);
            button.setTextSize(fontSize);
            button.setText(list.get(i));
            button.setPadding(10, 10, 10, 10);
            button.setOnClickListener(onClickListener);

            layout.addView(button);
        }
    }


    private void bigCategoryButtonClicked(Button btn) {
        nowBigCategoryName = btn.getText().toString();
        int index = nowCategorySet.bigCategoryList.indexOf(nowBigCategoryName);

        for(int i = 0;i<nowCategorySet.bigCategoryList.size();i++) {
            LinearLayout nowSmallLayout = (LinearLayout) nowCategorySet.smallCategoryLayoutSet.getChildAt(i);
            nowSmallLayout.setVisibility(INVISIBLE);

            if(i == index)
                nowSmallLayout.setVisibility(VISIBLE);
        }
    }

    private void smallCategoryButtonClicked(Button btn) {
        String nowSmallCategoryName = btn.getText().toString();
        nowCategoryID = manager.getSmallCategoryID(nowClassification, nowBigCategoryName, nowSmallCategoryName);

        topBigCategoryButton.setText(nowBigCategoryName);
        topSmallCategoryButton.setText(nowSmallCategoryName);

        resizeTotalLayout(false);
    }


    private void resizeTotalLayout(boolean expand) {
        if (expand) {
            LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            totalLayout.setLayoutParams(p);
            Log.e(TAG,"Expand");
        }
        else {
            LayoutParams p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            totalLayout.setLayoutParams(p);
        }

        this.totalLayoutExpanded = expand;
    }

    private void resizeTotalLayout() {
        resizeTotalLayout(!totalLayoutExpanded);
    }

    class CategorySet {
        ArrayList<String> bigCategoryList = new ArrayList<>();
        LinearLayout bigCategoryLayout1, bigCategoryLayout2;
        RelativeLayout smallCategoryLayoutSet;
    }



     /*private void fillButtonsTwoLines(ArrayList<String> list, LinearLayout layout, int fontSize, OnClickListener onClickListener) {
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);

        LinearLayout nowLinearLayout = new LinearLayout(context);;

        for(int i = 0;i<list.size();i++) {
            if(i % 2 == 0) {
                nowLinearLayout = new LinearLayout(context);
                nowLinearLayout.setLayoutParams(params);
                nowLinearLayout.setOrientation(HORIZONTAL);
                nowLinearLayout.setBackgroundColor(Color.TRANSPARENT);

                layout.addView(nowLinearLayout);
            }

            Button button = new Button(context);
            button.setLayoutParams(params);
            button.setTextSize(fontSize);
            button.setText(list.get(i));
            button.setPadding(10, 10, 10, 10);
            button.setMinEms(0);
            button.setMinWidth(0);
            button.setMinimumWidth(0);
            button.setOnClickListener(onClickListener);

            nowLinearLayout.addView(button);
        }
    }*/
}
