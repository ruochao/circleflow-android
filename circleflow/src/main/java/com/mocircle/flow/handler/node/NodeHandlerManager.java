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

import com.mocircle.flow.exception.FlowExecutionException;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.service.FlowLifecycleManageService;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler manager that handles all kinds of {@link FlowNode}, it supports extension to handle new
 * kind of node.
 */
public class NodeHandlerManager {

    private static NodeHandlerManager instance;
    private List<FlowNodeHandler> nodeHandlers = new ArrayList<>();

    private NodeHandlerManager() {
        registerNodeHandler(new InitialNodeHandler());
        registerNodeHandler(new ActionNodeHandler());
        registerNodeHandler(new DecisionHandler());
        registerNodeHandler(new MergeHandler());
        registerNodeHandler(new ForkHandler());
        registerNodeHandler(new JoinHandler());
        registerNodeHandler(new CustomControlNodeHandler());
        registerNodeHandler(new FinalNodeHandler());
        registerNodeHandler(new NullNodeHandler());
    }

    /**
     * Gets singleton instance.
     *
     * @return instance
     */
    public static NodeHandlerManager getInstance() {
        if (instance == null) {
            instance = new NodeHandlerManager();
        }
        return instance;
    }

    /**
     * Registers a node handler, in order to handle new kind of node.
     *
     * @param handler node handler
     */
    public void registerNodeHandler(FlowNodeHandler handler) {
        if (!nodeHandlers.contains(handler)) {
            nodeHandlers.add(handler);
        }
    }

    /**
     * Find the handler that can handle this kind of node.
     *
     * @param node node to be handled
     * @return node handler
     */
    public FlowNodeHandler findNodeHandler(FlowNode node) {
        for (FlowNodeHandler handler : nodeHandlers) {
            if (handler.isSupported(node)) {
                return handler;
            }
        }
        throw new FlowExecutionException("Cannot find node handler for " + node.getClass().getName());
    }

    /**
     * Sets lifecycle management service.
     *
     * @param service lifecycle management service
     */
    public void setLifecycleManageService(FlowLifecycleManageService service) {
        for (FlowNodeHandler handler : nodeHandlers) {
            handler.setLifecycleManageService(service);
        }
    }

}
