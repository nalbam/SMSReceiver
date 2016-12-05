package com.nalbam.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SMSBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context mContext, Intent intent) {
        String action = intent.getAction();

        if ("android.provider.Telephony.SMS_RECEIVED".equals(action)) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }

            Object messages[] = (Object[]) bundle.get("pdus");
            if (messages == null) {
                return;
            }

            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
            }

            this.abortBroadcast();

            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy년 MM월 HH시 mm분 ss초", Locale.KOREA);

            String originDate = mDateFormat.format(curDate);
            String originAddr = smsMessage[0].getOriginatingAddress();
            String originBody = smsMessage[0].getMessageBody();

            String message = originDate + "\n" + originAddr + "\n" + originBody;

            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

//            Intent showSMSIntent = new Intent(mContext, MainActivity.class);
//            showSMSIntent.putExtra("originDate", originDate);
//            showSMSIntent.putExtra("originAddr", originAddr);
//            showSMSIntent.putExtra("originBody", originBody);
//            showSMSIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//            Intent showSMSIntent = new Intent(mContext, ShowSMSActivity.class);
//            showSMSIntent.putExtra("originNum", origNumber);
//            showSMSIntent.putExtra("smsDate", originDate);
//            showSMSIntent.putExtra("originText", Message);
//            showSMSIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//            mContext.startActivity(showSMSIntent);
        }
    }

}
