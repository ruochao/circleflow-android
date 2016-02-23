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

package com.mocircle.flow.flows;

import com.mocircle.flow.FlowDefinition;
import com.mocircle.flow.model.control.FinalNode;
import com.mocircle.flow.model.control.Fork;
import com.mocircle.flow.model.control.InitialNode;
import com.mocircle.flow.model.control.Join;

public class JoinedFlow implements FlowDefinition {

    @Override
    public InitialNode getDefinition() {
        Join join = new Join();
        join.addOutgoingNode(new FinalNode());

        InitialNode node = new InitialNode();
        node.addOutgoingNode(
                new Fork().addOutgoingNode(new Task1().addOutgoingNode(join))
                        .addOutgoingNode(new Task2().addOutgoingNode(join)));
        return node;
    }
}
