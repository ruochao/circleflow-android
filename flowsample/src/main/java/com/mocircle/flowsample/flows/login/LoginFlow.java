package com.mocircle.flowsample.flows.login;

import com.mocircle.flow.FlowDefinition;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.control.Decision;
import com.mocircle.flow.model.control.FinalNode;
import com.mocircle.flow.model.control.InitialNode;

public class LoginFlow implements FlowDefinition {

    @Override
    public InitialNode getDefinition() {
        FlowNode loginFailedNode = new LoginFailedTask().addOutgoingNode(new FinalNode());

        InitialNode node = new InitialNode();
        node.addOutgoingNode(
                new AuthUserTask().addOutgoingNode(
                        new Decision()
                                .addOutgoingNode(Token.TYPE_FAILURE, loginFailedNode)
                                .addOutgoingNode(Token.TYPE_SUCCESS,
                                        new GetTokenTask().addOutgoingNode(
                                                new Decision()
                                                        .addOutgoingNode(Token.TYPE_FAILURE, loginFailedNode)
                                                        .addOutgoingNode(Token.TYPE_SUCCESS, new FinalNode())
                                        )
                                )));
        return node;
    }
}
