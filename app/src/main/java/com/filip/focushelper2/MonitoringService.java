package com.filip.focushelper2;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.filip.focushelper2.AppListPackage.AppList;

import java.io.File;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import static java.lang.StrictMath.abs;

import android.os.Handler;

//IntentService
public class MonitoringService extends IntentService implements SensorEventListener {
    public MonitoringService() {
        super("MonitoringService");
//        super();
    }

    //accelometr
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter

        if (mAccel > 11) {
            showNotification();
        }
    }

    /**
     * show notification when Accel is more then the given int.
     */
    private void showNotification() {
        final NotificationManager mgr = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder note = new NotificationCompat.Builder(this);
        note.setContentTitle("Go back to work!!!!!");
        note.setTicker("New Message Alert!");
        note.setAutoCancel(true);
        // to set default sound/light/vibrate or all
        note.setDefaults(Notification.DEFAULT_ALL);
        // Icon to be set on Notification
        note.setSmallIcon(R.mipmap.ic_launcher);
        // This pending intent will open after notification click
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
                MainActivity.class), 0);
        // set pending intent to notification builder
        note.setContentIntent(pi);
        mgr.notify(101, note.build());
    }


    //location service start

    private static final String TAG = "TESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10;

    private Location myLocation;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.wtf(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
            myLocation = mLastLocation;
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.wtf(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            myLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.wtf(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.wtf(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.wtf(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    //location service end

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        System.out.println("SERVICE STARTED! ! !");
    }


    List<String> blockedAppsList;
    List<String> blockedAppsPackeageList;
    List<AppList> installedApps;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //accelenemetr
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_UI, new Handler());

        //standard
        Toast.makeText(this, "onStartCommand!", Toast.LENGTH_SHORT).show();
        final List<String> stalkList = new ArrayList<>();

        blockedAppsList = getAllBlockedApps();
        blockedAppsPackeageList = new ArrayList<>();
        installedApps = getInstalledApps();
        for (String appName : blockedAppsList) {
            String temp = getPackageName(appName, installedApps);
            blockedAppsPackeageList.add(temp);
        }
        stalkList.addAll(blockedAppsPackeageList);

        //adding new clocked apps
        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                blockedAppsList = getAllBlockedApps();
                blockedAppsPackeageList = new ArrayList<>();
                installedApps = getInstalledApps();
                for (String appName : blockedAppsList) {
                    String temp = getPackageName(appName, installedApps);
                    blockedAppsPackeageList.add(temp);
                }
                stalkList.clear();
                stalkList.addAll(blockedAppsPackeageList);

            }
        }, 2000, 2000);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                String AcctiveAppStr = getCurrentRunningApp();

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
        }, 1000, 1000);

        // 200 i 200 - działa dobrze...ale nie wiem czy procesor i bateria wytrzymaja...

        return START_STICKY;
    }

    public String getCurrentRunningApp() {
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
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
        } else {
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
                //jesli profileName jest params to:
                if (profileName.contains("_Params")) {
                    if (isProfileCurrentlyActive(profileName) == true) {
                        //blocking apps
                        String profileNameApps = profileName.substring(0, (profileName.lastIndexOf("_")));
                        SharedPreferences sharedPreferences = getSharedPreferences(profileNameApps, MODE_PRIVATE);
                        Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();
                        for (Map.Entry<String, ?> entry : sharedPreferencesAll.entrySet()) {
                            Log.wtf("blocked_apps", entry.getKey() + " " + entry.getValue());
                            res.add(entry.getKey());
                        }
                    }
                }
            }
        }
        return new ArrayList<>(res);
    }

    private boolean isProfileCurrentlyActive(String profileName) {
        SharedPreferences sharedPreferences = getSharedPreferences(profileName, MODE_PRIVATE);

        boolean type = sharedPreferences.getBoolean("type", false);
        if (type == false) {


            if (isCurrentDayOftheWeek(sharedPreferences) == false) {
                return false;
            }
            int hoursStart = sharedPreferences.getInt("hoursStart", -1);
            int minutesStart = sharedPreferences.getInt("minutesStart", -1);
            int hoursStop = sharedPreferences.getInt("hoursStop", -1);
            int minutesStop = sharedPreferences.getInt("minutesStop", -1);
            //no settings
            if (hoursStart == -1 || minutesStart == -1 || hoursStop == -1 || minutesStop == -1) {
                return false;
            }
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            //not in time window
            if (!((hoursStart < hour || (hoursStart == hour && minutesStart <= minute))
                    && (hoursStop > hour || (hoursStop == hour && minutesStop >= minute)))) {
                return false;
            }
            return true;
        }
        //location
        else {
            float latitude = sharedPreferences.getFloat("latitude", 0);
            float longitude = sharedPreferences.getFloat("longitude", 0);
            float myLocationLatitude = (float) myLocation.getLatitude();
            float myLocationLongitude = (float) myLocation.getLongitude();
            if (abs(latitude - myLocationLatitude) < 0.00500 && abs(longitude - myLocationLongitude) < 0.00500) {
                //is in the range
                return true;
            } else {
                return false;
            }
        }

    }

    private boolean isCurrentDayOftheWeek(SharedPreferences sharedPreferences) {
        Calendar mcurrentTime = Calendar.getInstance();
        int dayOfWeek = mcurrentTime.get(Calendar.DAY_OF_WEEK);
        String weekday = new DateFormatSymbols(Locale.ENGLISH).getWeekdays()[dayOfWeek];

        String dayyy;
        dayyy = sharedPreferences.getString("Monday", "");
        if (dayyy.equals(weekday)) {
            return true;
        }
        dayyy = sharedPreferences.getString("Tuesday", "");
        if (dayyy.equals(weekday)) {
            return true;
        }
        dayyy = sharedPreferences.getString("Wednesday", "");
        if (dayyy.equals(weekday)) {
            return true;
        }
        dayyy = sharedPreferences.getString("Thursday", "");
        if (dayyy.equals(weekday)) {
            return true;
        }
        dayyy = sharedPreferences.getString("Friday", "");
        if (dayyy.equals(weekday)) {
            return true;
        }
        dayyy = sharedPreferences.getString("Saturday", "");
        if (dayyy.equals(weekday)) {
            return true;
        }
        dayyy = sharedPreferences.getString("Sunday", "");
        if (dayyy.equals(weekday)) {
            return true;
        }
        return false;
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

    private String getPackageName(String appeName, List<AppList> installedApps) {
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

}
