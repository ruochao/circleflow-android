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
import com.mocircle.flow.model.action.SimpleActionNode;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.control.Merge;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class MergeHandlerTest {

    Merge m;
    SimpleActionNode in1;
    SimpleActionNode in2;
    SimpleActionNode in3;
    SimpleActionNode out;

    @Before
    public void setup() {
        m = new Merge();
        in1 = new SimpleActionNode(Token.TYPE_NORMAL);
        in2 = new SimpleActionNode(Token.TYPE_NORMAL);
        in3 = new SimpleActionNode(Token.TYPE_NORMAL);
        out = new SimpleActionNode(Token.TYPE_NORMAL);
    }

    @Test
    public void testIsSupported() {
        MergeHandler handler = new MergeHandler();
        Assert.assertTrue(handler.isSupported(m));
        Assert.assertFalse(handler.isSupported(in1));
    }

    @Test
    public void testHandleNode() {
        MergeHandler handler = new MergeHandler();
        in1.addOutgoingNode(m);
        in2.addOutgoingNode(m);
        in3.addOutgoingNode(m);
        m.addOutgoingNode(out);
        NodeHandleResult result = handler.handleNode(m);
        Assert.assertEquals(1, result.getNextNodes().size());
        Assert.assertEquals(out, result.getNextNodes().get(0));
        result = handler.handleNode(m);
        Assert.assertNull(result);
        result = handler.handleNode(m);
        Assert.assertNull(result);
    }

}
