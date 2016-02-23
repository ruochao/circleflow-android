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
import com.mocircle.flow.listener.FlowSignalListener;
import com.mocircle.flow.model.signal.DeviceEvent;
import com.mocircle.flow.model.signal.LocalEvent;
import com.mocircle.flow.model.signal.Signal;
import com.mocircle.flow.test.CallbackVerify;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class LocalEventHandlerTest {

    @Test
    public void testSupport() {
        LocalEventHandler handler = new LocalEventHandler();
        Assert.assertFalse(handler.isSupported(Signal.class));
        Assert.assertTrue(handler.isSupported(LocalEvent.class));
        Assert.assertFalse(handler.isSupported(DeviceEvent.class));
    }

    @Test
    public void testHandle() {
        LocalEventHandler handler = new LocalEventHandler();
        SignalQueue queue = new SignalQueueImpl(ShadowLooper.getMainLooper());
        handler.setSignalQueue(queue);
        handler.setup();
        final CallbackVerify callback = new CallbackVerify();
        queue.addListener("event1", new FlowSignalListener() {
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
        handler.sendSignal(event);
        Assert.assertTrue(callback.isCalled());

        // Test noise
        handler.sendSignal(new LocalEvent("event2"));
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
                Assert.assertEquals(LocalEvent.class, signal.getClass());
            }
        };
        LocalEventHandler handler = new LocalEventHandler();
        SignalQueue queue = new SignalQueueImpl(ShadowLooper.getMainLooper());
        handler.setSignalQueue(queue);
        handler.setup();
        queue.addListener("event1", listener1);
        queue.addListener("event1", listener2);
        queue.removeListener("event1", listener1);
        handler.sendSignal(new LocalEvent("event1"));
        Assert.assertTrue(callback.isCalled());
        handler.shutdown();
    }

}
