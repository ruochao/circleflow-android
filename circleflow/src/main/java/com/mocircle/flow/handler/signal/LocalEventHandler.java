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

import com.mocircle.flow.model.signal.LocalEvent;
import com.mocircle.flow.model.signal.Signal;

/**
 * Handler for {@link LocalEvent}.
 */
public class LocalEventHandler implements SignalHandler {

    private SignalQueue queue;

    public LocalEventHandler() {
    }

    @Override
    public void setSignalQueue(SignalQueue queue) {
        this.queue = queue;
    }

    @Override
    public void setup() {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public boolean isSupported(Class<? extends Signal> signalClass) {
        return signalClass.equals(LocalEvent.class);
    }

    @Override
    public void sendSignal(Signal signal) {
        if (isSupported(signal.getClass())) {
            queue.queueSignal(signal);
        }
    }

}
