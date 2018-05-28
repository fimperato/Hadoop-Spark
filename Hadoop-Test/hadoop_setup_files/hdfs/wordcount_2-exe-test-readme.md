## hadoop wordcount test:

- run 
> %HADOOP_HOME%\sbin\start-dfs.cmd

- run 
> %HADOOP_HOME%\sbin\start-yarn.cmd

- creo la cartella e file di input e lancio il job per l'output

> hdfs dfs -mkdir /input/my_test_data
> hadoop fs -put C:\wsIdea\Spark_2018\Hadoop-Test\data\hdfs\rw_test\prova180515.txt  /input/my_test_data/prova180515.txt
> hadoop fs -put C:\wsIdea\Spark_2018\Hadoop-Test\data\hdfs\rw_test\prova180519.txt  /input/my_test_data/prova180519.txt
> hdfs dfs -ls /input/my_test_data
> hdfs dfs -cat /input/my_test_data/prova180519.txt

> hdfs dfs -rm -r /output/my_test_data
> hadoop jar C:\wsIdea\Spark_2018\Hadoop-Test\target\hadoop-test-java.jar /input/my_test_data /output/my_test_data

## check result job, and folder output:
   
   http://localhost:50070/explorer.html#/output/my_test_data
   
   http://localhost:8088/cluster

### Console result:

        map 0% reduce 0%
        map 100% reduce 0%
        map 100% reduce 100%
        Job job_1526717038199_0002 completed successfully
        Counters: 49
        File System Counters
                FILE: Number of bytes read=1801
                FILE: Number of bytes written=421093
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=1190
                HDFS: Number of bytes written=1136
                HDFS: Number of read operations=9
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Launched map tasks=2
                Launched reduce tasks=1
                Data-local map tasks=2
                Total time spent by all maps in occupied slots (ms)=9390
                Total time spent by all reduces in occupied slots (ms)=3020
                Total time spent by all map tasks (ms)=9390
                Total time spent by all reduce tasks (ms)=3020
                Total vcore-milliseconds taken by all map tasks=9390
                Total vcore-milliseconds taken by all reduce tasks=3020
                Total megabyte-milliseconds taken by all map tasks=9615360
                Total megabyte-milliseconds taken by all reduce tasks=3092480
        Map-Reduce Framework
                Map input records=12
                Map output records=143
                Map output bytes=1509
                Map output materialized bytes=1807
                Input split bytes=240
                Combine input records=0
                Combine output records=0
                Reduce input groups=127
                Reduce shuffle bytes=1807
                Reduce input records=143
                Reduce output records=127
                Spilled Records=286
                Shuffled Maps =2
                Failed Shuffles=0
                Merged Map outputs=2
                GC time elapsed (ms)=165
                CPU time spent (ms)=1262
                Physical memory (bytes) snapshot=789004288
                Virtual memory (bytes) snapshot=950517760
                Total committed heap usage (bytes)=549978112
        Shuffle Errors
                BAD_ID=0
                CONNECTION=0
                IO_ERROR=0
                WRONG_LENGTH=0
                WRONG_MAP=0
                WRONG_REDUCE=0
        File Input Format Counters
                Bytes Read=950
        File Output Format Counters
                Bytes Written=1136