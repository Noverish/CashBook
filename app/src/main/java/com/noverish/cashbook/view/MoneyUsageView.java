package com.noverish.cashbook.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.CashBookDBManager;
import com.noverish.cashbook.database.CategoryDBManager;
import com.noverish.cashbook.other.Essentials;
import com.noverish.cashbook.other.IconManager;
import com.noverish.cashbook.other.MoneyUsageItem;


/**
 * Created by Noverish on 2016-04-14.
 */
public class MoneyUsageView extends FrameLayout {
    private MoneyUsageItem moneyUsageItem;
    private int moneyUsageItemId;

    private final String TAG = getClass().getSimpleName();

    public MoneyUsageView(Context context, int moneyUsageItemId) {
        super(context);

        this.moneyUsageItemId = moneyUsageItemId;
        this.moneyUsageItem = CashBookDBManager.getCashBookDBManager(context).getItem(moneyUsageItemId);

        init(context, moneyUsageItem);
    }

    public MoneyUsageView(Context context, int moneyUsageItemId, MoneyUsageItem item) {
        super(context);

        this.moneyUsageItemId = moneyUsageItemId;
        this.moneyUsageItem = item;

        init(context, moneyUsageItem);

    }

    private void init(Context context, MoneyUsageItem item) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.money_usage_view, this, true);

        ImageView imageView = (ImageView) findViewById(R.id.money_usage_item_icon);
        TextView contentView = (TextView) findViewById(R.id.money_usage_item_content);
        TextView placeView = (TextView) findViewById(R.id.money_usage_place);
        TextView amountView = (TextView) findViewById(R.id.money_usage_item_amount);
        // Extract properties from cursor
        int classification = item.getClassification();
        String content = item.getContent();
        String place = item.getPlace();
        long amount = item.getAmount();

        Drawable icon = null;
        if(classification == MoneyUsageItem.EXPENDITURE)
        icon = IconManager.getIconManager(context).getDrawable(CategoryDBManager.getCategoryManager(context).getSmallCategoryById(classification, item.getCategoryIdOrToAccountID()).icon);
        // Populate fields with extracted properties

        contentView.setText(content);
        placeView.setText(place);
        amountView.setText(Essentials.numberWithComma(amount));
        if(icon != null)
            imageView.setBackground(icon);

        if(classification == MoneyUsageItem.EXPENDITURE) {
            amountView.setTextColor(ContextCompat.getColor(context, R.color.expense));
        } else if(classification == MoneyUsageItem.INCOME) {
            amountView.setTextColor(ContextCompat.getColor(context, R.color.income));
        } else if(classification == MoneyUsageItem.TRANSFER) {
            amountView.setTextColor(ContextCompat.getColor(context, R.color.transfer));
            imageView.setBackground(null);
        } else {
            Log.e("MoneyUsageCursorAdapter", "bindView Error");
        }
    }

    public MoneyUsageItem getMoneyUsageItem() {
        return moneyUsageItem;
    }

    public void setMoneyUsageItem(MoneyUsageItem moneyUsageItem) {
        this.moneyUsageItem = moneyUsageItem;
    }

    public int getMoneyUsageItemId() {
        return moneyUsageItemId;
    }

    public void setMoneyUsageItemId(int moneyUsageItemId) {
        this.moneyUsageItemId = moneyUsageItemId;
    }

    public void setIcon(Drawable drawable) {
        ImageView imageView = (ImageView) findViewById(R.id.money_usage_item_icon);

        imageView.setBackground(drawable);
    }
}
