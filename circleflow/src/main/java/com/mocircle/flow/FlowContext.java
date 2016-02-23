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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Flow context that holds flow status.
 */
public class FlowContext {

    public static final int STATUS_IDLE = 0;
    public static final int STATUS_STARTED = 1;
    public static final int STATUS_RUNNING = 2;
    public static final int STATUS_ENDED = 3;
    public static final int STATUS_CANCELLED = 4;

    private String flowId;
    private int currentStatus;
    private List<FlowNode> currentNodes;
    private Map<String, Object> output;

    public FlowContext(String flowId) {
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
     * Gets current flow status.
     *
     * @return flow status
     */
    public int getCurrentStatus() {
        return currentStatus;
    }

    /**
     * Sets current flow status.
     *
     * @param currentStatus flow status
     */
    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * Gets current executing nodes.
     *
     * @return current nodes
     */
    public List<FlowNode> getCurrentNodes() {
        if (currentNodes == null) {
            currentNodes = new ArrayList<>();
        }
        return currentNodes;
    }

    /**
     * Gets current output of node executing.
     *
     * @return node executing output
     */
    public Map<String, Object> getOutput() {
        return output;
    }

    /**
     * Sets current output of node executing.
     *
     * @param output node executing output
     */
    public void setOutput(Map<String, Object> output) {
        this.output = output;
    }
}
