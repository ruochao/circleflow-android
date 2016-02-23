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

package com.mocircle.flow.handler.signal;

import com.mocircle.flow.BuildConfig;
import com.mocircle.flow.CircleFlow;
import com.mocircle.flow.exception.FlowExecutionException;
import com.mocircle.flow.listener.FlowSignalListener;
import com.mocircle.flow.model.signal.DeviceEvent;
import com.mocircle.flow.model.signal.LocalEvent;
import com.mocircle.flow.model.signal.Signal;
import com.mocircle.flow.test.CallbackVerify;
import com.mocircle.flow.test.TestUtils;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class SignalHandlerManagerTest {

    class UnknownSignal extends Signal {
    }

    @Before
    public void setup() throws Exception {
        CircleFlow.getServices().getAndroidService().setup(RuntimeEnvironment.application);

        // Reset instance
        TestUtils.resetSingletonInstance(SignalHandlerManager.class);
    }

    @After
    public void tearDown() {
        SignalHandlerManager.getInstance().clearListeners();
    }

    @Test
    public void testNoHandlerCase() {
        SignalHandlerManager manager = SignalHandlerManager.getInstance();
        try {
            manager.sendSignal(new UnknownSignal());
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(FlowExecutionException.class, e.getClass());
        }
    }

    @Test
    public void testHandlerSetupAndShutdown() {
        SignalHandlerManager manager = SignalHandlerManager.getInstance();
        FlowSignalListener listener1 = new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
            }
        };
        FlowSignalListener listener2 = new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
            }
        };
        manager.addSignalListener("test1", listener1);
        manager.addSignalListener("test2", listener2);
        manager.removeSignalListener("test1", listener1);
        manager.removeSignalListener("test2", listener2);
    }

    @Test
    public void testHandleLocalEvent() {
        // Test send/receive signal
        SignalHandlerManager manager = SignalHandlerManager.getInstance();
        final CallbackVerify callback = new CallbackVerify();
        manager.addSignalListener("event1", new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
                callback.call();
                Assert.assertEquals(LocalEvent.class, signal.getClass());
                Assert.assertEquals("event1", signal.getName());
                Assert.assertEquals("value1", signal.getData("key1"));
                Assert.assertEquals(2, signal.getData("key2"));
            }
        });
        LocalEvent event = new LocalEvent();
        event.setName("event1");
        event.setData("key1", "value1");
        event.setData("key2", 2);
        manager.sendSignal(event);
        Assert.assertTrue(callback.isCalled());

        // Test noise
        manager.sendSignal(new LocalEvent("event2"));
        manager.sendSignal(new DeviceEvent("event2"));
    }

    @Test
    public void testHandleDeviceEvent() {
        // Test send/receive signal
        SignalHandlerManager manager = SignalHandlerManager.getInstance();
        manager.clearListeners();
        final CallbackVerify callback = new CallbackVerify();
        manager.addSignalListener("device1", new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
                callback.call();
                Assert.assertEquals(DeviceEvent.class, signal.getClass());
                Assert.assertEquals("device1", signal.getName());
                Assert.assertEquals("value1", signal.getData("key1"));
                Assert.assertEquals(2, signal.getData("key2"));
            }
        });
        DeviceEvent event = new DeviceEvent();
        event.setName("device1");
        event.setData("key1", "value1");
        event.setData("key2", 2);
        manager.sendSignal(event);
        Assert.assertTrue(callback.isCalled());

        // Test noise
        manager.sendSignal(new LocalEvent("device2"));
        manager.sendSignal(new DeviceEvent("device2"));
    }

}
