package com.filip.focushelper2;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.filip.focushelper2.AppListPackage.AppsListActivity;
import com.filip.focushelper2.ProfilePackage.ProfilesListActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toastMe(View view) {
        Toast myToast = Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT);
            myToast.show();
    }

    public void lunchService (View view) {
        Intent intent = new Intent(this, MonitoringService.class);
        startService(intent);
        }

        public void OnButtonProfiles(View view) {
            startActivity(new Intent(MainActivity.this,ProfilesListActivity.class));
        }


}
