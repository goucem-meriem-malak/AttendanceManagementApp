package com.example.attendancemanagementapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.attendancemanagementapp.classes.notification;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;

public class notifications extends AppCompatActivity {
    private Button menu, profile;
    private SessionManager sessionManager;
    private List<notification> notifications;
    private LinearLayout notificationContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        menu = findViewById(R.id.menu);
        profile = findViewById(R.id.profile);

        sessionManager = new SessionManager(getApplicationContext());

        getNotifications();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), departments.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void getNotifications() {
        notifications = sessionManager.getNotifications();
        notificationContainer = findViewById(R.id.list_notifications);

        for (int i = 0; i < notifications.size(); i++) {
            notification currentNotification = notifications.get(i);
            View notificationView = LayoutInflater.from(this).inflate(R.layout.notification, null);
            notificationView.setClickable(true);

            Timestamp timestamp = currentNotification.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

            TextView timeTextView = notificationView.findViewById(R.id.notification_time);
            TextView dateTextView = notificationView.findViewById(R.id.notification_date);
            TextView textTextView = notificationView.findViewById(R.id.notification_text);
            ImageView imgImageView = notificationView.findViewById(R.id.notification_img);

            timeTextView.setText(timeFormat.format(timestamp.toDate()));
            dateTextView.setText(dateFormat.format(timestamp.toDate()));
            textTextView.setText(currentNotification.getSubject());

            if (currentNotification.getIdJustification()!=null){
                imgImageView.setImageResource(R.drawable.accepted);
                imgImageView.setImageDrawable(getTintedDrawable(R.drawable.accepted, R.color.green));
            } else {
                imgImageView.setImageResource(R.drawable.warning);
                imgImageView.setImageDrawable(getTintedDrawable(R.drawable.warning, R.color.yellow));
            }
            imgImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), session.class);
                    intent.putExtra("idCourse", currentNotification.getIdCourse());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            textTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), session.class);
                    intent.putExtra("idCourse", currentNotification.getIdCourse());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            notificationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), session.class);
                    intent.putExtra("idCourse", currentNotification.getIdCourse());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });

            notificationContainer.addView(notificationView);
        }
    }

    private Drawable getTintedDrawable(int drawableResId, int colorResId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableResId);
        int color = ContextCompat.getColor(this, colorResId);
        drawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }
}