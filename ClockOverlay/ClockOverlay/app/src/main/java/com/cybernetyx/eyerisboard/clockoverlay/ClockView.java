package com.cybernetyx.eyerisboard.clockoverlay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ClockView extends View {

    private Paint circlePaint;
    private Paint hourHandPaint;
    private Paint minuteHandPaint;
    private Paint secondHandPaint;
    private Paint textPaint;
    private Paint datePaint;
    private Paint numberPaint;

    private int centerX;
    private int centerY;
    private int radius;

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Circle paint (clock face)
        circlePaint = new Paint();
        circlePaint.setColor(Color.argb(200, 33, 33, 33));
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);

        // Hour hand
        hourHandPaint = new Paint();
        hourHandPaint.setColor(Color.WHITE);
        hourHandPaint.setStrokeWidth(8);
        hourHandPaint.setStrokeCap(Paint.Cap.ROUND);
        hourHandPaint.setAntiAlias(true);

        // Minute hand
        minuteHandPaint = new Paint();
        minuteHandPaint.setColor(Color.WHITE);
        minuteHandPaint.setStrokeWidth(6);
        minuteHandPaint.setStrokeCap(Paint.Cap.ROUND);
        minuteHandPaint.setAntiAlias(true);

        // Second hand
        secondHandPaint = new Paint();
        secondHandPaint.setColor(Color.RED);
        secondHandPaint.setStrokeWidth(3);
        secondHandPaint.setStrokeCap(Paint.Cap.ROUND);
        secondHandPaint.setAntiAlias(true);

        // Digital time text
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);

        // Date text
        datePaint = new Paint();
        datePaint.setColor(Color.LTGRAY);
        datePaint.setTextSize(28);
        datePaint.setTextAlign(Paint.Align.CENTER);
        datePaint.setAntiAlias(true);

        // Clock numbers
        numberPaint = new Paint();
        numberPaint.setColor(Color.LTGRAY);
        numberPaint.setTextSize(24);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2 - 80; // Offset for digital display below
        radius = Math.min(centerX, centerY) - 20;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // Draw analog clock
        drawAnalogClock(canvas, hour, minute, second);

        // Draw digital time with seconds
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        String timeString = timeFormat.format(calendar.getTime());
        canvas.drawText(timeString, centerX, centerY + radius + 60, textPaint);

        // Draw date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
        String dateString = dateFormat.format(calendar.getTime());
        canvas.drawText(dateString, centerX, centerY + radius + 95, datePaint);
    }

    private void drawAnalogClock(Canvas canvas, int hour, int minute, int second) {
        // Draw clock face
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // Draw border
        Paint borderPaint = new Paint();
        borderPaint.setColor(Color.argb(255, 100, 100, 100));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(4);
        borderPaint.setAntiAlias(true);
        canvas.drawCircle(centerX, centerY, radius, borderPaint);

        // Draw hour markers and numbers
        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians((i * 30) - 90);
            float startX = (float) (centerX + (radius - 30) * Math.cos(angle));
            float startY = (float) (centerY + (radius - 30) * Math.sin(angle));
            float endX = (float) (centerX + (radius - 10) * Math.cos(angle));
            float endY = (float) (centerY + (radius - 10) * Math.sin(angle));

            Paint markerPaint = new Paint();
            markerPaint.setColor(Color.LTGRAY);
            markerPaint.setStrokeWidth(3);
            markerPaint.setAntiAlias(true);
            canvas.drawLine(startX, startY, endX, endY, markerPaint);

            // Draw numbers
            float numX = (float) (centerX + (radius - 50) * Math.cos(angle));
            float numY = (float) (centerY + (radius - 50) * Math.sin(angle) + 8);
            canvas.drawText(String.valueOf(i), numX, numY, numberPaint);
        }

        // Calculate angles
        float hourAngle = (hour % 12 + minute / 60f) * 30f - 90f;
        float minuteAngle = (minute + second / 60f) * 6f - 90f;
        float secondAngle = second * 6f - 90f;

        // Draw hour hand
        drawHand(canvas, hourAngle, radius * 0.5f, hourHandPaint);

        // Draw minute hand
        drawHand(canvas, minuteAngle, radius * 0.7f, minuteHandPaint);

        // Draw second hand
        drawHand(canvas, secondAngle, radius * 0.8f, secondHandPaint);

        // Draw center dot
        Paint centerPaint = new Paint();
        centerPaint.setColor(Color.WHITE);
        centerPaint.setStyle(Paint.Style.FILL);
        centerPaint.setAntiAlias(true);
        canvas.drawCircle(centerX, centerY, 10, centerPaint);
    }

    private void drawHand(Canvas canvas, float angle, float length, Paint paint) {
        double radians = Math.toRadians(angle);
        float endX = (float) (centerX + length * Math.cos(radians));
        float endY = (float) (centerY + length * Math.sin(radians));
        canvas.drawLine(centerX, centerY, endX, endY, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = 400; // Default size
        setMeasuredDimension(size, size + 120); // Extra space for digital display
    }
}
