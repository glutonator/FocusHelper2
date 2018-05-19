package com.filip.focushelper2;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class ProfilesListActivity extends AppCompatActivity {

    ProfileAdapter profileAdapter;
    ListView userSetProfiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles_list);

        final List<ProfileList> settedProfiles = getSettedProfiles();

        userSetProfiles = (ListView)findViewById(R.id.profiles_list);
        profileAdapter= new ProfileAdapter(ProfilesListActivity.this, settedProfiles);
        userSetProfiles.setAdapter(profileAdapter);
        Log.wtf("SDadsa","dsada");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private List<ProfileList> getSettedProfiles() {
        List<ProfileList> res = new LinkedList<>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
//                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
//                String packageName = p.applicationInfo.packageName;
//                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                res.add(new ProfileList("sdsdads","dfdffd"));
//                res.add(new AppList(appName, icon,packageName));
            }
        }
        return res;
    }
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }
}
