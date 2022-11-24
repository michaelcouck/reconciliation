package be.stib.maas.reconciliation.toolkit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class contains common utilities for files like deleting directories recursively, creating
 * files and directories and searching for files and directories recursively, before the {@link org.apache.commons.io.FileUtils}
 * from Apache was available.
 *
 * @author Michael Couck
 * @version 01.00
 * @since 25-01-2011
 */
@Slf4j
public abstract class FILE {

    /**
     * This method looks through all the files defined in the folder in the parameter
     * list, recursively, and gets the first one that matches the pattern.
     *
     * @param folder         the folder to start looking through
     * @param stringPatterns the patterns to look for in the file paths
     * @return the first file that was encountered that has the specified pattern(s) in it
     */
    public static File findFileRecursively(final File folder, final String... stringPatterns) {
        List<File> files = findFilesRecursively(folder, new ArrayList<>(), stringPatterns);
        return !files.isEmpty() ? files.get(0) : null;
    }

    /**
     * This method will recursively look for a directory in the file system starting at
     * the specified abstract file position and return the first one that is encountered.
     *
     * @param folder         the folder to start looking for the patterns
     * @param stringPatterns the patterns of the folder to look for
     * @return the first folder that satisfies the patterns specified
     */
    public static File findDirectoryRecursively(final File folder, final String... stringPatterns) {
        List<File> files = findFilesRecursively(folder, new ArrayList<>(), stringPatterns);
        files.removeIf(file -> !file.isDirectory());
        return !files.isEmpty() ? files.get(0) : null;
    }

    /**
     * This method will first walk backwards through the directories, looking for a parent with a
     * specific name, before doing a search for the directory/file pattern specified.
     *
     * @param folder         the folder to start looking through
     * @param toDirectory    the name of the parent directory to start looking for the file,
     *                       i.e. move up to this directory before starting to search
     * @param stringPatterns the patterns to look for in the file paths
     * @return the directory that matches the pattern startup from a higher directory
     */
    @SuppressWarnings("unused")
    public static File findDirectoryRecursivelyUp(final File folder, final String toDirectory, final String... stringPatterns) {
        File upFolder = moveUpDirectories(folder, toDirectory);
        return findDirectoryRecursively(upFolder, stringPatterns);
    }

    public static File moveUpDirectories(final File folder, final int upDirectories) {
        if (upDirectories == 0) {
            return folder;
        }
        int directories = upDirectories;
        String upFolderPath = cleanFilePath(folder.getAbsolutePath());
        File upFolder = new File(upFolderPath);
        do {
            upFolder = upFolder.getParentFile();
        } while (--directories > 0 && upFolder != null);
        return upFolder;
    }

    static File moveUpDirectories(final File folder, final String toFolder) {
        if (folder.getName().equals(toFolder)) {
            return folder;
        }
        File upFolder = moveUpDirectories(folder, 1);
        return moveUpDirectories(upFolder, toFolder);
    }

    /**
     * This method will look through all the files in the top level folder, and all
     * the sub folders, adding files to the list when they match the patterns that are provided.
     *
     * @param folder         the folder to start looking through
     * @param stringPatterns the patterns to match the file paths with
     * @param files          the files list to add all the files to
     * @return the list of files that match the patterns
     */
    public static List<File> findFilesRecursively(final File folder, final List<File> files, final String... stringPatterns) {
        if (folder != null && folder.isDirectory()) {
            File[] folderFiles = findFiles(folder, stringPatterns);
            if (folderFiles != null) {
                files.addAll(Arrays.asList(folderFiles));
            }
            File[] childFolders = folder.listFiles();
            if (childFolders != null) {
                for (final File childFolder : childFolders) {
                    findFilesRecursively(childFolder, files, stringPatterns);
                }
            }
        }
        return files;
    }

    /**
     * Finds files with the specified pattern only in the folder specified in the parameter list,
     * i.e. not recursively.
     *
     * @param folder         the folder to look for files in
     * @param stringPatterns the pattern to look for in the file path
     * @return an array of files with the specified pattern in the path
     */
    static File[] findFiles(final File folder, final String... stringPatterns) {
        final Pattern pattern = getPattern(stringPatterns);
        return folder.listFiles(file -> {
            String pathName = file.getAbsolutePath();
            return pattern.matcher(pathName).matches();
        });
    }

    /**
     * Creates the pattern object from the regular expression patterns.
     *
     * @param stringPatterns the regular expression patterns
     * @return the pattern generated from the strings
     */
    private static Pattern getPattern(final String... stringPatterns) {
        boolean first = Boolean.TRUE;
        StringBuilder builder = new StringBuilder();
        for (String stringPattern : stringPatterns) {
            if (!first) {
                // Or
                builder.append("|");
            } else {
                first = Boolean.FALSE;
            }
            // Concatenate the 'any character' regular expression to the string pattern
            builder.append(".*(").append(stringPattern).append(").*");
        }
        return Pattern.compile(builder.toString());
    }

    /**
     * This method will clean the path, as some operating systems add their special
     * characters, back spaces and the like, that interfere with the normal working of the
     * file system.
     *
     * @param path the path to clea, perhaps something like 'file:C:\\path\\.\\some\\more'
     * @return the path that can be used as an absolute path on the file system
     */
    public static String cleanFilePath(final String path) {
        String filePath = path;
        filePath = StringUtils.replace(filePath, "/./", "/");
        // For windows we must clean the path of 'file:/' because getting the
        // parent then appends the user path for some reason too, returning something
        // like C:/tmp/user/directory/C:/path/to/directory
        filePath = StringUtils.replace(filePath, "file:", "");
        filePath = StringUtils.replace(filePath, "file:/", "");
        filePath = StringUtils.replace(filePath, "file:\\", "");
        filePath = StringUtils.replace(filePath, "\\.\\", "/");
        filePath = StringUtils.replace(filePath, "\\", "/");
        filePath = StringUtils.removeEnd(filePath, ".");
        return filePath;
    }

}