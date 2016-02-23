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

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * A service interface for android related actions.
 */
public interface FlowAndroidService {

    /**
     * Sets up the service, suggest to call this method on application started.
     *
     * @param app android application object
     */
    void setup(Application app);

    /**
     * Shutdown the service.
     */
    void shutdown();

    /**
     * Gets current displayed activity.
     *
     * @return current activity
     */
    Activity getCurrentActivity();

    /**
     * Gets application context.
     *
     * @return context
     */
    Context getAppContext();

}
