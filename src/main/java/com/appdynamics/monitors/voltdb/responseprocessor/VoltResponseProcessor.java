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
