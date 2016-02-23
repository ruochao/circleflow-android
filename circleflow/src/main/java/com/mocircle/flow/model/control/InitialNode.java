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

package com.mocircle.flow.model.control;

import com.mocircle.flow.model.Token;

import java.util.Map;

/**
 * Initial node is a control node at which flow starts.
 */
public class InitialNode extends ControlNode {

    private Map<String, Object> initData;

    /**
     * Sets initial data passing into flow execution.
     *
     * @param initData initial data
     */
    public void setInitData(Map<String, Object> initData) {
        this.initData = initData;
    }

    @Override
    public Token getOutgoingToken() {
        Token token = new Token(Token.TYPE_NORMAL);
        token.setData(initData);
        return token;
    }

}
