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

import com.mocircle.flow.BuildConfig;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.action.SimpleActionNode;
import com.mocircle.flow.model.control.FinalNode;
import com.mocircle.flow.service.FlowServiceBuilderMock;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class FinalNodeHandlerTest {

    FinalNode n;
    SimpleActionNode in;
    SimpleActionNode out;

    @Before
    public void setup() {
        n = new FinalNode();
        in = new SimpleActionNode(Token.TYPE_NORMAL);
        out = new SimpleActionNode(Token.TYPE_NORMAL);
        n.setFlowId("");
    }

    @Test
    public void testIsSupported() {
        FinalNodeHandler handler = new FinalNodeHandler();
        Assert.assertTrue(handler.isSupported(n));
        Assert.assertFalse(handler.isSupported(out));
    }

    @Test
    public void testHandleNode() {
        FinalNodeHandler handler = new FinalNodeHandler();
        handler.setLifecycleManageService(new FlowServiceBuilderMock().buildLifecycleManageService());
        in.addOutgoingNode(n);
        n.addOutgoingNode(out);
        NodeHandleResult result = handler.handleNode(n);
        Assert.assertNull(result.getNextNodes());
    }

}
