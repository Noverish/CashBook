package com.noverish.cashbook.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.noverish.cashbook.NotificationItem;
import com.noverish.cashbook.R;
import com.noverish.cashbook.database.DatabaseClient;


/**
 * Created by Noverish on 2016-02-28.
 */
public class NotificationListenerActivity extends Activity {

    public static final int REQUEST_CODE_NOTIFICATION_LIST = 1234;
    public static final int REQUEST_GET_NOTIFICATION_CODE = 4321;

    private TextView txtView;
    private NotificationReceiver nReceiver;

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_listener);
        txtView = (TextView) findViewById(R.id.textView);
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.noverish.cashbook.notification.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver,filter);

        Button nowListButton = (Button) findViewById(R.id.now_list_button);
        Button savedListButton = (Button) findViewById(R.id.saved_list_button);
        nowListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowListBtnClicked();
            }
        });
        savedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedListBtnClicked();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    public void nowListBtnClicked() {
        Intent i = new Intent("LIST");
        i.putExtra("command", REQUEST_CODE_NOTIFICATION_LIST);
        sendBroadcast(i);
    }

    public void savedListBtnClicked() {
        StringBuilder builder = new StringBuilder();

        for(NotificationItem item : DatabaseClient.getAll(NotificationItem.class))
            builder.append(item.toString()).append("\n\n");

        txtView.setText(builder.toString());
    }

    class NotificationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            txtView.setText(intent.getStringExtra("notification_event"));
        }
    }
}