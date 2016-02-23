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
 * Executor that parses flow definition and handles flow nodes.
 */
public interface FlowExecutor {

    /**
     * Gets flow id.
     *
     * @return flow id
     */
    String getFlowId();

    /**
     * Gets flow definition.
     *
     * @return flow definition
     */
    FlowDefinition getDefinition();

    /**
     * Executes the flow.
     */
    void execute();

    /**
     * Executes the flow with initial data
     *
     * @param initData initial data
     */
    void execute(Map<String, Object> initData);

    /**
     * Cancel the flow execution.
     */
    void cancel();

    /**
     * Blocks until flow has ended or been cancelled.
     *
     * @return flow context
     */
    FlowContext waitForFinished();
}
