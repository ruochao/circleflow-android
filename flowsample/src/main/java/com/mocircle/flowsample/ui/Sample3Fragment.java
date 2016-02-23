package com.mocircle.flowsample.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mocircle.flow.CircleFlow;
import com.mocircle.flow.FlowExecutor;
import com.mocircle.flow.service.FlowLifecycleService;
import com.mocircle.flowsample.R;
import com.mocircle.flowsample.flows.appsetup.AppSetupFlow;

import java.util.HashMap;
import java.util.Map;

public class Sample3Fragment extends BaseFragment {

    private FlowExecutor executor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample3_fragment, container, false);
        View startButton = view.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFlow();
            }
        });
        View cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelFlow();
            }
        });
        return view;
    }

    @Override
    protected void setupFlow() {
        Map<String, Object> initData = new HashMap<>();
        initData.put("user", "demo");
        initData.put("password", "demo");
        executor = CircleFlow.getEngine().prepareFlow(new AppSetupFlow(), initData);
        FlowLifecycleService service = CircleFlow.getServices().getLifecycleService();
        service.addLifecycleListener(executor.getFlowId(), logListener);
    }

    private void startFlow() {
        clearOutput();
        executor.execute();
    }

    private void cancelFlow() {
        executor.cancel();
    }

}


