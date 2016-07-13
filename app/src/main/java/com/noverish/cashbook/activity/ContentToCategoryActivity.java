package com.noverish.cashbook.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.noverish.cashbook.R;
import com.noverish.cashbook.database.ContentToCategoryDatabase;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-07-14.
 */
public class ContentToCategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_to_category);

        ArrayList<String> database = ContentToCategoryDatabase.getContentToCategoryDatabase(this).getDatabase();

        ListView listView = (ListView)findViewById(R.id.activity_content_to_category_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for(String str : database) {
            adapter.add(str);
        }

        listView.setAdapter(adapter);
    }
}
