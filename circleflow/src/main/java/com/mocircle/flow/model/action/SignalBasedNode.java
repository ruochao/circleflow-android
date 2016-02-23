/*
 * Copyright (C) 2016 mocircle.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mocircle.flow.model.action;

import com.mocircle.android.logging.CircleLog;
import com.mocircle.flow.CircleFlow;
import com.mocircle.flow.listener.FlowSignalListener;
import com.mocircle.flow.model.CancelableNode;
import com.mocircle.flow.model.Token;
import com.mocircle.flow.model.signal.Signal;
import com.mocircle.flow.service.FlowSignalService;

/**
 * Signal based node is an action node which will block the execution until receiving specific
 * signal.
 */
public abstract class SignalBasedNode extends ActionNode implements CancelableNode {

    private static final String TAG = "SignalBasedNode";

    private Object waitObj = new Object();
    private String signalName;
    private long timeout;
    private Signal receivedSignal;
    private boolean cancelled;
    private FlowSignalListener listener = new FlowSignalListener() {
        @Override
        public void onReceivedSignal(Signal signal) {
            if (signal != null && signal.getName().equals(signalName)) {
                receivedSignal = signal;
                synchronized (waitObj) {
                    waitObj.notifyAll();
                }
            }
        }
    };

    public SignalBasedNode() {
    }

    public SignalBasedNode(String signalName) {
        this.signalName = signalName;
    }

    public SignalBasedNode(String signalName, long timeout) {
        this.signalName = signalName;
        this.timeout = timeout;
    }

    /**
     * Gets expected signal name.
     *
     * @return signal name
     */
    public String getSignalName() {
        return signalName;
    }

    /**
     * Sets expected signal name.
     *
     * @param signalName signal name
     */
    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    /**
     * Gets maximum time span during receiving a signal.
     *
     * @return timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Sets maximum time span during receiving a signal.
     *
     * @param timeout timeout
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public Token execute() {
        resetNodeStatus();
        FlowSignalService service = CircleFlow.getServices().getSignalService();
        service.addSignalListener(signalName, listener);
        synchronized (waitObj) {
            try {
                CircleLog.i(TAG, "Pause node, waiting for signal: " + signalName);
                if (timeout > 0) {
                    waitObj.wait(timeout);
                } else {
                    waitObj.wait();
                }
            } catch (InterruptedException e) {
                CircleLog.w(TAG, e);
            }
        }
        service.removeSignalListener(signalName, listener);
        if (cancelled) {
            return onCancelled();
        } else if (receivedSignal != null) {
            return onReceivedSignal(receivedSignal);
        } else {
            return onTimeout();
        }
    }

    @Override
    public void notifyCancel() {
        cancelled = true;
        synchronized (waitObj) {
            waitObj.notifyAll();
        }
    }

    /**
     * Called when received the signal.
     *
     * @param signal signal object
     * @return token generated after executed
     */
    protected abstract Token onReceivedSignal(Signal signal);

    /**
     * Called when timeout during receiving signal.
     *
     * @return token generated after executed
     */
    protected abstract Token onTimeout();

    /**
     * Called when flow is being cancelled.
     *
     * @return token generated after executed
     */
    protected Token onCancelled() {
        return new Token(Token.TYPE_CANCEL);
    }

    private void resetNodeStatus() {
        cancelled = false;
        receivedSignal = null;
    }

}
