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

import com.mocircle.android.logging.CircleLog;
import com.mocircle.flow.exception.FlowDefinitionException;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.NullNode;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.control.Decision;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for {@link Decision}.
 */
public class DecisionHandler extends AbstractFlowNodeHandler {

    private static final String TAG = "DecisionHandler";

    @Override
    public boolean isSupported(FlowNode node) {
        return node instanceof Decision;
    }

    @Override
    public NodeHandleResult handleNode(FlowNode node) {
        List<FlowNode> nextNodes = new ArrayList<>();

        Decision decision = (Decision) node;
        List<Token> tokens = decision.getIncomingTokens();
        Token outToken = decision.getOutgoingToken();
        if (tokens.size() == 1) {
            String tokenType = tokens.get(0).getTokenType();
            List<FlowNode> nodes = decision.getOutgoingNodes(new String[]{tokenType});
            if (nodes.size() == 0) {
                NullNode next = new NullNode();
                next.receiveToken(decision, outToken);
                nextNodes.add(next);
                CircleLog.w(TAG, "Cannot find outgoing node, auto create NullNode for it.");
            } else if (nodes.size() == 1) {
                FlowNode n = nodes.get(0);
                n.receiveToken(decision, outToken);
                nextNodes.add(n);
            } else {
                throw new FlowDefinitionException("Invalid node definition: Decision only allow one selected outgoing node.");
            }
        } else {
            throw new FlowDefinitionException("Invalid node definition: Decision only allow one incoming node.");
        }
        decision.destroyToken();
        return new NodeHandleResult(nextNodes, outToken);
    }
}
