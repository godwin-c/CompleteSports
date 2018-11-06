package com.completesportsnigeria.completesports.classes;

import android.content.Context;
import android.content.Intent;

import com.completesportsnigeria.completesports.HomeActivity;

public class RestartApp {

    public static void restartThroughIntentCompatMakeRestartActivityTask(Context context) {
        Context currentActivity = context;
        Intent intent = new Intent(currentActivity, HomeActivity.class);
        Intent restartIntent = Intent.makeRestartActivityTask(intent.getComponent());
        currentActivity.startActivity(restartIntent);
        System.exit(0);
    }

}
