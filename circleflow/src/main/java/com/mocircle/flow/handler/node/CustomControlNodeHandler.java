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
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.NullNode;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.control.CustomControlNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for {@link CustomControlNode}.
 */
public class CustomControlNodeHandler extends AbstractFlowNodeHandler {

    private static final String TAG = "CustomControlNodeHandler";

    @Override
    public boolean isSupported(FlowNode node) {
        return node instanceof CustomControlNode;
    }

    @Override
    public NodeHandleResult handleNode(FlowNode node) {
        List<FlowNode> nextNodes = new ArrayList<>();

        CustomControlNode control = (CustomControlNode) node;
        String[] types = control.getAcceptedTokenTypes();
        Token outToken = control.getOutgoingToken();
        List<FlowNode> nodes = control.getOutgoingNodes(types);
        if (nodes.size() == 0) {
            NullNode next = new NullNode();
            next.receiveToken(control, outToken);
            nextNodes.add(next);
            CircleLog.w(TAG, "Cannot find outgoing node, auto create NullNode for it.");
        } else {
            for (FlowNode n : nodes) {
                n.receiveToken(control, outToken);
                nextNodes.add(n);
            }
        }
        control.destroyToken();
        return new NodeHandleResult(nextNodes, outToken);
    }
}
