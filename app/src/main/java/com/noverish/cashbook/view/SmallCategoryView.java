package com.noverish.cashbook.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.cashbook.R;

/**
 * Created by Noverish on 2016-10-08.
 */

public class SmallCategoryView extends LinearLayout {
    private Context context;
    private String text;

    public SmallCategoryView(Context context, String text) {
        super(context);
        this.context = context;
        this.text = text;

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.category_view_small, this, true);

        TextView textView = (TextView) findViewById(R.id.category_view_small);
        textView.setText(text);
    }
}
