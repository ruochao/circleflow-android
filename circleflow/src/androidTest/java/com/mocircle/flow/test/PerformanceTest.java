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

package com.mocircle.flow.test;

import org.junit.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A utility class to help test performance
 */
public class PerformanceTest {

    private CountDownLatch countDownLatch;

    private Runnable task;
    private int repeatCount;
    private long timeout;
    private long result;

    public PerformanceTest setTask(Runnable task) {
        this.task = task;
        return this;
    }

    public PerformanceTest setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        return this;
    }

    public PerformanceTest setTimeout(long millisecond) {
        this.timeout = millisecond;
        return this;
    }

    public PerformanceTest runTest() throws InterruptedException {
        result = -1;
        countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                result = testTask();
                countDownLatch.countDown();
            }
        }).start();
        countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
        return this;
    }

    public long getTestResult() {
        return result;
    }

    public PerformanceTest assertNotTimeout() {
        Assert.assertNotEquals("Test execution time out.", -1, result);
        return this;
    }

    private long testTask() {
        long startTime = System.currentTimeMillis();
        int count = repeatCount;
        while (count-- > 0) {
            task.run();
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

}
