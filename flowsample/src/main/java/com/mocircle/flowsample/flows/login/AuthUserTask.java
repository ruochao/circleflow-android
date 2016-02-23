package com.mocircle.flowsample.flows.login;

import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.action.ActionNode;
import com.mocircle.flowsample.utils.Simulator;

public class AuthUserTask extends ActionNode {

    @Override
    public Token execute() {
        Simulator.simulateNetworkRequest();
        String user = getIncomingData("user").toString();
        String pass = getIncomingData("password").toString();
        if (user.equals("demo") && pass.equals("demo")) {
            return new Token(Token.TYPE_SUCCESS);
        } else {
            return new Token(Token.TYPE_FAILURE);
        }
    }
}
