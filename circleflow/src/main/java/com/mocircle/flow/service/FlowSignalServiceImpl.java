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

import com.mocircle.flow.handler.signal.SignalHandlerManager;
import com.mocircle.flow.listener.FlowSignalListener;
import com.mocircle.flow.model.signal.DeviceEvent;
import com.mocircle.flow.model.signal.LocalEvent;
import com.mocircle.flow.model.signal.Signal;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation for {@link FlowSignalService}.
 */
public class FlowSignalServiceImpl implements FlowSignalService {

    protected SignalHandlerManager handlerManager = SignalHandlerManager.getInstance();

    public FlowSignalServiceImpl() {
    }

    @Override
    public void addSignalListener(String name, FlowSignalListener listener) {
        handlerManager.addSignalListener(name, listener);
    }

    @Override
    public void removeSignalListener(String name, FlowSignalListener listener) {
        handlerManager.removeSignalListener(name, listener);
    }

    @Override
    public void sendSignal(Signal signal) {
        handlerManager.sendSignal(signal);
    }

    @Override
    public void sendLocalEvent(String name, Map<String, Object> data) {
        LocalEvent event = new LocalEvent(name, data);
        sendSignal(event);
    }

    @Override
    public void sendLocalEvent(String name) {
        LocalEvent event = new LocalEvent(name);
        sendSignal(event);
    }

    @Override
    public void sendLocalEvent(String name, String result) {
        Map<String, Object> data = new HashMap<>();
        data.put(Signal.KEY_DATA_RESULT, result);
        LocalEvent event = new LocalEvent(name, data);
        sendSignal(event);
    }

    @Override
    public void sendDeviceEvent(String name, Map<String, Object> data) {
        DeviceEvent event = new DeviceEvent(name, data);
        sendSignal(event);
    }

    @Override
    public void sendDeviceEvent(String name) {
        DeviceEvent event = new DeviceEvent(name);
        sendSignal(event);
    }

    @Override
    public void sendDeviceEvent(String name, String result) {
        Map<String, Object> data = new HashMap<>();
        data.put(Signal.KEY_DATA_RESULT, result);
        DeviceEvent event = new DeviceEvent(name, data);
        sendSignal(event);
    }
}
