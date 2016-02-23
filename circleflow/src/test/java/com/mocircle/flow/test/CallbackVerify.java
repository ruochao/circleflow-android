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

package com.mocircle.flow.test;

/**
 * A helper class to verify callback has been called
 */
public class CallbackVerify {

    private int count = 0;
    private Object obj;

    public synchronized void call() {
        count++;
    }

    public synchronized void call(Object param) {
        count++;
        this.obj = param;
    }

    public synchronized boolean isCalled() {
        return count > 0;
    }

    public synchronized int getCallCount() {
        return count;
    }

    public synchronized Object getInput() {
        return obj;
    }
}
