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
import com.mocircle.flow.test.CallbackVerify;

import junit.framework.Assert;

public class FlowLifecycleManageServiceVerifyOrderMock extends FlowLifecycleServiceImpl {

    private String flowId;
    private String[] nodeOrder;

    private int indexPre = 0;
    private int indexPost = 0;

    private CallbackVerify startCallback = new CallbackVerify();
    private CallbackVerify endCallback = new CallbackVerify();

    public FlowLifecycleManageServiceVerifyOrderMock() {
    }

    public void setExpectedFlowId(String flowId) {
        this.flowId = flowId;
    }

    public void setExpectedOrder(String[] order) {
        this.nodeOrder = order;
    }

    public boolean isStartedCalled() {
        return startCallback.isCalled();
    }

    public boolean isEndedCalled() {
        return endCallback.isCalled();
    }

    @Override
    public void notifyFlowStarted(String flowId, FlowDefinition flowDefinition) {
        startCallback.call();
        Assert.assertEquals(this.flowId, flowId);
    }

    @Override
    public void notifyPreExecuteNode(FlowNode node) {
        Assert.assertEquals(nodeOrder[indexPre++], node.getClass().getSimpleName());
    }

    @Override
    public void notifyPostExecuteNode(FlowNode node, Token token) {
        Assert.assertEquals(nodeOrder[indexPost++], node.getClass().getSimpleName());
    }

    @Override
    public void notifyFlowEnded(String flowId) {
        endCallback.call();
        Assert.assertEquals(this.flowId, flowId);
    }

    @Override
    public void notifyFlowCancelled(String flowId) {
        Assert.assertEquals(this.flowId, flowId);
    }

}
