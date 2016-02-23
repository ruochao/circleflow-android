/*
 * Copyright (C) 2016 mocircle.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mocircle.flow.android;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.WeakReference;

/**
 * Calculates activity status according to activity lifecycle.
 */
public class ActivityAnalyser implements Application.ActivityLifecycleCallbacks {

    private WeakReference<Activity> currentActivity;

    /**
     * Gets the current shown activity, this only applies to the caller app.
     *
     * @return current activity, or null if no activity shown.
     */
    public Activity getCurrentActivity() {
        if (currentActivity != null) {
            return currentActivity.get();
        }
        return null;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (currentActivity != null) {
            currentActivity.clear();
        }
        currentActivity = new WeakReference<>(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        clearActivityRecord(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        clearActivityRecord(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        clearActivityRecord(activity);
    }

    private void clearActivityRecord(Activity activity) {
        if (currentActivity != null && currentActivity.get() != null) {
            if (currentActivity.get().equals(activity)) {
                currentActivity.clear();
            }
        }
    }

}
