package it.imperato.test.hadoop.wordcount2;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 *
 * Cancellare l'output dir prima di fare partire il job:
 * hdfs dfs -rm -r /output/my_test_data
 *
 * [HADOOP_HOME]/bin>hadoop jar C:\wsIdea\Spark_2018\Hadoop-Test\target\hadoop-test-java.jar /input/my_test_data /output/my_test_data
 *
 * WordCount2Main data\hdfs\rw_test\wordcount1\input data\hdfs\rw_test\wordcount1\output
 *
 */
public class WordCount2Main {

    static Logger log = Logger.getLogger(WordCount2Main.class);

    //Mapper which implement the mapper() function
    public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {

                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }
    //Reducer which implement the reduce() function
    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }
    //Driver class to specific the Mapper and Reducer
    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        log.info("Leggo i dati di input da: "+(args!=null&&args[0]!=null?args[0]:"N.D."));
        log.info("Produzione dei dati di output su: "+(args!=null&&args[1]!=null?args[1]:"N.D."));

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount2Main.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        log.info(job.waitForCompletion(true) ? 0 : 1);
    }
}