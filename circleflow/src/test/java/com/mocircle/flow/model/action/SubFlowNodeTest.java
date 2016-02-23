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

import com.mocircle.flow.BuildConfig;
import com.mocircle.flow.CircleFlow;
import com.mocircle.flow.FlowDefinition;
import com.mocircle.flow.flows.Flow1;
import com.mocircle.flow.listener.FlowLifecycleListenerAdapter;
import com.mocircle.flow.service.FlowServiceBuilderMock;
import com.mocircle.flow.test.CallbackVerify;
import com.mocircle.flow.test.TestUtils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class SubFlowNodeTest {

    @Before
    public void setup() {
        TestUtils.resetSingletonInstance(CircleFlow.class);
        CircleFlow.getServices().injectServiceBuilder(new FlowServiceBuilderMock().replaceWithSyncSchedulingService());
    }

    @Test
    public void testConstructor() {
        SubFlowNode node;

        node = new SubFlowNode(new Flow1());
        Assert.assertEquals(Flow1.class, node.getFlowDefinition().getClass());

        node = new SubFlowNode(new Flow1(), true);
        Assert.assertEquals(Flow1.class, node.getFlowDefinition().getClass());
        Assert.assertEquals(true, node.isAsynchronous());
    }

    @Test
    public void testExecution() {
        final CallbackVerify callback = new CallbackVerify();
        CircleFlow.getServices().getLifecycleService().addGlobalLifecycleListener(new FlowLifecycleListenerAdapter() {
            @Override
            public void onFlowStarted(String flowId, FlowDefinition flowDefinition) {
                callback.call();
                Assert.assertEquals(Flow1.class, flowDefinition.getClass());
            }
        });

        SubFlowNode node = new SubFlowNode(new Flow1(), false);
        node.execute();
        Assert.assertTrue(callback.isCalled());
    }

}
