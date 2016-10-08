package com.noverish.cashbook.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.cashbook.R;

/**
 * Created by Noverish on 2016-10-08.
 */

public class BigCategoryView extends LinearLayout{
    public BigCategoryView(Context context, String text, int color) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.category_view_big, this, true);

        TextView textView = (TextView) findViewById(R.id.category_view_big);
        textView.setText(text);
        textView.setTextColor(color);
    }
}
