package com.filip.focushelper2.ProfilePackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.filip.focushelper2.AppListPackage.AppsListActivity;
import com.filip.focushelper2.MapsActivity;
import com.filip.focushelper2.R;

import java.util.Map;

public class ProfileLocationActivity extends AppCompatActivity {

    private String profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_location);

        Intent previousIntent = getIntent();
        profileName = previousIntent.getStringExtra("profileName");
//        if (profileName == null) {
//            Log.wtf("DSadadsa", "nulllllllllllll");
//            profileName = "temp";
//            deleteSharedPreferences(profileName);
//        }
        android.widget.EditText profileNameTextEdit = (android.widget.EditText) findViewById(R.id.editTextProfileName);
        profileNameTextEdit.setText(profileName);
        String profileNameParams = profileName + "_Params";
        android.content.SharedPreferences sharedPreferences = getSharedPreferences(profileNameParams, MODE_PRIVATE);
        //Todo: odczyt zapisanej lokazliazacji z pliku
        float latitude = sharedPreferences.getFloat("latitude",0);
        float longitude = sharedPreferences.getFloat("longitude",0);
        TextView textView=(TextView)findViewById(R.id.TextViewLatitude);
        textView.setText(Float.toString(latitude));
        TextView textView2=(TextView)findViewById(R.id.TextViewLongitude);
        textView2.setText(Float.toString(longitude));


    }

    public void OnButtonDelete(View view) {
        deleteSharedPreferences(profileName);
        deleteSharedPreferences(profileName+"_Params");

        finish();
    }
    public void OnButtonListOfBlockedApps(View view) {
        Intent myIntent = new Intent(ProfileLocationActivity.this, AppsListActivity.class);
        myIntent.putExtra("profileName", profileName);
        startActivity(myIntent);
    }

    public void OnButtonSetLocation(View view) {
        Intent myIntent = new Intent(ProfileLocationActivity.this, MapsActivity.class);
        myIntent.putExtra("profileName", profileName);
//        startActivity(myIntent);
        startActivityForResult(myIntent, 0);

    }

    public void OnButtonOkey(View view) {
        EditText txtDescription = (EditText) findViewById(R.id.editTextProfileName);
        String profileName = txtDescription.getText().toString();
        Toast.makeText(this, profileName, Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferencesTEMP = getSharedPreferences("temp", MODE_PRIVATE);
        SharedPreferences sharedPreferencesTEMP_PARAMS = getSharedPreferences("temp_Params", MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences(profileName, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Map<String, Boolean> sharedPreferencesTEMPAll = (Map<String, Boolean>) sharedPreferencesTEMP.getAll();

        for (Map.Entry<String, Boolean> entry : sharedPreferencesTEMPAll.entrySet()) {
            editor.putBoolean(entry.getKey(), entry.getValue());
            Log.wtf("sheredfiles_apps", entry.getKey() + " " + entry.getValue());

        }
        editor.apply();
        deleteSharedPreferences("temp");

        //adding configuration to location
        deleteSharedPreferences(profileName+"_Params");
        SharedPreferences sharedPreferencesParam = getSharedPreferences(profileName+"_Params", MODE_PRIVATE);
        SharedPreferences.Editor editorParam = sharedPreferencesParam.edit();

        TextView textView = (TextView)findViewById(R.id.TextViewLatitude);
        float latitude = Float.parseFloat(textView.getText().toString());
        TextView textView2 = (TextView)findViewById(R.id.TextViewLongitude);
        float longitude = Float.parseFloat(textView2.getText().toString());
        editorParam.putFloat("latitude",latitude);
        editorParam.putFloat("longitude",longitude);

        //setting type configuration
        editorParam.putBoolean("type",true);
        editorParam.apply();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        reload();
    }

    private void reload() {
//        finish();
//        Intent myIntent = new Intent(ProfilesListActivity.this, ProfilesListActivity.class);
//        startActivity(myIntent);
        android.content.SharedPreferences sharedPreferences;
        if(profileName==null) {
            sharedPreferences = getSharedPreferences("temp" + "_Params", MODE_PRIVATE);
        }
        else {
            sharedPreferences = getSharedPreferences(profileName + "_Params", MODE_PRIVATE);

        }
        float latitude =sharedPreferences.getFloat("latitude",0);
        float longitude =sharedPreferences.getFloat("longitude",0);

        TextView textView=(TextView)findViewById(R.id.TextViewLatitude);
        textView.setText(Float.toString(latitude));
        TextView textView2=(TextView)findViewById(R.id.TextViewLongitude);
        textView2.setText(Float.toString(longitude));
    }

}
