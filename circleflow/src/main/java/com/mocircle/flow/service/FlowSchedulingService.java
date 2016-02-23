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

/**
 * A service interface about job scheduling and execution.
 */
public interface FlowSchedulingService {

    /**
     * Executes job on a new thread.
     *
     * @param job task
     */
    void executeAsyncJob(Runnable job);

    /**
     * Executes job on background thread. If current is not main thread, it will run on current
     * thread, otherwise it will create a new thread.
     *
     * @param job task
     */
    void executeBackgroundJob(Runnable job);

    /**
     * Executes job on main/UI thread.
     *
     * @param job task
     */
    void executeUIJob(Runnable job);

}
