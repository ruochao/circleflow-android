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

package com.mocircle.flow.model.signal;

import com.mocircle.flow.BuildConfig;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 16)
public class LocalEventTest {

    @Test
    public void test() {
        LocalEvent event = new LocalEvent("name1");
        Assert.assertEquals("name1", event.getName());

        Map<String, Object> data = new HashMap<>();
        data.put("key1", "value1");
        data.put("key2", 2);
        event = new LocalEvent("name2", data);
        Assert.assertEquals("name2", event.getName());
        Assert.assertEquals("value1", event.getData("key1"));
        Assert.assertEquals(2, event.getData("key2"));
    }

}
