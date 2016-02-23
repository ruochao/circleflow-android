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
import com.mocircle.flow.model.control.Merge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler for {@link Merge}.
 */
public class MergeHandler extends AbstractFlowNodeHandler {

    private static final String TAG = "MergeHandler";

    private Map<String, Integer> callCount = new HashMap<>();

    @Override
    public boolean isSupported(FlowNode node) {
        return node instanceof Merge;
    }

    @Override
    public NodeHandleResult handleNode(FlowNode node) {
        if (!hitMerge(node)) {
            return null;
        }

        List<FlowNode> nextNodes = new ArrayList<>();
        Merge merge = (Merge) node;
        List<FlowNode> nodes = merge.getOutgoingNodes(null);
        Token outToken = merge.getOutgoingToken();
        if (nodes.size() == 0) {
            NullNode next = new NullNode();
            next.receiveToken(merge, outToken);
            nextNodes.add(next);
            CircleLog.w(TAG, "Cannot find outgoing node, auto create NullNode for it.");
        } else if (nodes.size() == 1) {
            FlowNode n = nodes.get(0);
            n.receiveToken(merge, outToken);
            nextNodes.add(n);
        } else {
            throw new FlowDefinitionException("Invalid node definition: Merge only allow one outgoing node.");
        }
        merge.destroyToken();
        return new NodeHandleResult(nextNodes, outToken);
    }

    private synchronized boolean hitMerge(FlowNode node) {
        CircleLog.v(TAG, "hit merge: thread=" + Thread.currentThread().getId() + ", nodeId=" + node.getId());
        Integer count = callCount.get(node.getId());
        if (count == null) {
            count = 0;
        }
        count++;
        callCount.put(node.getId(), count);
        CircleLog.d(TAG, "Incoming nodes: " + node.getIncomingNodes().size() + ", current hit: " + count);
        if (count > 1 && count < node.getIncomingNodes().size()) {
            // Drop off request
            return false;
        } else if (count >= node.getIncomingNodes().size()) {
            // Drop off request
            callCount.remove(node.getId());
            CircleLog.d(TAG, "Removed hit history for merge, nodeId=" + node.getId());
            return false;
        }
        // Only allow first hit
        return true;
    }

}
