package it.imperato.test.spark.dev.dataanalysis;

import it.imperato.test.hadoop.hdfs.HdfsReaderMain;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;

public class SparkAnalysis01 {

    private static final Logger log = Logger.getLogger(SparkAnalysis01.class);

    public static void main(String[] args) {

        String inputPath1 = null, inputPath2 = null, outputPath1 = null, outputPath2 = null;
        Double prezzoThr = null;
        int etaThr;

        if(args==null || args.length<6) {
            inputPath1 = "data/spark/testdata/acquisti.csv";
            inputPath2 = "data/spark/testdata/team.csv";
            outputPath1 = "data/spark/testoutput/analysis01_1.txt";
            // outputPath2 = "data/spark/testoutput/analysis01_2.txt";
            etaThr = 25;
            prezzoThr = new Double(30.5);
        }
        else {
            inputPath1 = args[0];
            inputPath2 = args[1];
            outputPath1 = args[2];
            outputPath2 = args[3];
            prezzoThr = Double.parseDouble(args[4]);
            etaThr = Integer.valueOf(args[4]);
            prezzoThr = Double.parseDouble(args[5]);
        }

        final Double prezzoThreshold = prezzoThr;
        String sparkMaster = "local[1]";
        SparkConf conf = new SparkConf().setAppName("sparkanalysis01").setMaster(sparkMaster);
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        JavaRDD<String> acquistiRDD = sparkContext.textFile(inputPath1).cache();
        JavaRDD<String> teamRDD = sparkContext.textFile(inputPath2);

        /*********
         * PART A - SELECT THE BOOKIDS OF THE EXPENSIVE NEVER SOLD BOOKS
         *******/
        JavaRDD<String> acquistiMappedRDD = acquistiRDD.map(new Function<String, String>() {
            @Override
            public String call(String arg0) throws Exception {
                String[] split = arg0.split(",");
                return split[1];
            }
        }).distinct();

        JavaRDD<String> etaMediaMaggioreTeamRDD = teamRDD.filter(new Function<String, Boolean>() {

            @Override
            public Boolean call(String arg0) throws Exception {
                String[] split = arg0.split(",");
                try {
                    if (Integer.valueOf(split[3]) >= etaThr) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    log.warn("Titolo elaborato.");
                }
                return false;
            }
        }).map(new Function<String, String>() {
            @Override
            public String call(String arg0) throws Exception {
                String[] split = arg0.split(",");
                return split[0];
            }
        });

        etaMediaMaggioreTeamRDD.saveAsTextFile(outputPath1);

        sparkContext.close();
    }

}
