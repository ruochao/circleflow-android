package com.mocircle.flowsample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mocircle.flow.CircleFlow;
import com.mocircle.flow.FlowExecutor;
import com.mocircle.flow.service.FlowLifecycleService;
import com.mocircle.flowsample.R;
import com.mocircle.flowsample.flows.login.LoginFlow;

import java.util.HashMap;
import java.util.Map;

public class Sample1Fragment extends BaseFragment {

    private EditText userText;
    private EditText asswordText;

    private FlowExecutor executor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample1_fragment, container, false);
        userText = (EditText) view.findViewById(R.id.username_text);
        asswordText = (EditText) view.findViewById(R.id.password_text);
        View loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        return view;
    }

    @Override
    protected void setupFlow() {
        executor = CircleFlow.getEngine().prepareFlow(new LoginFlow());
        FlowLifecycleService service = CircleFlow.getServices().getLifecycleService();
        service.addLifecycleListener(executor.getFlowId(), logListener);
    }

    private void login() {
        clearOutput();

        Map<String, Object> data = new HashMap<>();
        data.put("user", userText.getText().toString());
        data.put("password", asswordText.getText().toString());
        executor.execute(data);
    }

}


