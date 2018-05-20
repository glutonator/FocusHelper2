package com.filip.focushelper2.ProfilePackage;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.filip.focushelper2.AppListPackage.AppList;
import com.filip.focushelper2.AppListPackage.AppsListActivity;
import com.filip.focushelper2.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProfileSettingsActivity extends AppCompatActivity {

    private String profileName;
    private Set<String> setDaysOfTheWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        Intent previousIntent = getIntent();
        profileName = previousIntent.getStringExtra("profileName");
        EditText profileNameTextEdit = (EditText) findViewById(R.id.editTextProfileName);
        profileNameTextEdit.setText(profileName);

        setDaysOfTheWeek=new HashSet<>();

        //seting times prevoise settings
        String profileNameParams = profileName + "_Params";
        SharedPreferences sharedPreferences = getSharedPreferences(profileNameParams, MODE_PRIVATE);
        int hoursStart =sharedPreferences.getInt("hoursStart",8);
        int minutesStart =sharedPreferences.getInt("minutesStart",0);
        int hoursStop =sharedPreferences.getInt("hoursStop",16);
        int minutesStop =sharedPreferences.getInt("minutesStop",0);
        String dayyy;
        dayyy = sharedPreferences.getString("Monday", "");
        if (dayyy.equals("Monday")) {
            Button button = (Button) findViewById(R.id.day);
            button.callOnClick();
        }
        dayyy = sharedPreferences.getString("Tuesday", "");
        if (dayyy.equals("Tuesday")) {
            Button button = (Button) findViewById(R.id.day2);
            button.callOnClick();
        }
        dayyy = sharedPreferences.getString("Wednesday", "");
        if (dayyy.equals("Wednesday")) {
            Button button = (Button) findViewById(R.id.day3);
            button.callOnClick();
        }
        dayyy = sharedPreferences.getString("Thursday", "");
        if (dayyy.equals("Thursday")) {
            Button button = (Button) findViewById(R.id.day4);
            button.callOnClick();
        }
        dayyy = sharedPreferences.getString("Friday", "");
        if (dayyy.equals("Friday")) {
            Button button = (Button) findViewById(R.id.day5);
            button.callOnClick();
        }
        dayyy = sharedPreferences.getString("Saturday", "");
        if (dayyy.equals("Saturday")) {
            Button button = (Button) findViewById(R.id.day6);
            button.callOnClick();
        }
        dayyy = sharedPreferences.getString("Sunday", "");
        if (dayyy.equals("Sunday")) {
            Button button = (Button) findViewById(R.id.day7);
            button.callOnClick();
        }



//        Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();
//        for (Map.Entry<String, ?> entry : sharedPreferencesAll.entrySet()) {
//            String temp =(entry.getValue();
//            if(temp.equals("hoursStart")||temp.equals("minutesStart")||temp.equals("hoursStop")||temp.equals("minutesStop")) {
//
//            }
//            else {
//
//            }
//        }


//        editorParam.putInt("hoursStart",hoursStart);
//        editorParam.putInt("minutesStart",minutesStart);
//
//        editorParam.putInt("hoursStop",hoursStop);
//        editorParam.putInt("minutesStop",minutesStop);

        //setting time and dialogs
//        Calendar mcurrentTime = Calendar.getInstance();
//        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//        int minute = mcurrentTime.get(Calendar.MINUTE);

        TextView timeViewStart =(TextView) findViewById(R.id.timeTextViewStart);
//        timeViewStart.setText( hour + ":" + minute);
        timeViewStart.setText( hoursStart + ":" + minutesStart);

        timeViewStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ProfileSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeViewStart.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        TextView timeViewStop =(TextView) findViewById(R.id.timeTextViewStop);
//        timeViewStop.setText( hour+1 + ":" + minute);
        timeViewStop.setText( hoursStop + ":" + minutesStop);

        timeViewStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ProfileSettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeViewStop.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    public void OnButtonListOfBlockedApps(View view) {
        Intent myIntent = new Intent(ProfileSettingsActivity.this, AppsListActivity.class);
        myIntent.putExtra("profileName", profileName);
        startActivity(myIntent);
    }

    public void OnButtonOkey(View view) {
        EditText txtDescription = (EditText) findViewById(R.id.editTextProfileName);
        String profileName = txtDescription.getText().toString();
        Toast.makeText(this, profileName, Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferencesTEMP = getSharedPreferences("temp", MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences(profileName, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();


        Map<String, Boolean> sharedPreferencesTEMPAll = (Map<String, Boolean>) sharedPreferencesTEMP.getAll();

        for (Map.Entry<String, Boolean> entry : sharedPreferencesTEMPAll.entrySet()) {
            editor.putBoolean(entry.getKey(), entry.getValue());
            Log.wtf("sheredfiles_apps", entry.getKey() + " " + entry.getValue());

        }
        editor.apply();
        deleteSharedPreferences("temp");

        //add configuration date and time
        deleteSharedPreferences(profileName+"_Params");
        SharedPreferences sharedPreferencesParam = getSharedPreferences(profileName+"_Params", MODE_PRIVATE);
        SharedPreferences.Editor editorParam = sharedPreferencesParam.edit();
        for(String i : setDaysOfTheWeek) {
            editorParam.putString(i,i);
        }

        TextView timeViewStart =(TextView) findViewById(R.id.timeTextViewStart);
        String temp = timeViewStart.getText().toString();
        String [] result = temp.split(":");
        int hoursStart = Integer.parseInt(result[0]);
        int minutesStart = Integer.parseInt(result[1]);
        editorParam.putInt("hoursStart",hoursStart);
        editorParam.putInt("minutesStart",minutesStart);

        TextView timeViewStop =(TextView) findViewById(R.id.timeTextViewStop);
        String temp2 = timeViewStop.getText().toString();
        String [] result2 = temp2.split(":");
        int hoursStop = Integer.parseInt(result2[0]);
        int minutesStop = Integer.parseInt(result2[1]);
        editorParam.putInt("hoursStop",hoursStop);
        editorParam.putInt("minutesStop",minutesStop);

        editorParam.apply();

        finish();
    }

    public void OnButtonDelete(View view) {
        deleteSharedPreferences(profileName);
        finish();
    }

    private void setSetDaysOfTheWeek(String name) {
        this.setDaysOfTheWeek.add(name);
    }

    private void deleteSetDaysOfTheWeek(String name) {
        this.setDaysOfTheWeek.remove(name);
    }

    private boolean isInSetDaysOfTheWeek(String name) {
        return this.setDaysOfTheWeek.contains(name);
    }


    public void onButton1(View view) {
        String day="Monday";
        Button button =(Button) findViewById(R.id.day);
        //zawiera już zapisany
        if(isInSetDaysOfTheWeek(day)==true) {
            deleteSetDaysOfTheWeek(day);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//            button.setBackgroundColor(R.color.colorPrimary);
        }
        else {
            setSetDaysOfTheWeek(day);
            button.setBackgroundColor(Color.RED);
        }
    }
    public void onButton2(View view) {
        String day="Tuesday";
        Button button =(Button) findViewById(R.id.day2);
        if(isInSetDaysOfTheWeek(day)==true) {
            deleteSetDaysOfTheWeek(day);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else {
            setSetDaysOfTheWeek(day);
            button.setBackgroundColor(Color.RED);
        }
    }
    public void onButton3(View view) {
        String day="Wednesday";
        Button button =(Button) findViewById(R.id.day3);
        if(isInSetDaysOfTheWeek(day)==true) {
            deleteSetDaysOfTheWeek(day);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else {
            setSetDaysOfTheWeek(day);
            button.setBackgroundColor(Color.RED);
        }
    }
    public void onButton4(View view) {
        String day="Thursday";
        Button button =(Button) findViewById(R.id.day4);
        if(isInSetDaysOfTheWeek(day)==true) {
            deleteSetDaysOfTheWeek(day);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else {
            setSetDaysOfTheWeek(day);
            button.setBackgroundColor(Color.RED);
        }
    }
    public void onButton5(View view) {
        String day="Friday";
        Button button =(Button) findViewById(R.id.day5);
        if(isInSetDaysOfTheWeek(day)==true) {
            deleteSetDaysOfTheWeek(day);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else {
            setSetDaysOfTheWeek(day);
            button.setBackgroundColor(Color.RED);
        }
    }
    public void onButton6(View view) {
        String day="Saturday";
        Button button =(Button) findViewById(R.id.day6);
        if(isInSetDaysOfTheWeek(day)==true) {
            deleteSetDaysOfTheWeek(day);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else {
            setSetDaysOfTheWeek(day);
            button.setBackgroundColor(Color.RED);
        }
    }
    public void onButton7(View view) {
        String day="Sunday";
        Button button =(Button) findViewById(R.id.day7);
        if(isInSetDaysOfTheWeek(day)==true) {
            deleteSetDaysOfTheWeek(day);
            button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else {
            setSetDaysOfTheWeek(day);
            button.setBackgroundColor(Color.RED);
        }
    }

}

