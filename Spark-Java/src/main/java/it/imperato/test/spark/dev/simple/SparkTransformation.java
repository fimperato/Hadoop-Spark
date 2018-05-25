package it.imperato.test.spark.dev.simple;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;

import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * %spark_home%/bin/spark-submit --class it.imperato.test.spark.dev.simple.SparkTransformation --master local[1] C:\wsIdea\Spark_2018\Spark-Java\target\spark-java.jar /input.txt /output-dir
 *
 */
public class SparkTransformation {

    private static final Logger log = Logger.getLogger(SparkProgram.class);

    public static void main(String args[]) {

        System.setProperty("hadoop.home.dir", "C:\\Spark\\spark-2.3.0-bin-hadoop2.7");

        // Definisco la configurazione per Spark:
        String sparkMaster = "local[1]";
        SparkConf conf = new SparkConf()
                .setAppName("SparkProgram_1")
                .setMaster(sparkMaster);
        JavaSparkContext sparkContext = new JavaSparkContext(conf);

        // Lettura file verso RDD (Resilient Distributed Dataset):
        JavaRDD<String> lines = sparkContext.textFile(
                "data/worldcupplayerinfo_20140701.tsv");

        log.info("###### Valori iniziali RDD ...");
        for(String line : lines.collect()){
            log.info("* " + line);
        }

        // Operazione di: Map
        // Trasformazione caratteri in lower case

        // Java 7
//        JavaRDD<String> caratteriPerLinea = lines.map(new Function<String, String>()  {
//            public String call(String s) {
//                String result = s.trim().toUpperCase();
//                return result;
//            }
//        });

        JavaRDD<String> trasformazioneLinea = lines.map((String s) -> {
            String result = s.trim().toUpperCase();
            return result;
        });

        // Collect RDD per la stampa risultati
        log.info("###### Verifica risultato mapping [trasformazione map] ...");
        for(String line : trasformazioneLinea.collect()){
            log.info("* " + line);
        }

//        JavaRDD<String> result = lines.flatMap(new FlatMapFunction<String, String>() {
//            public Iterator<String> call(String s) {
//                return Arrays.asList(s.split(" ")).iterator();
//            }
//        });

        JavaRDD<String> parolePerLinea = lines.flatMap((String s) -> {
            return Arrays.asList(s.split(" ")).iterator();
        });

        // Collect RDD per la stampa risultati
        log.info("###### Verifica risultato mapping [trasformazione flatmap] ...");
        for(String line : parolePerLinea.collect()){
            log.info("* " + line);
        }


        sparkContext.close();

    }
}
