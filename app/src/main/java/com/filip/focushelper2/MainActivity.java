package com.filip.focushelper2;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toastMe(View view) {
        Toast myToast = Toast.makeText(this, "Hello!", Toast.LENGTH_SHORT);
            myToast.show();
        // val myToast = Toast.makeText(this, message, duration);
//        Helper.statApp(MainActivity.this,"com.facebook.orca");
//        if (Helper.isAppRunning(MainActivity.this, "com.facebook.orca")) {
//            // App is running
//            Toast myToast = Toast.makeText(this, "App runing!", Toast.LENGTH_SHORT);
//            myToast.show();
//        } else {
//            // App is not running
//            Toast myToast = Toast.makeText(this, "App not running!", Toast.LENGTH_SHORT);
//            myToast.show();
//        }


    }

    public void countMe (View view) {
        // Get the text view
        TextView showCountTextView = findViewById(R.id.textView);

        // Get the value of the text view.
        String countString = showCountTextView.getText().toString();

        // Convert value to a number and increment it
        int count = Integer.parseInt(countString);
        count++;

        // Display the new value in the text view.
        showCountTextView.setText(Integer.toString(count));
    }

    public void onClickButton(View view) {
        startActivity(new Intent(MainActivity.this,AppsListActivity.class));
    }

    public void ListOfActiveApps (View view) {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();

        for (int i = 0; i < runningAppProcessInfo.size(); i++) {
//            isForeground(getApplicationContext(),"com.facebook.orca");
            if(runningAppProcessInfo.get(i).processName.equals("com.facebook.orca")) {
                Toast myToast = Toast.makeText(this, "App messenger!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        }
    }

    public static boolean isForeground(Context ctx, String myPackage){
        ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
//        List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks(4);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = manager.getRunningAppProcesses();
        ComponentName componentInfo = runningAppProcessInfos.get(0).importanceReasonComponent;

//        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        if(componentInfo.getPackageName().equals(myPackage)) {
            return true;
        }
        return false;
    }

    public static Activity getActivity() throws ClassNotFoundException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);

        Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
        if (activities == null)
            return null;

        for (Object activityRecord : activities.values()) {
            Class activityRecordClass = activityRecord.getClass();
            Field pausedField = activityRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true);
            if (!pausedField.getBoolean(activityRecord)) {
                Field activityField = activityRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                Activity activity = (Activity) activityField.get(activityRecord);
                return activity;
            }
        }

        return null;
    }

    public void lunchService (View view) {
        Intent intent = new Intent(this, MonitoringService.class);
        startService(intent);

//            String currentApp = "NULL";
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                //noinspection ResourceType
//                UsageStatsManager usm = (UsageStatsManager)getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
//                long time = System.currentTimeMillis();
//                List <UsageStats>appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
//                if (appList != null && appList.size() > 0) {
//                    SortedMap<Long,UsageStats> mySortedMap = new TreeMap();
//                    for (UsageStats usageStats : appList) {
//                        mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
//                    }
//                    if (mySortedMap != null && !mySortedMap.isEmpty()) {
//                        currentApp = (mySortedMap.get(mySortedMap.lastKey())).getPackageName();
//                    }
//                }
//            } else {
//                ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
//                List <ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
//                currentApp = tasks.get(0).processName;
//            }
//
//            Log.wtf("adapter", "Current App in foreground is: " + currentApp);
//            //return currentApp;
        }


}
