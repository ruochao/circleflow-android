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

package com.mocircle.flow.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base node in a flow.
 */
public abstract class FlowNode extends FlowElement {

    protected String flowId;
    protected List<FlowNode> incomingNodes = new ArrayList<>();
    protected Map<String, FlowNode> outgoingNodes = new HashMap<>();
    protected Map<String, Token> tokens = new HashMap<>();

    /**
     * Gets the identifier of a flow which the node belongs to.
     *
     * @return flow id
     */
    public String getFlowId() {
        return flowId;
    }

    /**
     * Sets flow id.
     *
     * @param flowId flow id
     */
    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    /**
     * Gets all incoming nodes.
     *
     * @return incoming nodes
     */
    public List<FlowNode> getIncomingNodes() {
        return incomingNodes;
    }

    /**
     * Adds outgoing node with token type.
     *
     * @param tokenType token type
     * @param node      outgoing node
     * @return this object
     */
    public FlowNode addOutgoingNode(String tokenType, FlowNode node) {
        if (TextUtils.isEmpty(tokenType)) {
            tokenType = node.toString();
        }
        outgoingNodes.put(tokenType, node);
        node.addIncomingNode(this);
        return this;
    }

    /**
     * Adds a outgoing node.
     *
     * @param node outgoing node
     * @return this object
     */
    public FlowNode addOutgoingNode(FlowNode node) {
        addOutgoingNode(null, node);
        return this;
    }

    /**
     * Gets selected outgoing nodes according to token types.
     *
     * @param tokenTypes token types
     * @return outgoing nodes
     */
    public List<FlowNode> getOutgoingNodes(String[] tokenTypes) {
        if (tokenTypes == null) {
            return new ArrayList<>(outgoingNodes.values());
        }
        List<FlowNode> nodes = new ArrayList<>();
        for (String condition : tokenTypes) {
            FlowNode node = outgoingNodes.get(condition);
            if (node != null) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    /**
     * Gets all potential outgoing nodes.
     *
     * @return outgoing nodes
     */
    public List<FlowNode> getOutgoingNodes() {
        return getOutgoingNodes(null);
    }

    /**
     * Gets all incoming tokens.
     *
     * @return incoming tokens
     */
    public List<Token> getIncomingTokens() {
        return new ArrayList<>(tokens.values());
    }

    /**
     * Gets data from incoming tokens
     *
     * @param key data key
     * @return data value
     */
    public Object getIncomingData(String key) {
        return getIncomingData().get(key);
    }

    /**
     * Gets all data from incoming tokens
     *
     * @return all data
     */
    public Map<String, Object> getIncomingData() {
        Token tempToken = new Token();
        Collection<Token> tokenCollection = tokens.values();
        for (Token token : tokenCollection) {
            tempToken.mergeData(token);
        }
        return tempToken.getData();
    }

    /**
     * Receives a {@link Token} and adds to to current node.
     *
     * @param node  original node
     * @param token token from original node
     */
    public void receiveToken(FlowNode node, Token token) {
        tokens.put(node != null ? node.getId() : null, token);
    }

    /**
     * Clears all incoming tokens.
     */
    public void destroyToken() {
        tokens.clear();
    }

    protected void addIncomingNode(FlowNode node) {
        if (!incomingNodes.contains(node)) {
            incomingNodes.add(node);
        }
    }

}
