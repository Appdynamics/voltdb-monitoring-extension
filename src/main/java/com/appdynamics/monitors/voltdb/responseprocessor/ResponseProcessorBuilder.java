/*
 * Copyright 2014. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.monitors.voltdb.responseprocessor;

import com.appdynamics.monitors.voltdb.Mode;
import com.google.common.base.Preconditions;
import java.util.List;

public class ResponseProcessorBuilder {

    private Mode mode;

    private ResponseProcessorBuilder(Mode mode) {
        this.mode = mode;
    }

    public static ResponseProcessorBuilder build(Mode mode) {
        return new ResponseProcessorBuilder(mode);
    }

    public ResponseProcessor getResponseProcessor(String metricPath, List<String> appenderColumns) {
        Preconditions.checkArgument(mode != null, "Mode can not be null");
        if (Mode.ClientAPI.equals(mode)) {
            return new VoltDBResponseProcessor(metricPath, appenderColumns);
        } else {
            return new VoltDBJsonResponseProcessor(metricPath, appenderColumns);
        }
    }

}
