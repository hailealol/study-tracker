package com.example.finalproject_hailea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    SharedPreferences userPrefs;
    TextView welcome;
    ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            userPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            welcome = findViewById(R.id.tvWelcome);
            pic = findViewById(R.id.ivProfilePic);

            loadProfile();

            String[] navigation ={
                    "Events Tracker",
                    "Timer",
                    "Edit Profile"
            };

            ArrayAdapter arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,android.R.id.text1, navigation);
            ListView listView= findViewById(R.id.lvNavigation);
            // connect the array to the listview
            listView.setAdapter(arrayAdapter);

            // handle the action when tapping on an item in the listView
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) { // position represents the item index in the array string
                        case 0:
                            startActivity(new Intent(MainActivity.this, EventsTracker.class));
                            break;
                        case 1:
                            startActivity(new Intent(MainActivity.this, Timer.class));
                            break;
                        case 2:
                            startActivity(new Intent(MainActivity.this, Profile.class));
                            break;
                    }
                }
            });

            return insets;
        });
    }
    public void loadProfile() {
        String name = userPrefs.getString("userName", "");
        int picId = userPrefs.getInt("userImg", R.drawable.default_pfp);

        updateUI(name, picId);
    }

    public void updateUI (String name, int id) {
        if (name.isEmpty()) {
            welcome.setText("Welcome!");
        } else {
            welcome.setText("Welcome, " + name + "!");
        }

        pic.setImageResource(id);
    }
}