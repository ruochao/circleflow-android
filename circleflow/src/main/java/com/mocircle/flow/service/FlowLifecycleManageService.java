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

import com.mocircle.flow.FlowDefinition;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.Token;

/**
 * A internal service interface for control flow lifecycle during execution.
 */
public interface FlowLifecycleManageService {

    /**
     * Notifies flow execution has started.
     *
     * @param flowId         flow id
     * @param flowDefinition flow definition object
     */
    void notifyFlowStarted(String flowId, FlowDefinition flowDefinition);

    /**
     * Notifies the flow status before executing node.
     *
     * @param node current node
     */
    void notifyPreExecuteNode(FlowNode node);

    /**
     * Notifies the flow status after executing node.
     *
     * @param node  current node
     * @param token outgoing token
     */
    void notifyPostExecuteNode(FlowNode node, Token token);

    /**
     * Notifies the flow has ended.
     *
     * @param flowId flow id
     */
    void notifyFlowEnded(String flowId);

    /**
     * Notifies the flow has been cancelled.
     *
     * @param flowId flow id
     */
    void notifyFlowCancelled(String flowId);

    /**
     * Starts to manage the flow lifecycle.
     *
     * @param flowId flow id
     */
    void createLifecycleContext(String flowId);
}
