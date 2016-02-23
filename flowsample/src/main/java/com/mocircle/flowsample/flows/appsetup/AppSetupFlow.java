package com.mocircle.flowsample.flows.appsetup;

import com.mocircle.flow.FlowDefinition;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.control.Decision;
import com.mocircle.flow.model.control.FinalNode;
import com.mocircle.flow.model.control.Fork;
import com.mocircle.flow.model.control.InitialNode;
import com.mocircle.flow.model.control.Join;

public class AppSetupFlow implements FlowDefinition {

    @Override
    public InitialNode getDefinition() {
        InitialNode node = new InitialNode();

        Join join = new Join();
        join.addOutgoingNode(new FinalNode());

        node.addOutgoingNode(new LoginTask().addOutgoingNode(
                new CheckLoginTask().addOutgoingNode(
                        new Decision()
                                .addOutgoingNode(Token.TYPE_FAILURE, new FinalNode())
                                .addOutgoingNode(Token.TYPE_SUCCESS,
                                        new Fork()
                                                .addOutgoingNode(new SyncDataTask().addOutgoingNode(join))
                                                .addOutgoingNode(new GetAppConfigTask().addOutgoingNode(join))
                                )
                )));

        return node;
    }
}
