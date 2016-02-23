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
 * To build the service.
 */
public interface FlowServiceBuilder {

    /**
     * Builds the scheduling service.
     *
     * @return scheduling service instance
     */
    FlowSchedulingService buildSchedulingService();

    /**
     * Builds the lifecycle service.
     *
     * @return lifecycle service instance
     */
    FlowLifecycleService buildLifecycleService();

    /**
     * Builds the lifecycle management service.
     *
     * @return lifecycle management service
     */
    FlowLifecycleManageService buildLifecycleManageService();

    /**
     * Builds the signal service.
     *
     * @return signal service instance
     */
    FlowSignalService buildSignalService();

    /**
     * Builds the android service.
     *
     * @return android service instance
     */
    FlowAndroidService buildAndroidService();
}
