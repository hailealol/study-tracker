package com.example.finalproject_hailea;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends AppCompatActivity {

    public static Integer[] profilePictures = {
            R.drawable.option01,
            R.drawable.option02,
            R.drawable.option03,
            R.drawable.option04,
    };

    ImageView pic;
    GridView grid;
    EditText chooseName;
    Button clear, save, home;
    SharedPreferences userPrefs;
    int imgId;
    int selectedPosition = -1;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            pic = findViewById(R.id.ivSelectedImg);
            grid = findViewById(R.id.gvPictures);

            chooseName = findViewById(R.id.etName);

            save = findViewById(R.id.btnSave);
            clear = findViewById(R.id.btnClear);
            home = findViewById(R.id.btnHome);

            grid.setAdapter(new ImageAdapter(this));

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    pic.setImageResource(profilePictures[position]);
                    imgId = profilePictures[position];
                    selectedPosition = position;
                }
            });

            userPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            loadPreferences();

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savePreferences();
                }
            });

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearPreferences();
                }
            });

            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Profile.this, MainActivity.class));
                }
            });

            return insets;
        });
    }

    public void savePreferences() {
        userName = chooseName.getText().toString();

        if (userName.isEmpty()) {
            chooseName.setError("Name field can not be empty");
            return;
        }

        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putString("userName", userName);
        if (selectedPosition == -1) {
            editor.putInt("userImg", R.drawable.default_pfp);
        } else {
            editor.putInt("userImg", imgId);
        }
        editor.commit();

        updateUI(userName,imgId);
        Toast.makeText(this, "Your profile changes have been saved!", Toast.LENGTH_LONG).show();
    }

    public void loadPreferences() {
        String loadName = userPrefs.getString("userName", "");
        int loadImg = userPrefs.getInt("userImg", R.drawable.default_pfp);

        if (loadName.isEmpty()) {
            return;
        }

        chooseName.setText(loadName);
        imgId = loadImg;

        for (int i = 0; i < profilePictures.length; i++) {
            if (profilePictures[i] == loadImg) {
                selectedPosition = i;
                break;
            }
        }

        updateUI(loadName, imgId);
    }

    public void clearPreferences() {
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.clear();
        editor.commit();

        chooseName.setText("");
        pic.setImageResource(R.drawable.default_pfp);
        Toast.makeText(this, "Your profile has been cleared!", Toast.LENGTH_LONG).show();
    }

    public void updateUI(String name, int id) {
        chooseName.setText(name);

        if (id != 0) {
            pic.setImageResource(id);
        } else {
            pic.setImageResource(R.drawable.default_pfp);
        }
    }
}