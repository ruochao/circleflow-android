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

package com.mocircle.flow.model.action;

import com.mocircle.flow.CircleFlow;
import com.mocircle.flow.FlowContext;
import com.mocircle.flow.FlowDefinition;
import com.mocircle.flow.FlowExecutor;
import com.mocircle.flow.model.Token;

/**
 * Sub flow node is an action node to trigger another flow execution.
 */
public class SubFlowNode extends ActionNode {

    protected FlowDefinition flowDefinition;
    protected boolean asynchronous;

    public SubFlowNode() {
    }

    public SubFlowNode(FlowDefinition flowDefinition) {
        this.flowDefinition = flowDefinition;
    }

    public SubFlowNode(FlowDefinition flowDef, boolean asynchronous) {
        this.flowDefinition = flowDef;
        this.asynchronous = asynchronous;
    }

    /**
     * Indicates the execution is asynchronous or not. If it's asynchronous the {@link #execute()}
     * method will return immediately, otherwise it will be blocked until execution finished.
     *
     * @return true if it's asynchronous
     */
    public boolean isAsynchronous() {
        return asynchronous;
    }

    /**
     * Sets if it's asynchronous.
     *
     * @param asynchronous asynchronous value
     */
    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    /**
     * Gets sub flow definition.
     *
     * @return flow definition
     */
    public FlowDefinition getFlowDefinition() {
        return flowDefinition;
    }

    /**
     * Sets sub flow definition.
     *
     * @param flowDefinition flow definition
     */
    public void setFlowDefinition(FlowDefinition flowDefinition) {
        this.flowDefinition = flowDefinition;
    }

    @Override
    public Token execute() {
        FlowExecutor executor = CircleFlow.getEngine().prepareFlow(flowDefinition, getIncomingData());
        executor.execute();
        if (asynchronous) {
            FlowContext context = executor.waitForFinished();
            return new Token(Token.TYPE_NORMAL, context.getOutput());
        } else {
            return new Token(Token.TYPE_NORMAL);
        }
    }

}
