package com.mocircle.flowsample.flows.appsetup;

import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.action.ActionNode;

public class CheckLoginTask extends ActionNode {

    @Override
    public Token execute() {
        Object token = getIncomingData().get("token");
        if (token != null) {
            return new Token(Token.TYPE_SUCCESS, getIncomingData());
        } else {
            return new Token(Token.TYPE_FAILURE);
        }
    }
}
