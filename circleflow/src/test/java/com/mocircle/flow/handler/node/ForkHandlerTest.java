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
import com.mocircle.flow.model.control.Fork;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class ForkHandlerTest {

    Fork f;
    SimpleActionNode in;
    SimpleActionNode out1;
    SimpleActionNode out2;
    SimpleActionNode out3;

    @Before
    public void setup() {
        f = new Fork();
        in = new SimpleActionNode(Token.TYPE_NORMAL);
        out1 = new SimpleActionNode(Token.TYPE_NORMAL);
        out2 = new SimpleActionNode(Token.TYPE_NORMAL);
        out3 = new SimpleActionNode(Token.TYPE_NORMAL);
    }

    @Test
    public void testIsSupported() {
        ForkHandler handler = new ForkHandler();
        Assert.assertTrue(handler.isSupported(f));
        Assert.assertFalse(handler.isSupported(in));
    }

    @Test
    public void testHandleNode() {
        ForkHandler handler = new ForkHandler();
        in.addOutgoingNode(f);
        f.addOutgoingNode(out1);
        f.addOutgoingNode(out2);
        f.addOutgoingNode(out3);
        NodeHandleResult result = handler.handleNode(f);
        Assert.assertEquals(3, result.getNextNodes().size());
    }

}
