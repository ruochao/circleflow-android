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

import android.text.TextUtils;

import com.mocircle.android.logging.CircleLog;
import com.mocircle.flow.handler.node.FlowNodeHandler;
import com.mocircle.flow.handler.node.NodeHandleResult;
import com.mocircle.flow.handler.node.NodeHandlerManager;
import com.mocircle.flow.listener.FlowLifecycleListener;
import com.mocircle.flow.listener.FlowLifecycleListenerAdapter;
import com.mocircle.flow.model.CancelableNode;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.control.InitialNode;
import com.mocircle.flow.service.FlowLifecycleManageService;
import com.mocircle.flow.service.FlowLifecycleService;
import com.mocircle.flow.service.FlowSchedulingService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Default implementation for {@link FlowExecutor}.
 */
public class FlowExecutorImpl implements FlowExecutor {

    private static final String TAG = "FlowExecutorImpl";

    private FlowSchedulingService schedulingService;
    private FlowLifecycleManageService lifecycleManageService;
    private FlowLifecycleService lifecycleService;
    private NodeHandlerManager nodeHandlerManager;

    private FlowDefinition flowDef;
    private Map<String, Object> initData;
    private String flowId;

    private boolean cancelled = false;
    private final Object cancelObject = new Object();
    private final Object waitFinishObj = new Object();

    public FlowExecutorImpl(FlowDefinition flowDef, Map<String, Object> data) {
        this.flowDef = flowDef;
        this.initData = data;
        setup();
    }

    @Override
    public String getFlowId() {
        return flowId;
    }

    @Override
    public FlowDefinition getDefinition() {
        return flowDef;
    }

    @Override
    public void execute() {
        resetState();

        // Notify flow started
        lifecycleManageService.notifyFlowStarted(flowId, flowDef);

        final InitialNode initialNode = flowDef.getDefinition();
        if (initData != null) {
            initialNode.setInitData(initData);
        }
        schedulingService.executeAsyncJob(new Runnable() {
            @Override
            public void run() {
                executeNode(initialNode);
            }
        });
    }

    @Override
    public void execute(Map<String, Object> initData) {
        this.initData = initData;
        execute();
    }

    @Override
    public void cancel() {
        synchronized (cancelObject) {
            cancelled = true;
        }
        // Notify current executing nodes
        FlowContext context = lifecycleService.getFlowContext(flowId);
        List<FlowNode> nodes = context.getCurrentNodes();
        for (FlowNode node : nodes) {
            if (node instanceof CancelableNode) {
                CircleLog.d(TAG, "Notify current node flow cancelled: " + node.getClass().getSimpleName());
                ((CancelableNode) node).notifyCancel();
            }
        }
    }

    @Override
    public FlowContext waitForFinished() {
        // Flow has finished, just return
        FlowContext context = lifecycleService.getFlowContext(flowId);
        if (context.getCurrentStatus() == FlowContext.STATUS_ENDED ||
                context.getCurrentStatus() == FlowContext.STATUS_CANCELLED) {
            return context;
        }

        // Block thread until flow ended or cancelled
        FlowLifecycleListener listener = new FlowLifecycleListenerAdapter() {

            @Override
            public void onFlowEnded(String flowId) {
                synchronized (waitFinishObj) {
                    waitFinishObj.notify();
                }
            }

            @Override
            public void onFlowCancelled(String flowId) {
                synchronized (waitFinishObj) {
                    waitFinishObj.notify();
                }
            }
        };
        lifecycleService.addLifecycleListener(flowId, listener);
        try {
            synchronized (waitFinishObj) {
                waitFinishObj.wait();
            }
        } catch (InterruptedException e) {
            CircleLog.w(TAG, e);
        }
        lifecycleService.removeLifecycleListener(flowId, listener);
        return lifecycleService.getFlowContext(flowId);
    }

    private void setup() {
        this.flowId = assignId();

        schedulingService = CircleFlow.getInternalInstance().getSchedulingService();
        lifecycleManageService = CircleFlow.getInternalInstance().getLifecycleManageService();
        lifecycleService = CircleFlow.getInternalInstance().getLifecycleService();
        nodeHandlerManager = NodeHandlerManager.getInstance();
        nodeHandlerManager.setLifecycleManageService(lifecycleManageService);

        // Create flow history
        lifecycleManageService.createLifecycleContext(flowId);
    }

    private void resetState() {
        synchronized (cancelObject) {
            cancelled = false;
        }
    }

    private void executeNode(final FlowNode node) {
        CircleLog.d(TAG, "Executing node: " + node.getClass().getSimpleName() + ", id=" + node.getId());
        // Check for cancellation
        synchronized (cancelObject) {
            if (cancelled) {
                CircleLog.d(TAG, "Execution cancelled at node: " + node.getClass().getName());
                lifecycleManageService.notifyFlowCancelled(flowId);
                return;
            }
        }

        // Not allow concurrency node execution
        NodeHandleResult nodeHandleResult;
        synchronized (this) {
            node.setFlowId(flowId);
            if (TextUtils.isEmpty(node.getId())) {
                node.setId(assignId());
            }

            lifecycleManageService.notifyPreExecuteNode(node);
            FlowNodeHandler handler = nodeHandlerManager.findNodeHandler(node);
            nodeHandleResult = handler.handleNode(node);
            lifecycleManageService.notifyPostExecuteNode(node, nodeHandleResult == null ? null : nodeHandleResult.getOutgoingToken());
            handler.callAfterHandled(node, nodeHandleResult);
        }

        // Find next nodes and execute
        if (nodeHandleResult != null) {
            List<FlowNode> nodes = nodeHandleResult.getNextNodes();
            if (nodes != null && nodes.size() > 0) {
                if (nodes.size() == 1) {
                    executeNode(nodes.get(0));
                } else {
                    for (final FlowNode n : nodes) {
                        schedulingService.executeAsyncJob(new Runnable() {
                            @Override
                            public void run() {
                                executeNode(n);
                            }
                        });
                    }
                }
            } else {
                CircleLog.d(TAG, "Next nodes are empty, ignored.");
            }
        } else {
            CircleLog.d(TAG, "Node handle result is null, ignored.");
        }
    }

    private String assignId() {
        return UUID.randomUUID().toString();
    }

}
