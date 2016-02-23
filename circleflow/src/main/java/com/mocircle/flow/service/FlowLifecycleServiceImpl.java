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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.mocircle.flow.FlowContext;
import com.mocircle.flow.FlowDefinition;
import com.mocircle.flow.FlowHistory;
import com.mocircle.flow.listener.FlowLifecycleListener;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation for {@link FlowLifecycleService} and {@link FlowLifecycleManageService}.
 */
public class FlowLifecycleServiceImpl implements FlowLifecycleService, FlowLifecycleManageService {

    static class MyHandler extends Handler {

        private FlowLifecycleServiceImpl service;

        public MyHandler(Looper looper, FlowLifecycleServiceImpl service) {
            super(looper);
            this.service = service;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FLOW_STARTED:
                    Object[] startResults = (Object[]) msg.obj;
                    service.notifyListenersFlowStarted((String) startResults[0], (FlowDefinition) startResults[1]);
                    break;
                case MSG_FLOW_PRE_EXECUTE:
                    service.notifyListenersPreExecuteNode((FlowNode) msg.obj);
                    break;
                case MSG_FLOW_POST_EXECUTE:
                    Object[] postResults = (Object[]) msg.obj;
                    service.notifyListenersPostExecuteNode((FlowNode) postResults[0], (Token) postResults[1]);
                    break;
                case MSG_FLOW_ENDED:
                    service.notifyListenersFlowEnded(msg.obj.toString());
                    break;
                case MSG_FLOW_CANCELLED:
                    service.notifyListenersFlowCancelled(msg.obj.toString());
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private static final int MSG_FLOW_STARTED = 1;
    private static final int MSG_FLOW_PRE_EXECUTE = 2;
    private static final int MSG_FLOW_POST_EXECUTE = 3;
    private static final int MSG_FLOW_ENDED = 4;
    private static final int MSG_FLOW_CANCELLED = 5;

    private List<FlowLifecycleListener> globalFlowListeners = new ArrayList<>();
    private Map<String, List<FlowLifecycleListener>> flowListeners = new HashMap<>();

    private final Handler handler = new MyHandler(Looper.getMainLooper(), this);
    private final Map<String, FlowContext> flowContextMap = new HashMap<>();
    private final Map<String, FlowHistory> flowHistoryMap = new HashMap<>();

    private FlowLifecycleListener contextListener = new FlowLifecycleListener() {

        @Override
        public void onFlowStarted(String flowId, FlowDefinition flowDefinition) {
            synchronized (flowContextMap) {
                FlowContext context = flowContextMap.get(flowId);
                if (context != null) {
                    context.setCurrentStatus(FlowContext.STATUS_STARTED);
                    context.getCurrentNodes().clear();
                    context.setOutput(null);
                }
            }
        }

        @Override
        public void preExecuteNode(FlowNode node) {
            synchronized (flowContextMap) {
                FlowContext context = flowContextMap.get(node.getFlowId());
                if (context != null) {
                    context.setCurrentStatus(FlowContext.STATUS_RUNNING);
                    context.getCurrentNodes().add(node);
                    context.setOutput(null);
                }
            }
        }

        @Override
        public void postExecuteNode(FlowNode node, Token token) {
            synchronized (flowContextMap) {
                FlowContext context = flowContextMap.get(node.getFlowId());
                if (context != null) {
                    context.setCurrentStatus(FlowContext.STATUS_RUNNING);
                    context.getCurrentNodes().remove(node);
                    context.setOutput(token != null ? token.getData() : null);
                }
            }
        }

        @Override
        public void onFlowEnded(String flowId) {
            synchronized (flowContextMap) {
                FlowContext context = flowContextMap.get(flowId);
                if (context != null) {
                    context.setCurrentStatus(FlowContext.STATUS_ENDED);
                }
            }
        }

        @Override
        public void onFlowCancelled(String flowId) {
            synchronized (flowContextMap) {
                FlowContext context = flowContextMap.get(flowId);
                if (context != null) {
                    context.setCurrentStatus(FlowContext.STATUS_CANCELLED);
                }
            }
        }
    };

    private FlowLifecycleListener historyListener = new FlowLifecycleListener() {

        @Override
        public void onFlowStarted(String flowId, FlowDefinition flowDefinition) {
            FlowHistory history;
            synchronized (flowHistoryMap) {
                history = flowHistoryMap.get(flowId);
            }
            if (history != null) {
                history.setStartTimestamp(System.currentTimeMillis());
                history.setEndTimestamp(0);
                if (history.getExecutionOrder() != null) {
                    history.getExecutionOrder().clear();
                }
            }
        }

        @Override
        public void preExecuteNode(FlowNode node) {
            synchronized (flowHistoryMap) {
                FlowHistory history = flowHistoryMap.get(node.getFlowId());
                if (history != null) {
                    if (history.getExecutionOrder() == null) {
                        history.setExecutionOrder(new ArrayList<FlowHistory.NodeHistory>());
                    }
                    FlowHistory.NodeHistory item = new FlowHistory.NodeHistory();
                    item.setNodeId(node.getId());
                    item.setNodeClass(node.getClass());
                    item.setBeforeTimestamp(System.currentTimeMillis());
                    history.getExecutionOrder().add(item);
                }
            }
        }

        @Override
        public void postExecuteNode(FlowNode node, Token token) {
            synchronized (flowHistoryMap) {
                FlowHistory history = flowHistoryMap.get(node.getFlowId());
                if (history != null) {
                    List<FlowHistory.NodeHistory> items = history.getExecutionOrder();
                    for (int i = 0; i < items.size(); i++) {
                        FlowHistory.NodeHistory item = items.get(items.size() - i - 1);
                        if (item.getNodeId().equals(node.getId())) {
                            item.setAfterTimestamp(System.currentTimeMillis());
                            item.setTokenAfterExecuted(token != null ? (Token) token.clone() : null);
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public void onFlowEnded(String flowId) {
            FlowHistory history;
            synchronized (flowHistoryMap) {
                history = flowHistoryMap.get(flowId);
            }
            if (history != null) {
                history.setEndTimestamp(System.currentTimeMillis());
            }
        }

        @Override
        public void onFlowCancelled(String flowId) {
            FlowHistory history;
            synchronized (flowHistoryMap) {
                history = flowHistoryMap.get(flowId);
            }
            if (history != null) {
                history.setEndTimestamp(System.currentTimeMillis());
            }
        }
    };

    FlowLifecycleServiceImpl() {
        addGlobalLifecycleListener(contextListener);
        addGlobalLifecycleListener(historyListener);
    }

    @Override
    public void addGlobalLifecycleListener(FlowLifecycleListener listener) {
        synchronized (globalFlowListeners) {
            if (!globalFlowListeners.contains(listener)) {
                globalFlowListeners.add(listener);
            }
        }
    }

    @Override
    public void removeGlobalLifecycleListener(FlowLifecycleListener listener) {
        synchronized (globalFlowListeners) {
            globalFlowListeners.remove(listener);
        }
    }

    @Override
    public void addLifecycleListener(String flowId, FlowLifecycleListener listener) {
        synchronized (flowListeners) {
            List<FlowLifecycleListener> listeners = flowListeners.get(flowId);
            if (listeners == null) {
                listeners = new ArrayList<>();
                flowListeners.put(flowId, listeners);
            }
            listeners.add(listener);
        }
    }

    @Override
    public void removeLifecycleListener(String flowId, FlowLifecycleListener listener) {
        synchronized (flowListeners) {
            List<FlowLifecycleListener> listeners = flowListeners.get(flowId);
            if (listeners != null) {
                listeners.remove(listener);
            }
        }
    }

    @Override
    public void removeLifecycleListener(String flowId) {
        synchronized (flowListeners) {
            flowListeners.remove(flowId);
        }
    }

    @Override
    public void notifyFlowStarted(String flowId, FlowDefinition flowDefinition) {
        Message msg = handler.obtainMessage(MSG_FLOW_STARTED);
        msg.obj = new Object[]{flowId, flowDefinition};
        handler.sendMessage(msg);
    }

    @Override
    public void notifyPreExecuteNode(FlowNode node) {
        Message msg = handler.obtainMessage(MSG_FLOW_PRE_EXECUTE);
        msg.obj = node;
        handler.sendMessage(msg);
    }

    @Override
    public void notifyPostExecuteNode(FlowNode node, Token token) {
        Message msg = handler.obtainMessage(MSG_FLOW_POST_EXECUTE);
        msg.obj = new Object[]{node, token};
        handler.sendMessage(msg);
    }

    @Override
    public void notifyFlowEnded(String flowId) {
        Message msg = handler.obtainMessage(MSG_FLOW_ENDED);
        msg.obj = flowId;
        handler.sendMessage(msg);
    }

    @Override
    public void notifyFlowCancelled(String flowId) {
        Message msg = handler.obtainMessage(MSG_FLOW_CANCELLED);
        msg.obj = flowId;
        handler.sendMessage(msg);
    }

    @Override
    public FlowContext getFlowContext(String flowId) {
        synchronized (flowContextMap) {
            return flowContextMap.get(flowId);
        }
    }

    @Override
    public FlowHistory getFlowHistory(String flowId) {
        synchronized (flowHistoryMap) {
            return flowHistoryMap.get(flowId);
        }
    }

    @Override
    public void createLifecycleContext(String flowId) {
        // Create for context
        FlowContext context = new FlowContext(flowId);
        context.setCurrentStatus(FlowContext.STATUS_IDLE);
        synchronized (flowContextMap) {
            flowContextMap.put(flowId, context);
        }

        // Create for history
        FlowHistory history = new FlowHistory(flowId);
        synchronized (flowHistoryMap) {
            flowHistoryMap.put(flowId, history);
        }
    }

    private void notifyListenersFlowStarted(String flowId, FlowDefinition flowDefinition) {
        synchronized (globalFlowListeners) {
            for (FlowLifecycleListener listener : globalFlowListeners) {
                listener.onFlowStarted(flowId, flowDefinition);
            }
        }
        synchronized (flowListeners) {
            List<FlowLifecycleListener> listeners = flowListeners.get(flowId);
            if (listeners != null) {
                for (FlowLifecycleListener listener : listeners) {
                    listener.onFlowStarted(flowId, flowDefinition);
                }
            }
        }
    }

    private void notifyListenersPreExecuteNode(FlowNode node) {
        synchronized (globalFlowListeners) {
            for (FlowLifecycleListener listener : globalFlowListeners) {
                listener.preExecuteNode(node);
            }
        }
        synchronized (flowListeners) {
            List<FlowLifecycleListener> listeners = flowListeners.get(node.getFlowId());
            if (listeners != null) {
                for (FlowLifecycleListener listener : listeners) {
                    listener.preExecuteNode(node);
                }
            }
        }
    }

    private void notifyListenersPostExecuteNode(FlowNode node, Token token) {
        synchronized (globalFlowListeners) {
            for (FlowLifecycleListener listener : globalFlowListeners) {
                listener.postExecuteNode(node, token);
            }
        }
        synchronized (flowListeners) {
            List<FlowLifecycleListener> listeners = flowListeners.get(node.getFlowId());
            if (listeners != null) {
                for (FlowLifecycleListener listener : listeners) {
                    listener.postExecuteNode(node, token);
                }
            }
        }
    }

    private void notifyListenersFlowEnded(String flowId) {
        synchronized (globalFlowListeners) {
            for (FlowLifecycleListener listener : globalFlowListeners) {
                listener.onFlowEnded(flowId);
            }
        }
        synchronized (flowListeners) {
            List<FlowLifecycleListener> listeners = flowListeners.get(flowId);
            if (listeners != null) {
                for (FlowLifecycleListener listener : listeners) {
                    listener.onFlowEnded(flowId);
                }
            }
        }
    }

    private void notifyListenersFlowCancelled(String flowId) {
        synchronized (globalFlowListeners) {
            for (FlowLifecycleListener listener : globalFlowListeners) {
                listener.onFlowCancelled(flowId);
            }
        }
        synchronized (flowListeners) {
            List<FlowLifecycleListener> listeners = flowListeners.get(flowId);
            if (listeners != null) {
                for (FlowLifecycleListener listener : listeners) {
                    listener.onFlowCancelled(flowId);
                }
            }
        }
    }

}
