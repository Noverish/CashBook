package com.noverish.cashbook.z_deprecated;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.CashBookDBManager;
import com.noverish.cashbook.other.Essentials;
import com.noverish.cashbook.other.MoneyUsageItem;


/**
 * Created by Noverish on 2016-01-11.
 */
public class MoneyUsageCursorAdapter extends CursorAdapter {

    public MoneyUsageCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.money_usage_view, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView contentView = (TextView) view.findViewById(R.id.money_usage_item_content);
        TextView placeView = (TextView) view.findViewById(R.id.money_usage_place);
        TextView amountView = (TextView) view.findViewById(R.id.money_usage_item_amount);
        // Extract properties from cursor
        int classification = cursor.getInt(cursor.getColumnIndexOrThrow(CashBookDBManager.CLASSIFICATION_COLUMN));
        String content = cursor.getString(cursor.getColumnIndexOrThrow(CashBookDBManager.CONTENT_COLUMN));
        String place = cursor.getString(cursor.getColumnIndexOrThrow(CashBookDBManager.PLACE_COLUMN));
        long amount = cursor.getLong(cursor.getColumnIndexOrThrow(CashBookDBManager.AMOUNT_COLUMN));
        // Populate fields with extracted properties

        contentView.setText(content);
        placeView.setText(place);
        amountView.setText(Essentials.numberWithComma(amount));

        if(classification == MoneyUsageItem.EXPENDITURE) {
            amountView.setTextColor(ContextCompat.getColor(context, R.color.expense));
        } else if(classification == MoneyUsageItem.INCOME) {
            amountView.setTextColor(ContextCompat.getColor(context, R.color.income));
        } else if(classification == MoneyUsageItem.TRANSFER) {
            amountView.setTextColor(ContextCompat.getColor(context, R.color.transfer));
        } else {
            Log.e("MoneyUsageCursorAdapter","bindView Error");
        }
    }

    /* private static MoneyUsageCursorAdapter moneyUsageAdapter = null;
    public static MoneyUsageCursorAdapter getMoneyUsageAdapter(Context context) {
        if(moneyUsageAdapter == null) {
            moneyUsageAdapter = new MoneyUsageCursorAdapter(context);
        }
        return moneyUsageAdapter;
    }

    private ViewGroup listLayout;
    private Context context;
    private ArrayList<MoneyUsageItem> items;
    private CashBookDBManager manager;

    private MoneyUsageCursorAdapter(Context context) {
        this.context = context;
        manager = CashBookDBManager.getCashBookDBManager(this.context);
        items = manager.getAllDatabase();
    }

    public int getCount() {
        return items.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MoneyUsageView itemView;

        if(convertView == null) {
            itemView = new MoneyUsageView(context, items.get(position), position);
        } else {
            itemView = (MoneyUsageView) convertView;
            itemView.init(items.get(position));
        }

        return itemView;
    }



    public void addItem(MoneyUsageItem i) {
        Log.e("[Log]MoneyUsageCursorAdapter", "addItem");

        manager.insertSmallCategory(i);
        items.add(i);


    }

    public void changeItem(int position, MoneyUsageItem i) {
        Log.e("[Log]MoneyUsageCursorAdapter","changeItem");
        manager.modify(i.getId(), i);
        items.remove(position);
        items.add(position, i);
    }

    public void deleteItem(int position) {
        Log.e("[Log]MoneyUsageCursorAdapter", "deleteItem");

        MoneyUsageItem item = items.get(position);

        manager.delete(item.getId());

        items.remove(position);
    }

    public long getItemId(int position) {
        if(0<=position && position< items.size())
            return items.get(position).getId();
        return -1;
    }

    public MoneyUsageItem getItem(int position) {
        if(0 <= position && position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    public MoneyUsageItem getItemById(int id) {
        int start = 0;
        int end = items.size() - 1;
        int middle;

        while (end >= start) {
            middle = (start + end) / 2;
            MoneyUsageItem tmp = items.get(middle);

            if (tmp.getId() == id) {
                return tmp;
            }
            if (tmp.getId() < id) {
                start = middle + 1;
            }
            if (tmp.getId() > id) {
                end = middle - 1;
            }
        }

        return null;
    }



    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        listLayout.invalidate();
    }

    public void setListLayout(ViewGroup view) {
        this.listLayout = view;
    }*/
}
