package com.example.callerthing;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MyPhoneStateListener extends PhoneStateListener {

    protected Context context;
    public String jsonText;
    public static Boolean phoneRinging = false;
    public String callerData;

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
                getCallerData(context, incomingNumber);
                break;
        }
    }
    public void getCallerData (final Context context, final String number) {
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        String url = "https://api.ekata.com/3.0/phone.json?api_key=337a4654a9854d20b556ef2926eaa919&phone.country_hint=1&phone="+number;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        jsonText = response;
                        //PARSE
                        String name = "Unknown";
                        String city = "Unknown";
                        String state_code = "";
                        String type = "Unknown";

                        JsonParser parser = new JsonParser();
                        JsonObject jso = parser.parse(jsonText).getAsJsonObject();
                        try {
                            JsonObject entity = jso.getAsJsonArray("belongs_to").get(0).getAsJsonObject();
                            name = entity.get("name").getAsString();
                            type = entity.get("type").getAsString();
                        } catch (Exception e) {
                        }
                        try {
                            JsonObject address = jso.getAsJsonArray("current_addresses").get(0).getAsJsonObject();
                            city = address.get("city").getAsString();
                            state_code = address.get("state_code").getAsString();
                        } catch (Exception e) {
                        }
                        String finalString;
                        finalString = "Name: " + name + "\n";
                        finalString += "City: " + city;
                        if (state_code != "") finalString += ", " + state_code;
                        finalString += "\nType: " + type;
                        triggerOverlay(context, finalString);
                        HomeActivity.insertItem(name, number);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                jsonText = "error";
            }
        });
        queue.add(stringRequest);
    }
    private void triggerOverlay(Context context, String callId) {
        Intent svc = new Intent(context, MainService.class);
        svc.putExtra("callId", callId);
        context.stopService(svc);
        context.startService(svc);
    }
}