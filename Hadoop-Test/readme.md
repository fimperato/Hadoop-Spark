## The Hadoop FS command line:

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

#### spostamento file con put (utility), da local file system ad HDFS:
hadoop fs -put C:/temp/prova.txt  /input/my_test_data/prova180515.txt

## copia da hdfs a locale su progetto
hdfs dfs -get /input/my_test_data/prova180515.txt C:\wsIdea\Spark_2018\Hadoop-Test\data\hdfs\rw_test

## verifica effettuando il browse sul filesystem hdfs da:
http://localhost:50070/explorer.html#/input/my_test_data

[Administrator] winutils.exe chmod -R 777 C:\hadoop-2.7.6\logs

#### To avoid: CreateSymbolicLink error (1314): A required privilege is not held by the client
- run Command Prompt in admin mode
- execute etc\hadoop\hadoop-env.cmd
- run %HADOOP_HOME%\sbin\start-dfs.cmd
- run %HADOOP_HOME%\sbin\start-yarn.cmd
- lancio di eventuale job, tramite jar file

## check result job, and folder output:
http://localhost:8088/cluster

http://localhost:50070/explorer.html

## check free space hdfs: 
Sintesi sullo spazio dell'intero file system HDFS
    
    hdfs dfs -df

## local dir, log dir 

    hdfs dfs -mkdir /tmp/hadoop-Francesco/nm-local-dir
    hdfs dfs -mkdir /dep/logs/userlogs

## create dir con folder interne:
Per evitare errori su creazioni di nested folder quando non esistono ancora, usare l'opzione -p

    hdfs dfs -mkdir -p /deep/tmp/folder

#### check filesystem directory:

    hdfs fsck /tmp/hadoop-Francesco/nm-local-dir
    hdfs fsck /dep/logs/userlogs

##### note for 'local-dirs are bad' error:
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
 

#### check dir folder permission per una data folder:
> hdfs dfs -mkdir /tmp/hdp/nm-local-dir/usercache/Francesco/appcache
> hdfs dfs -ls /tmp/hdp/nm-local-dir/usercache/Francesco/appcache
> hdfs dfs -chown Francesco /tmp/hdp/nm-local-dir/usercache/Francesco/appcache 
> hdfs dfs -chmod 2750 /tmp/hdp/nm-local-dir/usercache/Francesco/appcache

#### set ownership, ricorsivamente, sulla hadoop browser view user (dr.who) per una data folder:
> hdfs dfs -chown -R dr.who /tmp/hdp/nm-local-dir/usercache/Francesco/appcache
#### set permission, ricorsivamente, sulla hadoop browser view user (dr.who) per una data folder:
> hdfs dfs -chmod -R 744 /tmp/hdp/nm-local-dir/usercache/Francesco/appcache
> "hdfs dfs -chmod 774" / oppure: > "hdfs dfs -chmod 747 /" per permettere la cancellazione di cartelle da browser view 


#### set permission per tutti gli user per una data folder (ricorsivamente -R su tutti i file interni):
> hdfs dfs -chmod -R 777 /tmp/hdp/nm-local-dir/usercache/Francesco/appcache
> hdfs dfs -chmod -R 777 /tmp/hadoop-Francesco/nm-local-dir/usercache/Francesco/appcache

#### check spazio consumato per tutte le repliche del file:
> hdfs dfs -du -h /output

#### verifica del contenuto per un file remoto su HDFS:
> hdfs dfs -cat /testo-prova.txt

##### NOTE / KEY WORD:
* Commodity hardware: hardware accessibile/a costo non elevato, senza particolari richieste di performance. 
In Hadoop utilizzato per lavoro in parallelo. Legato allo scaling orizzontale, invece che ad uno scaling verticale.

* Operazioni di modifica su file system distribuito (GFS, HDFS): semplifica l'implementazione del DFS, 
e viene incontro al pattern di uso dei dati 'write once, read many'.

* Split dei file in blocchi tra i server in un DFS: permette la distruzione dei dati uniforme sui server DFS.

* Contenuto dei namenode metadata: locazione dei blocchi di file, data di creazione del file, permessi.

* Per un client HDFS è necessario sia possibile l'accesso a tutti i server per la lettura dei files.

* La protezione, implementata in HDFS, di file di importanza maggiore, può avvenire incrementando il fattore 
di replica per questo file e restringendo i permessi sul file.

* Il restore avviene prima sul server relativo al Namenode e successivamente sui server dei Datanode.

* In HDFS la grandezza dei blocchi dopende dalla RAM del Namenode, dal rapporto tra il tempo di seek 
e il tempo di lettura per il blocco, e non dipende invece dalla grandezza blocchi per datanode filesystem locale.

##### Hadoop federation 

Permette di scalare orizzontalmente (architettura scale-out) il service name con Namenode aggiuntivi. 
Usa diversi namenode or namespace che sono independenti l'uno dall'altro, non è necessaria la coordinazione tra i namenode.
 * Questi namenode indipendenti sono federati i.e. non richiedono una coordinazione tra di loro.
 * Previene la possibilità di crash di sistema nel caso di fault su singolo Namenode: il fault 
 su singolo Namenode non impedisce ai Datanode di servire gli altri Namenode dell'intero cluster. 
 * Permette la caratteristica di isolamento, ad es. nel caso in cui gruppi diversi di utenti accedono 
al FS potrebbero usare separati, e isolati tra loro, Namenode.
