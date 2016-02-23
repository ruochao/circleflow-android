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

import com.mocircle.flow.model.signal.Signal;

/**
 * Interface for handling {@link Signal}.
 */
public interface SignalHandler {

    /**
     * Sets the signal queue implementation.
     *
     * @param queue signal queue
     */
    void setSignalQueue(SignalQueue queue);

    /**
     * Sets up the handler.
     */
    void setup();

    /**
     * Shutdown the handler.
     */
    void shutdown();

    /**
     * Indicates the handler can handle this type of signal or not.
     *
     * @param signalClass signal type
     * @return true if handler can handle the signal
     */
    boolean isSupported(Class<? extends Signal> signalClass);

    /**
     * Sends a signal.
     *
     * @param signal signal object
     */
    void sendSignal(Signal signal);

}
