package it.imperato.test.spark.dev.simple;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 *
 * %spark_home%/bin/spark-submit --class it.imperato.test.spark.dev.simple.NumberTransformation --master local[1] C:\wsIdea\Spark_2018\Spark-Java\target\spark-java.jar
 *
 * hdfs uri = hdfs://0.0.0.0:19000
 *
 * Clean precedenti test:
 * hdfs dfs -rm -r /output/test_multipliedRDD_spark_2018
 * hdfs dfs -rm -r "/output/test_spark_2018_[0-9]*.xml"
 *
 * Check:
 * http://localhost:50070/explorer.html#/output/test_spark_2018
 *
 */
public class NumberTransformation {

    private static final Logger log = Logger.getLogger(SparkProgram.class);

    private static final String FS_PARAM_NAME = "fs.defaultFS";

    private static final String HDSF_URI = "hdfs://0.0.0.0:19000";

    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf();

        sparkConf.setAppName("Hello Spark");
        sparkConf.setMaster("local");

        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        JavaRDD<Integer> numbersRDD = sparkContext.parallelize(Arrays.asList(1,2,3));

        JavaRDD<Integer> squaresRDD = numbersRDD.map( n -> n*n );
        log.info("###### SQUARE: "+squaresRDD.collect().toString());

        JavaRDD<Integer> evenRDD = squaresRDD.filter( n -> n%2==0 );
        log.info("###### MOD-2 on square list: "+evenRDD.collect().toString());

        JavaRDD<Integer> multipliedRDD = numbersRDD.flatMap( n->Arrays.asList(n,n*2,n*3).iterator());
        log.info("###### MULTI ITERATOR: "+multipliedRDD.collect().toString());

        String hdfsLocationRddSave = null;
        try {
            // Salvataggio RDD su HDFS

//            multipliedRDD.foreach(new VoidFunction<Integer>() {
//                private static final long serialVersionUID = 1L;
//                public void call(Integer arg0) throws Exception {
//                    FileSystem fs = getHdfsFileSystem();
//                    log.info("Value is " + arg0);
//                    Path filenamePath = new Path("/output/test_multipliedRDD_spark_2018"); // + System.nanoTime() + ".xml");
//                    FSDataOutputStream fdos = fs.create(filenamePath);
//                    fdos.writeUTF(arg0 + "");
//                    fdos.close();
//                }
//            });

            hdfsLocationRddSave = HDSF_URI + "/output/test_multipliedRDD_spark_2018";
            multipliedRDD.saveAsObjectFile(hdfsLocationRddSave);
            JavaRDD<Integer> loaded = sparkContext.objectFile(hdfsLocationRddSave);
            // check file RDD ricaricato da HDFS:
            for(Integer line : loaded.collect()){
                log.info("###### Read RDD integer info salvata su HDFS: " + line);
            }

        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
        }

        // Caso di gestione di pair value RDD, save e reload:
//        multipliedRDD.saveAsObjectFile(hdfsLocationRddSave);
//        JavaPairRDD<Text, IntWritable> newReadRDDs = sparkContext.sequenceFile(hdfsLocationRddSave, Text.class, IntWritable.class);
//        JavaPairRDD<String, Integer> resultReadRDDs = newReadRDDs.mapToPair(new ConvertToNativeTypes());
//        List<Tuple2<String, Integer>> resultListReadRDDs = resultReadRDDs.collect();
//        for (Tuple2<String, Integer> record : resultListReadRDDs) {
//            log.info("###### record found in hdfs: " + record._2());
//        }

        sparkContext.close();

    }

    private static FileSystem getHdfsFileSystem() throws IOException {
        FileSystem hdfsFs;// Init del HDFS File System object (dalla proprietà fs.default.name in core-site.xml)
        String HDSFUri = "hdfs://0.0.0.0:19000";
        Configuration hdfsConf = new Configuration();
        hdfsConf.set("fs.defaultFS", HDSFUri);
        hdfsFs = FileSystem.get(URI.create(HDSFUri), hdfsConf);
        return hdfsFs;
    }

    public static class ConvertToNativeTypes implements PairFunction<Tuple2<Text, IntWritable>, String, Integer> {
        public Tuple2<String, Integer> call(Tuple2<Text, IntWritable> record) {
            return new Tuple2(record._2, record._2);
        }
    }

}
