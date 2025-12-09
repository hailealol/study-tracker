package com.example.finalproject_hailea;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTest extends AppCompatActivity {

    EditText subject, notes;
    Button date, save, back;
    TextView selectedDate;
    DateFormat dateFormat;
    SharedPreferences userPrefs;
    String chosenDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_test);

        subject = findViewById(R.id.etSubject);
        notes = findViewById(R.id.etNotes);
        date = findViewById(R.id.btnPickDate);
        save = findViewById(R.id.btnSaveTest);
        back = findViewById(R.id.btnReturn);
        selectedDate = findViewById(R.id.tvSelectedDate);

        userPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTest.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year,month,dayOfMonth);
                                dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
                                chosenDate = dateFormat.format(calendar.getTime());

                                selectedDate.setText(chosenDate);
                            }
                        },
                        year,
                        month,
                        day
                );
                datePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTest();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddTest.this, EventsTracker.class));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });
    }

    public void addTest() {
        String getSubject = subject.getText().toString().trim();
        String getNotes = notes.getText().toString().trim();

        if (getSubject.isEmpty()) {
            Toast.makeText(this, "Add a subject to proceed.", Toast.LENGTH_LONG).show();
            return;
        }

        if (chosenDate.isEmpty()) {
            Toast.makeText(this, "Add a date to proceed.", Toast.LENGTH_LONG).show();
            return;
        }

        // Get all tests
        String allTheTests = userPrefs.getString("tests","");

        String blueprint = getSubject + " | " + chosenDate + " | " + getNotes;

        if (allTheTests.isEmpty()) {
            allTheTests = blueprint;
        } else {
            allTheTests += ";;" + blueprint; // ;; is a separator between entries
        }

        userPrefs.edit().putString("tests", allTheTests).commit();

        Toast.makeText(this, "Your test has been added to your list!", Toast.LENGTH_LONG).show();
    }
}