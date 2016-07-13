package com.noverish.cashbook.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noverish.cashbook.R;

/**
 * Created by Noverish on 2016-04-19.
 */
public class SettingActivity extends AppCompatActivity{

    Context context;
    android.os.Handler handler = new Handler();
    LinearLayout layout;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.setting_layout);

        layout = (LinearLayout) findViewById(R.id.setting_layout_layout);
        context = this;
    }

    public class CustomReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("text");

             Log.e("TAG", "asdf000");

            if(str != null) {
                TextView view = new TextView(context);
                view.setText(str);

                layout.addView(view);
            }
        }
    }
}
