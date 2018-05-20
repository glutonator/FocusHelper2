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
                Intent myIntent = new Intent(ProfilesListActivity.this, ProfileSettingsActivity.class);

                ProfileList profileList = (ProfileList) profileAdapter.getItem(position);

                myIntent.putExtra("profileName", profileList.getProfileName());
//                startActivity(myIntent);
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

                SharedPreferences sharedPreferences = getSharedPreferences(profileName, MODE_PRIVATE);
                Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();
                String Appsnames = "";
                for (Map.Entry<String, ?> entry : sharedPreferencesAll.entrySet()) {
                    Appsnames += entry.getKey() + ", ";
                    Log.wtf("sheredfiles_apps", entry.getKey() + " " + entry.getValue());

                }
                res.add(new ProfileList(profileName, Appsnames));

            }

            for (String name : list) {
                Log.wtf("sheredfiles", name);
            }

        }
        return res;
    }

    public void NewProfile(View view) {
        Intent myIntent = new Intent(ProfilesListActivity.this, ProfileSettingsActivity.class);

//        myIntent.putExtra("profileName",profileList.getProfileName());
//        startActivity(myIntent);
        startActivityForResult(myIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        reload();
    }

    private void reload() {
        finish();
        Intent myIntent = new Intent(ProfilesListActivity.this, ProfilesListActivity.class);
        startActivity(myIntent);
    }

}
