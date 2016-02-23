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

/**
 * Base element in a flow.
 */
public abstract class FlowElement {

    protected String id;

    /**
     * Gets identifier of the element.
     *
     * @return element id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets element id.
     *
     * @param id element id
     */
    public void setId(String id) {
        this.id = id;
    }

}
