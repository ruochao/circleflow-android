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

import com.mocircle.flow.exception.FlowDefinitionException;
import com.mocircle.flow.model.action.ActionNode;
import com.mocircle.flow.model.control.ControlNode;
import com.mocircle.flow.service.FlowAndroidService;
import com.mocircle.flow.service.FlowLifecycleManageService;
import com.mocircle.flow.service.FlowLifecycleService;
import com.mocircle.flow.service.FlowSchedulingService;
import com.mocircle.flow.service.FlowServiceBuilder;
import com.mocircle.flow.service.FlowServiceBuilderImpl;
import com.mocircle.flow.service.FlowSignalService;

import java.util.HashMap;
import java.util.Map;


/**
 * CircleFlow is a light flow system. It simulates the flow diagram or activity diagram of UML,
 * separate the code piece or logic unit as different {@link ActionNode}, and than connect them with
 * {@link ControlNode}.
 * <p/>
 * If you want to control the flow operations, you should call {@link #getEngine()} to get {@link
 * FlowEngine} interface. If you want to listener for flow status change or history, you can call
 * {@link #getServices()}.
 */
public final class CircleFlow implements FlowEngine, FlowServices {

    private static final String TAG = "CircleFlow";
    private static CircleFlow instance;

    private FlowServiceBuilder serviceBuilder;
    private FlowSchedulingService schedulingService;
    private FlowLifecycleService lifecycleService;
    private FlowLifecycleManageService lifecycleManageService;
    private FlowSignalService signalService;
    private FlowAndroidService androidService;

    private Map<String, FlowExecutor> executorMap = new HashMap<>();

    private CircleFlow() {
    }

    /**
     * Gets {@link FlowEngine} implementation to control flow behaviors.
     *
     * @return {@link FlowEngine} instance
     */
    public static FlowEngine getEngine() {
        return getInternalInstance();
    }

    /**
     * Gets {@link FlowServices} implementation for getting concrete services.
     *
     * @return {@link FlowServices} instance
     */
    public static FlowServices getServices() {
        return getInternalInstance();
    }

    /**
     * Gets internal instance.
     *
     * @return instance
     */
    static CircleFlow getInternalInstance() {
        if (instance == null) {
            synchronized (CircleFlow.class) {
                if (instance == null) {
                    instance = new CircleFlow();
                }
            }
        }
        return instance;
    }

    @Override
    public void injectServiceBuilder(FlowServiceBuilder serviceBuilder) {
        this.serviceBuilder = serviceBuilder;
        schedulingService = null;
        lifecycleService = null;
        signalService = null;
        androidService = null;
    }

    @Override
    public FlowLifecycleService getLifecycleService() {
        if (lifecycleService == null) {
            lifecycleService = getServiceBuilder().buildLifecycleService();
        }
        return lifecycleService;
    }

    @Override
    public FlowSignalService getSignalService() {
        if (signalService == null) {
            signalService = getServiceBuilder().buildSignalService();
        }
        return signalService;
    }

    @Override
    public FlowAndroidService getAndroidService() {
        if (androidService == null) {
            androidService = getServiceBuilder().buildAndroidService();
        }
        return androidService;
    }

    @Override
    public FlowExecutor prepareFlow(FlowDefinition flowDef, Map<String, Object> data) {
        // Warm the executor
        FlowExecutor executor = new FlowExecutorImpl(flowDef, data);
        synchronized (executorMap) {
            executorMap.put(executor.getFlowId(), executor);
        }
        return executor;
    }

    @Override
    public FlowExecutor prepareFlow(Class<? extends FlowDefinition> flowDefClass, Map<String, Object> data) {
        try {
            FlowDefinition flowDef = flowDefClass.newInstance();
            return prepareFlow(flowDef, data);
        } catch (InstantiationException e) {
            throw new FlowDefinitionException("Cannot create flow definition object", e);
        } catch (IllegalAccessException e) {
            throw new FlowDefinitionException("Cannot create flow definition object", e);
        }
    }

    @Override
    public FlowExecutor prepareFlow(String flowDefClassName, Map<String, Object> data) {
        try {
            Class<? extends FlowDefinition> clazz = (Class<? extends FlowDefinition>) Class.forName(flowDefClassName);
            return prepareFlow(clazz, data);
        } catch (ClassNotFoundException e) {
            throw new FlowDefinitionException("Cannot create flow definition object", e);
        } catch (ClassCastException e) {
            throw new FlowDefinitionException("Flow definition class does not implement FlowDefinition interface", e);
        }
    }

    @Override
    public FlowExecutor prepareFlow(FlowDefinition flowDefinition) {
        return prepareFlow(flowDefinition, null);
    }

    @Override
    public FlowExecutor prepareFlow(Class<? extends FlowDefinition> flowDefinitionClass) {
        return prepareFlow(flowDefinitionClass, null);
    }

    @Override
    public FlowExecutor prepareFlow(String flowDefinitionClassName) {
        return prepareFlow(flowDefinitionClassName, null);
    }

    @Override
    public void executeFlow(FlowDefinition flowDefinition, Map<String, Object> data) {
        prepareFlow(flowDefinition, data).execute();
    }

    @Override
    public void executeFlow(Class<? extends FlowDefinition> flowDefinitionClass, Map<String, Object> data) {
        prepareFlow(flowDefinitionClass, data).execute();
    }

    @Override
    public void executeFlow(String flowDefinitionClassName, Map<String, Object> data) {
        prepareFlow(flowDefinitionClassName, data).execute();
    }

    @Override
    public void executeFlow(FlowDefinition flowDefinition) {
        executeFlow(flowDefinition, null);
    }

    @Override
    public void executeFlow(Class<? extends FlowDefinition> flowDefinitionClass) {
        executeFlow(flowDefinitionClass, null);
    }

    @Override
    public void executeFlow(String flowDefinitionClassName) {
        executeFlow(flowDefinitionClassName, null);
    }

    @Override
    public void cancelFlow(String flowId) {
        synchronized (executorMap) {
            FlowExecutor executor = executorMap.get(flowId);
            if (executor != null) {
                executor.cancel();
            }
        }
    }

    /**
     * Gets internal lifecycle management service.
     *
     * @return lifecycle management service
     */
    FlowLifecycleManageService getLifecycleManageService() {
        if (lifecycleManageService == null) {
            lifecycleManageService = getServiceBuilder().buildLifecycleManageService();
        }
        return lifecycleManageService;
    }

    /**
     * Gets internal scheduling service.
     *
     * @return scheduling service
     */
    FlowSchedulingService getSchedulingService() {
        if (schedulingService == null) {
            schedulingService = getServiceBuilder().buildSchedulingService();
        }
        return schedulingService;
    }

    private FlowServiceBuilder getServiceBuilder() {
        if (serviceBuilder == null) {
            serviceBuilder = FlowServiceBuilderImpl.create();
        }
        return serviceBuilder;
    }

}
