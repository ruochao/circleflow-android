package com.mocircle.flowsample.flows.login;

import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.action.ActionNode;
import com.mocircle.flowsample.utils.Simulator;

import java.util.Map;

public class GetTokenTask extends ActionNode {

    @Override
    public Token execute() {
        Simulator.simulateNetworkRequest();
        Map<String, Object> data = getIncomingData();
        data.put("token", "xx-token-yy");
        return new Token(Token.TYPE_SUCCESS, data);
    }
}
