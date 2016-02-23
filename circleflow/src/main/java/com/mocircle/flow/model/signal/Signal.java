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

import com.mocircle.flow.model.FlowElement;

import java.util.HashMap;
import java.util.Map;

/**
 * A base signal definition.
 */
public abstract class Signal extends FlowElement {

    public static final String KEY_DATA_RESULT = "key_data_result";

    protected String name;
    protected Map<String, Object> data = new HashMap<>();

    /**
     * Gets signal name, which is identifier for a signal.
     *
     * @return signal name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets signal name.
     *
     * @param name signal name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the whole data object in a signal.
     *
     * @return signal data
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * Gets the data value.
     *
     * @param key data key
     * @return data value
     */
    public Object getData(String key) {
        return data.get(key);
    }

    /**
     * Sets data value.
     *
     * @param key   data key
     * @param value data value
     */
    public void setData(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Sets data object.
     *
     * @param data signal object
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
