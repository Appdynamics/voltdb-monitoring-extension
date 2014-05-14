# VoltDB Monitoring Extension  

##Use Case

The VoltDB Monitoring Extension collects the stats from VoltDB and reports them to the AppDynamics Controller.

This extension works only with the standalone machine agent.

##Installation
1. Run 'mvn clean install' from the voltdb-monitoring-extension directory
2. Download the file VoltDBMonitor.zip found in the 'target' directory into \<machineagent install dir\>/monitors/
3. Unzip the downloaded file and cd into VoltDBMonitor
4. Open the monitor.xml file and provide values for voltdb-host, voltdb-port, user-name, password and mode
5. Restart the Machine Agent.
6. In the AppDynamics controller, look for events in Custom Metrics|VoltDB|

##Directory Structure

<table><tbody>
<tr>
<th align="left"> Directory/File </th>
<th align="left"> Description </th>
</tr>
<tr>
<td class='confluenceTd'> src/main/java </td>
<td class='confluenceTd'> Contains source code to VoltDB Monitoring Extension  </td>
</tr>
<tr>
<td class='confluenceTd'> src/main/resources </td>
<td class='confluenceTd'> Contains monitor.xml </td>
</tr>
<tr>
<td class='confluenceTd'> target </td>
<td class='confluenceTd'> Only obtained when using maven. Run 'maven clean install' to get the distributable .zip file </td>
</tr>
<tr>
<td class='confluenceTd'> pom.xml </td>
<td class='confluenceTd'> Maven script file (required only if changing Java code) </td>
</tr>
</tbody>
</table>

##XML Examples

###  monitor.xml


| Param | Description |
| ----- | ----- |
| voltdb-host | VoltDB host  |
| voltdb-port | VoltDb port. For client API default port is 21212 and for REST API default port is 8080 |
| user-name | User who has permission to execute system procedures |
| password | password |
| mode | Mode in which user wants to gather the stats. Possible values are clientAPI/restAPI |

~~~~
<monitor>
        <name>VoltDBMonitor</name>
        <type>managed</type>
        <description>Volt DB monitor</description>
        <monitor-configuration></monitor-configuration>
        <monitor-run-task>
                <execution-style>periodic</execution-style>
                <execution-frequency-in-seconds>60</execution-frequency-in-seconds>
                <name>VoltDB Monitor Run Task</name>
                <display-name>VoltDB Monitor Task</display-name>
                <description>VoltDB Monitor Task</description>
                <type>java</type>
                <execution-timeout-in-secs>60</execution-timeout-in-secs>
                <task-arguments>
                    <argument name="voltdb-host" is-required="true" default-value="localhost" />
                    <argument name="voltdb-port" is-required="true" default-value="21212" />
                    <!--User should have access to execute system procedures -->
                    <argument name="user-name" is-required="false" default-value="admin" />
                    <argument name="password" is-required="false" default-value="voltdb" />
                    <!--Mode through which you want to fetch stats. Possible values [clientAPI/restAPI] -->
                    <argument name="mode" is-required="false" default-value="clientAPI" />
		       </task-arguments>
                <java-task>
                    <classpath>voltdb-monitoring-extension.jar;</classpath>
                    <impl-class>com.appdynamics.monitors.voltdb.VoltDBMonitor</impl-class>
                </java-task>
        </monitor-run-task>
</monitor>
~~~~

##VoltDB

###Steps to secure VoltDB with username and password
1. Create Role in VoltDB
  ...To create a role user has to execute the below query </br>
  ...CREATE ROLE admin WITH sysproc,adhoc,DEFAULTPROC; </br>
  ...The above query createa role with admin with sysproc, adhoc and DEFAULTPROC permissions. </br>
2. In the deployment.xml add the following lines 
  ~~~~
  <security enabled="true"/> 
  <users>
    <user name="admin" password="voltdb" roles="admin"/>
  </users>
  ~~~~
  ...It will enable security and you can only access the VoltDB with user 'admin' and password 'voltdb'. 


##Metrics

###Index
Metrics related to Index

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/Index/{SITE_ID}/{PARTITION_ID}/{TABLE_NAME}/{INDEX_NAME}/ENTRY_COUNT | The number of index entries currently in the partition  |
| Custom Metrics/VoltDB/Stats/Index/{SITE_ID}/{PARTITION_ID}/{TABLE_NAME}/{INDEX_NAME}/IS_COUNTABLE | A byte value specifying whether the index maintains a counter to optimize COUNT(*) queries  |
| Custom Metrics/VoltDB/Stats/Index/{SITE_ID}/{PARTITION_ID}/{TABLE_NAME}/{INDEX_NAME}/IS_UNIQUE | A byte value specifying whether the index is unique (1) or not (0) |
| Custom Metrics/VoltDB/Stats/Index/{SITE_ID}/{PARTITION_ID}/{TABLE_NAME}/{INDEX_NAME}/MEMORY_ESTIMATE | The estimated amount of memory (in kilobytes) consumed by the current index entries |

###IOSTATS
Metrics related to IO operations

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/IO/{CONNECTION_HOSTNAME}/{CONNECTION_ID}/BYTES_READ | The number of bytes of data sent from the client to the host  |
| Custom Metrics/VoltDB/Stats/IO/{CONNECTION_HOSTNAME}/{CONNECTION_ID}/BYTES_WRITTEN | The number of bytes of data sent from the host to the client  |
| Custom Metrics/VoltDB/Stats/IO/{CONNECTION_HOSTNAME}/{CONNECTION_ID}/CONNECTION_ID | Numeric ID of the client connection invoking the procedure  |
| Custom Metrics/VoltDB/Stats/IO/{CONNECTION_HOSTNAME}/{CONNECTION_ID}/MESSAGES_READ | The number of individual messages sent from the client to the host |
| Custom Metrics/VoltDB/Stats/IO/{CONNECTION_HOSTNAME}/{CONNECTION_ID}/MESSAGES_WRITTEN | The number of individual messages sent from the host to the client |

###LIVECLIENTS
Metrics related to client connections currently active on the cluster

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/Live Clients/{CLIENT_HOSTNAME}/ADMIN | A byte value specifying whether the connection is to the client port (0) or the admin port (1) |
| Custom Metrics/VoltDB/Stats/Live Clients/{CLIENT_HOSTNAME}/CONNECTION_ID | Numeric ID of the client connection invoking the procedure  |
| Custom Metrics/VoltDB/Stats/Live Clients/{CLIENT_HOSTNAME}/OUTSTANDING_​REQUEST_BYTES | The number of bytes of data sent from the client currently pending on the host  |
| Custom Metrics/VoltDB/Stats/Live Clients/{CLIENT_HOSTNAME}/OUTSTANDING_​RESPONSE_MESSAGES | The number of messages on the host queue waiting to be retrieved by the client  |
| Custom Metrics/VoltDB/Stats/Live Clients/{CLIENT_HOSTNAME}/OUTSTANDING_ ​TRANSACTIONS | The number of transactions (that is, stored procedures) initiated on behalf of the client that have yet to be completed |

###MEMORY
Metrics related to server in the cluster

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/Memory/{HOSTNAME}/INDEXMEMORY | The amount of memory (in kilobytes) currently in use for storing database indexes  |
| Custom Metrics/VoltDB/Stats/Memory/{HOSTNAME}/JAVAUNUSED | The amount of memory (in kilobytes) allocated by Java but unused. (In other words, free space in the Java heap.)  |
| Custom Metrics/VoltDB/Stats/Memory/{HOSTNAME}/JAVAUSED | The amount of memory (in kilobytes) allocated by Java and currently in use by VoltDB |
| Custom Metrics/VoltDB/Stats/Memory/{HOSTNAME}/POOLEDMEMORY | The total size of memory (in megabytes) allocated for tasks other than database records, indexes, and strings  |
| Custom Metrics/VoltDB/Stats/Memory/{HOSTNAME}/RSS | The current resident set size. That is, the total amount of memory allocated to the VoltDB processes on the server |
| Custom Metrics/VoltDB/Stats/Memory/{HOSTNAME}/STRINGMEMORY | The amount of memory (in kilobytes) currently in use for storing string and binary data that is not stored "in-line" in the database record  |
| Custom Metrics/VoltDB/Stats/Memory/{HOSTNAME}/TUPLEALLOCATED | The amount of memory (in kilobytes) allocated for the storage of database records (including free space)  |
| Custom Metrics/VoltDB/Stats/Memory/{HOSTNAME}/TUPLECOUNT | The total number of database records currently in memory  |
| Custom Metrics/VoltDB/Stats/Memory/{HOSTNAME}/TUPLEDATA | The amount of memory (in kilobytes) currently in use for storing database records |

###PARTITIONCOUNT
Metrics related to total number of partitions and the host

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/Partition Count/{HOSTNAME}|PARTITION_COUNT | The number of unique or logical partitions on the cluster  |

###PROCEDURE
Metrics related to every stored procedure that has been executed on the cluster

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/ABORTS | The number of times the procedure was aborted  |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/AVG_EXECUTION_TIME | The average length of time (in nanoseconds) it took to execute the stored procedure  |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/AVG_PARAMETER​_SET_SIZE | The average size (in bytes) of the parameters passed as input to the procedure   |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/AVG_RESULT_SIZE | The average size (in bytes) of the results returned by the procedure  |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/FAILURES | The number of times the procedure failed unexpectedly  |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/INVOCATIONS | The total number of invocations of this procedure at this site  |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/MAX_EXECUTION_TIME	 | The maximum length of time (in nanoseconds) it took to execute the stored procedure  |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/MAX_PARAMETER​_SET_SIZE | The maximum size (in bytes) of the parameters passed as input to the procedure  |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/MAX_RESULT_SIZE | The maximum size (in bytes) of the results returned by the procedure  |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/MIN_EXECUTION_TIME | The minimum length of time (in nanoseconds) it took to execute the stored procedure  |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/MIN_PARAMETER​_SET_SIZE | The minimum size (in bytes) of the parameters passed as input to the procedure  |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/MIN_RESULT_SIZE | The minimum size (in bytes) of the results returned by the procedures  |
| Custom Metrics/VoltDB/Stats/Procedure/{PROCEDURE}/TIMED_INVOCATIONS | The number of invocations used to measure the minimum, maximum, and average execution time  |

###PROCEDUREINPUT
Metrics related to every stored procedure that has been executed on the cluster

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/Procedure Input/{PROCEDURE}/AVG_PARAMETER_SET_SIZE | The average parameter set size in bytes   |
| Custom Metrics/VoltDB/Stats/Procedure Input/{PROCEDURE}/INVOCATIONS | The total number of invocations of this procedure |
| Custom Metrics/VoltDB/Stats/Procedure Input/{PROCEDURE}/MAX_PARAMETER​_SET_SIZE | The maximum parameter set size in bytes  |
| Custom Metrics/VoltDB/Stats/Procedure Input/{PROCEDURE}/MIN_PARAMETER​_SET_SIZE | The minimum parameter set size in bytes  |
| Custom Metrics/VoltDB/Stats/Procedure Input/{PROCEDURE}/TOTAL_PARAMETER​_SET_SIZE_MB | The total input for all invocations of this stored procedure measured in megabytes |

###PROCEDUREOUTPUT
Metrics related to every stored procedure that has been executed on the cluster

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/Procedure Output/{PROCEDURE}/AVG_RESULT_SIZE | The average result set size in bytes  |
| Custom Metrics/VoltDB/Stats/Procedure Output/{PROCEDURE}/INVOCATIONS | The total number of invocations of this procedure |
| Custom Metrics/VoltDB/Stats/Procedure Output/{PROCEDURE}/MAX_RESULT_SIZE | The maximum result set size in bytes |
| Custom Metrics/VoltDB/Stats/Procedure Output/{PROCEDURE}/MIN_RESULT_SIZE | The minimum result set size in bytes  |
| Custom Metrics/VoltDB/Stats/Procedure Output/{PROCEDURE}/TOTAL_RESULT​_SIZE_MB | The total output returned by all invocations of this stored procedure measured in megabytes  |

###PROCEDUREPROFILE
Metrics related to every stored procedure that has been executed on the cluster

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/Procedure Profile/{PROCEDURE}/ABORTS | The number of times the procedure was aborted |
| Custom Metrics/VoltDB/Stats/Procedure Profile/{PROCEDURE}/AVG | The average length of time (in nanoseconds) it took to execute the stored procedure |
| Custom Metrics/VoltDB/Stats/Procedure Profile/{PROCEDURE}/FAILURES | The number of times the procedure failed unexpectedly |
| Custom Metrics/VoltDB/Stats/Procedure Profile/{PROCEDURE}/INVOCATIONS | The total number of invocations of this procedure |
| Custom Metrics/VoltDB/Stats/Procedure Profile/{PROCEDURE}/MAX | The maximum length of time (in nanoseconds) it took to execute the stored procedure |
| Custom Metrics/VoltDB/Stats/Procedure Profile/{PROCEDURE}/MIN | The minimum length of time (in nanoseconds) it took to execute the stored procedure |


###REPLICATION(DR)
Metrics to shows the current state of replication and how much data is currently queued for the DR agent

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/Replication/{PARTITION_ID}/LASTACKTIMESTAMP | The timestamp of the last acknowledgement received from the DR agent |
| Custom Metrics/VoltDB/Stats/Replication/{PARTITION_ID}/TOTALBUFFERS | The total number of buffers in this partition currently waiting for acknowledgement from the DR agent |
| Custom Metrics/VoltDB/Stats/Replication/{PARTITION_ID}/TOTALBYTES | The total number of bytes currently queued for transmission to the DR agent |
| Custom Metrics/VoltDB/Stats/Replication/{PARTITION_ID}/TOTALBYTES​IN​MEMORY | The total number of bytes of queued data currently held in memory |

###SNAPSHOTSTATUS
Metrics to shows every snapshot file in the recent snapshots performed on the cluster

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/Snapshot Status/{HOSTNAME}/{TABLE}/DURATION | The length of time (in milliseconds) it took to complete the snapshot |
| Custom Metrics/VoltDB/Stats/Snapshot Status/{HOSTNAME}/{TABLE}/END_TIME | The timestamp when the snapshot was completed (in milliseconds) |
| Custom Metrics/VoltDB/Stats/Snapshot Status/{HOSTNAME}/{TABLE}/SIZE | The total size, in bytes, of the file |
| Custom Metrics/VoltDB/Stats/Snapshot Status/{HOSTNAME}/{TABLE}/START_TIME | The timestamp when the snapshot began (in milliseconds) |
| Custom Metrics/VoltDB/Stats/Snapshot Status/{HOSTNAME}/{TABLE}/THROUGHPUT | The average number of bytes per second written to the file during the snapshot process |
| Custom Metrics/VoltDB/Stats/Snapshot Status/{HOSTNAME}/{TABLE}/TXNID | The transaction ID of the snapshot |

###TABLE
Metrics related to every table, per partition

| Name | Description |
| ----- | ----- |
| Custom Metrics/VoltDB/Stats/Table/{SITE_ID}/{PARTITION_ID}/{TABLE_NAME}/PERCENT_FULL | The percentage of the row limit currently in use by table rows in this partition |
| Custom Metrics/VoltDB/Stats/Table/{SITE_ID}/{PARTITION_ID}/{TABLE_NAME}/STRING_DATA​_MEMORY | The total memory, in kilobytes, used for storing non-inline variable length data |
| Custom Metrics/VoltDB/Stats/Table/{SITE_ID}/{PARTITION_ID}/{TABLE_NAME}/TUPLE_ALLOCATED​_MEMORY | The total size of memory, in kilobytes, allocated for storing inline data associated with this table in this partition |
| Custom Metrics/VoltDB/Stats/Table/{SITE_ID}/{PARTITION_ID}/{TABLE_NAME}/TUPLE_COUNT | The number of rows currently stored for this table in the current partition |
| Custom Metrics/VoltDB/Stats/Table/{SITE_ID}/{PARTITION_ID}/{TABLE_NAME}/TUPLE_DATA_MEMORY | The total memory, in kilobytes, used for storing inline data associated with this table in this partition |
| Custom Metrics/VoltDB/Stats/Table/{SITE_ID}/{PARTITION_ID}/{TABLE_NAME}/TUPLE_LIMIT | The row limit for this table |

For more info on stats please visit http://voltdb.com/docs/UsingVoltDB/sysprocstatistics.php


## Custom Dashboard ##
![](https://github.com/Appdynamics/voltdb-monitoring-extension/raw/master/voltdb-custom-dashboard.png)

##Contributing

Always feel free to fork and contribute any changes directly via [GitHub](https://github.com/Appdynamics/voltdb-monitoring-extension).

##Community

Find out more in the [AppSphere]() community.

##Support

For any questions or feature request, please contact [AppDynamics Center of Excellence](mailto:ace-request@appdynamics.com).

