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
import com.mocircle.flowsample.flows.order.OrderFlow;

public class Sample2Fragment extends BaseFragment {

    private FlowExecutor executor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sample2_fragment, container, false);
        View checkoutButton = view.findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkout();
            }
        });
        View payButton = view.findViewById(R.id.pay_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });
        View cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        return view;
    }

    @Override
    protected void setupFlow() {
        executor = CircleFlow.getEngine().prepareFlow(new OrderFlow());
        FlowLifecycleService service = CircleFlow.getServices().getLifecycleService();
        service.addLifecycleListener(executor.getFlowId(), logListener);
    }

    private void checkout() {
        clearOutput();
        executor.execute();
    }

    private void pay() {
        CircleFlow.getServices().getSignalService().sendLocalEvent("pay");
    }

    private void cancel() {
        CircleFlow.getEngine().cancelFlow(executor.getFlowId());
    }
}


