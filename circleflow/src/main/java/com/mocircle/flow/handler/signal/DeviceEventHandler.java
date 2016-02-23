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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mocircle.android.logging.CircleLog;
import com.mocircle.flow.CircleFlow;
import com.mocircle.flow.model.signal.DeviceEvent;
import com.mocircle.flow.model.signal.Signal;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler for {@link DeviceEvent}.
 */
public class DeviceEventHandler implements SignalHandler {

    private static final String TAG = "DeviceEventHandler";
    private static final String ACTION_SEND_DEVICE_EVENT = "com.mocircle.flow.actions.ACTION_SEND_DEVICE_EVENT";
    private static final String EXTRA_EVENT_NAME = "extra_event_name";
    private static final String EXTRA_EVENT_DATA = "extra_event_data";

    private SignalQueue queue;
    private BroadcastReceiver receiver;

    public DeviceEventHandler() {
    }

    @Override
    public boolean isSupported(Class<? extends Signal> signalClass) {
        return signalClass.equals(DeviceEvent.class);
    }

    @Override
    public void setSignalQueue(SignalQueue queue) {
        this.queue = queue;
    }

    @Override
    public void setup() {
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String name = intent.getStringExtra(EXTRA_EVENT_NAME);
                    Map<String, Object> data = (Map) intent.getSerializableExtra(EXTRA_EVENT_DATA);

                    DeviceEvent event = new DeviceEvent();
                    event.setName(name);
                    event.setData(data);
                    queue.queueSignal(event);
                }
            };
            if (getContext() != null) {
                getContext().registerReceiver(receiver, new IntentFilter(ACTION_SEND_DEVICE_EVENT));
                CircleLog.d(TAG, "Device event handler broadcast receiver registered.");
            }
        }
    }

    @Override
    public void shutdown() {
        if (receiver != null) {
            if (getContext() != null) {
                getContext().unregisterReceiver(receiver);
                CircleLog.d(TAG, "Unregistered for device event handler broadcast receiver");
            }
            receiver = null;
        }
    }

    @Override
    public void sendSignal(Signal signal) {
        if (isSupported(signal.getClass())) {
            DeviceEvent event = (DeviceEvent) signal;
            Intent intent = new Intent(ACTION_SEND_DEVICE_EVENT);
            intent.putExtra(EXTRA_EVENT_NAME, event.getName());
            intent.putExtra(EXTRA_EVENT_DATA, (HashMap) event.getData());
            getContext().sendBroadcast(intent);
        }
    }

    private Context getContext() {
        return CircleFlow.getServices().getAndroidService().getAppContext();
    }

}
