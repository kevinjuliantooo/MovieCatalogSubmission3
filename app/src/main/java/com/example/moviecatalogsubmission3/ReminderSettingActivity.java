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
    private AlarmReceiverRelease alarmReceiverRelease;
    private AlarmReceiverDaily alarmReceiverDaily;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_setting);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();

        alarmReceiverDaily = new AlarmReceiverDaily();
        alarmReceiverRelease = new AlarmReceiverRelease();

        release_toggle = findViewById(R.id.release_switch);
        daily_toggle = findViewById(R.id.daily_switch);

        final boolean release_status = sharedPreferences.getBoolean("release_status", false);
        final boolean daily_status = sharedPreferences.getBoolean("daily_status", false);

        checkDailyStatus(daily_status);
        checkReleaseStatus(release_status);

        release_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putBoolean("release_status", true);
                    editor.apply();
                    System.out.println("Release Status: On");
//                    checkReleaseStatus(release_status);
                } else {
                    editor.putBoolean("release_status", false);
                    editor.apply();
                    System.out.println("Release Status: Off");
//                    checkReleaseStatus(release_status);
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
//                    checkDailyStatus(daily_status);
                } else {
                    editor.putBoolean("daily_status", false);
                    editor.apply();
                    System.out.println("Daily Status: Off");
//                    checkDailyStatus(daily_status);
                }
            }
        });
    }

    private void checkReleaseStatus(Boolean release_status) {
        if (release_status) {
            release_toggle.setChecked(true);
            alarmReceiverRelease.setRepeatingAlarm(this, AlarmReceiverDaily.TYPE_REPEATING,
                    "8:00");
            System.out.println("Release Status: On");
        } else {
            release_toggle.setChecked(false);
            alarmReceiverRelease.setCancelAlarm(getApplicationContext());
            System.out.println("Release Status: Off");
        }
    }

    private void checkDailyStatus(Boolean daily_status) {
        if (daily_status) {
            daily_toggle.setChecked(true);
            alarmReceiverDaily.setRepeatingAlarm(this, AlarmReceiverDaily.TYPE_REPEATING,
                    "7:00");
            System.out.println("Daily Status: On");
        } else {
            daily_toggle.setChecked(false);
            alarmReceiverDaily.setCancelAlarm(getApplicationContext());
            System.out.println("Daily Status: Off");
        }
    }
}
