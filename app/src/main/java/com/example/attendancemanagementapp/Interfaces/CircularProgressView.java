package com.example.attendancemanagementapp.Interfaces;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircularProgressView extends View {
    private Paint paint;
    private int progress;

    public CircularProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20); // Set your desired stroke width
        paint.setColor(getResources().getColor(android.R.color.holo_green_light)); // Set your desired color (green in this case)
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 5; // Subtracting 5 for padding

        // Draw the full red circle
        paint.setColor(getResources().getColor(android.R.color.holo_red_light)); // Set your desired color (red in this case)
        canvas.drawCircle(centerX, centerY, radius, paint);

        // Draw the progress arc
        paint.setColor(getResources().getColor(android.R.color.holo_green_light)); // Set your desired color (green in this case)
        paint.setStyle(Paint.Style.STROKE);
        float angle = 360 * progress / 100f;
        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(rectF, -90, angle, false, paint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
