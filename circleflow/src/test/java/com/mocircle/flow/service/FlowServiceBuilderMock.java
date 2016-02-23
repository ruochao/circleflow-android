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

package com.mocircle.flow.service;

public class FlowServiceBuilderMock implements FlowServiceBuilder {

    private FlowSchedulingService schedulingService;
    private FlowLifecycleService lifecycleService;
    private FlowLifecycleManageService lifecycleManageService;
    private FlowSignalService signalService;
    private FlowAndroidService androidService;

    public FlowServiceBuilderMock() {
        FlowServiceBuilder builder = FlowServiceBuilderImpl.create();
        schedulingService = builder.buildSchedulingService();
        lifecycleService = builder.buildLifecycleService();
        lifecycleManageService = builder.buildLifecycleManageService();
        signalService = builder.buildSignalService();
        androidService = builder.buildAndroidService();
    }

    public FlowServiceBuilderMock replace(FlowSchedulingService service) {
        schedulingService = service;
        return this;
    }

    public FlowServiceBuilderMock replace(FlowLifecycleService service) {
        lifecycleService = service;
        return this;
    }

    public FlowServiceBuilderMock replace(FlowLifecycleManageService service) {
        lifecycleManageService = service;
        return this;
    }

    public FlowServiceBuilderMock replace(FlowSignalService service) {
        signalService = service;
        return this;
    }

    public FlowServiceBuilderMock replace(FlowAndroidService service) {
        androidService = service;
        return this;
    }

    public FlowServiceBuilderMock replaceWithSyncSchedulingService() {
        schedulingService = new FlowSchedulingServiceSyncMock();
        return this;
    }

    @Override
    public FlowSchedulingService buildSchedulingService() {
        return schedulingService;
    }

    @Override
    public FlowLifecycleService buildLifecycleService() {
        return lifecycleService;
    }

    @Override
    public FlowLifecycleManageService buildLifecycleManageService() {
        return lifecycleManageService;
    }

    @Override
    public FlowSignalService buildSignalService() {
        return signalService;
    }

    @Override
    public FlowAndroidService buildAndroidService() {
        return androidService;
    }
}
