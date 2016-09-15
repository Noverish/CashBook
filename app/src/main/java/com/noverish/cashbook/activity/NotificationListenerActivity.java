package com.noverish.cashbook.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.noverish.cashbook.R;


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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    public void buttonClicked(View v){
        if(v.getId() == R.id.btnListNotify){
            Intent i = new Intent("LIST");
            i.putExtra("command", REQUEST_CODE_NOTIFICATION_LIST);
            sendBroadcast(i);
        }
    }

    class NotificationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + "\nn" + txtView.getText();
            txtView.setText(temp);
        }
    }
}