package com.appdynamics.monitors.voltdb.responseprocessor;

import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import java.util.Map;

public interface JsonResponseProcessor extends ResponseProcessor {

    /**
     * Processes the HTTP response from VoltDB procedure and creates stats from the response.
     *
     * @param json
     * @return
     */
    Map<String, Number> parseJson(String json) throws TaskExecutionException;
}
