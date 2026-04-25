package com.cybernetyx.eyerisboard.clockoverlay;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import androidx.core.app.NotificationCompat;

public class ClockOverlayService extends Service {

    private WindowManager windowManager;
    private View overlayView;
    private ClockView clockView;
    private Handler handler;
    private Runnable updateRunnable;

    private static final String CHANNEL_ID = "ClockOverlayChannel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create notification channel for Android O and above
        createNotificationChannel();

        // Start as foreground service
        startForeground(NOTIFICATION_ID, createNotification());

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Inflate the overlay layout
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_clock, null);
        clockView = overlayView.findViewById(R.id.clockView);

        // Set up window parameters
        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 100;
        params.y = 100;

        // Add view to window
        windowManager.addView(overlayView, params);

        // Make the overlay draggable
        overlayView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(overlayView, params);
                        return true;
                }
                return false;
            }
        });

        // Start updating the clock
        handler = new Handler();
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                clockView.invalidate();
                handler.postDelayed(this, 1000); // Update every second
            }
        };
        handler.post(updateRunnable);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Clock Overlay Service",
                    NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Keeps the clock overlay running");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("EyeRIS Clock")
                .setContentText("Clock overlay is running")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(pendingIntent)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayView != null && windowManager != null) {
            windowManager.removeView(overlayView);
        }
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
