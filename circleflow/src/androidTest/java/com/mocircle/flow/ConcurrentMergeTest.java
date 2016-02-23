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

package com.mocircle.flow;

import android.support.test.runner.AndroidJUnit4;

import com.mocircle.flow.flows.MergedFlow;
import com.mocircle.flow.test.PerformanceTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ConcurrentMergeTest {

    @Test
    public void test() throws InterruptedException {
        // Make sure in concurrency environment, we are still able to merge nodes.
        new PerformanceTest()
                .setRepeatCount(1000)
                .setTimeout(10000)
                .setTask(new Runnable() {
                    @Override
                    public void run() {
                        FlowExecutor executor = CircleFlow.getEngine().prepareFlow(new MergedFlow());
                        executor.execute();
                        executor.waitForFinished();
                    }
                })
                .runTest()
                .assertNotTimeout();
    }

}
