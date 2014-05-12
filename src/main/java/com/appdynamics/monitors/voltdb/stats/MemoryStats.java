package com.appdynamics.monitors.voltdb.stats;

import com.appdynamics.monitors.voltdb.Mode;
import com.appdynamics.monitors.voltdb.ProcedureExecutor;
import com.appdynamics.monitors.voltdb.responseprocessor.ResponseProcessor;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoryStats extends Stats {

    private static final String METRIC_PATH = "Custom Metrics|VoltDB|Stats|Memory";

    public MemoryStats(ProcedureExecutor procedureExecutor, Mode mode) {
        super(procedureExecutor, mode);
    }

    @Override
    public Map<String, Number> fetch() throws TaskExecutionException {

        List<String> appenderColumns = new ArrayList<String>();
        appenderColumns.add("HOSTNAME");

        ResponseProcessor responseProcessor = buildResponseProcessor(METRIC_PATH, appenderColumns);
        return getProcedureExecutor().executeProcedure(PROCEDURE_STATS, "MEMORY", responseProcessor);
    }
}