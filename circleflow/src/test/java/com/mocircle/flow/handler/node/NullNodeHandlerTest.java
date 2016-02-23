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
import com.mocircle.flow.model.NullNode;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.action.SimpleActionNode;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class NullNodeHandlerTest {

    NullNode n;
    SimpleActionNode in1;
    SimpleActionNode out1;

    @Before
    public void setup() {
        n = new NullNode();
        in1 = new SimpleActionNode(Token.TYPE_SUCCESS);
        out1 = new SimpleActionNode(Token.TYPE_NORMAL);
    }

    @Test
    public void testIsSupported() {
        NullNodeHandler handler = new NullNodeHandler();
        Assert.assertTrue(handler.isSupported(n));
        Assert.assertFalse(handler.isSupported(out1));
    }

    @Test
    public void testHandleNode() {
        NullNodeHandler handler = new NullNodeHandler();
        in1.addOutgoingNode(n);
        n.addOutgoingNode(out1);
        NodeHandleResult result = handler.handleNode(n);
        Assert.assertNull(result);
    }

}
