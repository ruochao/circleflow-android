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

package com.mocircle.flow.service;

import android.os.Handler;

import com.mocircle.flow.BuildConfig;
import com.mocircle.flow.test.CallbackVerify;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;
import org.robolectric.util.concurrent.RoboExecutorService;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class FlowSchedulingServiceImplTest {

    @Test
    public void testAsyncJob() {
        FlowSchedulingServiceImpl service = new FlowSchedulingServiceImpl();
        service.setExecutorService(new RoboExecutorService());
        final CallbackVerify callback = new CallbackVerify();
        service.executeAsyncJob(new Runnable() {
            @Override
            public void run() {
                callback.call();
            }
        });
        Assert.assertTrue(callback.isCalled());
    }

    @Test
    public void testBackgroundJob() {
        FlowSchedulingServiceImpl service = new FlowSchedulingServiceImpl();
        service.setExecutorService(new RoboExecutorService());
        final CallbackVerify callback = new CallbackVerify();
        service.executeBackgroundJob(new Runnable() {
            @Override
            public void run() {
                callback.call();
            }
        });
        Assert.assertTrue(callback.isCalled());
    }

    @Test
    public void testUiJob() {
        FlowSchedulingServiceImpl service = new FlowSchedulingServiceImpl();
        service.setUIHandler(new Handler(ShadowLooper.getMainLooper()));
        final CallbackVerify callback = new CallbackVerify();
        service.executeUIJob(new Runnable() {
            @Override
            public void run() {
                callback.call();
            }
        });
        Assert.assertTrue(callback.isCalled());
    }
}
