package com.filip.focushelper2;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AppsListActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_apps_list);
//    }

    List<AppList> selectedIteams = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);

        final ListView userInstalledApps = (ListView)findViewById(R.id.installed_app_list);
        //
        userInstalledApps.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //
        final List<AppList> installedApps = getInstalledApps();
        Collections.sort(installedApps);
        //
        //selectedIteams=installedApps;
        //
        AppAdapter installedAppAdapter = new AppAdapter(AppsListActivity.this, installedApps);
        userInstalledApps.setAdapter(installedAppAdapter);
        userInstalledApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(AppsListActivity.this, position, Toast.LENGTH_SHORT);
                AppList selectedIteam=  installedApps.get(position); //.getItemAtPosition(position);
                //AppList selectedIteam2 = (AppList) findViewById(R.id.textView);
                Log.wtf("AppsListActivity",selectedIteam.toString());
                Log.wtf("AppsListActivity",""+position+" "+id);
                System.out.print(position+" "+id);
                //AppList selectedIteam = view.findViewById(position);
                if(selectedIteams.contains(selectedIteam)){
                    selectedIteams.remove(selectedIteam);
                    userInstalledApps.setItemChecked(position,false);
                    Log.wtf(""+position,""+userInstalledApps.isItemChecked(position));
//                    userInstalledApps.refreshDrawableState();
                    //todo: uncheck iteam - może się nie restertuje widok...nie wiem...
                }
                else {
                    selectedIteams.add(selectedIteam);
                    userInstalledApps.setItemChecked(position,true);
                    Log.wtf(""+position,""+userInstalledApps.isItemChecked(position));
                    view.invalidate();
//                    userInstalledApps.refreshDrawableState();
                    //todo: check iteam - może się nie restertuje widok...nie wiem...
                }
            }
        });
    }

    public void showSelectedIteams() {
        String iteams="";
        for(AppList iteam:selectedIteams) {
            iteams+="-"+iteam.toString()+"\n";
        }
        Toast.makeText(this,iteams,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        item.setCheckable(true);
//        System.out.print("sdasa");
        showSelectedIteams();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<AppList> getInstalledApps() {
        List<AppList> res = new LinkedList<>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                res.add(new AppList(appName, icon));
            }
        }
        return res;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }
}
