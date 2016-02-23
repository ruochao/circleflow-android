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

package com.mocircle.flow;

import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.Token;

import java.util.List;

/**
 * A history holds flow execution status.
 */
public class FlowHistory {

    /**
     * A history holds node execution status.
     */
    public static class NodeHistory {

        private String nodeId;
        private Class<? extends FlowNode> nodeClass;
        private long beforeTimestamp;
        private long afterTimestamp;
        private Token tokenAfterExecuted;

        /**
         * Gets node id.
         *
         * @return node id
         */
        public String getNodeId() {
            return nodeId;
        }

        /**
         * Sets node id.
         *
         * @param nodeId node id
         */
        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }

        /**
         * Gets node class.
         *
         * @return node class
         */
        public Class<? extends FlowNode> getNodeClass() {
            return nodeClass;
        }

        /**
         * Sets node class.
         *
         * @param nodeClass node class
         */
        public void setNodeClass(Class<? extends FlowNode> nodeClass) {
            this.nodeClass = nodeClass;
        }

        /**
         * Gets the timestamp before executing a node.
         *
         * @return timestamp
         */
        public long getBeforeTimestamp() {
            return beforeTimestamp;
        }

        /**
         * Sets the timestamp before executing a node.
         *
         * @param beforeTimestamp timestamp
         */
        public void setBeforeTimestamp(long beforeTimestamp) {
            this.beforeTimestamp = beforeTimestamp;
        }

        /**
         * Gets the timestamp after executing a node.
         *
         * @return timestamp
         */
        public long getAfterTimestamp() {
            return afterTimestamp;
        }

        /**
         * Sets the timestamp after executing a node.
         *
         * @param afterTimestamp timestamp
         */
        public void setAfterTimestamp(long afterTimestamp) {
            this.afterTimestamp = afterTimestamp;
        }

        /**
         * Gets the output token after executing a node.
         *
         * @return output token
         */
        public Token getTokenAfterExecuted() {
            return tokenAfterExecuted;
        }

        /**
         * Sets the output token after executing a node.
         *
         * @param tokenAfterExecuted output token
         */
        public void setTokenAfterExecuted(Token tokenAfterExecuted) {
            this.tokenAfterExecuted = tokenAfterExecuted;
        }
    }

    private String flowId;
    private List<NodeHistory> executionOrder;
    private long startTimestamp;
    private long endTimestamp;

    public FlowHistory(String flowId) {
        this.flowId = flowId;
    }

    /**
     * Gets flow id.
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
     * Gets nodes execution order and history.
     *
     * @return node execution history
     */
    public List<NodeHistory> getExecutionOrder() {
        return executionOrder;
    }

    /**
     * Sets nodes execution order.
     *
     * @param executionOrder node execution order
     */
    public void setExecutionOrder(List<NodeHistory> executionOrder) {
        this.executionOrder = executionOrder;
    }

    /**
     * Gets timestamp when flow execution starts.
     *
     * @return timestamp
     */
    public long getStartTimestamp() {
        return startTimestamp;
    }

    /**
     * Sets timestamp of flow starts.
     *
     * @param startTimestamp timestamp
     */
    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    /**
     * Gets the timestamp after flow execution finished.
     *
     * @return timestamp timestamp
     */
    public long getEndTimestamp() {
        return endTimestamp;
    }

    /**
     * Sets the timestamp after flow execution finished.
     *
     * @param endTimestamp timestamp
     */
    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

}
