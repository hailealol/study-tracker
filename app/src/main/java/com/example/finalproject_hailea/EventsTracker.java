package com.example.finalproject_hailea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventsTracker extends AppCompatActivity {

    LinearLayout listOfTests;
    Button addTest, home;
    SharedPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events_tracker);

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });
    }

    public void loadPreferences() {
        String tests = userPrefs.getString("tests", "");

        // iterate through tests and display each of them in a row with linearlayout
        if (!tests.isEmpty()) { // if tests isn't empty
            String[] testSection = tests.split(";;"); // split at separators

            for (String testString : testSection) {
                String[] testParts = testString.split("\\|");

                if (testParts.length == 3) {
                    String subject = testParts[0];
                    String date = testParts[1];
                    String notes = testParts[2];

                    addRow(subject, date, notes, testString);
                }


            }
        }
    }

    public void addRow(String subject, String date, String notes, String testString) {
        LinearLayout row = new LinearLayout(this); // create a row for each test

        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(5,5,5,5);


        // display the test information
        TextView tvInfo = new TextView(this);
        tvInfo.setText(subject + " | " + date + "\nNotes: " + notes);
        tvInfo.setTextSize(16);

        // a button to delete any test entry
        Button btnDelete = new Button(this);
        btnDelete.setText("Delete this test");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTest(testString);
            }
        });

        row.addView(tvInfo); // add the info to the row
        row.addView(btnDelete); // add the button to the row
        listOfTests.addView(row); // add the actual row to our main layout
    }

    public void removeTest(String testString) {
        String tests = userPrefs.getString("tests", "");

        if (!tests.isEmpty()) {
            String[] testSection = tests.split(";;");
            List<String> testList = new ArrayList<>(Arrays.asList(testSection)); // rebuild string

            // remove the test the user wants to delete
            if (testList.remove(testString)) {
                String updatedTests = String.join(";;", testList);
                userPrefs.edit().putString("tests", updatedTests).commit();
                Toast.makeText(this, "Test has been deleted.", Toast.LENGTH_LONG).show();
                updateUI();
            }
        }
    }

    // update the UI so it gets rid of the deleted test
    public void updateUI() {
        listOfTests.removeAllViews(); // get rid of everything
        loadPreferences(); // reload everything
    }
}