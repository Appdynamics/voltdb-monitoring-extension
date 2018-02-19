/*
 * Copyright 2014. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.monitors.voltdb.client;

import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;

public interface Client<T> {

    T callProcedure(String procedureName, String parameter) throws TaskExecutionException;
}
