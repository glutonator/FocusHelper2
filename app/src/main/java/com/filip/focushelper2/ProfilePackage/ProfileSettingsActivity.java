package com.filip.focushelper2.ProfilePackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.filip.focushelper2.AppListPackage.AppList;
import com.filip.focushelper2.AppListPackage.AppsListActivity;
import com.filip.focushelper2.R;

import java.util.Map;

public class ProfileSettingsActivity extends AppCompatActivity {

    private String profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        Intent previousIntent = getIntent();
        profileName = previousIntent.getStringExtra("profileName");
        EditText profileNameTextEdit = (EditText) findViewById(R.id.editTextProfileName);
        profileNameTextEdit.setText(profileName);
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
        finish();
    }

    public void OnButtonDelete(View view) {
        deleteSharedPreferences(profileName);
        finish();
    }
}

