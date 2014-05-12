package com.appdynamics.monitors.voltdb;

import com.appdynamics.monitors.voltdb.client.Client;
import com.appdynamics.monitors.voltdb.responseprocessor.JsonResponseProcessor;
import com.appdynamics.monitors.voltdb.responseprocessor.ResponseProcessor;
import com.appdynamics.monitors.voltdb.responseprocessor.VoltResponseProcessor;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import java.util.Map;
import org.apache.log4j.Logger;
import org.voltdb.client.ClientResponse;

public class ProcedureExecutor<T> {

    private static final Logger LOG = Logger.getLogger(ProcedureExecutor.class);
    private final Client<T> client;

    public ProcedureExecutor(Client<T> client) {
        Preconditions.checkArgument(client != null, "client can not be null");
        this.client = client;
    }

    /**
     * Executes procedures on VoltDB and returns stats by processing the response from the procedure
     *
     * @param procedureName
     * @param parameter
     * @param responseProcessor
     * @return
     */
    public Map<String, Number> executeProcedure(String procedureName, String parameter, ResponseProcessor responseProcessor) throws TaskExecutionException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(procedureName), "procedureName can not be null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(parameter), "parameter can not be null");
        Preconditions.checkArgument(responseProcessor != null, "responseProcessor can not be null");
        try {
            T response = client.callProcedure(procedureName, parameter);
            Map<String, Number> statsMap = null;
            if (response instanceof String) {
                statsMap = ((JsonResponseProcessor) responseProcessor).parseJson((String) response);
            } else if (response instanceof ClientResponse) {
                statsMap = ((VoltResponseProcessor) responseProcessor).process((ClientResponse) response);
            }

            return statsMap;
        } catch (Exception e) {
            LOG.error("Exception while executing procedure [" + procedureName + "] with parameter [" + parameter + "]", e);
        }
        return Maps.newHashMap();
    }
}
