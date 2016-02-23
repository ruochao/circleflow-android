package com.mocircle.flowsample.flows.order;

import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.action.SignalBasedNode;
import com.mocircle.flow.model.signal.Signal;

public class WaitForPaymentTask extends SignalBasedNode {

    public WaitForPaymentTask() {
        super("pay", 6000);
    }

    @Override
    protected Token onReceivedSignal(Signal signal) {
        return new Token(Token.TYPE_SUCCESS);
    }

    @Override
    protected Token onTimeout() {
        return new Token(Token.TYPE_FAILURE);
    }
}
