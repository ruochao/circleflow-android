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

package com.mocircle.flow.handler.node;

import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.service.FlowLifecycleManageService;

/**
 * Interface for handling {@link FlowNode}.
 */
public interface FlowNodeHandler {

    /**
     * Indicates the handler can handle this node or not.
     *
     * @param node node to be handled
     * @return true if handler can handle this node
     */
    boolean isSupported(FlowNode node);

    /**
     * Starts to handle the node.
     *
     * @param node node to be handled
     * @return handling result
     */
    NodeHandleResult handleNode(FlowNode node);

    /**
     * Called after handling the node
     *
     * @param node   node handled
     * @param result handled result
     */
    void callAfterHandled(FlowNode node, NodeHandleResult result);

    /**
     * Sets {@link FlowLifecycleManageService} implementation.
     *
     * @param service lifecycle management service implementation
     */
    void setLifecycleManageService(FlowLifecycleManageService service);
}
