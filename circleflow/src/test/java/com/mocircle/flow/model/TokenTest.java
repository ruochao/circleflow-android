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

package com.mocircle.flow.model;

import com.mocircle.flow.BuildConfig;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class TokenTest {

    @Test
    public void testMerge() {
        Token t1 = new Token(Token.TYPE_SUCCESS);
        t1.setData("key1", "value1");
        t1.setData("key2", 2);
        t1.setData("key3", 3.1D);

        Token t2 = new Token(Token.TYPE_FAILURE);
        t2.mergeData(t1);

        Assert.assertEquals(Token.TYPE_FAILURE, t2.getTokenType());
        Assert.assertEquals("value1", t2.getData("key1"));
        Assert.assertEquals(2, t2.getData("key2"));
        Assert.assertEquals(3.1D, t2.getData("key3"));
    }

    @Test
    public void testMergeNullCase() {
        Token t1 = new Token(Token.TYPE_SUCCESS);
        Token t2 = new Token(Token.TYPE_FAILURE);

        t1.setData(null);
        t2.setData(null);
        t2.mergeData(t1);
        Assert.assertNull(t2.getData());

        t1.setData("key", "value");
        t2.setData(null);
        t2.mergeData(t1);
        Assert.assertEquals("value", t2.getData("key"));

        t1.setData(null);
        t2.setData("key", "value");
        t2.mergeData(t1);
        Assert.assertEquals(1, t2.getData().size());
        Assert.assertEquals("value", t2.getData("key"));
    }

    @Test
    public void testCloneWithNullData() {
        Token t1 = new Token(Token.TYPE_SUCCESS);
        t1.setData(null);
        Token c1 = (Token) t1.clone();
        Assert.assertEquals(t1.getTokenType(), c1.getTokenType());
        Assert.assertEquals(t1.getData(), c1.getData());
        Assert.assertNotEquals(t1, c1);
    }

    @Test
    public void testClone() {
        Token t1 = new Token(Token.TYPE_SUCCESS);
        t1.setData("key1", "value1");
        t1.setData("key2", 2);
        Token c1 = (Token) t1.clone();
        Assert.assertEquals(t1.getTokenType(), c1.getTokenType());
        Assert.assertEquals(t1.getData(), c1.getData());
        Assert.assertNotEquals(t1, c1);
    }

}
