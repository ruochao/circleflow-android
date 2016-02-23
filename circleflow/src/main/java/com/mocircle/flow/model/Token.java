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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Token holds the execution result and data, passing between the flow nodes.
 */
public class Token implements Serializable, Cloneable {

    public static final String TYPE_NORMAL = "circleflow.normal";
    public static final String TYPE_SUCCESS = "circleflow.success";
    public static final String TYPE_FAILURE = "circleflow.failure";
    public static final String TYPE_CANCEL = "circleflow.cancel";

    protected String type;
    protected Map<String, Object> data = new HashMap<>();

    public Token() {
    }

    public Token(String type) {
        this.type = type;
    }

    public Token(String type, Map<String, Object> data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Gets token type.
     *
     * @return token type
     */
    public String getTokenType() {
        return type;
    }

    /**
     * Sets token type.
     *
     * @param type token type
     */
    public void setTokenType(String type) {
        this.type = type;
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
     * Gets whole data belongs to a token.
     *
     * @return token data
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * Sets token data.
     *
     * @param key   data key
     * @param value token data value
     */
    public void setData(String key, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(key, value);
    }

    /**
     * Sets token data.
     *
     * @param data token data
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * Merges token data.
     *
     * @param token token to be merged
     */
    public void mergeData(Token token) {
        if (token != null && token.data != null) {
            if (data == null) {
                data = new HashMap<>();
            }
            Set<String> keys = token.data.keySet();
            for (String key : keys) {
                data.put(key, token.data.get(key));
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        Token token;
        try {
            token = (Token) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }

        if (data != null) {
            token.data = (Map<String, Object>) ((HashMap) data).clone();
        }
        return token;
    }
}
