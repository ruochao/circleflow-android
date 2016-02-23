package com.mocircle.flowsample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mocircle.flow.CircleFlow;
import com.mocircle.flow.FlowContext;
import com.mocircle.flow.FlowExecutor;
import com.mocircle.flow.FlowHistory;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.service.FlowLifecycleService;
import com.mocircle.flowsample.R;
import com.mocircle.flowsample.flows.appsetup.AppSetupFlow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sample4Fragment extends BaseFragment {

    private FlowExecutor executor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample4_fragment, container, false);
        View startButton = view.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFlow();
            }
        });
        View contextButton = view.findViewById(R.id.context_button);
        contextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printContext();
            }
        });
        View historyButton = view.findViewById(R.id.history_button);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printHistory();
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

    private void printContext() {
        FlowContext context = CircleFlow.getServices().getLifecycleService().getFlowContext(executor.getFlowId());
        if (context != null) {
            String currentNodes = "";
            if (context.getCurrentNodes().size() > 0) {
                for (FlowNode node : context.getCurrentNodes()) {
                    currentNodes += node.getClass().getSimpleName() + ", ";
                }
                currentNodes = currentNodes.substring(0, currentNodes.length() - 2);
            } else {
                currentNodes = "[empty]";
            }
            String msg = "Current status: " + context.getCurrentStatus() + "\r\n"
                    + "Current node: " + currentNodes;
            print(msg);
        } else {
            print("Current context is null.");
        }
    }

    private void printHistory() {
        FlowHistory history = CircleFlow.getServices().getLifecycleService().getFlowHistory(executor.getFlowId());
        if (history != null) {
            String msg = "Execution history: ";
            List<FlowHistory.NodeHistory> order = history.getExecutionOrder();
            for (FlowHistory.NodeHistory item : order) {
                double timestamp = (item.getAfterTimestamp() - item.getBeforeTimestamp()) / 1000D;
                msg += item.getNodeClass().getSimpleName() + "(" + timestamp + " sec) -> ";
            }
            print(msg);
        } else {
            print("History is null");
        }
    }

}


