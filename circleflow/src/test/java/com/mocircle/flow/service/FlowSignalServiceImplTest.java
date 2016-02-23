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

import com.mocircle.flow.BuildConfig;
import com.mocircle.flow.CircleFlow;
import com.mocircle.flow.handler.signal.SignalHandlerManager;
import com.mocircle.flow.listener.FlowSignalListener;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.signal.DeviceEvent;
import com.mocircle.flow.model.signal.LocalEvent;
import com.mocircle.flow.model.signal.Signal;
import com.mocircle.flow.test.CallbackVerify;
import com.mocircle.flow.test.TestUtils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class FlowSignalServiceImplTest {

    @Before
    public void setup() {
        TestUtils.resetSingletonInstance(CircleFlow.class);
        TestUtils.resetSingletonInstance(SignalHandlerManager.class);
        CircleFlow.getServices().getAndroidService().setup(RuntimeEnvironment.application);
    }

    @Test
    public void testSendLocalEvent() {
        FlowSignalServiceImpl service = new FlowSignalServiceImpl();
        final CallbackVerify callback = new CallbackVerify();
        service.addSignalListener("local", new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
                callback.call();
                Assert.assertEquals(LocalEvent.class, signal.getClass());
                Assert.assertEquals("local", signal.getName());
                Assert.assertEquals(0, signal.getData().size());
            }
        });
        service.sendLocalEvent("local");
        Assert.assertTrue(callback.isCalled());
    }

    @Test
    public void testSendLocalEventWithString() {
        FlowSignalServiceImpl service = new FlowSignalServiceImpl();
        final CallbackVerify callback = new CallbackVerify();
        service.addSignalListener("local", new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
                callback.call();
                Assert.assertEquals(LocalEvent.class, signal.getClass());
                Assert.assertEquals("local", signal.getName());
                Assert.assertEquals(Token.TYPE_SUCCESS, signal.getData().get(Signal.KEY_DATA_RESULT));
            }
        });
        service.sendLocalEvent("local", Token.TYPE_SUCCESS);
        Assert.assertTrue(callback.isCalled());
    }

    @Test
    public void testSendDeviceEvent() {
        FlowSignalServiceImpl service = new FlowSignalServiceImpl();
        final CallbackVerify callback = new CallbackVerify();
        service.addSignalListener("device", new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
                callback.call();
                Assert.assertEquals(DeviceEvent.class, signal.getClass());
                Assert.assertEquals("device", signal.getName());
                Assert.assertEquals(0, signal.getData().size());
            }
        });
        service.sendDeviceEvent("device");
        Assert.assertTrue(callback.isCalled());
    }

    @Test
    public void testSendDeviceEventWithString() {
        FlowSignalServiceImpl service = new FlowSignalServiceImpl();
        final CallbackVerify callback = new CallbackVerify();
        service.addSignalListener("device", new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
                callback.call();
                Assert.assertEquals(DeviceEvent.class, signal.getClass());
                Assert.assertEquals("device", signal.getName());
                Assert.assertEquals(Token.TYPE_FAILURE, signal.getData().get(Signal.KEY_DATA_RESULT));
            }
        });
        service.sendDeviceEvent("device", Token.TYPE_FAILURE);
        Assert.assertTrue(callback.isCalled());
    }

}
