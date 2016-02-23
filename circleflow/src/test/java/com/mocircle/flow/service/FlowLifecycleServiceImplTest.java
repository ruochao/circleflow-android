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

import com.mocircle.flow.BuildConfig;
import com.mocircle.flow.FlowContext;
import com.mocircle.flow.FlowDefinition;
import com.mocircle.flow.FlowHistory;
import com.mocircle.flow.flows.ActionNodeC1;
import com.mocircle.flow.flows.ActionNodeF1;
import com.mocircle.flow.listener.FlowLifecycleListener;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.control.Decision;
import com.mocircle.flow.model.control.FinalNode;
import com.mocircle.flow.model.control.Fork;
import com.mocircle.flow.model.control.InitialNode;
import com.mocircle.flow.test.CallbackVerify;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class FlowLifecycleServiceImplTest {

    @Test
    public void testGlobalListener() {
        FlowLifecycleServiceImpl service = new FlowLifecycleServiceImpl();

        final CallbackVerify startCallback = new CallbackVerify();
        final CallbackVerify preCallback = new CallbackVerify();
        final CallbackVerify postCallback = new CallbackVerify();
        final CallbackVerify endCallback = new CallbackVerify();
        final CallbackVerify cancelCallback = new CallbackVerify();
        service.addGlobalLifecycleListener(new FlowLifecycleListener() {
            @Override
            public void onFlowStarted(String flowId, FlowDefinition flowDefinition) {
                startCallback.call();
                Assert.assertEquals("flow-id", flowId);
            }

            @Override
            public void preExecuteNode(FlowNode node) {
                preCallback.call();
                Assert.assertEquals(Decision.class, node.getClass());
                Assert.assertEquals("flow-id-pre", node.getFlowId());
            }

            @Override
            public void postExecuteNode(FlowNode node, Token token) {
                postCallback.call();
                Assert.assertEquals(Fork.class, node.getClass());
                Assert.assertEquals("flow-id-post", node.getFlowId());
                Assert.assertEquals(null, token);
            }

            @Override
            public void onFlowEnded(String flowId) {
                endCallback.call();
                Assert.assertEquals("flow-id-end", flowId);
            }

            @Override
            public void onFlowCancelled(String flowId) {
                cancelCallback.call();
                Assert.assertEquals("flow-id-cancel", flowId);
            }
        });

        service.notifyFlowStarted("flow-id", null);
        Assert.assertEquals(1, startCallback.getCallCount());
        Assert.assertEquals(0, preCallback.getCallCount());
        Assert.assertEquals(0, postCallback.getCallCount());
        Assert.assertEquals(0, endCallback.getCallCount());
        Assert.assertEquals(0, cancelCallback.getCallCount());

        Decision d = new Decision();
        d.setFlowId("flow-id-pre");
        service.notifyPreExecuteNode(d);
        Assert.assertEquals(1, startCallback.getCallCount());
        Assert.assertEquals(1, preCallback.getCallCount());
        Assert.assertEquals(0, postCallback.getCallCount());
        Assert.assertEquals(0, endCallback.getCallCount());
        Assert.assertEquals(0, cancelCallback.getCallCount());

        Fork f = new Fork();
        f.setFlowId("flow-id-post");
        service.notifyPostExecuteNode(f, null);
        Assert.assertEquals(1, startCallback.getCallCount());
        Assert.assertEquals(1, preCallback.getCallCount());
        Assert.assertEquals(1, postCallback.getCallCount());
        Assert.assertEquals(0, endCallback.getCallCount());
        Assert.assertEquals(0, cancelCallback.getCallCount());

        service.notifyFlowEnded("flow-id-end");
        Assert.assertEquals(1, startCallback.getCallCount());
        Assert.assertEquals(1, preCallback.getCallCount());
        Assert.assertEquals(1, postCallback.getCallCount());
        Assert.assertEquals(1, endCallback.getCallCount());
        Assert.assertEquals(0, cancelCallback.getCallCount());

        service.notifyFlowCancelled("flow-id-cancel");
        Assert.assertEquals(1, startCallback.getCallCount());
        Assert.assertEquals(1, preCallback.getCallCount());
        Assert.assertEquals(1, postCallback.getCallCount());
        Assert.assertEquals(1, endCallback.getCallCount());
        Assert.assertEquals(1, cancelCallback.getCallCount());
    }

    @Test
    public void testGlobalListenerCallCount() {
        FlowLifecycleServiceImpl service = new FlowLifecycleServiceImpl();

        final CallbackVerify startCallback = new CallbackVerify();
        final CallbackVerify preCallback = new CallbackVerify();
        final CallbackVerify postCallback = new CallbackVerify();
        final CallbackVerify endCallback = new CallbackVerify();
        final CallbackVerify cancelCallback = new CallbackVerify();
        service.addGlobalLifecycleListener(new FlowLifecycleListener() {
            @Override
            public void onFlowStarted(String flowId, FlowDefinition flowDefinition) {
                startCallback.call();
            }

            @Override
            public void preExecuteNode(FlowNode node) {
                preCallback.call();
            }

            @Override
            public void postExecuteNode(FlowNode node, Token token) {
                postCallback.call();
            }

            @Override
            public void onFlowEnded(String flowId) {
                endCallback.call();
            }

            @Override
            public void onFlowCancelled(String flowId) {
                cancelCallback.call();
            }
        });

        service.notifyFlowStarted("a", null);
        service.notifyFlowStarted("b", null);
        Assert.assertEquals(2, startCallback.getCallCount());

        service.notifyFlowEnded("a");
        service.notifyFlowEnded("b");
        service.notifyFlowEnded("c");
        Assert.assertEquals(3, endCallback.getCallCount());

        service.notifyFlowCancelled("a");
        service.notifyFlowCancelled("b");
        Assert.assertEquals(2, cancelCallback.getCallCount());
    }

    @Test
    public void testFlowListener() {
        FlowLifecycleServiceImpl service = new FlowLifecycleServiceImpl();

        final CallbackVerify startCallback = new CallbackVerify();
        final CallbackVerify preCallback = new CallbackVerify();
        final CallbackVerify postCallback = new CallbackVerify();
        final CallbackVerify endCallback = new CallbackVerify();
        final CallbackVerify cancelCallback = new CallbackVerify();
        service.addLifecycleListener("my-flow-id", new FlowLifecycleListener() {
            @Override
            public void onFlowStarted(String flowId, FlowDefinition flowDefinition) {
                startCallback.call();
                Assert.assertEquals("my-flow-id", flowId);
            }

            @Override
            public void preExecuteNode(FlowNode node) {
                preCallback.call();
                Assert.assertEquals(Decision.class, node.getClass());
                Assert.assertEquals("my-flow-id", node.getFlowId());
            }

            @Override
            public void postExecuteNode(FlowNode node, Token token) {
                postCallback.call();
                Assert.assertEquals(Fork.class, node.getClass());
                Assert.assertEquals("my-flow-id", node.getFlowId());
                Assert.assertEquals(null, token);
            }

            @Override
            public void onFlowEnded(String flowId) {
                endCallback.call();
                Assert.assertEquals("my-flow-id", flowId);
            }

            @Override
            public void onFlowCancelled(String flowId) {
                cancelCallback.call();
                Assert.assertEquals("my-flow-id", flowId);
            }
        });


        service.notifyFlowStarted("my-flow-id", null);
        service.notifyFlowStarted("my-flow-id-bad", null);
        Assert.assertEquals(1, startCallback.getCallCount());
        Assert.assertEquals(0, preCallback.getCallCount());
        Assert.assertEquals(0, postCallback.getCallCount());
        Assert.assertEquals(0, endCallback.getCallCount());
        Assert.assertEquals(0, cancelCallback.getCallCount());

        Decision d = new Decision();
        d.setFlowId("my-flow-id");
        service.notifyPreExecuteNode(d);
        Assert.assertEquals(1, startCallback.getCallCount());
        Assert.assertEquals(1, preCallback.getCallCount());
        Assert.assertEquals(0, postCallback.getCallCount());
        Assert.assertEquals(0, endCallback.getCallCount());
        Assert.assertEquals(0, cancelCallback.getCallCount());

        Fork f = new Fork();
        f.setFlowId("my-flow-id");
        service.notifyPostExecuteNode(f, null);
        Assert.assertEquals(1, startCallback.getCallCount());
        Assert.assertEquals(1, preCallback.getCallCount());
        Assert.assertEquals(1, postCallback.getCallCount());
        Assert.assertEquals(0, endCallback.getCallCount());
        Assert.assertEquals(0, cancelCallback.getCallCount());

        service.notifyFlowEnded("my-flow-id");
        Assert.assertEquals(1, startCallback.getCallCount());
        Assert.assertEquals(1, preCallback.getCallCount());
        Assert.assertEquals(1, postCallback.getCallCount());
        Assert.assertEquals(1, endCallback.getCallCount());
        Assert.assertEquals(0, cancelCallback.getCallCount());

        service.notifyFlowCancelled("my-flow-id");
        Assert.assertEquals(1, startCallback.getCallCount());
        Assert.assertEquals(1, preCallback.getCallCount());
        Assert.assertEquals(1, postCallback.getCallCount());
        Assert.assertEquals(1, endCallback.getCallCount());
        Assert.assertEquals(1, cancelCallback.getCallCount());
    }

    @Test
    public void testFlowContext() {
        final String testFlowId = "flow-context-id";
        FlowLifecycleServiceImpl service = new FlowLifecycleServiceImpl();
        FlowContext context = service.getFlowContext(testFlowId);
        Assert.assertNull(context);

        service.createLifecycleContext(testFlowId);
        context = service.getFlowContext(testFlowId);
        Assert.assertEquals(testFlowId, context.getFlowId());
        Assert.assertEquals(FlowContext.STATUS_IDLE, context.getCurrentStatus());

        service.notifyFlowStarted(testFlowId, null);
        Assert.assertEquals(FlowContext.STATUS_STARTED, context.getCurrentStatus());

        Decision d = new Decision();
        d.setFlowId(testFlowId);
        service.notifyPreExecuteNode(d);
        Assert.assertEquals(FlowContext.STATUS_RUNNING, context.getCurrentStatus());
        Assert.assertEquals(Decision.class, context.getCurrentNodes().get(0).getClass());

        service.notifyFlowEnded(testFlowId);
        Assert.assertEquals(FlowContext.STATUS_ENDED, context.getCurrentStatus());

        service.notifyFlowCancelled(testFlowId);
        Assert.assertEquals(FlowContext.STATUS_CANCELLED, context.getCurrentStatus());
    }

    @Test
    public void testFlowHistoryBasic() {
        final String testFlowId = "flow-history-id";
        FlowLifecycleServiceImpl service = new FlowLifecycleServiceImpl();
        FlowHistory history = service.getFlowHistory(testFlowId);
        Assert.assertNull(history);

        service.createLifecycleContext(testFlowId);
        history = service.getFlowHistory(testFlowId);
        Assert.assertTrue(history.getStartTimestamp() == 0);
        Assert.assertTrue(history.getEndTimestamp() == 0);

        service.notifyFlowStarted(testFlowId, null);
        Assert.assertTrue(history.getStartTimestamp() > 0);
        Assert.assertTrue(history.getEndTimestamp() == 0);

        service.notifyFlowEnded(testFlowId);
        Assert.assertTrue(history.getStartTimestamp() > 0);
        Assert.assertTrue(history.getEndTimestamp() > 0);
        Assert.assertTrue(history.getEndTimestamp() >= history.getStartTimestamp());
    }

    @Test
    public void testFlowHistoryDetails() {
        final String testFlowId = "flow-history-detail-id";

        InitialNode node1 = new InitialNode();
        node1.setFlowId(testFlowId);
        node1.setId("node1");
        ActionNodeC1 node2 = new ActionNodeC1();
        node2.setFlowId(testFlowId);
        node2.setId("node2");
        Decision node3 = new Decision();
        node3.setFlowId(testFlowId);
        node3.setId("node3");
        ActionNodeF1 node4 = new ActionNodeF1();
        node4.setFlowId(testFlowId);
        node4.setId("node4");
        FinalNode node5 = new FinalNode();
        node5.setFlowId(testFlowId);
        node5.setId("node5");

        FlowLifecycleServiceImpl service = new FlowLifecycleServiceImpl();
        service.createLifecycleContext(testFlowId);
        service.notifyFlowStarted(testFlowId, null);
        service.notifyPreExecuteNode(node1);
        service.notifyPostExecuteNode(node1, null);
        service.notifyPreExecuteNode(node2);
        service.notifyPostExecuteNode(node2, null);
        service.notifyPreExecuteNode(node3);
        service.notifyPostExecuteNode(node3, new Token(Token.TYPE_NORMAL));
        service.notifyPreExecuteNode(node4);
        service.notifyPostExecuteNode(node4, null);
        service.notifyPreExecuteNode(node5);
        service.notifyPostExecuteNode(node5, null);
        service.notifyFlowEnded(testFlowId);

        FlowHistory history = service.getFlowHistory(testFlowId);
        List<FlowHistory.NodeHistory> items = history.getExecutionOrder();
        Assert.assertEquals(5, items.size());

        Assert.assertEquals(InitialNode.class, items.get(0).getNodeClass());
        Assert.assertEquals("node1", items.get(0).getNodeId());
        Assert.assertEquals(null, items.get(0).getTokenAfterExecuted());

        Assert.assertEquals(ActionNodeC1.class, items.get(1).getNodeClass());
        Assert.assertEquals("node2", items.get(1).getNodeId());
        Assert.assertEquals(null, items.get(1).getTokenAfterExecuted());

        Assert.assertEquals(Decision.class, items.get(2).getNodeClass());
        Assert.assertEquals("node3", items.get(2).getNodeId());
        Assert.assertEquals(Token.TYPE_NORMAL, items.get(2).getTokenAfterExecuted().getTokenType());

        Assert.assertEquals(ActionNodeF1.class, items.get(3).getNodeClass());
        Assert.assertEquals("node4", items.get(3).getNodeId());
        Assert.assertEquals(null, items.get(3).getTokenAfterExecuted());

        Assert.assertEquals(FinalNode.class, items.get(4).getNodeClass());
        Assert.assertEquals("node5", items.get(4).getNodeId());
        Assert.assertEquals(null, items.get(4).getTokenAfterExecuted());

        for (FlowHistory.NodeHistory item : items) {
            Assert.assertTrue(item.getBeforeTimestamp() > 0);
            Assert.assertTrue(item.getAfterTimestamp() > 0);
            Assert.assertTrue(item.getAfterTimestamp() >= item.getBeforeTimestamp());
        }
    }

}
