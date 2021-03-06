package com.filip.focushelper2.ProfilePackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.filip.focushelper2.AppListPackage.AppList;
import com.filip.focushelper2.AppListPackage.AppsListActivity;
import com.filip.focushelper2.ChooseProfileModeActivity;
import com.filip.focushelper2.MainActivity;
import com.filip.focushelper2.R;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProfilesListActivity extends AppCompatActivity {

    ProfileAdapter profileAdapter;
    ListView userSetProfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles_list);

        final List<ProfileList> settedProfiles = getSheredFilesName();

        userSetProfiles = (ListView) findViewById(R.id.profiles_list);
        profileAdapter = new ProfileAdapter(ProfilesListActivity.this, settedProfiles);
        userSetProfiles.setAdapter(profileAdapter);
        Log.wtf("SDadsa", "dsada");

        userSetProfiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProfileList profileList = (ProfileList) profileAdapter.getItem(position);
                Intent myIntent;
                if (profileList.isType() == false) {
                    myIntent = new Intent(ProfilesListActivity.this, ProfileSettingsActivity.class);
                } else {
                    myIntent = new Intent(ProfilesListActivity.this, ProfileLocationActivity.class);
                }
                myIntent.putExtra("profileName", profileList.getProfileName());
                startActivityForResult(myIntent, 0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private List<ProfileList> getSheredFilesName() {
        File prefsdir = new File(getApplicationInfo().dataDir, "shared_prefs");
        List<ProfileList> res = new LinkedList<>();
        if (prefsdir.exists() && prefsdir.isDirectory()) {
            String[] list = prefsdir.list();
            for (String profileName : list) {
                profileName = profileName.substring(0, (profileName.lastIndexOf(".")));
                if (profileName.contains("MapviewInitializer") || profileName.contains("com.google.maps.api")) {
                    continue;
                }
                //new
                if (profileName.contains("_Params")) {
                    continue;
                }
                //
                SharedPreferences sharedPreferences = getSharedPreferences(profileName, MODE_PRIVATE);
                Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();
                String Appsnames = "";
                for (Map.Entry<String, ?> entry : sharedPreferencesAll.entrySet()) {
                    Appsnames += entry.getKey() + ", ";
                    Log.wtf("sheredfiles_apps", entry.getKey() + " " + entry.getValue());

                }
                SharedPreferences sharedPreferencesParams = getSharedPreferences(profileName + "_Params", MODE_PRIVATE);
                boolean type = sharedPreferencesParams.getBoolean("type", false);
                res.add(new ProfileList(profileName, Appsnames, type));
            }

            for (String name : list) {
                Log.wtf("sheredfiles", name);
            }

        }
        return res;
    }

    public void NewProfile(View view) {
        Intent myIntent = new Intent(ProfilesListActivity.this, ChooseProfileModeActivity.class);
        startActivityForResult(myIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        reload();
    }

    private void reload() {
        finish();
        Intent myIntent = new Intent(ProfilesListActivity.this, ProfilesListActivity.class);
        startActivity(myIntent);
    }

}
