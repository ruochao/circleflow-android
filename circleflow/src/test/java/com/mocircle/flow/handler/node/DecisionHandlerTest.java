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
import com.mocircle.flow.exception.FlowDefinitionException;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.action.SimpleActionNode;
import com.mocircle.flow.model.control.Decision;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class DecisionHandlerTest {

    Decision d;
    SimpleActionNode out1;
    SimpleActionNode out2;

    @Before
    public void setup() {
        out1 = new SimpleActionNode(Token.TYPE_NORMAL);
        out2 = new SimpleActionNode(Token.TYPE_NORMAL);
        d = new Decision();
    }

    @Test
    public void testIsSupported() {
        DecisionHandler handler = new DecisionHandler();
        Assert.assertTrue(handler.isSupported(d));
        Assert.assertFalse(handler.isSupported(out1));
    }

    @Test
    public void testSuccessToken() {
        DecisionHandler handler = new DecisionHandler();
        d.addOutgoingNode(Token.TYPE_SUCCESS, out1);
        d.addOutgoingNode(Token.TYPE_FAILURE, out2);
        d.receiveToken(null, new Token(Token.TYPE_SUCCESS));

        NodeHandleResult result = handler.handleNode(d);
        List<FlowNode> nextNodes = result.getNextNodes();
        Assert.assertEquals(1, nextNodes.size());
        Assert.assertEquals(out1, nextNodes.get(0));

        Assert.assertNotNull(result.getOutgoingToken());
        Assert.assertEquals(Token.TYPE_NORMAL, result.getOutgoingToken().getTokenType());
    }

    @Test
    public void testFailureToken() {
        DecisionHandler handler = new DecisionHandler();
        d.addOutgoingNode(Token.TYPE_SUCCESS, out1);
        d.addOutgoingNode(Token.TYPE_FAILURE, out2);
        d.receiveToken(null, new Token(Token.TYPE_FAILURE));

        NodeHandleResult result = handler.handleNode(d);
        List<FlowNode> nextNodes = result.getNextNodes();
        Assert.assertEquals(1, nextNodes.size());
        Assert.assertEquals(out2, nextNodes.get(0));

        Assert.assertNotNull(result.getOutgoingToken());
        Assert.assertEquals(Token.TYPE_NORMAL, result.getOutgoingToken().getTokenType());
    }


    @Test
    public void testNodeNotFound() {
        DecisionHandler handler = new DecisionHandler();
        d.addOutgoingNode(Token.TYPE_SUCCESS, out1);
        d.addOutgoingNode(Token.TYPE_FAILURE, out2);
        d.receiveToken(null, new Token(Token.TYPE_CANCEL));

        NodeHandleResult result = handler.handleNode(d);
        List<FlowNode> nextNodes = result.getNextNodes();
        Assert.assertEquals(1, nextNodes.size());
        Assert.assertEquals("NullNode", nextNodes.get(0).getClass().getSimpleName());

        Assert.assertNotNull(result.getOutgoingToken());
        Assert.assertEquals(Token.TYPE_NORMAL, result.getOutgoingToken().getTokenType());
    }


    @Test
    public void testException() {
        DecisionHandler handler = new DecisionHandler();
        SimpleActionNode in1 = new SimpleActionNode(Token.TYPE_NORMAL);
        SimpleActionNode in2 = new SimpleActionNode(Token.TYPE_NORMAL);
        in1.setId("1");
        in2.setId("2");
        d.receiveToken(in1, new Token(Token.TYPE_SUCCESS));
        d.receiveToken(in2, new Token(Token.TYPE_FAILURE));
        try {
            handler.handleNode(d);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(FlowDefinitionException.class, e.getClass());
        }
    }

    @Test
    public void testTokenPass() {
        DecisionHandler handler = new DecisionHandler();
        Map<String, Object> data = new HashMap<>();
        data.put("test1", 1);
        data.put("test2", "2");
        data.put("test3", 3.0D);
        d.addOutgoingNode(Token.TYPE_SUCCESS, out1);
        d.addOutgoingNode(Token.TYPE_FAILURE, out2);
        d.receiveToken(null, new Token(Token.TYPE_SUCCESS, data));
        NodeHandleResult result = handler.handleNode(d);
        List<FlowNode> nextNodes = result.getNextNodes();
        FlowNode out = nextNodes.get(0);
        List<Token> tokens = out.getIncomingTokens();
        Assert.assertEquals(1, tokens.size());

        Token token = tokens.get(0);
        Assert.assertEquals(Token.TYPE_NORMAL, token.getTokenType());
        Assert.assertEquals(1, token.getData("test1"));
        Assert.assertEquals("2", token.getData("test2"));
        Assert.assertEquals(3.0D, token.getData("test3"));
    }

}
