/*
 * Copyright 2014. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

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
