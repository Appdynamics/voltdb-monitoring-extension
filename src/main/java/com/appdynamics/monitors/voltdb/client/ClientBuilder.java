package com.appdynamics.monitors.voltdb.client;

import com.appdynamics.monitors.voltdb.Mode;
import com.google.common.base.Preconditions;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;

public class ClientBuilder {
    private Mode mode;

    private ClientBuilder(Mode mode) {
        this.mode = mode;
    }

    public static ClientBuilder build(Mode mode) {
        return new ClientBuilder(mode);
    }

    public Client<?> getClient(String host, String port, String user, String password) throws TaskExecutionException {
        Preconditions.checkArgument(mode != null, "Mode can not be null");
        if (Mode.ClientAPI.equals(mode)) {
            return new VoltClient(host, port, user, password);
        } else {
            return new RestClient(host, port, user, password);
        }
    }
}
