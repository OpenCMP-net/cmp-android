package com.opencmp.inapplib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class OpenCmpLifecycleListener implements Application.ActivityLifecycleCallbacks {

    final private OpenCmp openCmp;

    OpenCmpLifecycleListener(OpenCmp openCmp) {
        this.openCmp = openCmp;
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) { }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        openCmp.setCurrentActivity(activity);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        try {
            openCmp.setWindowHasFocus(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) { }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        openCmp.disableActivity(activity);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) { }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) { }
}
