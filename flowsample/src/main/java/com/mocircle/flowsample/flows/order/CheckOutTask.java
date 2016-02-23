package com.mocircle.flowsample.flows.order;


import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.action.ActionNode;

public class CheckOutTask extends ActionNode {

    @Override
    public Token execute() {
        return new Token(Token.TYPE_SUCCESS);
    }
}
