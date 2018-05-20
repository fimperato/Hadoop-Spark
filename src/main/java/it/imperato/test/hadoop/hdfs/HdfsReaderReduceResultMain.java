package it.imperato.test.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Start hadoop:
 * > %HADOOP_HOME%\sbin\start-dfs.cmd
 * > %HADOOP_HOME%\sbin\start-yarn.cmd
 *
 * Read-Write on hadoop:
 * launch HdfsReaderReduceResultMain
 *
 * http://localhost:8088
 * http://localhost:50070
 * hdfs uri = hdfs://0.0.0.0:19000
 *
 * Con command line:
 * hdfs dfs -getmerge -nl /output/my_test_data/ C:/wsIdea/Spark_2018/SparkJava/data/hdfs/rw_test/read_reduce_dir_resultByHdfs.txt
 *
 * other:
 * hdfs dfs -copyFromLocal Desktop/someLocalFolderFiles /user/someFolder
 * hdfs dfs -ls /user/someFolder/someLocalFolderFiles
 *
 */
public class HdfsReaderReduceResultMain extends Configured implements Tool {

    private static final Logger log = Logger.getLogger(HdfsReaderReduceResultMain.class);

    public static final String FS_PARAM_NAME = "fs.defaultFS";

    public int run(String[] args) throws Exception {

        if (args.length < 2) {
            log.warn("Usati la hdfs input dir con risultati map-reduce, " +
                    "e local output default ([hdfs input dir path] [local file output path])");

            args = new String[2];
            args[0] = "/output/my_test_data/";
            args[1] = "data/hdfs/rw_test/read_reduce_dir_resultByHdfs.txt";
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
        List<String> linesFound = this.readLines(hdfsInputPath,hdfsConf,hdfsFs);
        linesFound.forEach(lineFound -> log.info("Trovato il risultato: "+lineFound));
        return 0;
    }

    public static void main( String[] args ) throws Exception {
        PropertyConfigurator.configure("wordcountLog4j.properties");

        int returnCode = ToolRunner.run(new HdfsReaderReduceResultMain(), args);
        log.info("exit with return code: "+returnCode);
        System.exit(returnCode);

    }

    public List<String> readLines(Path location, Configuration conf) throws Exception {
        FileSystem fileSystem = FileSystem.get(location.toUri(), conf);
        return readLines(location, conf, fileSystem);
    }

    private List<String> readLines(Path location, Configuration conf, FileSystem fileSystem) throws IOException {
        CompressionCodecFactory factory = new CompressionCodecFactory(conf);
        FileStatus[] items = fileSystem.listStatus(location);
        if (items == null) return new ArrayList<String>();
        List<String> results = new ArrayList<String>();
        for(FileStatus item: items) {

            // ignoring files like _SUCCESS
            if(item.getPath().getName().startsWith("_")) {
                continue;
            }

            CompressionCodec codec = factory.getCodec(item.getPath());
            InputStream stream = null;

            // check if we have a compression codec we need to use
            if (codec != null) {
                stream = codec.createInputStream(fileSystem.open(item.getPath()));
            }
            else {
                stream = fileSystem.open(item.getPath());
            }

            StringWriter writer = new StringWriter();
            org.apache.commons.io.IOUtils.copy(stream, writer, "UTF-8");
            String raw = writer.toString();
            String[] resulting = raw.split("\n");
            for(String str: raw.split("\n")) {
                results.add(str);
            }
        }
        return results;
    }

    /**
     *
     * Sequence Files
     * Sequence files are harder to read as you have to read in key/value pairs.
     * Here is a simple function, again taken straight from my Hadoop test helper library.
     *
     * example usage
     * List<Tuple<LongWritable, Text>> results = readSequenceFile(new Path("/a/b/c"), new Configuration(), LongWritable.class, Text.class);
     *
     * Rif. https://blog.matthewrathbone.com/2013/12/28/reading-data-from-hdfs-even-if-it-is-compressed
     *
     * @param path
     * @param conf
     * @param acls
     * @param bcls
     * @param <A>
     * @param <B>
     * @return
     * @throws Exception
     */
    public <A extends Writable, B extends Writable> List<Tuple<A, B>> readSequenceFile(Path path, Configuration conf, Class<A> acls, Class<B> bcls) throws Exception {

        SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path));
        long position = reader.getPosition();

        A key = acls.newInstance();
        B value = bcls.newInstance();

        List<Tuple<A, B>> results = new ArrayList<Tuple<A,B>>();
        while(reader.next(key,value)) {
            results.add(new Tuple(key, value));
            key = acls.newInstance();
            value = bcls.newInstance();
        }
        return results;
    }

}
