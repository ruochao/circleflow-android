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

import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.Token;

import java.util.List;

/**
 * Control node used to change the flow execution direction.
 */
public abstract class ControlNode extends FlowNode {

    /**
     * Collects the incoming tokens and generates as one outgoing token.
     *
     * @return outgoing token
     */
    public Token getOutgoingToken() {
        Token token = new Token(Token.TYPE_NORMAL);
        List<Token> tokens = getIncomingTokens();
        for (Token t : tokens) {
            token.mergeData(t);
        }
        return token;
    }

}
