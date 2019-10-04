package com.example.moviecatalogsubmission3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class ReminderSettingActivity extends AppCompatActivity {

    private Switch release_toggle, daily_toggle;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setting);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        release_toggle = findViewById(R.id.release_switch);
        daily_toggle = findViewById(R.id.daily_switch);

        boolean release_status = sharedPreferences.getBoolean("release_status", false);
        boolean daily_status = sharedPreferences.getBoolean("daily_status", false);

        if (release_status) {
            release_toggle.setChecked(true);
            System.out.println("Release Status: On");
        } else {
            release_toggle.setChecked(false);
            System.out.println("Release Status: Off");
        }

        if (daily_status) {
            daily_toggle.setChecked(true);
            System.out.println("Daily Status: On");
        } else {
            daily_toggle.setChecked(false);
            System.out.println("Daily Status: Off");
        }

        release_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putBoolean("release_status", true);
                    editor.apply();
                    System.out.println("Release Status: On");
                } else {
                    editor.putBoolean("release_status", false);
                    editor.apply();
                    System.out.println("Release Status: Off");
                }
            }
        });

        daily_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putBoolean("daily_status", true);
                    editor.apply();
                    System.out.println("Daily Status: On");
                } else {
                    editor.putBoolean("daily_status", false);
                    editor.apply();
                    System.out.println("Daily Status: Off");
                }
            }
        });









    }
}
