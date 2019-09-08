package com.example.callerthing;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneStateListener extends PhoneStateListener {

    protected Context context;
    public static Boolean phoneRinging = false;

    public MyPhoneStateListener(Context context) {
        this.context = context;
    }

    public void onCallStateChanged(int state, String incomingNumber) {

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                Log.d("DEBUG", "IDLE");
                phoneRinging = false;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.d("DEBUG", "OFFHOOK");
                phoneRinging = false;
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d("DEBUG", "RINGING");
                phoneRinging = true;
                triggerOverlay(context, incomingNumber);
                break;
        }
    }
    private void triggerOverlay(Context context, String number) {
        Intent svc = new Intent(context, MainService.class);
        svc.putExtra("number", number);
        context.stopService(svc);
        context.startService(svc);
    }

}