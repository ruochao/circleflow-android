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

import com.mocircle.flow.listener.FlowSignalListener;
import com.mocircle.flow.model.signal.Signal;

import java.util.Map;

/**
 * A service interface for managing signal sending and receiving.
 */
public interface FlowSignalService {

    /**
     * Adds signal listener.
     *
     * @param name     signal name
     * @param listener signal listener
     */
    void addSignalListener(String name, FlowSignalListener listener);

    /**
     * Removes signal listener.
     *
     * @param name     signal name
     * @param listener signal listener
     */
    void removeSignalListener(String name, FlowSignalListener listener);

    /**
     * Sends a signal.
     *
     * @param signal signal object
     */
    void sendSignal(Signal signal);

    /**
     * Sends a local event.
     *
     * @param name event name
     * @param data event data
     */
    void sendLocalEvent(String name, Map<String, Object> data);

    /**
     * Sends a local event without data.
     *
     * @param name event name
     */
    void sendLocalEvent(String name);

    /**
     * Sends a local event.
     *
     * @param name   event name
     * @param result data for result
     */
    void sendLocalEvent(String name, String result);

    /**
     * Sends a device level event.
     *
     * @param name event name
     * @param data event data
     */
    void sendDeviceEvent(String name, Map<String, Object> data);

    /**
     * Sends a device level event without data.
     *
     * @param name event name
     */
    void sendDeviceEvent(String name);

    /**
     * Sends a device level event.
     *
     * @param name   event name
     * @param result data for result
     */
    void sendDeviceEvent(String name, String result);
}
