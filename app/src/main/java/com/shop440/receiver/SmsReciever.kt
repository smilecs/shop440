package com.shop440.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import com.shop440.utils.SmsListener

class SmsReciever : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val data = intent.extras
        Log.d("Reciever", "test")
        val pdus = data!!.get("pdus") as Array<Any>
        for (i in pdus.indices) {
            val smsMessage = SmsMessage.createFromPdu(pdus[i] as ByteArray)
            val sender = smsMessage.displayOriginatingAddress
            if (sender == "shop440") {
                val messageBody = smsMessage.messageBody
                smsListener!!.messageReceived(messageBody)
            }
        }
    }

    companion object {
        private var smsListener: SmsListener? = null

        fun bindListener(listener: SmsListener) {
            smsListener = listener
        }
    }
}
