package com.mocircle.flowsample.flows.appsetup;

import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.action.ActionNode;
import com.mocircle.flowsample.utils.Simulator;

public class GetAppConfigTask extends ActionNode {

    @Override
    public Token execute() {
        Simulator.simulateNetworkRequest();
        return new Token(Token.TYPE_SUCCESS);
    }
}
