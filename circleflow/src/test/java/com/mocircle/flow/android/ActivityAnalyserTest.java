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

import com.mocircle.flow.BuildConfig;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class ActivityAnalyserTest {

    class Activity1 extends Activity {
    }

    class Activity2 extends Activity {
    }

    class Activity3 extends Activity {
    }

    @Test
    public void test1() {
        Activity1 a1 = new Activity1();

        ActivityAnalyser analyser = new ActivityAnalyser();
        analyser.onActivityCreated(a1, null);
        analyser.onActivityStarted(a1);
        analyser.onActivityResumed(a1);
        Assert.assertEquals(a1, analyser.getCurrentActivity());

        analyser.onActivityPaused(a1);
        analyser.onActivityStopped(a1);
        analyser.onActivityDestroyed(a1);
        Assert.assertEquals(null, analyser.getCurrentActivity());
    }

    @Test
    public void test2() {
        Activity1 a1 = new Activity1();
        Activity2 a2 = new Activity2();
        Activity3 a3 = new Activity3();

        ActivityAnalyser analyser = new ActivityAnalyser();
        analyser.onActivityCreated(a1, null);
        analyser.onActivityStarted(a1);
        analyser.onActivityResumed(a1);

        analyser.onActivityPaused(a1);
        analyser.onActivityResumed(a2);
        Assert.assertEquals(a2, analyser.getCurrentActivity());

        analyser.onActivityPaused(a2);
        analyser.onActivityResumed(a3);
        Assert.assertEquals(a3, analyser.getCurrentActivity());
    }

}
