package com.example.finalproject_hailea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashSet;
import java.util.Set;

public class EventsTracker extends AppCompatActivity {

    LinearLayout listOfTests;
    Button addTest, home;
    SharedPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events_tracker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            listOfTests = findViewById(R.id.llEvents);
            addTest = findViewById(R.id.btnAddTest);
            home = findViewById(R.id.btnHomeTracker);

            userPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            loadPreferences();

            addTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(EventsTracker.this, AddTest.class));
                }
            });

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(EventsTracker.this, MainActivity.class));
                }
            });

            return insets;
        });
    }

    public void loadPreferences() {
        Set<String> tests = userPrefs.getStringSet("tests", new HashSet<>());

        int count = 0;
        for (String testString : tests) {
            String[] testSection = testString.split("\\|");
        }
    }
}