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

import android.content.Intent;

import com.mocircle.flow.BuildConfig;
import com.mocircle.flow.CircleFlow;
import com.mocircle.flow.listener.FlowSignalListener;
import com.mocircle.flow.model.signal.DeviceEvent;
import com.mocircle.flow.model.signal.LocalEvent;
import com.mocircle.flow.model.signal.Signal;
import com.mocircle.flow.test.CallbackVerify;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLooper;

import java.util.HashMap;
import java.util.List;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class DeviceEventHandlerTest {

    @Before
    public void setup() {
        CircleFlow.getServices().getAndroidService().setup(RuntimeEnvironment.application);
    }

    @Test
    public void testSupport() {
        DeviceEventHandler handler = new DeviceEventHandler();
        Assert.assertFalse(handler.isSupported(Signal.class));
        Assert.assertFalse(handler.isSupported(LocalEvent.class));
        Assert.assertTrue(handler.isSupported(DeviceEvent.class));
    }

    @Test
    public void testSendSignalDirectly() {
        DeviceEventHandler handler = new DeviceEventHandler();
        DeviceEvent event = new DeviceEvent();
        event.setName("device1");
        event.setData("key1", "value1");
        event.setData("key2", 2);
        handler.sendSignal(event);

        List<Intent> intents = ShadowApplication.getInstance().getBroadcastIntents();
        Assert.assertEquals(1, intents.size());
        Intent intent = intents.get(0);
        Assert.assertEquals("com.mocircle.flow.actions.ACTION_SEND_DEVICE_EVENT", intent.getAction());
        Assert.assertEquals("device1", intent.getStringExtra("extra_event_name"));
        HashMap<String, Object> data = (HashMap) intent.getSerializableExtra("extra_event_data");
        Assert.assertEquals("value1", data.get("key1"));
        Assert.assertEquals(2, data.get("key2"));
    }

    @Test
    public void testHandle() {
        // Test send/receive signal
        DeviceEventHandler handler = new DeviceEventHandler();
        SignalQueue queue = new SignalQueueImpl(ShadowLooper.getMainLooper());
        handler.setSignalQueue(queue);
        handler.setup();
        final CallbackVerify callback = new CallbackVerify();
        queue.addListener("device1", new FlowSignalListener() {
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
        handler.sendSignal(event);
        Assert.assertTrue(callback.isCalled());

        // Test noise
        handler.sendSignal(new DeviceEvent("device2"));
        handler.shutdown();
    }

    @Test
    public void testRemoveListener() {
        final CallbackVerify callback = new CallbackVerify();
        FlowSignalListener listener1 = new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
                Assert.fail();
            }
        };
        FlowSignalListener listener2 = new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
                callback.call();
                Assert.assertEquals(DeviceEvent.class, signal.getClass());
            }
        };
        DeviceEventHandler handler = new DeviceEventHandler();
        SignalQueue queue = new SignalQueueImpl(ShadowLooper.getMainLooper());
        handler.setSignalQueue(queue);
        handler.setup();
        queue.addListener("event1", listener1);
        queue.addListener("event1", listener2);
        queue.removeListener("event1", listener1);
        handler.sendSignal(new DeviceEvent("event1"));
        Assert.assertTrue(callback.isCalled());
        handler.shutdown();
    }

}
