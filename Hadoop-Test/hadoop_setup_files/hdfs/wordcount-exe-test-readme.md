## hadoop wordcount test:

- run 
> %HADOOP_HOME%\sbin\start-dfs.cmd

- run 
> %HADOOP_HOME%\sbin\start-yarn.cmd

- creo la cartella e file di input e lancio il job per l'output
> hdfs dfs -mkdir /input_dir
> hadoop fs -put C:/data/input_file.txt /input_dir
> hdfs dfs -ls /input_dir/
> hdfs dfs -cat /input_dir/input_file.txt
> hdfs dfs -rm -r /output_dir
> hadoop jar C:/wsIdea/Spark_2018/Hadoop-Test/eseguibili/MapReduceClient.jar wordcount /input_dir /output_dir

## check result job, and folder output:
   
   http://localhost:50070/explorer.html#/output_dir
   
   http://localhost:8088/cluster

### Console result:

    map 0% reduce 0%
    map 100% reduce 0%
    map 100% reduce 100%
    Job job_1526717038199_0001 completed successfully
    INFO mapreduce.Job: Counters: 49
        File System Counters
                FILE: Number of bytes read=195
                FILE: Number of bytes written=278615
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=1998
                HDFS: Number of bytes written=120
                HDFS: Number of read operations=6
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
        Job Counters
                Launched map tasks=1
                Launched reduce tasks=1
                Data-local map tasks=1
                Total time spent by all maps in occupied slots (ms)=2990
                Total time spent by all reduces in occupied slots (ms)=3118
                Total time spent by all map tasks (ms)=2990
                Total time spent by all reduce tasks (ms)=3118
                Total vcore-milliseconds taken by all map tasks=2990
                Total vcore-milliseconds taken by all reduce tasks=3118
                Total megabyte-milliseconds taken by all map tasks=3061760
                Total megabyte-milliseconds taken by all reduce tasks=3192832
        Map-Reduce Framework
                Map input records=30
                Map output records=390
                Map output bytes=2730
                Map output materialized bytes=195
                Input split bytes=110
                Combine input records=390
                Combine output records=21
                Reduce input groups=21
                Reduce shuffle bytes=195
                Reduce input records=21
                Reduce output records=21
                Spilled Records=42
                Shuffled Maps =1
                Failed Shuffles=0
                Merged Map outputs=1
                GC time elapsed (ms)=87
                CPU time spent (ms)=873
                Physical memory (bytes) snapshot=497344512
                Virtual memory (bytes) snapshot=621248512
                Total committed heap usage (bytes)=348651520
        Shuffle Errors
                BAD_ID=0
                CONNECTION=0
                IO_ERROR=0
                WRONG_LENGTH=0
                WRONG_MAP=0
                WRONG_REDUCE=0
        File Input Format Counters
                Bytes Read=1888
        File Output Format Counters
                Bytes Written=120