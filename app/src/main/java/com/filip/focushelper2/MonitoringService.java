package com.filip.focushelper2;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.filip.focushelper2.AppListPackage.AppList;
import com.filip.focushelper2.ProfilePackage.ProfileList;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.logging.Logger;

//IntentService
public class MonitoringService extends IntentService {
    public MonitoringService() {
        super("MonitoringService");
//        super();

    }

//     @Nullable
//     @Override
//     public IBinder onBind(Intent intent) {
//         System.out.println("SERVICE STARTED! ! !");
//         return null;
//     }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        System.out.println("SERVICE STARTED! ! !");
    }

    //
    SharedPreferences sharedPrefsapp;
    Map<String, ?> allEntries;
    //

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final List<String> stalkList = new ArrayList<>();
//        stalkList.add("com.filip.focushelper2");

//        stalkList.add("com.facebook.orca");
//        stalkList.add("com.facebook.katana");
//        stalkList.add("com.instagram.android");

        List<String> blockedAppsList=getAllBlockedApps();
        List<String> blockedAppsPackeageList=new ArrayList<>();
        List<AppList> installedApps =getInstalledApps();
        for(String appName:blockedAppsList) {
            String temp = getPackageName(appName,installedApps);
            blockedAppsPackeageList.add(temp);
        }
        stalkList.addAll(blockedAppsPackeageList);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                final List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
                //
                String AcctiveAppStr = getCurrentRunningApp();
                sharedPrefsapp = getApplicationContext().getSharedPreferences("appdb", Context.MODE_PRIVATE);
                allEntries = null;
                allEntries = sharedPrefsapp.getAll();

                for (String blockedAppStr : stalkList) {
                    if (blockedAppStr.equals(AcctiveAppStr)) {
                        Log.wtf("procInfos", "Usuwanie:  " + AcctiveAppStr);
                        Intent intent = new Intent(getBaseContext(), BlockDisplayActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        activityManager.killBackgroundProcesses(AcctiveAppStr);
                    }
                }


            }
        }, 20000, 16000);  // every 6 seconds
        // 200 i 200 - działa dobrze...ale nie wiem czy procesor i bateria wytrzymaja...

        return START_STICKY;
    }

    public String getCurrentRunningApp() {
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            //noinspection ResourceType
            UsageStatsManager usm = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            // Sort the stats by the last time used
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = (mySortedMap.get(mySortedMap.lastKey())).getPackageName();
                }
            }
        } else
        {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        Log.wtf("adapter", "Current App in foreground is: " + currentApp);
        return currentApp;

    }

    private List<String> getAllBlockedApps() {
        File prefsdir = new File(getApplicationInfo().dataDir, "shared_prefs");
        Set<String> res = new HashSet<>();
        if (prefsdir.exists() && prefsdir.isDirectory()) {
            String[] list = prefsdir.list();
            for (String profileName : list) {
                profileName = profileName.substring(0, (profileName.lastIndexOf(".")));

                SharedPreferences sharedPreferences = getSharedPreferences(profileName, MODE_PRIVATE);
                Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();
//                String Appsnames = "";
                for (Map.Entry<String, ?> entry : sharedPreferencesAll.entrySet()) {

//                    Appsnames += entry.getKey() + ", ";
                    Log.wtf("blocked_apps", entry.getKey() + " " + entry.getValue());
                    res.add(entry.getKey());


                }
//                res.add(new ProfileList(profileName, Appsnames));

            }

//            for (String name : list) {
//                Log.wtf("sheredfiles", name);
//            }

        }
        return new ArrayList<>(res);
    }

    private String getPackageName(String packageName) {
        List<AppList> installedApps = getInstalledApps();
        for (AppList appList : installedApps) {
            if (appList.getPackageName().equals(packageName)) {
                return appList.getName();
            }
        }
        return null;
    }
    private String getPackageName(String appeName,List<AppList> installedApps) {
        for (AppList appList : installedApps) {
            if (appList.getName().equals(appeName)) {
                return appList.getPackageName();
            }
        }
        return null;
    }

    private List<AppList> getInstalledApps() {
        List<AppList> res = new LinkedList<>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((isSystemPackage(p) == false)) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                String packageName = p.applicationInfo.packageName;
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                res.add(new AppList(appName, icon, packageName));
            }
        }
        return res;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }




/////////////////////////////
    private ActivityManager.RunningAppProcessInfo getForegroundApp() {
        ActivityManager.RunningAppProcessInfo result = null, info = null;

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while (i.hasNext()) {
            info = i.next();
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && !isRunningService(info.processName)) {
                result = info;
                break;
            }
        }
        return result;
    }

    private boolean isRunningService(String processName) {
        if(processName == null)
            return false;

        ActivityManager.RunningServiceInfo service;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningServiceInfo> l = activityManager.getRunningServices(9999);
        Iterator <ActivityManager.RunningServiceInfo> i = l.iterator();
        while(i.hasNext()){
            service = i.next();
            if(service.process.equals(processName))
                return true;
        }
        return false;
    }

    private boolean isRunningApp(String processName) {
        if(processName == null)
            return false;

        ActivityManager.RunningAppProcessInfo app;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
        Iterator <ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while(i.hasNext()){
            app = i.next();
            if(app.processName.equals(processName) && app.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE)
                return true;
        }
        return false;
    }


    private boolean checkifThisIsActive(ActivityManager.RunningAppProcessInfo target){
        boolean result = false;
        ActivityManager.RunningTaskInfo info;

        if(target == null)
            return false;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> l = activityManager.getRunningTasks(9999);
        Iterator<ActivityManager.RunningTaskInfo> i = l.iterator();

        while(i.hasNext()){
            info=i.next();
            if(info.baseActivity.getPackageName().equals(target.processName)) {
                result = true;
                break;
            }
        }

        return result;
    }


    // what is in b that is not in a ?
    public static Collection subtractSets(Collection a, Collection b)
    {
        Collection result = new ArrayList(b);
        result.removeAll(a);
        return result;
    }
}
