package it.imperato.test.utils;

import java.io.File;

public class FileUtils {

    public static void deleteDirectoryWithFiles(String directoryToDeletePath) {
        File directoryToDelete = new File(directoryToDeletePath);
        if (directoryToDelete.exists()) {
            String[] entries = directoryToDelete.list();
            if (entries != null) {
                for (String s : entries) {
                    File currentFile = new File(directoryToDelete.getPath(), s);
                    currentFile.delete();
                }
            }
            directoryToDelete.delete();
        }
    }
}
