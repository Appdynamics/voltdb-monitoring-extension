package com.appdynamics.monitors.voltdb;

import com.appdynamics.monitors.voltdb.client.Client;
import com.appdynamics.monitors.voltdb.client.ClientBuilder;
import com.appdynamics.monitors.voltdb.stats.IOStats;
import com.appdynamics.monitors.voltdb.stats.IndexStats;
import com.appdynamics.monitors.voltdb.stats.InitiatorStats;
import com.appdynamics.monitors.voltdb.stats.LiveClientsStats;
import com.appdynamics.monitors.voltdb.stats.MemoryStats;
import com.appdynamics.monitors.voltdb.stats.PartitionCountStats;
import com.appdynamics.monitors.voltdb.stats.PlannerStats;
import com.appdynamics.monitors.voltdb.stats.ProcedureInputStats;
import com.appdynamics.monitors.voltdb.stats.ProcedureOutputStats;
import com.appdynamics.monitors.voltdb.stats.ProcedureProfileStats;
import com.appdynamics.monitors.voltdb.stats.ProcedureStats;
import com.appdynamics.monitors.voltdb.stats.ReplicationStats;
import com.appdynamics.monitors.voltdb.stats.SnapshotStatusStats;
import com.appdynamics.monitors.voltdb.stats.TableStats;
import com.google.common.base.Strings;
import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * Monitor to get VoltDB stats and publish them to Controller
 */
public class VoltDBMonitor extends AManagedMonitor {

    private static final Logger LOG = Logger.getLogger(VoltDBMonitor.class);

    private static final String VOLTDB_HOST = "voltdb-host";
    private static final String VOLTDB_PORT = "voltdb-port";
    public static final String VOLTDB_USER = "user-name";
    public static final String VOLTDB_PASSWORD = "password";
    public static final String MODE = "mode";

    public VoltDBMonitor() {
        String details = VoltDBMonitor.class.getPackage().getImplementationTitle();
        String msg = "Using Monitor Version [" + details + "]";
        LOG.info(msg);
        System.out.println(msg);
    }

    /**
     * @param taskArgs
     * @param taskExecutionContext
     * @return
     * @throws TaskExecutionException
     */
    public TaskOutput execute(Map<String, String> taskArgs, TaskExecutionContext taskExecutionContext) throws TaskExecutionException {

        String voltDBHost = taskArgs.get(VOLTDB_HOST);
        String voltDBPort = taskArgs.get(VOLTDB_PORT);

        if (Strings.isNullOrEmpty(voltDBHost) || Strings.isNullOrEmpty(voltDBPort)) {
            String errorMessage = "null or empty values passed to voltdb-host or voltdb-port";
            LOG.error(errorMessage);
            throw new TaskExecutionException(errorMessage);
        }

        String voltDBUser = taskArgs.get(VOLTDB_USER);
        String voltDBPassword = taskArgs.get(VOLTDB_PASSWORD);

        String modeString = taskArgs.get(MODE);

        Mode mode = null;
        try {
            mode = Mode.get(modeString);
            LOG.info("Using " + mode.getMode() + " to get stats");
        } catch (EnumConstantNotPresentException e) {
            LOG.error("Invalid mode specified. Acepts only restAPI or clientAPI");
            throw new TaskExecutionException("Invalid mode specified. Acepts only restAPI or clientAPI");
        }

        Client<?> client = ClientBuilder.build(mode).getClient(voltDBHost, voltDBPort, voltDBUser, voltDBPassword);

        ProcedureExecutor procedureExecutor = new ProcedureExecutor(client);

        gatherAndPrintStats(procedureExecutor, mode);

        return new TaskOutput("VoltDB monitoring task completed successfully.");
    }

    /**
     * Gathers all the stats by calling procedures on VoltDB and prints the stats
     *
     * @param procedureExecutor
     * @param mode
     */
    private void gatherAndPrintStats(ProcedureExecutor procedureExecutor, Mode mode) throws TaskExecutionException {
        LOG.info("Gathering stats");
        ReplicationStats replicationStats = new ReplicationStats(procedureExecutor, mode);
        Map<String, Number> replicationStatsMap = replicationStats.fetch();
        printMetric(replicationStatsMap);

        IndexStats indexStats = new IndexStats(procedureExecutor, mode);
        Map<String, Number> indexStatsMap = indexStats.fetch();
        printMetric(indexStatsMap);

        InitiatorStats initiatorStats = new InitiatorStats(procedureExecutor, mode);
        Map<String, Number> initiatorStatsMap = initiatorStats.fetch();
        printMetric(initiatorStatsMap);

        IOStats ioStats = new IOStats(procedureExecutor, mode);
        Map<String, Number> ioStatsMap = ioStats.fetch();
        printMetric(ioStatsMap);

        LiveClientsStats liveClientsStats = new LiveClientsStats(procedureExecutor, mode);
        Map<String, Number> liveClientsStatsMap = liveClientsStats.fetch();
        printMetric(liveClientsStatsMap);

        MemoryStats memoryStats = new MemoryStats(procedureExecutor, mode);
        Map<String, Number> memoryStatsMap = memoryStats.fetch();
        printMetric(memoryStatsMap);

        PartitionCountStats partitionCountStats = new PartitionCountStats(procedureExecutor, mode);
        Map<String, Number> partitionCountStatsMap = partitionCountStats.fetch();
        printMetric(partitionCountStatsMap);

        PlannerStats plannerStats = new PlannerStats(procedureExecutor, mode);
        Map<String, Number> plannerStatsMap = plannerStats.fetch();
        printMetric(plannerStatsMap);

        ProcedureStats procedureStats = new ProcedureStats(procedureExecutor, mode);
        Map<String, Number> procedureStatsMap = procedureStats.fetch();
        printMetric(procedureStatsMap);

        ProcedureInputStats procedureInputStats = new ProcedureInputStats(procedureExecutor, mode);
        Map<String, Number> procedureInputStatsMap = procedureInputStats.fetch();
        printMetric(procedureInputStatsMap);

        ProcedureOutputStats procedureOutputStats = new ProcedureOutputStats(procedureExecutor, mode);
        Map<String, Number> procedureOutputStatsMap = procedureOutputStats.fetch();
        printMetric(procedureOutputStatsMap);

        ProcedureProfileStats procedureProfileStats = new ProcedureProfileStats(procedureExecutor, mode);
        Map<String, Number> procedureProfileStatsMap = procedureProfileStats.fetch();
        printMetric(procedureProfileStatsMap);

        SnapshotStatusStats snapshotStatusStats = new SnapshotStatusStats(procedureExecutor, mode);
        Map<String, Number> snapshotStatusStatsMap = snapshotStatusStats.fetch();
        printMetric(snapshotStatusStatsMap);

        TableStats tableStats = new TableStats(procedureExecutor, mode);
        Map<String, Number> tableStatsMap = tableStats.fetch();
        printMetric(tableStatsMap);

        LOG.info("Finished gathering stats");
    }

    /*private void printToConsole(Map<String, Number> stats) {
        for (Map.Entry<String, Number> stat : stats.entrySet()) {
            System.out.println(stat.getKey() + " : " + stat.getValue());
        }
    }*/

    /**
     * @param metrics
     */
    private void printMetric(Map<String, Number> metrics) {

        for (Map.Entry<String, Number> metric : metrics.entrySet()) {
            MetricWriter metricWriter = super.getMetricWriter(metric.getKey(), MetricWriter.METRIC_AGGREGATION_TYPE_AVERAGE, MetricWriter.METRIC_TIME_ROLLUP_TYPE_AVERAGE, MetricWriter.METRIC_CLUSTER_ROLLUP_TYPE_COLLECTIVE
            );
            Object metricValue = metric.getValue();
            if (metricValue instanceof BigDecimal) {
                metricWriter.printMetric(String.valueOf(((BigDecimal) metricValue).longValue()));
            } else if (metricValue instanceof Double) {
                metricWriter.printMetric(String.valueOf(Math.round((Double) metricValue)));
            } else if (metricValue instanceof Float) {
                metricWriter.printMetric(String.valueOf(Math.round((Float) metricValue)));
            } else {
                metricWriter.printMetric(String.valueOf(metricValue));
            }
        }
    }

    public static void main(String[] args) throws TaskExecutionException {

        Map<String, String> taskArgs = new HashMap<String, String>();
        taskArgs.put(VOLTDB_HOST, "localhost");
        taskArgs.put(VOLTDB_PORT, "8080");
        taskArgs.put(VOLTDB_USER, "admin");
        taskArgs.put(VOLTDB_PASSWORD, "voltdb");
        //taskArgs.put(MODE, "clientAPI");
        taskArgs.put(MODE, "restAPI");

        VoltDBMonitor voltDBMonitor = new VoltDBMonitor();
        voltDBMonitor.execute(taskArgs, null);

    }
}
