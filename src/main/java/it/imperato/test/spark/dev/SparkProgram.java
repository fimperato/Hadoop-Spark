package it.imperato.test.spark.dev;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkProgram {

    private static final Logger log = Logger.getLogger(SparkProgram.class);

    public static void main(String args[]) {

        System.setProperty("hadoop.home.dir", "C:\\Spark\\spark-2.3.0-bin-hadoop2.7");

        // Definisco la configurazione per Spark:
        String sparkMaster = "local[2]";
        //SparkConf confEmpty = new SparkConf();
        SparkConf conf = new SparkConf()
                .setAppName("SparkProgram_1")
                .setMaster(sparkMaster);

        // Creo Spark Context con relativa configurazione:
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Creo un RDD (Resilient Distributed Dataset) per il file in input:
        // ogni linea del file in input sarà un record RDD

        JavaRDD<String> lines = sc.textFile(
                "data/worldcupplayerinfo_20140701.tsv");

        // Estrazione info base: count delle linee presenti
        log.info("###### Linee presenti nel file: " + lines.count());

        // Operazione di: Map
        // Mapping del numero dei caratteri per riga come RDD
        JavaRDD<Integer> caratteriPerLinea = lines.map(s -> s.length());

        // Operazione di: Reduce
        // Calcolo dei totali dei caratteri, su tutte le righe */
        int totaleCaratteri = caratteriPerLinea.reduce((a, b) -> a + b);

        log.info("###### Totale dei caratteri nel file: " + totaleCaratteri);

        // Operazione di: Reduce
        // verifica su ogni linea per una data squadra e aggregazione finale
        log.info("###### Totale giocatori per la squadra 'Real Madrid' dal file: "
                + lines.filter(oneLine -> oneLine.contains("Real Madrid")).count());

        // Operazione di: Reduce
        // verifica su ogni linea per un dato ruolo e aggregazione finale
        log.info("###### Totale giocatori per il ruolo 'Forward' dal file: "
                + lines.filter(oneLine -> oneLine.contains("Forward")).count());

        sc.close();
    }

}
