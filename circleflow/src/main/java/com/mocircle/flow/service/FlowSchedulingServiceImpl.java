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

package com.mocircle.flow.service;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Default implementation for {@link FlowSchedulingService}.
 */
public class FlowSchedulingServiceImpl implements FlowSchedulingService {

    private ExecutorService executor = Executors.newCachedThreadPool();
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    public void executeAsyncJob(Runnable job) {
        executor.execute(job);
    }

    @Override
    public void executeBackgroundJob(Runnable job) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            executeAsyncJob(job);
        } else {
            job.run();
        }
    }

    @Override
    public void executeUIJob(Runnable job) {
        uiHandler.post(job);
    }

    public void setExecutorService(ExecutorService executor) {
        this.executor = executor;
    }

    public void setUIHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

}
