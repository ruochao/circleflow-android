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

import com.mocircle.flow.flows.Flow1;
import com.mocircle.flow.service.FlowLifecycleManageService;
import com.mocircle.flow.service.FlowLifecycleManageServiceVerifyOrderMock;
import com.mocircle.flow.service.FlowLifecycleService;
import com.mocircle.flow.service.FlowServiceBuilderMock;
import com.mocircle.flow.test.TestUtils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class FlowExecutorImplTest {

    @Before
    public void setup() {
        TestUtils.resetSingletonInstance(CircleFlow.class);
    }

    @Test
    public void testExecuteOrder() {
        FlowLifecycleManageServiceVerifyOrderMock service = new FlowLifecycleManageServiceVerifyOrderMock();
        CircleFlow.getServices().injectServiceBuilder(
                new FlowServiceBuilderMock()
                        .replace((FlowLifecycleManageService) service)
                        .replace((FlowLifecycleService) service)
                        .replaceWithSyncSchedulingService());

        String[] order = new String[]{"InitialNode", "ActionNodeS1", "Decision", "ActionNodeN1", "FinalNode"};
        FlowExecutorImpl executor = new FlowExecutorImpl(new Flow1(), null);
        String flowId = executor.getFlowId();
        Assert.assertNotNull(flowId);
        service.setExpectedFlowId(flowId);
        service.setExpectedOrder(order);
        executor.execute();

        Assert.assertTrue(service.isStartedCalled());
        Assert.assertTrue(service.isEndedCalled());
    }


}
