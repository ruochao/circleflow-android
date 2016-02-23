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
 * A base class for lifecycle listener, it's empty implementation for {@link
 * FlowLifecycleListener}.
 */
public abstract class FlowLifecycleListenerAdapter implements FlowLifecycleListener {

    @Override
    public void onFlowStarted(String flowId, FlowDefinition flowDefinition) {
    }

    @Override
    public void preExecuteNode(FlowNode node) {
    }

    @Override
    public void postExecuteNode(FlowNode node, Token token) {
    }

    @Override
    public void onFlowEnded(String flowId) {
    }

    @Override
    public void onFlowCancelled(String flowId) {
    }

}
