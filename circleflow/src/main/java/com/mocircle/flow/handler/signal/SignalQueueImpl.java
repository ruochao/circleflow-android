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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.mocircle.flow.listener.FlowSignalListener;
import com.mocircle.flow.model.signal.Signal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Default implementation for {@link SignalQueue}.
 */
public class SignalQueueImpl implements SignalQueue {

    static class MyHandler extends Handler {

        private SignalQueueImpl queue;

        public MyHandler(Looper looper, SignalQueueImpl queue) {
            super(looper);
            this.queue = queue;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONSUME_SIGNAL:
                    queue.notifyListeners();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private static final int MSG_CONSUME_SIGNAL = 1;

    private Map<String, List<FlowSignalListener>> signalListeners = new HashMap<>();
    private Queue<Signal> signalQueue = new LinkedList<>();
    private Handler handler;

    public SignalQueueImpl(Looper looper) {
        handler = new MyHandler(looper, this);
    }

    @Override
    public void addListener(String name, FlowSignalListener listener) {
        synchronized (signalListeners) {
            List<FlowSignalListener> listeners = signalListeners.get(name);
            if (listeners == null) {
                listeners = new ArrayList<>();
                signalListeners.put(name, listeners);
            }
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    @Override
    public void removeListener(String name, FlowSignalListener listener) {
        synchronized (signalListeners) {
            List<FlowSignalListener> listeners = signalListeners.get(name);
            if (listeners != null) {
                listeners.remove(listener);
            }
        }
    }

    @Override
    public void removeAllListeners() {
        synchronized (signalListeners) {
            signalListeners.clear();
        }
    }

    @Override
    public void queueSignal(Signal signal) {
        synchronized (signalQueue) {
            signalQueue.add(signal);
            handler.sendEmptyMessage(MSG_CONSUME_SIGNAL);
        }
    }

    @Override
    public int getCurrentListenerCount() {
        synchronized (signalListeners) {
            int size = 0;
            Set<String> keys = signalListeners.keySet();
            for (String key : keys) {
                size += signalListeners.get(key).size();
            }
            return size;
        }
    }

    @Override
    public int getCurrentQueueSize() {
        synchronized (signalQueue) {
            return signalQueue.size();
        }
    }

    private void notifyListeners() {
        synchronized (signalQueue) {
            while (!signalQueue.isEmpty()) {
                Signal signal = signalQueue.poll();
                synchronized (signalListeners) {
                    List<FlowSignalListener> listeners = signalListeners.get(signal.getName());
                    if (listeners != null) {
                        for (FlowSignalListener listener : listeners) {
                            listener.onReceivedSignal(signal);
                        }
                    }
                }
            }
        }
    }
}
