package com.noverish.cashbook.other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Noverish on 2016-05-03.
 */
public class BootCompleteReceiver extends BroadcastReceiver{
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            Intent myIntent = new Intent(context, NotificationsReadService.class);
            context.startService(myIntent);
        } else {
            Log.e(TAG, "아니야");
        }
    }
}
