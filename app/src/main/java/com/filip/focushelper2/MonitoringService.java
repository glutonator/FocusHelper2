package com.filip.focushelper2;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
        stalkList.add("com.facebook.orca");
        stalkList.add("com.facebook.katana");
        stalkList.add("com.instagram.android");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                final List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);
                //
                String AcctiveAppStr = func();
                sharedPrefsapp = getApplicationContext().getSharedPreferences("appdb", Context.MODE_PRIVATE);
                allEntries = null;
                allEntries = sharedPrefsapp.getAll();

                for (String blockedAppStr : stalkList) {
                    if (blockedAppStr.equals(AcctiveAppStr)) {
                        Log.wtf("procInfos", "Usuwanie:  " + AcctiveAppStr);
                        Intent intent = new Intent(getBaseContext(), BlockDisplayActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        activityManager.killBackgroundProcesses("AcctiveAppStr");
                    }
                }

                //


//                for (int i = 0; i < services.size(); i++) {
////                    Log.wtf("servisy", services.toString());
//                    if (!stalkList.contains(services.get(i).baseActivity.getPackageName())) {
//                        // you may broad cast a new application launch here.
//                        stalkList.add(services.get(i).baseActivity.getPackageName());
//                    }
//                }
//
//                List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
//
//                for (int i = 0; i < procInfos.size(); i++) {
//
//                    ArrayList<String> runningPkgs = new ArrayList<String>(Arrays.asList(procInfos.get(i).pkgList));
////                    Log.wtf("procInfos", runningPkgs.toString());
//                    Collection diff = subtractSets(runningPkgs, stalkList);
//
//                    if (diff != null) {
//                        stalkList.removeAll(diff);
//                    }
//                }


            }
        }, 20000, 16000);  // every 6 seconds


        return START_STICKY;
    }

public String func() {
    String currentApp = "NULL";
            if(android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.LOLLIPOP)

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

            Log.wtf("adapter","Current App in foreground is: "+currentApp);
    return currentApp;

}

    private ActivityManager.RunningAppProcessInfo getForegroundApp() {
        ActivityManager.RunningAppProcessInfo result = null, info = null;

        final ActivityManager activityManager  =  (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
        Iterator <ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while(i.hasNext()) {
            info = i.next();
            if(info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
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
