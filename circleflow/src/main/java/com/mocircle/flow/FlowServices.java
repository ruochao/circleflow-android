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

import com.mocircle.flow.service.FlowAndroidService;
import com.mocircle.flow.service.FlowLifecycleService;
import com.mocircle.flow.service.FlowServiceBuilder;
import com.mocircle.flow.service.FlowSignalService;

/**
 * A service hub to returns all kinds of services.
 */
public interface FlowServices {

    /**
     * Sets service builder implementation, all services will be reset after setting the service
     * builder.
     *
     * @param serviceBuilder service builder
     */
    void injectServiceBuilder(FlowServiceBuilder serviceBuilder);

    /**
     * Gets lifecycle service.
     *
     * @return lifecycle service
     */
    FlowLifecycleService getLifecycleService();

    /**
     * Gets signal service.
     *
     * @return signal service
     */
    FlowSignalService getSignalService();

    /**
     * Gets android service.
     *
     * @return android service
     */
    FlowAndroidService getAndroidService();

}
