package com.appdynamics.monitors.voltdb.client;

import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;

public interface Client<T> {

    T callProcedure(String procedureName, String parameter) throws TaskExecutionException;
}
