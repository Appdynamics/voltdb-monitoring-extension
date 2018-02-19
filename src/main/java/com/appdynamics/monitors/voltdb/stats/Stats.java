/*
 * Copyright 2014. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.monitors.voltdb.stats;

import com.appdynamics.monitors.voltdb.Mode;
import com.appdynamics.monitors.voltdb.ProcedureExecutor;
import com.appdynamics.monitors.voltdb.responseprocessor.ResponseProcessor;
import com.appdynamics.monitors.voltdb.responseprocessor.ResponseProcessorBuilder;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import java.util.List;
import java.util.Map;

public abstract class Stats {

    static final String PROCEDURE_STATS = "@Statistics";

    private ProcedureExecutor procedureExecutor;
    private Mode mode;

    public Stats(ProcedureExecutor procedureExecutor, Mode mode) {
        this.procedureExecutor = procedureExecutor;
        this.mode = mode;
    }

    public abstract Map<String, Number> fetch() throws TaskExecutionException;

    public ProcedureExecutor getProcedureExecutor() {
        return procedureExecutor;
    }

    public ResponseProcessor buildResponseProcessor(String metricPath, List<String> appenderColumns) {
        return ResponseProcessorBuilder.build(mode).getResponseProcessor(metricPath, appenderColumns);
    }

}