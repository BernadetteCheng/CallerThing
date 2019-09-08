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

public class MainService extends Service implements View.OnTouchListener {
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
        this.finalString = intent.getStringExtra("callId");
        addOverlayView();
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