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
import com.mocircle.flow.model.Token;

import java.util.List;

/**
 * The output after handling a node, which holds the handling result and data.
 */
public class NodeHandleResult {

    private List<FlowNode> nextNodes;
    private Token outgoingToken;

    public NodeHandleResult() {
    }

    public NodeHandleResult(List<FlowNode> nextNodes, Token outgoingToken) {
        this.nextNodes = nextNodes;
        this.outgoingToken = outgoingToken;
    }

    /**
     * Gets the next nodes to be handled.
     *
     * @return next nodes
     */
    public List<FlowNode> getNextNodes() {
        return nextNodes;
    }

    /**
     * Sets next nodes.
     *
     * @param nextNodes next nodes
     */
    public void setNextNodes(List<FlowNode> nextNodes) {
        this.nextNodes = nextNodes;
    }

    /**
     * Gets the outgoing token after handling a node.
     *
     * @return outgoing token
     */
    public Token getOutgoingToken() {
        return outgoingToken;
    }

    /**
     * Sets outgoing token.
     *
     * @param outgoingToken outgoing token
     */
    public void setOutgoingToken(Token outgoingToken) {
        this.outgoingToken = outgoingToken;
    }
}
