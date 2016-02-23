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
import com.mocircle.flow.flows.Flow1;
import com.mocircle.flow.listener.FlowLifecycleListenerAdapter;
import com.mocircle.flow.model.control.FinalNode;
import com.mocircle.flow.model.control.InitialNode;
import com.mocircle.flow.service.FlowServiceBuilderMock;
import com.mocircle.flow.test.CallbackVerify;
import com.mocircle.flow.test.DelayThread;
import com.mocircle.flow.test.TestUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class CircleFlowTest {

    class BadFlow1 implements FlowDefinition {

        private BadFlow1() {
        }

        @Override
        public InitialNode getDefinition() {
            return null;
        }
    }

    class BadFlow2 implements FlowDefinition {

        private BadFlow2(int i) {
        }

        @Override
        public InitialNode getDefinition() {
            return null;
        }
    }

    @Before
    public void setup() {
        TestUtils.resetSingletonInstance(CircleFlow.class);
        CircleFlow.getServices().injectServiceBuilder(new FlowServiceBuilderMock().replaceWithSyncSchedulingService());
    }

    @Test
    public void testGetService() {
        Assert.assertNotNull(CircleFlow.getInternalInstance().getAndroidService());
        Assert.assertNotNull(CircleFlow.getInternalInstance().getLifecycleService());
        Assert.assertNotNull(CircleFlow.getInternalInstance().getLifecycleManageService());
        Assert.assertNotNull(CircleFlow.getInternalInstance().getSchedulingService());
        Assert.assertNotNull(CircleFlow.getInternalInstance().getSignalService());
    }

    @Test
    public void testPrepareFlowSuccessfulCase() {
        Map<String, Object> data = new HashMap<>();
        try {
            FlowExecutor executor = CircleFlow.getEngine().prepareFlow(new Flow1());
            Assert.assertNotNull(executor);
            executor = CircleFlow.getEngine().prepareFlow(new Flow1(), data);
            Assert.assertNotNull(executor);
        } catch (FlowDefinitionException e) {
            Assert.fail();
        }
        try {
            FlowExecutor executor = CircleFlow.getEngine().prepareFlow(Flow1.class);
            Assert.assertNotNull(executor);
            executor = CircleFlow.getEngine().prepareFlow(Flow1.class, data);
            Assert.assertNotNull(executor);
        } catch (FlowDefinitionException e) {
            Assert.fail();
        }
        try {
            FlowExecutor executor = CircleFlow.getEngine().prepareFlow("com.mocircle.flow.flows.Flow1");
            Assert.assertNotNull(executor);
            executor = CircleFlow.getEngine().prepareFlow("com.mocircle.flow.flows.Flow1", data);
            Assert.assertNotNull(executor);
        } catch (FlowDefinitionException e) {
            Assert.fail();
        }
    }

    @Test
    public void testPrepareFlowFailedCase() {
        try {
            CircleFlow.getEngine().prepareFlow(BadFlow1.class);
            Assert.fail();
        } catch (FlowDefinitionException e) {
        }
        try {
            CircleFlow.getEngine().prepareFlow(BadFlow2.class);
            Assert.fail();
        } catch (FlowDefinitionException e) {
        }
        try {
            CircleFlow.getEngine().prepareFlow("com.mocircle.flow.flows.ActionNodeC1");
            Assert.fail();
        } catch (FlowDefinitionException e) {
        }
        try {
            Map<String, Object> data = new HashMap<>();
            CircleFlow.getEngine().prepareFlow("com.mocircle.flow.flows.ActionNodeC1", data);
            Assert.fail();
        } catch (FlowDefinitionException e) {
        }

    }

    @Test
    public void testListener() {
        CircleFlow engine = CircleFlow.getInternalInstance();
        final CallbackVerify callback = new CallbackVerify();
        engine.getLifecycleService().addGlobalLifecycleListener(new FlowLifecycleListenerAdapter() {
            @Override
            public void onFlowEnded(String flowId) {
                callback.call();
            }
        });
        FlowExecutor executor = engine.prepareFlow(Flow1.class, null);
        executor.execute();
        Assert.assertTrue(callback.isCalled());
    }

    @Test
    public void testWaitForFinished() {
        CircleFlow engine = CircleFlow.getInternalInstance();
        FlowExecutor executor = engine.prepareFlow(new Flow1());
        executor.execute();
        FlowContext context = executor.waitForFinished();
        Assert.assertEquals(FlowContext.STATUS_ENDED, context.getCurrentStatus());
        Assert.assertEquals(0, context.getCurrentNodes().size());
    }

    @Test
    public void testWaitForFinishedDelayed() {
        CircleFlow engine = CircleFlow.getInternalInstance();
        engine.injectServiceBuilder(new FlowServiceBuilderMock());

        FlowExecutor executor = engine.prepareFlow(new Flow1());
        executor.execute();
        FlowContext context = engine.getLifecycleService().getFlowContext(executor.getFlowId());
        Assert.assertEquals(FlowContext.STATUS_STARTED, context.getCurrentStatus());

        DelayThread.delayRun(new Runnable() {
            @Override
            public void run() {
                ShadowLooper.unPauseMainLooper();
            }
        });

        context = executor.waitForFinished();
        Assert.assertEquals(FlowContext.STATUS_ENDED, context.getCurrentStatus());
        Assert.assertEquals(0, context.getCurrentNodes().size());
    }

}
