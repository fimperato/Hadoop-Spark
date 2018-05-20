package it.imperato.test.hadoop.wordcount1;

import it.imperato.test.hadoop.wordcount2.WordCount2Main;
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
import org.apache.log4j.PropertyConfigurator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WordCount1Main extends Configured implements Tool {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(WordCount2Main.class);

    public int run(String[] args) throws Exception {

        if(args.length < 2) {
            args = new String[2];
            // Args di default:
            String inputPath = "data\\hdfs\\rw_test\\wordcount1\\test_input.txt";
            String outputDir = "data\\hdfs\\rw_test\\wordcount1\\test_output_"+new SimpleDateFormat("yyyyMMddHHmm").format(new Date());;
            args[0] = inputPath;
            args[1] = outputDir;
        }
        log.info("Leggo i dati di input da: "+(args!=null&&args[0]!=null?args[0]:"N.D."));
        log.info("Produzione dei dati di output su: "+(args!=null&&args[1]!=null?args[1]:"N.D."));

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
        //PropertiesConfigurator is used to configure logger from properties file
        PropertyConfigurator.configure("wordcountLog4j.properties");

        log.info(ToolRunner.run(new WordCount1Main(), args));
    }
}
