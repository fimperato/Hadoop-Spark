package it.imperato.test.hadoop.wordcount1;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordCount1Main extends Configured implements Tool {

    private static final Logger log = LoggerFactory.getLogger(WordCount1Main.class);

    public int run(String[] args) throws Exception {

        if(args.length < 2) {
            args = new String[2];
            // Args di default:
            String inputPath = "C:/temp/hadoop/data/test/wordcount1/test_input.txt";
            String outputDir = "C:/temp/hadoop/data/test/wordcount1/test_output.txt";
            args[0] = inputPath;
            args[1] = outputDir;
        }

        Job job = Job.getInstance(getConf());
        job.setJarByClass(getClass());

        FileInputFormat.setInputPaths(job, new Path(args[0]));

        job.setMapperClass(WordCount1Mapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //job.setReducerClass(WordCount1Reduce.class); // LongSumReducer
        job.setReducerClass(LongSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new WordCount1Main(), args));
    }
}
