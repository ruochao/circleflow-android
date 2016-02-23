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
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.signal.Signal;
import com.mocircle.flow.test.CallbackVerify;
import com.mocircle.flow.test.DelayThread;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class SignalBasedNodeTest {

    class SignalBasedNodeMockImpl extends SignalBasedNode {

        public SignalBasedNodeMockImpl() {
        }

        public SignalBasedNodeMockImpl(String signalName) {
            super(signalName);
        }

        public SignalBasedNodeMockImpl(String signalName, long timeout) {
            super(signalName, timeout);
        }

        @Override
        protected Token onReceivedSignal(Signal signal) {
            return null;
        }

        @Override
        protected Token onTimeout() {
            return null;
        }
    }

    @Before
    public void setup() {
        CircleFlow.getServices().getAndroidService().setup(RuntimeEnvironment.application);
    }

    @Test
    public void testConstructor() {
        SignalBasedNode node = new SignalBasedNodeMockImpl("name1");
        Assert.assertEquals("name1", node.getSignalName());
        Assert.assertEquals(0, node.getTimeout());

        node = new SignalBasedNodeMockImpl("name2", 1234);
        Assert.assertEquals("name2", node.getSignalName());
        Assert.assertEquals(1234, node.getTimeout());
    }

    @Test
    public void testReceivedSignal() {
        final CallbackVerify receivedCallback = new CallbackVerify();
        final CallbackVerify timeoutCallback = new CallbackVerify();
        final SignalBasedNode node = new SignalBasedNode() {
            @Override
            protected Token onReceivedSignal(Signal signal) {
                receivedCallback.call();
                return new Token(Token.TYPE_SUCCESS);
            }

            @Override
            protected Token onTimeout() {
                timeoutCallback.call();
                return null;
            }
        };
        node.setSignalName("test-signal");
        DelayThread.delayRun(new Runnable() {
            @Override
            public void run() {
                CircleFlow.getServices().getSignalService().sendLocalEvent("test-signal");
                ShadowLooper.unPauseMainLooper();
            }
        });

        Token token = node.execute();
        Assert.assertTrue(receivedCallback.isCalled());
        Assert.assertFalse(timeoutCallback.isCalled());
        Assert.assertEquals(Token.TYPE_SUCCESS, token.getTokenType());
    }

    @Test
    public void testTimeout() {
        final CallbackVerify receivedCallback = new CallbackVerify();
        final CallbackVerify timeoutCallback = new CallbackVerify();
        final SignalBasedNode node = new SignalBasedNode() {
            @Override
            protected Token onReceivedSignal(Signal signal) {
                receivedCallback.call();
                return null;
            }

            @Override
            protected Token onTimeout() {
                timeoutCallback.call();
                return new Token(Token.TYPE_FAILURE);
            }
        };
        node.setSignalName("test-signal");
        node.setTimeout(500);

        Token token = node.execute();
        Assert.assertFalse(receivedCallback.isCalled());
        Assert.assertTrue(timeoutCallback.isCalled());
        Assert.assertEquals(Token.TYPE_FAILURE, token.getTokenType());
    }

}
