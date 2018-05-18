package com.filip.focushelper2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class Helper {

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean statApp(final Context context, final String packageName) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            context.startActivity(launchIntent);//null pointer check in case package name was not found
            return true;
        }
        return false;
    }


    }
