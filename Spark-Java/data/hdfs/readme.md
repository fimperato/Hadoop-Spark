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

### spostamento file con put:
hadoop fs -put C:/temp/prova.txt  /input/my_test_data/prova180515.txt

## copia da hdfs a locale su progetto
hdfs dfs -get /input/my_test_data/prova180515.txt C:\wsIdea\Spark_2018\SparkJava\data\hdfs\rw_test

## verifica effettuando il browse sul filesystem hdfs da:
http://localhost:50070/explorer.html#/input/my_test_data

[Administrator] winutils.exe chmod -R 777 C:\hadoop-2.7.6\logs

### To avoid: CreateSymbolicLink error (1314): A required privilege is not held by the client
- run Command Prompt in admin mode
- execute etc\hadoop\hadoop-env.cmd
- run %HADOOP_HOME%\sbin\start-dfs.cmd
- run %HADOOP_HOME%\sbin\start-yarn.cmd
- lancio di eventuale job, tramite jar file

## check result job, and folder output:
http://localhost:8088/cluster

http://localhost:50070/explorer.html

## check free space hdfs
hdfs dfs -df

## local dir, log dir 
hdfs dfs -mkdir /tmp/hadoop-Francesco/nm-local-dir
hdfs dfs -mkdir /dep/logs/userlogs

### check filesystem directory:
hdfs fsck /tmp/hadoop-Francesco/nm-local-dir
hdfs fsck /dep/logs/userlogs

#### note for 'local-dirs are bad' error:
 - Inserire questa property in "yarn-site.xml". Questo dovrebbe risolvere l'errore:

<property>
  <name>yarn.nodemanager.disk-health-checker.max-disk-utilization-per-disk-percentage</name>
  <value>98.5</value>
</property>

La più comune causa di 'local-dirs are bad' è data dall'available disk space del nodo, 
che eccede il valore dello yarn max-disk-utilization-per-disk-percentage di default, pari al 90.0%.

 - Se viene settato il path del local dir, custom, ad es. con: 
 > file:\\\C:\hadoop\tmp\hdp\nm-local-dir\usercache\Francesco\appcache
 
 Allora settare anche il path dei file temporanei per i mapreduce job:
 > file:\\\C:\hadoop\tmp\hdp\nm-local-dir\usercache\Francesco\appcache
 

### check dir folder permission per una data folder:
> hdfs dfs -mkdir /tmp/hdp/nm-local-dir/usercache/Francesco/appcache
> hdfs dfs -ls /tmp/hdp/nm-local-dir/usercache/Francesco/appcache
> hdfs dfs -chown Francesco /tmp/hdp/nm-local-dir/usercache/Francesco/appcache 
> hdfs dfs -chmod 2750 /tmp/hdp/nm-local-dir/usercache/Francesco/appcache

### set permission sulla hadoop browser view per una data folder:
> hdfs dfs -chown -R dr.who /tmp/hdp/nm-local-dir/usercache/Francesco/appcache
> hdfs dfs -chmod -R u+rX /tmp/hdp/nm-local-dir/usercache/Francesco/appcache

### set permission per tutti gli user per una data folder:
> hdfs dfs -chmod -R 777 /tmp/hdp/nm-local-dir/usercache/Francesco/appcache
> hdfs dfs -chmod -R 777 /tmp/hadoop-Francesco/nm-local-dir/usercache/Francesco/appcache
