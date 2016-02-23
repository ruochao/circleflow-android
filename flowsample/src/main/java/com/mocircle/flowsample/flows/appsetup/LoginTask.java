package com.mocircle.flowsample.flows.appsetup;

import com.mocircle.flow.model.action.SubFlowNode;
import com.mocircle.flowsample.flows.login.LoginFlow;

public class LoginTask extends SubFlowNode {

    public LoginTask() {
        super(new LoginFlow(), true);
    }
}
