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

import java.util.Map;

/**
 * A interface that defines the basic flow operations. e.g. execution, cancellation.
 */
public interface FlowEngine {

    /**
     * Prepares a flow execution with initial data, in order to get more information before
     * executing. (e.g. flow id)
     *
     * @param flowDef flow definition object
     * @param data    initial data
     * @return flow executor object
     */
    FlowExecutor prepareFlow(FlowDefinition flowDef, Map<String, Object> data);

    /**
     * Prepares a flow execution with initial data, in order to get more information before
     * executing. (e.g. flow id)
     *
     * @param flowDefClass class for {@link FlowDefinition}
     * @param data         initial data
     * @return flow executor object
     */
    FlowExecutor prepareFlow(Class<? extends FlowDefinition> flowDefClass, Map<String, Object> data);

    /**
     * Prepares a flow execution with initial data, in order to get more information before
     * executing. (e.g. flow id)
     *
     * @param flowDefClassName class name for {@link FlowDefinition}
     * @param data             initial data
     * @return flow executor object
     */
    FlowExecutor prepareFlow(String flowDefClassName, Map<String, Object> data);

    /**
     * Prepares a flow execution, in order to get more information before executing. (e.g. flow id)
     *
     * @param flowDefinition flow definition object
     * @return flow executor object
     */
    FlowExecutor prepareFlow(FlowDefinition flowDefinition);

    /**
     * Prepares a flow execution, in order to get more information before executing. (e.g. flow id)
     *
     * @param flowDefinitionClass class for {@link FlowDefinition}
     * @return flow executor object
     */
    FlowExecutor prepareFlow(Class<? extends FlowDefinition> flowDefinitionClass);

    /**
     * Prepares a flow execution, in order to get more information before executing. (e.g. flow id)
     *
     * @param flowDefinitionClassName class name for {@link FlowDefinition}
     * @return flow executor object
     */
    FlowExecutor prepareFlow(String flowDefinitionClassName);

    /**
     * Executes a flow with initial data.
     *
     * @param flowDefinition flow definition object
     * @param data           initial data
     */
    void executeFlow(FlowDefinition flowDefinition, Map<String, Object> data);

    /**
     * Executes a flow with initial data.
     *
     * @param flowDefinitionClass class for {@link FlowDefinition}
     * @param data                initial data
     */
    void executeFlow(Class<? extends FlowDefinition> flowDefinitionClass, Map<String, Object> data);

    /**
     * Executes a flow with initial data.
     *
     * @param flowDefinitionClassName class name for {@link FlowDefinition}
     * @param data                    initial data
     */
    void executeFlow(String flowDefinitionClassName, Map<String, Object> data);

    /**
     * Executes a flow.
     *
     * @param flowDefinition flow definition object
     */
    void executeFlow(FlowDefinition flowDefinition);

    /**
     * Executes a flow.
     *
     * @param flowDefinitionClass class for {@link FlowDefinition}
     */
    void executeFlow(Class<? extends FlowDefinition> flowDefinitionClass);

    /**
     * Executes a flow.
     *
     * @param flowDefinitionClassName class name for {@link FlowDefinition}
     */
    void executeFlow(String flowDefinitionClassName);

    /**
     * Cancels a flow execution.
     *
     * @param flowId flow id
     */
    void cancelFlow(String flowId);

}
