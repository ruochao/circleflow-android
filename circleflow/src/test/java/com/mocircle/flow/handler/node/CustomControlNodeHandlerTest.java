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
import com.mocircle.flow.model.control.CustomControlNode;
import com.mocircle.flow.model.control.Decision;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class CustomControlNodeHandlerTest {

    class Custom1Node extends CustomControlNode {

        @Override
        public String[] getAcceptedTokenTypes() {
            return new String[0];
        }
    }

    class Custom2Node extends CustomControlNode {

        @Override
        public String[] getAcceptedTokenTypes() {
            return new String[]{Token.TYPE_FAILURE};
        }
    }

    class Custom3Node extends CustomControlNode {

        @Override
        public String[] getAcceptedTokenTypes() {
            return new String[]{Token.TYPE_SUCCESS, Token.TYPE_FAILURE};
        }
    }

    Custom1Node c1;
    Custom2Node c2;
    Custom3Node c3;
    SimpleActionNode out1;
    SimpleActionNode out2;

    @Before
    public void setup() {
        out1 = new SimpleActionNode(Token.TYPE_NORMAL);
        out2 = new SimpleActionNode(Token.TYPE_NORMAL);
        c1 = new Custom1Node();
        c2 = new Custom2Node();
        c3 = new Custom3Node();
    }

    @Test
    public void testSupport() {
        CustomControlNodeHandler handler = new CustomControlNodeHandler();
        Assert.assertTrue(handler.isSupported(c1));
        Assert.assertFalse(handler.isSupported(new SimpleActionNode(Token.TYPE_NORMAL)));
        Assert.assertFalse(handler.isSupported(new Decision()));
    }

    @Test
    public void testHandleNodeWithEmptyToken() {
        c1.addOutgoingNode(Token.TYPE_SUCCESS, out1);
        c1.addOutgoingNode(Token.TYPE_FAILURE, out2);

        CustomControlNodeHandler handler = new CustomControlNodeHandler();
        NodeHandleResult result = handler.handleNode(c1);
        Assert.assertEquals(1, result.getNextNodes().size());
        Assert.assertEquals(NullNode.class, result.getNextNodes().get(0).getClass());
    }

    @Test
    public void testHandleNodeWithOneToken() {
        c2.addOutgoingNode(Token.TYPE_SUCCESS, out1);
        c2.addOutgoingNode(Token.TYPE_FAILURE, out2);

        CustomControlNodeHandler handler = new CustomControlNodeHandler();
        NodeHandleResult result = handler.handleNode(c2);
        Assert.assertEquals(1, result.getNextNodes().size());
        Assert.assertEquals(out2, result.getNextNodes().get(0));
    }

    @Test
    public void testHandleNodeWithTwoTokens() {
        c3.addOutgoingNode(Token.TYPE_SUCCESS, out1);
        c3.addOutgoingNode(Token.TYPE_FAILURE, out2);

        CustomControlNodeHandler handler = new CustomControlNodeHandler();
        NodeHandleResult result = handler.handleNode(c3);
        Assert.assertEquals(2, result.getNextNodes().size());
    }

}
