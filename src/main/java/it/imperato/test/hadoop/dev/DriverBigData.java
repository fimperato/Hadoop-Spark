package it.imperato.test.hadoop.dev;

import it.imperato.test.spark.dev.SparkProgram;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

/**
 * Programma MapReduce
 * winutils.exe chmod -R 777 C:\tmp\hadoop-Francesco
 * winutils.exe ls -F C:\tmp\hadoop-Francesco
 */
public class DriverBigData extends Configured implements Tool {

    private static final Logger log = Logger.getLogger(SparkProgram.class);

    @Override
    public int run(String[] args) throws Exception {
        Path inputPath;
        Path outputDir;
        int numberOfReducers;
        int exitCode;

        // Lettura parametri
        numberOfReducers = Integer.parseInt(args[0]);
        inputPath = new Path(args[1]);
        outputDir = new Path(args[2]);

        Configuration conf = this.getConf();
        // Crea JOB
        Job job = Job.getInstance(conf);
        // Assegna nome al JOB
        job.setJobName("Esempio");

        // Imposta PATH INPUT
        FileInputFormat.addInputPath(job, inputPath);
        // Imposta PATH INPUT
        FileOutputFormat.setOutputPath(job, outputDir);

        // Imposta classe del Driver
        job.setJarByClass(DriverBigData.class);

        // Imposta formato input
        job.setInputFormatClass(TextInputFormat.class);
        // Imposta formato output
        job.setOutputFormatClass(TextOutputFormat.class);

        // Imposta classe MAPPER
        job.setMapperClass(MapperBigData.class);

        // Imposta le classi per Key e Value
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // Imposta la classe REDUCER
        job.setReducerClass(ReducerBigData.class);

        // Imposta le classi per Key e Value in output
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // Imposta numero dei REDUCER
        job.setNumReduceTasks(numberOfReducers);

        // Esegue il job e rimane in attesa per il suo termine
        if (job.waitForCompletion(true)==true)
            exitCode=0;
        else
            exitCode=1;

        return exitCode;
    }

    /**
     * Main
     *
     */
    public static void main(String args[]) throws Exception {

        System.setProperty("hadoop.home.dir", "C:\\Spark\\spark-2.3.0-bin-hadoop2.7");

        if(args.length < 3) {
            args = new String[3];
            // Args di default:
            String numberOfReducers = "1";
            String inputPath = "C:/temp/hadoop/data/test/test_input.txt";
            String outputDir = "C:/temp/hadoop/data/test/test_output.txt";
            args[0] = numberOfReducers;
            args[1] = inputPath;
            args[2] = outputDir;
        }

        // Usa la classe ToolRunner per configurare e lanciare l'applicazione Hadoop
        int res = ToolRunner.run(new Configuration(), new DriverBigData(), args);
        System.exit(res);

    }
}
