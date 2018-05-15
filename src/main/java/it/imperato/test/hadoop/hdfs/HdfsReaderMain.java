package it.imperato.test.hadoop.hdfs;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Start hadoop:
 * c:\hadoop-2.7.6\sbin>start-all
 *
 * Read-Write on hadoop:
 * launch HdfsReaderMain
 *
 * http://localhost:8088
 * http://localhost:50070
 */
public class HdfsReaderMain extends Configured implements Tool {

    private static final Logger log = LoggerFactory.getLogger(HdfsReaderMain.class);

    public static final String FS_PARAM_NAME = "fs.defaultFS";

    public int run(String[] args) throws Exception {

        if (args.length < 2) {
            log.warn("hdfs input and local output default used ([hdfs input path] [local output path])");
            args = new String[2];
            args[0] = "C:\\hadoop-2.7.6\\data\\datanode\\rw_test_output.txt";
            args[1] = "data\\hdfs\\rw_test\\rw_test_readbyhdfs_out.txt";
            //return 1;
        }

        Path inputPath = new Path(args[0]);
        String localOutputPath = args[1];
        Configuration conf = getConf();
        log.info("configured filesystem = " + conf.get(FS_PARAM_NAME));
        FileSystem fs = FileSystem.get(conf);
        boolean exists = fs.exists(inputPath);
        log.info("inputPath "+inputPath+" exists = " + exists);
        InputStream is = fs.open(inputPath);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(localOutputPath));
        IOUtils.copyBytes(is, os, conf);
        return 0;
    }

    public static void main( String[] args ) throws Exception {

        int returnCode = ToolRunner.run(new HdfsReaderMain(), args);
        System.exit(returnCode);

    }
}
