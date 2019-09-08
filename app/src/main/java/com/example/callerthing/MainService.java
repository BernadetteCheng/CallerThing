package com.example.callerthing;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainService extends Service implements View.OnTouchListener {
    public String number;
    public String jsonText;
    public Object jsonObject;
    public String finalString;
    private static final String TAG = MainService.class.getSimpleName();

    private WindowManager windowManager;

    private View floatyView;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.number = intent.getStringExtra("number");
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.ekata.com/3.0/phone.json?api_key=337a4654a9854d20b556ef2926eaa919&phone.country_hint=1&phone="+number;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        jsonText = response;
                        addOverlayView();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                jsonText = "error";
            }
        });
        queue.add(stringRequest);
        return Service.START_STICKY;
    }
    @Override
    public void onCreate() {

        super.onCreate();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }

    private void addOverlayView() {

        final WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        0,
                        PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 0;

        FrameLayout interceptorLayout = new FrameLayout(this);

        floatyView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.floating_view, interceptorLayout);

        floatyView.setOnTouchListener(this);

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

        finalString = "Name: " + name + "\n";
        finalString += "City: " + city;
        if (state_code != "") finalString += ", " + state_code;
        finalString += "\nType: " + type;

        TextView textView = interceptorLayout.findViewById(R.id.floatyText);
        if(finalString != "") {
            textView.setText(finalString);
        } else {
            textView.setText("error!");
        }
        windowManager.addView(floatyView, params);
    }

    @Override
    public void onDestroy() {
        if (floatyView != null) {
            windowManager.removeView(floatyView);
            floatyView = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        onDestroy();
        return true;
    }
}