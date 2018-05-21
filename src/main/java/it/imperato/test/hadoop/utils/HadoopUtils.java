package it.imperato.test.hadoop.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import shapeless.the;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HadoopUtils {

    /*
     * Helper method to get the list of files from the HDFS path
     *
     */
    public static List<String> listFilesFromHDFSPath(Configuration hadoopConfiguration, String hdfsPath,
                                                     boolean recursive) {
        // resulting list of files
        List<String> filePaths = new ArrayList<String>();
        FileSystem fs = null;

        try
        {
            // get path from string and then the filesystem
            Path path = new Path(hdfsPath);  //throws IllegalArgumentException, all others will only throw IOException
            fs = path.getFileSystem(hadoopConfiguration);

            // resolve hdfsPath first to check whether the path exists => either a real directory or o real file
            // resolvePath() returns fully-qualified variant of the path
            path = fs.resolvePath(path);


            // if recursive approach is requested
            if (recursive)
            {
                // (heap issues with recursive approach) => using a queue
                Queue<Path> fileQueue = new LinkedList<Path>();

                // add the obtained path to the queue
                fileQueue.add(path);

                // while the fileQueue is not empty
                while (!fileQueue.isEmpty())
                {
                    // get the file path from queue
                    Path filePath = fileQueue.remove();

                    // filePath refers to a file
                    if (fs.isFile(filePath))
                    {
                        filePaths.add(filePath.toString());
                    }
                    else // else filePath refers to a directory
                    {
                        // list paths in the directory and add to the queue
                        FileStatus[] fileStatuses = fs.listStatus(filePath);
                        for (FileStatus fileStatus : fileStatuses)
                        {
                            fileQueue.add(fileStatus.getPath());
                        }
                    }

                }

            }
            else        //non-recursive approach => no heap overhead
            {
                // if the given hdfsPath is actually directory
                if (fs.isDirectory(path))
                {
                    FileStatus[] fileStatuses = fs.listStatus(path);

                    // loop all file statuses
                    for (FileStatus fileStatus : fileStatuses)
                    {
                        // if the given status is a file, then update the resulting list
                        if (fileStatus.isFile())
                            filePaths.add(fileStatus.getPath().toString());
                    }
                }
                else // it is a file then
                {
                    // return the one and only file path to the resulting list
                    filePaths.add(path.toString());
                }

            }

        }
        catch(Exception ex) //will catch all exception including IOException and IllegalArgumentException
        {
            ex.printStackTrace();

            // if some problem occurs return an empty array list
            return new ArrayList<String>();
        }
        finally
        {
            // close filesystem; not more operations
            try
            {
                if(fs != null)
                    fs.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }

        // return the resulting list; list can be empty if given path is an empty directory
        // without files and sub-directories
        return filePaths;
    }


    /**
     *
     * Working with java.io.File API then the following method will help you list files only from local file system;
     * namely path string that starts with file://.
     *
     * Helper method to list files from the local path in the local file system
     *
     */
    public static List<String> listFilesFromLocalPath(String localPathString, boolean recursive) {

        // resulting list of files
        List<String> localFilePaths = new ArrayList<String>();

        // get the Java file instance from local path string
        File localPath = new File(localPathString);


        // this case is possible if the given localPathString does not exit => which means neither file nor a directory
        if(!localPath.exists())
        {
            System.err.println("\n" + localPathString + " is neither a file nor a directory; please provide correct local path");

            // return with empty list
            return new ArrayList<String>();
        }

        // at this point localPath does exist in the file system => either as a directory or a file

        // if recursive approach is requested
        if (recursive)
        {
            // recursive approach => using a queue
            Queue<File> fileQueue = new LinkedList<File>();

            // add the file in obtained path to the queue
            fileQueue.add(localPath);

            // while the fileQueue is not empty
            while (!fileQueue.isEmpty())
            {
                // get the file from queue
                File file = fileQueue.remove();

                // file instance refers to a file
                if (file.isFile())
                {
                    // update the list with file absolute path
                    localFilePaths.add(file.getAbsolutePath());
                }
                else   // else file instance refers to a directory
                {
                    // list files in the directory and add to the queue
                    File[] listedFiles = file.listFiles();
                    for (File listedFile : listedFiles)
                    {
                        fileQueue.add(listedFile);
                    }
                }

            }
        }
        else // non-recursive approach
        {
            // if the given localPathString is actually a directory
            if (localPath.isDirectory())
            {
                File[] listedFiles = localPath.listFiles();

                // loop all listed files
                for (File listedFile : listedFiles)
                {
                    // if the given listedFile is actually a file, then update the resulting list
                    if (listedFile.isFile())
                        localFilePaths.add(listedFile.getAbsolutePath());
                }
            }
            else // it is a file then
            {
                // return the one and only file absolute path to the resulting list
                localFilePaths.add(localPath.getAbsolutePath());
            }
        }

        // return the resulting list; list can be empty if given path is an empty directory
        // without files and sub-directories
        return localFilePaths;
    }

}
