package com.mocircle.flowsample.flows.login;

import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.action.ActionNode;

public class LoginFailedTask extends ActionNode {

    @Override
    public Token execute() {
        return new Token(Token.TYPE_SUCCESS);
    }
}
