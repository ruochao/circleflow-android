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

package com.mocircle.flow.handler.node;

import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.service.FlowLifecycleManageService;

/**
 * A base handler for {@link FlowNodeHandler} implementations.
 */
public abstract class AbstractFlowNodeHandler implements FlowNodeHandler {

    protected FlowLifecycleManageService lifecycleManageService;

    @Override
    public void callAfterHandled(FlowNode node, NodeHandleResult result) {
    }

    @Override
    public void setLifecycleManageService(FlowLifecycleManageService service) {
        lifecycleManageService = service;
    }
}
