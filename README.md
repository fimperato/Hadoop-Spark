# Analisi sui dati con Hadoop - Spark 

## HDFS 
Filesystem distribuito con memorizzazione dati su commodity hardware (device compatibili in network per fornire maggiore potenza di elaborazione e memoria), e con una banda aggregata molto alta.

## Hadoop 2.8
Apache Hadoop: framework open-source per la memorizzazione e l'analisi distribuita di grandi quantità di dati.

* Yarn: gestore, presente in Hadoop, delle risorse computazionali sui cluster e assegnazione ad applicazioni 

## MapReduce 
Framework per la creazione di programmi in grado di processare dati in maniera distribuita su un cluster: 
* calcoli altamente parallelizzabili
* map - primo passo del paradigma: ogni nodo del cluster elabora la parte dei dati di interesse localmente
* reduce - secondo step del paradigma: raggruppamento su uno stesso nodo dei dati del primo step ed elaborazione secondo la logica del programma (eventuale reiterazione del paradigma)

## Spark 2.3 con Java
Sistema di in-memory computing distribuito (può effettuare operazioni direttamente in memoria centrale), più efficiente di Map-Reduce, con prestazioni superiori di 100 volte. 
Risulta più efficiente in special modo per operazioni complesse e ripetute su dataset, o per cicli di analisi dati di tipo iterativo, con sequenze di job map-reduce.

Spark è un computation framework indipendente da Hadoop (che è invece uno storage system distribuito, con gestione processi distrubuita su cluster di Yarn). 
La sorgente in input per Spark può essere: 
* HDFS
* Disco locale
* Database (via jdbc)
* Kafka topic (subscribe e topic consumer)

Elaborazione basata su:
* RDD (Resilent Distribuited Dataset): immutabile, soggetto a trasformazione/creazione, può rimanere in-memory o passare su disco (HDFS)
* Driver node: dove risiede e parte l'applicazione con la logica di processo Spark e lo Spark Context; manda in run gli Executor nei Worker node
* Cluster resource manager: esegue la trasformazione/creazione/processo di RDD (tipi di cluster resource manager che si occupano di schedulare e far partire i task: YARN, Mesos, Spark Standalone) 
* Master node: macchina dove risiede ed è in run, il cluster resource manager; richiede la creazione di Executors ai Worker node; invia le informazioni ottenute al Driver
* Worker node: nodi che lavorano in parallelo nel cluster, con i loro processi; inviano al master node le info richieste per lo Spark process principale.
