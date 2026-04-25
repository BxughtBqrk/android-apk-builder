package com.cybernetyx.eyerisboard.clockoverlay;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 1001;
    private Button toggleButton;
    private boolean isServiceRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = findViewById(R.id.toggleButton);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isServiceRunning) {
                    stopClockService();
                } else {
                    if (checkOverlayPermission()) {
                        startClockService();
                    } else {
                        requestOverlayPermission();
                    }
                }
            }
        });

        // Check if service should be running
        updateButtonState();
    }

    private boolean checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            if (checkOverlayPermission()) {
                startClockService();
            } else {
                Toast.makeText(this, "Overlay permission is required for clock display", 
                    Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startClockService() {
        Intent intent = new Intent(this, ClockOverlayService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        isServiceRunning = true;
        updateButtonState();
        Toast.makeText(this, "Clock overlay started", Toast.LENGTH_SHORT).show();
    }

    private void stopClockService() {
        Intent intent = new Intent(this, ClockOverlayService.class);
        stopService(intent);
        isServiceRunning = false;
        updateButtonState();
        Toast.makeText(this, "Clock overlay stopped", Toast.LENGTH_SHORT).show();
    }

    private void updateButtonState() {
        if (isServiceRunning) {
            toggleButton.setText("Stop Clock Overlay");
            toggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            toggleButton.setText("Start Clock Overlay");
            toggleButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // You might want to check if service is actually running
        // For simplicity, we're using a local flag
    }
}
