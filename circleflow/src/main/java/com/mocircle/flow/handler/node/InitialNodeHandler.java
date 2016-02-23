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

package com.mocircle.flow.handler.node;

import com.mocircle.flow.exception.FlowDefinitionException;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.control.InitialNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for {@link InitialNode}.
 */
public class InitialNodeHandler extends AbstractFlowNodeHandler {

    @Override
    public boolean isSupported(FlowNode node) {
        return node instanceof InitialNode;
    }

    @Override
    public NodeHandleResult handleNode(FlowNode node) {
        List<FlowNode> nextNodes = new ArrayList<>();

        InitialNode initialNode = (InitialNode) node;
        Token outToken = initialNode.getOutgoingToken();
        int size = initialNode.getOutgoingNodes().size();
        if (size == 1) {
            FlowNode next = initialNode.getOutgoingNodes().get(0);
            next.receiveToken(initialNode, outToken);
            nextNodes.add(next);
        } else {
            throw new FlowDefinitionException("Invalid node definition: InitialNode only allow one outgoing node.");
        }
        return new NodeHandleResult(nextNodes, outToken);
    }
}
