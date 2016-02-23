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

import com.mocircle.flow.listener.FlowSignalListener;
import com.mocircle.flow.model.signal.Signal;

/**
 * A queue for observing signal sending and receiving.
 */
public interface SignalQueue {

    /**
     * Adds signal listener for observing signal receiving.
     *
     * @param name     signal name
     * @param listener signal listener
     */
    void addListener(String name, FlowSignalListener listener);

    /**
     * Removes the given signal listener.
     *
     * @param name     signal name
     * @param listener signal listener
     */
    void removeListener(String name, FlowSignalListener listener);

    /**
     * Removes all signal listeners.
     */
    void removeAllListeners();

    /**
     * Enqueues a signal object.
     *
     * @param signal signal object
     */
    void queueSignal(Signal signal);

    /**
     * Gets current registered listener count.
     *
     * @return listener count
     */
    int getCurrentListenerCount();

    /**
     * Gets current signal count in queue.
     *
     * @return signal count in queue
     */
    int getCurrentQueueSize();
}
