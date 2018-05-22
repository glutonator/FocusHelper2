package com.filip.focushelper2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.filip.focushelper2.ProfilePackage.ProfileLocationActivity;
import com.filip.focushelper2.ProfilePackage.ProfileSettingsActivity;
import com.filip.focushelper2.ProfilePackage.ProfilesListActivity;

public class ChooseProfileModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_profile_mode);
    }

    public void onClickTimerButton(View view) {
        finish();
        startActivity(new Intent(ChooseProfileModeActivity.this, ProfileSettingsActivity.class));

    }

    public void onClickLocationButton(View view) {
        finish();
        startActivity(new Intent(ChooseProfileModeActivity.this, ProfileLocationActivity.class));
    }
}
