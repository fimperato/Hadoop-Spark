package it.imperato.test.hadoop.hdfs;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Start hadoop:
 * c:\hadoop-2.7.6\sbin>start-all
 *
 * Read-Write on hadoop:
 * launch HdfsReaderMain
 *
 * http://localhost:8088
 * http://localhost:50070
 * hdfs uri = hdfs://0.0.0.0:19000
 *
 * hdfs dfs -getmerge  /output/my_test_data/ C:/wsIdea/Spark_2018/SparkJava/data/hdfs/rw_test/read_reduce_dir_resultByHdfs.txt
 *
 */
public class HdfsReaderMain extends Configured implements Tool {

    private static final Logger log = Logger.getLogger(HdfsReaderMain.class);

    public static final String FS_PARAM_NAME = "fs.defaultFS";

    public int run(String[] args) throws Exception {

        if (args.length < 2) {
            log.warn("hdfs input and local output default used ([hdfs input path] [local output path])");

            args = new String[2];
            args[0] = "data\\hdfs\\rw_test\\rw_test.txt";
            args[1] = "data\\hdfs\\rw_test\\rw_test_readbylocal_out.txt";
            //return 1;

            Path inputLocalPath = new Path(args[0]);
            String localOutputPath = args[1];
            Configuration confLocalFilesystem = getConf();
            log.info("configured LOCAL filesystem = " + confLocalFilesystem.get(FS_PARAM_NAME));
            FileSystem localFs = FileSystem.get(confLocalFilesystem);
            boolean exists = localFs.exists(inputLocalPath);
            log.info("inputPath "+inputLocalPath+" exists = " + exists);
            InputStream is = localFs.open(inputLocalPath);
            OutputStream os = new BufferedOutputStream(new FileOutputStream(localOutputPath));
            IOUtils.copyBytes(is, os, confLocalFilesystem);
            is.close();
            os.close();
            log.info("TEST su filesystem locale, default, terminato.");

            // Per test su HDFS:
            args[0] = "/testo-prova.txt";
            args[0] = "/output/my_test_data";
            args[1] = "data\\hdfs\\rw_test\\rw_test_readbyhdfs_out.txt";
        }

        Path hdfsInputPath = new Path(args[0]);
        String localOutputPath = args[1];
        log.info("TEST su HDFS init.");

        // Init del HDFS File System object (dalla proprietà fs.default.name in core-site.xml)
        String HDSFUri = "hdfs://0.0.0.0:19000";
        Configuration hdfsConf = new Configuration();
        // Set del FileSystem URI
        hdfsConf.set("fs.defaultFS", HDSFUri);
        // Per Maven
        hdfsConf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        hdfsConf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        // Set dell'user HADOOP (Francesco)
        System.setProperty("HADOOP_USER_NAME", "Francesco");
        System.setProperty("hadoop.home.dir", "/");

        //Get the filesystem - HDFS
        FileSystem hdfsFs = FileSystem.get(URI.create(HDSFUri), hdfsConf);
        log.info("configured LOCAL filesystem = " + hdfsConf.get(FS_PARAM_NAME));
        // FileSystem hdfsFs = FileSystem.get(hdfsConf);
        boolean exists = hdfsFs.exists(hdfsInputPath);
        log.info("inputPath "+hdfsInputPath+" exists = " + exists);
        InputStream is = hdfsFs.open(hdfsInputPath);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(localOutputPath));
        IOUtils.copyBytes(is, os, hdfsConf);

        is.close();
        os.close();
        return 0;
    }

    public static void main( String[] args ) throws Exception {
        PropertyConfigurator.configure("wordcountLog4j.properties");

        int returnCode = ToolRunner.run(new HdfsReaderMain(), args);
        log.info("exit with return code: "+returnCode);
        System.exit(returnCode);

    }
}
