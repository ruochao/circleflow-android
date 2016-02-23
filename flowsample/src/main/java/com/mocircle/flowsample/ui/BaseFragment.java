package com.mocircle.flowsample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.TextView;

import com.mocircle.flow.FlowDefinition;
import com.mocircle.flow.listener.FlowLifecycleListener;
import com.mocircle.flow.listener.FlowLifecycleListenerAdapter;
import com.mocircle.flow.model.FlowNode;
import com.mocircle.flow.model.Token;
import com.mocircle.flowsample.R;

import java.util.Date;

public class BaseFragment extends Fragment {

    protected TextView outputText;

    protected FlowLifecycleListener logListener = new FlowLifecycleListenerAdapter() {

        @Override
        public void onFlowStarted(String flowId, FlowDefinition flowDefinition) {
            print("Flow starts at " + new Date().toLocaleString());
        }

        @Override
        public void postExecuteNode(FlowNode node, Token token) {
            print("Execute " + node.getClass().getSimpleName() + ", result=" + (token == null ? "null" : token.getTokenType()));
        }

        @Override
        public void onFlowEnded(String flowId) {
            print("Flow ends at " + new Date().toLocaleString());
        }

        @Override
        public void onFlowCancelled(String flowId) {
            print("Flow cancelled at " + new Date().toLocaleString());
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        outputText = (TextView) getView().findViewById(R.id.output_text);

        setupFlow();
    }

    protected void print(String msg) {
        outputText.append(msg + "\r\n");
    }

    protected void clearOutput() {
        outputText.setText("");
    }

    protected void setupFlow() {
    }
}
