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

import com.mocircle.flow.FlowContext;
import com.mocircle.flow.FlowHistory;
import com.mocircle.flow.listener.FlowLifecycleListener;

/**
 * A service interface for monitoring lifecycle status change and execution history.
 */
public interface FlowLifecycleService {

    /**
     * Adds lifecycle listener for all flows.
     *
     * @param listener listener
     */
    void addGlobalLifecycleListener(FlowLifecycleListener listener);

    /**
     * Removes global lifecycle listener.
     *
     * @param listener listener
     */
    void removeGlobalLifecycleListener(FlowLifecycleListener listener);

    /**
     * Adds lifecycle listener for specific flow.
     *
     * @param flowId   flow id
     * @param listener listener
     */
    void addLifecycleListener(String flowId, FlowLifecycleListener listener);

    /**
     * Removes lifecycle listener.
     *
     * @param flowId   flow id
     * @param listener listener
     */
    void removeLifecycleListener(String flowId, FlowLifecycleListener listener);

    /**
     * Removes all lifecycle listeners for specific flow.
     *
     * @param flowId flow id
     */
    void removeLifecycleListener(String flowId);

    /**
     * Gets the flow context.
     *
     * @param flowId flow id
     * @return flow context
     */
    FlowContext getFlowContext(String flowId);

    /**
     * Gets the flow execution history.
     *
     * @param flowId flow id
     * @return flow history
     */
    FlowHistory getFlowHistory(String flowId);

}
