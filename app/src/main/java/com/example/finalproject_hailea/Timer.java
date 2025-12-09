package com.example.finalproject_hailea;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Timer extends AppCompatActivity {

    TextView timer;
    EditText etMinutes, etSeconds;
    Button start, reset, home;
    CountDownTimer countDownTimer;
    long totalTimeMilliseconds;
    boolean isTimerRunning = false;

    private static final String CHANNEL_ID = "timer channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timer);

        timer = findViewById(R.id.tvTimerDisplay);
        etMinutes = findViewById(R.id.etTime);
        etSeconds = findViewById(R.id.etSec);
        start = findViewById(R.id.btnStart);
        reset = findViewById(R.id.btnReset);
        home = findViewById(R.id.btnTimerHome);

        createNotification();

        // get permission from the user to receive notifications
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},1);
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    startTimer();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Timer.this, MainActivity.class));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void startTimer() {
        String minInput = etMinutes.getText().toString().trim();
        String secInput = etSeconds.getText().toString().trim();
        int min;
        int sec;

        if (!minInput.isEmpty()) {
            min = Integer.parseInt(minInput);
        } else {
            min = 0;
        }

        if (!secInput.isEmpty()) {
            sec = Integer.parseInt(secInput);
        } else {
            sec = 0;
        }

        totalTimeMilliseconds = (min * 60 + sec) * 1000L; // convert to ms

        if (totalTimeMilliseconds <= 0) {
            Toast.makeText(this, "Please enter a time.", Toast.LENGTH_LONG).show();
            return;
        }

        countDownTimer = new CountDownTimer(totalTimeMilliseconds, 1000) {
            @Override
            public void onFinish() {
                isTimerRunning = false;
                timer.setText("00:00");
                updateTimer();
                sendNotification();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                totalTimeMilliseconds = millisUntilFinished;
                updateTimer();
            }
        }.start();
        isTimerRunning = true;
    }

    public void updateTimer() {
        int totalSec = (int) (totalTimeMilliseconds/1000);
        int min =  totalSec / 60;
        int sec = totalSec % 60; // get the remainder

        String formatTime = String.format("%02d:%02d", min, sec);
        timer.setText(formatTime);
    }

    public void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        isTimerRunning = false;
        totalTimeMilliseconds = 0;
        timer.setText("00:00");
        etMinutes.setText("");
        etSeconds.setText("");
    }

    public void createNotification() {
        CharSequence name = "Timer Channel";
        String desc = "Notifications for when timer completes";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new android.app.NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(desc);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void sendNotification() {
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Timer complete!")
                .setContentText("Your study timer has ended.")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .build();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
    }
}