package com.appdynamics.monitors.voltdb.client;

import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

public class VoltClient implements Client<ClientResponse> {

    private static final Logger LOG = Logger.getLogger(RestClient.class);

    private org.voltdb.client.Client client;

    public VoltClient(String host, String port, String user, String password) throws TaskExecutionException {
        ClientConfig config = new ClientConfig(user, password);
        client = ClientFactory.createClient(config);
        try {
            client.createConnection(host, Integer.parseInt(port));
        } catch (IOException e) {
            LOG.error("Unable to connect to voltdb", e);
            throw new TaskExecutionException("Unable to connect to voltdb", e);
        }
    }

    public ClientResponse callProcedure(String procedureName, String parameter) throws TaskExecutionException {
        ClientResponse clientResponse = null;
        try {
            clientResponse = client.callProcedure(procedureName, parameter);
        } catch (IOException e) {
            LOG.error("Exception while executing procedure [" + procedureName + "] with parameter [" + parameter + "]", e);
            throw new TaskExecutionException("Exception while executing procedure [" + procedureName + "] with parameter [" + parameter + "]", e);
        } catch (ProcCallException e) {
            LOG.error("Exception while executing procedure [" + procedureName + "] with parameter [" + parameter + "]", e);
            throw new TaskExecutionException("Exception while executing procedure [" + procedureName + "] with parameter [" + parameter + "]", e);
        }
        return clientResponse;
    }

    public void release() {
        try {
            client.drain();
            client.close();
        } catch (NoConnectionsException e) {
            LOG.error("Unable to close the connection", e);
        } catch (InterruptedException e) {
            LOG.error("Unable to close the connection", e);
        }
    }

    
}
