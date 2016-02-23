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
import com.mocircle.flow.model.control.Join;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class JoinHandlerTest {

    Join j;
    SimpleActionNode in1;
    SimpleActionNode in2;
    SimpleActionNode in3;
    SimpleActionNode out;

    @Before
    public void setup() {
        j = new Join();
        in1 = new SimpleActionNode(Token.TYPE_NORMAL);
        in2 = new SimpleActionNode(Token.TYPE_NORMAL);
        in3 = new SimpleActionNode(Token.TYPE_NORMAL);
        out = new SimpleActionNode(Token.TYPE_NORMAL);
    }

    @Test
    public void testIsSupported() {
        JoinHandler handler = new JoinHandler();
        Assert.assertTrue(handler.isSupported(j));
        Assert.assertFalse(handler.isSupported(in1));
    }

    @Test
    public void testHandleNode() {
        JoinHandler handler = new JoinHandler();
        in1.addOutgoingNode(j);
        in2.addOutgoingNode(j);
        in3.addOutgoingNode(j);
        j.addOutgoingNode(out);
        NodeHandleResult result = handler.handleNode(j);
        Assert.assertNull(result);
        result = handler.handleNode(j);
        Assert.assertNull(result);
        result = handler.handleNode(j);
        Assert.assertEquals(1, result.getNextNodes().size());
        Assert.assertEquals(out, result.getNextNodes().get(0));
    }

}
