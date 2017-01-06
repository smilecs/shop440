package com.shop440.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.shop440.Models.SmsListener;

public class SmsReciever extends BroadcastReceiver {
    private static SmsListener smsListener;

    @Override
    public void onReceive(Context context, Intent intent) {
    Bundle data = intent.getExtras();
        Log.d("Reciever", "test");
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0; i<pdus.length; i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            if(sender.equals("shop440")){
                String messageBody = smsMessage.getMessageBody();
                smsListener.messageReceived(messageBody);
            }
        }
    }

    public static void bindListener(SmsListener listener) {
        smsListener = listener;
    }
}
