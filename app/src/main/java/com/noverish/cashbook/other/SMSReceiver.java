package com.noverish.cashbook.other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Noverish on 2016-05-18.
 */
public class SMSReceiver extends BroadcastReceiver {
    final SmsManager sms = SmsManager.getDefault();

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {
                // SMS 메시지를 파싱합니다.
                Object messages[] = (Object[])bundle.get("pdus");
                SmsMessage smsMessage[] = new SmsMessage[messages.length];

                for(int i = 0; i < messages.length; i++) {
                    // PDU 포맷으로 되어 있는 메시지를 복원합니다.
                    //smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);

                    if (Build.VERSION.SDK_INT >= 19) { //KITKAT
                        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                        smsMessage[i] = msgs[0];
                    } else {
                        Object pdus[] = (Object[]) bundle.get("pdus");
                        smsMessage[i] = SmsMessage.createFromPdu((byte[]) pdus[0]);
                    }

                }

                // SMS 수신 시간 확인
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(smsMessage[0].getTimestampMillis());
                Log.e(TAG, "문자 수신 시간 : " + calendar.toString());

                // SMS 발신 번호 확인
                String origNumber = smsMessage[0].getOriginatingAddress();
                Log.e(TAG, "문자 발신 번호 : " + origNumber);

                // SMS 메시지 확인
                String message = smsMessage[0].getMessageBody();
                Log.e(TAG, "문자 수신 내용 : " + message);
            } // bundle is null

        } catch (Exception e) {
            Log.e(TAG, "Exception smsReceiver");
            e.printStackTrace();
        }
    }
}
