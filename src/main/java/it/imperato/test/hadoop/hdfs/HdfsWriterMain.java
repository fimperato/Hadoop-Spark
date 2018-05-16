package it.imperato.test.hadoop.hdfs;

import it.imperato.test.hadoop.wordcount1.WordCount1Main;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.util.Tool;

import java.io.*;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Start hadoop:
 * c:\hadoop-2.7.6\sbin>start-all
 *
 * Read-Write on hadoop:
 * launch HdfsWriterMain
 *
 * http://localhost:8088
 * http://localhost:50070
 */
public class HdfsWriterMain extends Configured implements Tool {

    private static final Logger log = LoggerFactory.getLogger(HdfsWriterMain.class);

    public static final String FS_PARAM_NAME = "fs.defaultFS";

    public int run(String[] args) throws Exception {

        if (args.length < 2) {
            log.warn("local input and hdfs output default used ([local input path] [hdfs output path])");
            args = new String[4];
            args[0] = "data\\hdfs\\rw_test\\rw_test.txt";
            args[1] = "/input/my_test_data/rw_test_output_01.txt";
            args[2] = "/input/my_test_data/rw_test_output_02.txt";
            args[3] = "/input/my_test_data/hdfs_dest_file_out.txt"; // hdfs dest file
            //return 1;
        }

        String localInputPath = args[0];
        Path outputPath = new Path(args[1]);
        Path outputPath2 = new Path(args[2]);

        Configuration conf = getConf();
        log.info("configured filesystem = " + conf.get(FS_PARAM_NAME));
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            log.error("output path already exists!");
            return 1;
        }


        // java io :
        OutputStream os = fs.create(outputPath);
        InputStream is = new BufferedInputStream(new FileInputStream(localInputPath));
        IOUtils.copyBytes(is, os, conf);


        // FSDataOutputStream :
        // Create a new file and write data to it.
        FSDataOutputStream out = fs.create(outputPath2);
        InputStream in = new BufferedInputStream(new FileInputStream(
                new File(localInputPath)));
        byte[] b = new byte[1024];
        int numBytes = 0;
        while ((numBytes = in.read(b)) > 0) {
            out.write(b, 0, numBytes);
        }

        //Source file in the local file system
        String localSrc3 = args[0];
        //Destination file in HDFS
        String dst3 = args[3];

        //Input stream for the file in local file system to be written to HDFS
        InputStream in3 = new BufferedInputStream(new FileInputStream(localSrc3));

        //Get configuration of Hadoop system
        Configuration conf3 = new Configuration();
        log.info("Connecting to -- "+conf.get("fs.defaultFS"));

        //Destination file in HDFS
        FileSystem fs3 = FileSystem.get(URI.create(dst3), conf);
        OutputStream out3 = fs.create(new Path(dst3));

        //Copy file from local to HDFS
        IOUtils.copyBytes(in, out, 4096, true);
        log.info(dst3 + " copied to HDFS");

        // Close all the file descripters
        in.close();
        out.close();
        fs.close();
        fs3.close();
        out3.close();

        return 0;
    }

    public static void main( String[] args ) throws Exception {

        int returnCode = ToolRunner.run(new HdfsWriterMain(), args);
        System.exit(returnCode);

    }
}