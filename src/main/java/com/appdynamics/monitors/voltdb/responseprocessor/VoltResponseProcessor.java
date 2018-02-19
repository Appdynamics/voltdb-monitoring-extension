/*
 * Copyright 2014. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 */

package com.appdynamics.monitors.voltdb.responseprocessor;

import java.util.Map;
import org.voltdb.client.ClientResponse;

public interface VoltResponseProcessor extends ResponseProcessor {
    /**
     * Processes the VoltDB procedure response and creates stats from the response.
     *
     * @param clientResponse
     * @return
     */
    Map<String, Number> process(ClientResponse clientResponse);
}
