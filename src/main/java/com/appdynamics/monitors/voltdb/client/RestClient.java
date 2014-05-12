package com.appdynamics.monitors.voltdb.client;

import com.google.common.base.Strings;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import java.io.IOException;
import java.text.MessageFormat;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class RestClient implements Client<String> {

    private static final Logger LOG = Logger.getLogger(RestClient.class);

    private static String procedureURL = "http://{0}:{1}/api/1.0/?Procedure={2}&Parameters=[%22{3}%22,0]";

    private String host;
    private String port;
    private String user;
    private String password;

    public RestClient(String host, String port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public String callProcedure(String procedureName, String parameter) throws TaskExecutionException {

        String url = getUrl(procedureURL, procedureName, parameter);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Executing GET request " + url);
        }
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        String resp = null;
        try {
            resp = execute(httpClient, get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    private String getUrl(String procedureURL, String procedureName, String parameter) {

        String formatURL = MessageFormat.format(procedureURL, host, port, procedureName, parameter);
        if (!Strings.isNullOrEmpty(user)) {
            formatURL += "&User=" + user;
        }

        if (!Strings.isNullOrEmpty(password)) {
            formatURL += "&Password=" + password;
        }
        return formatURL;
    }

    public static void main(String[] args) {
        RestClient restClient = new RestClient("localhost", "80", "aaa", "bbb");
        String url = restClient.getUrl(procedureURL, "ccc", "ddd");
        System.out.println(url);
    }

    private String execute(HttpClient httpClient, HttpRequestBase req) throws TaskExecutionException, IOException {
        HttpResponse response = httpClient.execute(req);
        if (response.getStatusLine().getStatusCode() >= 300) {
            throw new TaskExecutionException(EntityUtils.toString(response.getEntity()));
        }

        if (response.getStatusLine().getStatusCode() == 204) {
            return "";
        }
        return EntityUtils.toString(response.getEntity());
    }
}
