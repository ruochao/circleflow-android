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

/**
 * Abstraction for remote {@link Signal}, it's designed for signal passing between different
 * terminals. <br/> e.g. signal from server side, from different devices
 */
public abstract class RemoteSignal extends Signal {

    protected String remoteId;

    /**
     * Gets the identifier for remote signal.
     *
     * @return remote id
     */
    public String getRemoteId() {
        return remoteId;
    }

    /**
     * Sets remote id.
     *
     * @param remoteId remote id
     */
    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }
}
