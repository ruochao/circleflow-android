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

import com.mocircle.flow.exception.FlowExecutionException;
import com.mocircle.flow.listener.FlowSignalListener;
import com.mocircle.flow.model.signal.Signal;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler manager that handles all kinds of {@link Signal}, it supports extension to handle new
 * kind of signal.
 */
public class SignalHandlerManager {

    private static SignalHandlerManager instance;
    private List<SignalHandler> signalHandlers = new ArrayList<>();
    private SignalQueue signalQueue = new SignalQueueImpl(Looper.getMainLooper());

    private SignalHandlerManager() {
        registerSignalHandler(new LocalEventHandler());
        registerSignalHandler(new DeviceEventHandler());
    }

    /**
     * Gets singleton instance.
     *
     * @return instance
     */
    public static SignalHandlerManager getInstance() {
        if (instance == null) {
            instance = new SignalHandlerManager();
        }
        return instance;
    }

    /**
     * Sets signal queue implementation.
     *
     * @param signalQueue signal queue implementation
     */
    public void setSignalQueue(SignalQueue signalQueue) {
        this.signalQueue = signalQueue;
        for (SignalHandler handler : signalHandlers) {
            handler.setSignalQueue(signalQueue);
        }
    }

    /**
     * Registers a signal handler, in order to handle new kind of signal.
     *
     * @param handler signal handler
     */
    public void registerSignalHandler(SignalHandler handler) {
        if (!signalHandlers.contains(handler)) {
            signalHandlers.add(handler);
            handler.setSignalQueue(this.signalQueue);
        }
    }

    /**
     * Adds signal listener for observing signal receiving.
     *
     * @param name     signal name
     * @param listener signal listener
     */
    public void addSignalListener(String name, FlowSignalListener listener) {
        signalQueue.addListener(name, listener);
        if (signalQueue.getCurrentListenerCount() == 1) {
            for (SignalHandler handler : signalHandlers) {
                handler.setup();
            }
        }
    }

    /**
     * Removes the given signal listener.
     *
     * @param name     signal name
     * @param listener signal listener
     */
    public void removeSignalListener(String name, FlowSignalListener listener) {
        signalQueue.removeListener(name, listener);
        if (signalQueue.getCurrentListenerCount() == 0) {
            for (SignalHandler handler : signalHandlers) {
                handler.shutdown();
            }
        }
    }

    /**
     * Remove all listeners and shutdown all known handlers.
     */
    public void clearListeners() {
        signalQueue.removeAllListeners();
        for (SignalHandler handler : signalHandlers) {
            handler.shutdown();
        }
    }

    /**
     * Send a signal.
     *
     * @param signal signal object
     */
    public void sendSignal(Signal signal) {
        for (SignalHandler handler : signalHandlers) {
            if (handler.isSupported(signal.getClass())) {
                handler.sendSignal(signal);
                return;
            }
        }
        throw new FlowExecutionException("Cannot find signal handler for " + signal.getClass().getName());
    }
}
