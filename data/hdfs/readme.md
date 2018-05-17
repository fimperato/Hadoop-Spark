## The hadoop fs command line:

hadoop fs <arguments>

FS relates to any generic file system which could be local, HDFS, DFTP, S3 FS etc.. 
Most of the Hadoop distributions are configured to default to HDFS  if you specify hadoop fs. 
You can explicitly specify HDFS in the hadoop command line, like so:
 
hadoop dfs <arguments>

This explicitly specifies HDFS, and would work for any HDFS operations.
However, this command has been deprecated. You should use the hdfs dfs syntax instead, as is shown below:
 
hdfs dfs <arguments>

This syntax works for all operations against HDFS in it is the recommend command to use instead of hadoop fs or hadoop dfs. 
Indeed, if you use the hadoop dfs syntax, it will locate hdfs and delegate the command to hdfs dfs.


## creazione cartella hdsf
c:\hadoop-2.7.6\bin>hdfs dfs -mkdir /input
hdfs dfs -mkdir /output

hdfs dfs -mkdir /input/my_test_data
hdfs dfs -mkdir /output/my_test_data

## lo stesso per hdfs per sintassi URL
hdfs dfs -mkdir hdfs://sandbox.hortonworks.com/fimperato/my_test_data

## copia da locale a hdsf e verifica item copiato
hadoop fs -copyFromLocal C:/temp/prova.txt  /input/my_test_data/prova180515.txt

hdfs dfs -ls /input/my_test_data

### altro:
hadoop fs -put C:/temp/prova.txt  /input/my_test_data/prova180515.txt

## copia da hdfs a locale su progetto
hdfs dfs -get /input/my_test_data/prova180515.txt C:\wsIdea\Spark_2018\SparkJava\data\hdfs\rw_test

## verifica effettuando il browse sul filesystem hdfs da:
http://localhost:50070/explorer.html#/input/my_test_data

[Administrator] winutils.exe chmod -R 777 C:\hadoop-2.7.6\logs

### To avoid: CreateSymbolicLink error (1314): A required privilege is not held by the client
- run Command Prompt in admin mode
- execute etc\hadoop\hadoop-env.cmd
- run sbin\start-dfs.cmd
- run sbin\start-yarn.cmd
- let's start job, with jar file, i.e.:
  [HADOOP_HOME]/bin>hdfs dfs -rm -r /output/my_test_data
  [HADOOP_HOME]/bin>hadoop jar C:\wsIdea\Spark_2018\SparkJava\target\spark-java.jar /input/my_test_data /output/my_test_data

## check result job, and folder output:
http://localhost:8088/cluster

http://localhost:50070/explorer.html
