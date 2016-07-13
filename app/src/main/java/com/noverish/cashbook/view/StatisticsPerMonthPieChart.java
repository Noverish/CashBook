package com.noverish.cashbook.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.noverish.cashbook.R;
import com.noverish.cashbook.database.CashBookDBManager;
import com.noverish.cashbook.database.CategoryDBManager;
import com.noverish.cashbook.other.Essentials;
import com.noverish.cashbook.other.MoneyUsageItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Noverish on 2016-05-14.
 */
public class StatisticsPerMonthPieChart extends PieChart implements OnChartValueSelectedListener{

    private CategoryDBManager categoryDBManager;

    private ArrayList<String> labels;
    private HashMap<Integer, Long> totalAmount;
    private long sum;

    private Context context;

    private final String TAG = getClass().getSimpleName();
    private final static double CUTLINE = 0.01;

    public StatisticsPerMonthPieChart(Context context) {
        super(context);

        if(!isInEditMode()) {
            init(context);
        }
    }

    public StatisticsPerMonthPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode()) {
            init(context);
        }
    }

    private void init(Context context) {
        this.context = context;
    }

    public void setPieChartData(int year, int month) {
        CashBookDBManager cashBookDBManager = CashBookDBManager.getCashBookDBManager(context);
        categoryDBManager = CategoryDBManager.getCategoryManager(context);

        totalAmount = cashBookDBManager.getAmountOfBigCategory(0, year, month);


        sum = 0;
        ArrayList<Entry> entries = new ArrayList<>();
        for(int bigCategoryId : totalAmount.keySet()) {
            entries.add(new Entry(totalAmount.get(bigCategoryId), bigCategoryId));
            sum += totalAmount.get(bigCategoryId);
        }

        PieDataSet dataset = new PieDataSet(entries, "# of Calls");
        dataset.setColors(new int[]{
                ContextCompat.getColor(context, R.color.pastel_red),
                ContextCompat.getColor(context, R.color.pastel_red_orange),
                ContextCompat.getColor(context, R.color.pastel_orange),
                ContextCompat.getColor(context, R.color.pastel_yellow),
                ContextCompat.getColor(context, R.color.pastel_yellow_green),
                ContextCompat.getColor(context, R.color.pastel_green),
                ContextCompat.getColor(context, R.color.pastel_green_sky_blue),
                ContextCompat.getColor(context, R.color.pastel_sky_blue),
                ContextCompat.getColor(context, R.color.pastel_blue),
                ContextCompat.getColor(context, R.color.pastel_blue_purple),
                ContextCompat.getColor(context, R.color.pastel_purple),
                ContextCompat.getColor(context, R.color.pastel_pink),}); // set the color
        dataset.setDrawValues(false);

        labels = new ArrayList<>();
        for(int bigCategoryId : totalAmount.keySet()) {
            if((double)totalAmount.get(bigCategoryId)/sum > CUTLINE) {
                String name = categoryDBManager.getBigCategoryNameById(MoneyUsageItem.EXPENDITURE, bigCategoryId);
                labels.add(name);
            } else {
                labels.add("");
            }
        }

        PieData data = new PieData(labels, dataset); // initialize Piedata
        data.setValueTextSize(15);

        setData(data);
        setDescription("");  // set the description
        getLegend().setEnabled(false);

        setCenterText(Essentials.numberWithComma(sum));
        setCenterTextSize(25);

        animateY(2000);

        setOnChartValueSelectedListener(this);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        int index = e.getXIndex();

        for(int bigCategoryId : totalAmount.keySet()) {
            if((double)totalAmount.get(bigCategoryId)/sum > CUTLINE) {
                if (bigCategoryId == index) {
                    labels.set(bigCategoryId, Essentials.numberWithComma(totalAmount.get(bigCategoryId)));
                } else {
                    labels.set(bigCategoryId, categoryDBManager.getBigCategoryNameById(MoneyUsageItem.EXPENDITURE, bigCategoryId));
                }
            }
        }
    }

    @Override
    public void onNothingSelected() {
        for(int bigCategoryId : totalAmount.keySet())
            if((double)totalAmount.get(bigCategoryId)/sum > CUTLINE)
                labels.set(bigCategoryId, categoryDBManager.getBigCategoryNameById(MoneyUsageItem.EXPENDITURE, bigCategoryId));
    }
}
