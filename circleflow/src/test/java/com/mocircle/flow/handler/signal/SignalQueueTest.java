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

import android.os.Looper;

import com.mocircle.flow.BuildConfig;
import com.mocircle.flow.listener.FlowSignalListener;
import com.mocircle.flow.model.signal.DeviceEvent;
import com.mocircle.flow.model.signal.LocalEvent;
import com.mocircle.flow.model.signal.Signal;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class SignalQueueTest {

    @Test
    public void testQueueSignalOnMainLooper() {
        Looper looper = ShadowLooper.getMainLooper();
        SignalQueue queue = new SignalQueueImpl(looper);
        Assert.assertEquals(0, queue.getCurrentQueueSize());

        ShadowLooper.pauseMainLooper();
        queue.queueSignal(new LocalEvent());
        queue.queueSignal(new LocalEvent());
        queue.queueSignal(new DeviceEvent());
        Assert.assertEquals(3, queue.getCurrentQueueSize());

        ShadowLooper.unPauseMainLooper();
        Assert.assertEquals(0, queue.getCurrentQueueSize());
    }

    @Test
    public void testQueueSignalOnMyLooper() {
        Looper looper = ShadowLooper.myLooper();
        SignalQueue queue = new SignalQueueImpl(looper);
        Assert.assertEquals(0, queue.getCurrentQueueSize());

        ShadowLooper.pauseLooper(looper);
        queue.queueSignal(new DeviceEvent());
        queue.queueSignal(new DeviceEvent());
        queue.queueSignal(new LocalEvent());
        Assert.assertEquals(3, queue.getCurrentQueueSize());

        ShadowLooper.unPauseLooper(looper);
        Assert.assertEquals(0, queue.getCurrentQueueSize());
    }

    @Test
    public void testQueueListener() {
        Looper looper = ShadowLooper.getMainLooper();
        SignalQueue queue = new SignalQueueImpl(looper);
        Assert.assertEquals(0, queue.getCurrentListenerCount());

        queue.addListener("signal1", new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
                Assert.assertEquals(Looper.getMainLooper(), Looper.myLooper());
                Assert.assertEquals("signal1", signal.getName());
                Assert.assertEquals("value1", signal.getData("key1"));
                Assert.assertEquals(2, signal.getData("key2"));
            }
        });
        queue.addListener("signal1", new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
                Assert.assertEquals(Looper.getMainLooper(), Looper.myLooper());
                Assert.assertEquals("signal1", signal.getName());
                Assert.assertEquals("value1", signal.getData("key1"));
                Assert.assertEquals(2, signal.getData("key2"));
            }
        });
        FlowSignalListener signal2Listener = new FlowSignalListener() {
            @Override
            public void onReceivedSignal(Signal signal) {
                Assert.fail();
            }
        };
        queue.addListener("signal2", signal2Listener);
        Assert.assertEquals(3, queue.getCurrentListenerCount());

        Signal signal = new LocalEvent();
        signal.setName("signal1");
        signal.setData("key1", "value1");
        signal.setData("key2", 2);
        queue.queueSignal(signal);

        queue.removeListener("signal2", signal2Listener);
        Assert.assertEquals(2, queue.getCurrentListenerCount());
        signal.setName("signal2");
        queue.queueSignal(signal);
    }

}
