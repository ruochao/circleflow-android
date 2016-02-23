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

package com.mocircle.flow.listener;

import com.mocircle.flow.FlowDefinition;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.Token;

/**
 * Listener for listening flow execution.
 */
public interface FlowLifecycleListener {

    /**
     * Called when start executing flow.
     *
     * @param flowId         flow id
     * @param flowDefinition flow definition
     */
    void onFlowStarted(String flowId, FlowDefinition flowDefinition);


    /**
     * Called before executing a node.
     *
     * @param node node to be handled
     */
    void preExecuteNode(FlowNode node);

    /**
     * Called after executed a node.
     *
     * @param node  node has been handled
     * @param token outgoing token after executing the node
     */
    void postExecuteNode(FlowNode node, Token token);

    /**
     * Called when flow process has ended.
     *
     * @param flowId flow id
     */
    void onFlowEnded(String flowId);

    /**
     * Called when flow process has been cancelled.
     *
     * @param flowId flow id
     */
    void onFlowCancelled(String flowId);

}
