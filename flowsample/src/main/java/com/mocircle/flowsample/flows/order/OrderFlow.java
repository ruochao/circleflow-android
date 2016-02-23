package com.mocircle.flowsample.flows.order;

import com.mocircle.flow.FlowDefinition;
import com.mocircle.flow.model.control.FinalNode;
import com.mocircle.flow.model.control.InitialNode;

public class OrderFlow implements FlowDefinition {

    @Override
    public InitialNode getDefinition() {
        InitialNode node = new InitialNode();
        node.addOutgoingNode(
                new CheckOutTask().addOutgoingNode(
                        new WaitForPaymentTask().addOutgoingNode(new FinalNode())));
        return node;
    }
}
