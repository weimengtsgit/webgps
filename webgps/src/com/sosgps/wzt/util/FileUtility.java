/**
 * <p>Title:Toten</p>
 * <p>Description: The utility class to manipulate File</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Winchannel </p>
 */
package com.sosgps.wzt.util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
/**
 * This class provides basic facilities for manipulating files and file paths.
 *
 * <h3>Path-related methods</h3>
 *
 * <p>Methods exist to retrieve the components of a typical file path. For example
 * <code>/www/hosted/mysite/index.html</code>, can be broken into:
 * <ul>
 *   <li><code>/www/hosted/mysite/</code> -- retrievable through {@link #getPath}</li>
 *   <li><code>index.html</code> -- retrievable through {@link #removePath}</li>
 *   <li><code>/www/hosted/mysite/index</code> -- retrievable through {@link #removeExtension}</li>
 *   <li><code>html</code> -- retrievable through {@link #getExtension}</li>
 * </ul>
 * There are also methods to {@link #catPath concatenate two paths}, {@link #resolveFile resolve a
 * path relative to a File} and {@link #normalize} a path.
 * </p>
 *
 * <h3>File-related methods</h3>
 * <p>
 * There are methods to  create a {@link #toFile File from a URL}, copy a
 * {@link #copyFileToDirectory File to a directory},
 * copy a {@link #copyFile File to another File},
 * copy a {@link #copyURLToFile URL's contents to a File},
 * as well as methods to {@link #deleteDirectory(File) delete} and {@link #cleanDirectory(File)
 * clean} a directory.
 * </p>
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @author <a href="mailto:jefft@apache.org">Jeff Turner</a>
 */
public class FileUtility {
	/**
	 * Private constructor to prevent instantiation.
	 *
	 */
	private FileUtility(){}
	
	   /**
	     * Compare the contents of two files to determine if they are equal or not.
	     *
	     * @param file1 the first file
	     * @param file2 the second file
	     * @return true if the content of the files are equal or they both don't exist, false otherwise
	     */
	    public static boolean contentEquals(final File file1, final File file2) throws IOException {
	        final boolean file1Exists = file1.exists();
	        if (file1Exists != file2.exists()) {
	            return false;
	        }

	        if (!file1Exists) {
	            // two not existing files are equal
	            return true;
	        }

	        if (file1.isDirectory() || file2.isDirectory()) {
	            // don't want to compare directory contents
	            return false;
	        }

	        InputStream input1 = null;
	        InputStream input2 = null;
	        try {
	            input1 = new FileInputStream(file1);
	            input2 = new FileInputStream(file2);
	            final InputStream bufferedInput1 = new BufferedInputStream(input1);
	            final InputStream bufferedInput2 = new BufferedInputStream(input2);

	            int ch = bufferedInput1.read();
	            while (ch != -1) {
	                if (ch != bufferedInput2.read()) {
	                    return false;
	                }
	                ch = bufferedInput1.read();
	            }
	            if (-1 != bufferedInput2.read()) {
	                return false;
	            }
	            return true;
	        }
	        finally {
	            IOUtility.shutdownStream(input1);
	            IOUtility.shutdownStream(input2);
	        }
	    }

	    /**
	     * Convert from a <code>URL</code> to a <code>File</code>.
	     * @param url File URL.
	     * @return The equivalent <code>File</code> object, or <code>null</code> if the URL's protocol
	     * is not <code>file</code>
	     */
	    public static File toFile(final URL url) {
	        if (url.getProtocol().equals("file") == false) {
	            return null;
	        }
	        else {
	            final String filename = url.getFile().replace('/', File.separatorChar);
	            return new File(filename);
	        }
	    }

	    /**
	     * Convert the array of Files into a list of URLs.
	     *
	     * @param files the array of files
	     * @return the array of URLs
	     * @exception IOException if an error occurs
	     */
	    public static URL[] toURLs(final File[] files) throws IOException {
	        final URL[] urls = new URL[files.length];

	        for (int i = 0; i < urls.length; i++) {
	            urls[i] = files[i].toURL();
	        }

	        return urls;
	    }

	   	    /**
	     * Remove extension from filename.
	     * ie
	     * <pre>
	     * foo.txt    --> foo
	     * a\b\c.jpg --> a\b\c
	     * a\b\c     --> a\b\c
	     * </pre>
	     *
	     * @param filename the filename
	     * @return the filename minus extension
	     */
	    public static String removeExtension(final String filename) {
	        final int index = filename.lastIndexOf('.');

	        if (-1 == index) {
	            return filename;
	        }
	        else {
	            return filename.substring(0, index);
	        }
	    }

	    /**
	     * Get extension from filename.
	     * ie
	     * <pre>
	     * foo.txt    --> "txt"
	     * a\b\c.jpg --> "jpg"
	     * a\b\c     --> ""
	     * </pre>
	     *
	     * @param filename the filename
	     * @return the extension of filename or "" if none
	     */
	    public static String getExtension(final String filename) {
	        final int index = filename.lastIndexOf('.');

	        if (-1 == index) {
	            return "";
	        }
	        else {
	            return filename.substring(index + 1);
	        }
	    }

	    /**
	     * Remove path from filename. Equivalent to the unix command <code>basename</code>
	     * ie.
	     * <pre>
	     * a/b/c.txt --> c.txt
	     * a.txt     --> a.txt
	     * </pre>
	     *
	     * @param filepath the filepath
	     * @return the filename minus path
	     */
	    public static String removePath(final String filepath) {
	        return removePath(filepath, File.separatorChar);
	    }

	    /**
	     * Remove path from filename.
	     * ie.
	     * <pre>
	     * a/b/c.txt --> c.txt
	     * a.txt     --> a.txt
	     * </pre>
	     *
	     * @param filepath the filepath
	     * @return the filename minus path
	     */
	    public static String removePath(final String filepath, final char fileSeparatorChar) {
	        final int index = filepath.lastIndexOf(fileSeparatorChar);

	        if (-1 == index) {
	            return filepath;
	        }
	        else {
	            return filepath.substring(index + 1);
	        }
	    }

	    /**
	     * Get path from filename. Roughly equivalent to the unix command <code>dirname</code>.
	     * ie.
	     * <pre>
	     * a/b/c.txt --> a/b
	     * a.txt     --> ""
	     * </pre>
	     *
	     * @param filepath the filepath
	     * @return the filename minus path
	     */
	    public static String getPath(final String filepath) {
	        return getPath(filepath, File.separatorChar);
	    }

	    /**
	     * Get path from filename.
	     * ie.
	     * <pre>
	     * a/b/c.txt --> a/b
	     * a.txt     --> ""
	     * </pre>
	     *
	     * @param filepath the filepath
	     * @return the filename minus path
	     */
	    public static String getPath(final String filepath, final char fileSeparatorChar) {
	        final int index = filepath.lastIndexOf(fileSeparatorChar);
	        if (-1 == index) {
	            return "";
	        }
	        else {
	            return filepath.substring(0, index);
	        }
	    }

	    /**
	     * Copy file from source to destination. If <code>destinationDirectory</code> does not exist, it
	     * (and any parent directories) will be created. If a file <code>source</code> in
	     * <code>destinationDirectory</code> exists, it will be overwritten.
	     *
	     * @param source An existing <code>File</code> to copy.
	     * @param destination A directory to copy <code>source</code> into.
	     *
	     * @throws FileNotFoundException if <code>source</code> isn't a normal file.
	     * @throws IllegalArgumentException if <code>destinationDirectory</code> isn't a directory.
	     * @throws IOException if <code>source</code> does not exist, the file in
	     * <code>destinationDirectory</code> cannot be written to, or an IO error occurs during copying.
	     */
	    public static void copyFileToDirectory(final String source, final String destinationDirectory) throws IOException {
	        copyFileToDirectory(new File(source), new File(destinationDirectory));
	    }

	    /**
	     * Copy file from source to destination. If <code>destinationDirectory</code> does not exist, it
	     * (and any parent directories) will be created. If a file <code>source</code> in
	     * <code>destinationDirectory</code> exists, it will be overwritten.
	     *
	     * @param source An existing <code>File</code> to copy.
	     * @param destination A directory to copy <code>source</code> into.
	     *
	     * @throws FileNotFoundException if <code>source</code> isn't a normal file.
	     * @throws IllegalArgumentException if <code>destinationDirectory</code> isn't a directory.
	     * @throws IOException if <code>source</code> does not exist, the file in
	     * <code>destinationDirectory</code> cannot be written to, or an IO error occurs during copying.
	     */
	    public static void copyFileToDirectory(final File source, final File destinationDirectory) throws IOException {
	        if (destinationDirectory.exists() && !destinationDirectory.isDirectory()) {
	            throw new IllegalArgumentException("Destination is not a directory");
	        }

	        copyFile(source, new File(destinationDirectory, source.getName()));
	    }

	    /**
	     * Copy file from source to destination. The directories up to <code>destination</code> will be
	     * created if they don't already exist. <code>destination</code> will be overwritten if it
	     * already exists.
	     *
	     * @param source An existing non-directory <code>File</code> to copy bytes from.
	     * @param destination A non-directory <code>File</code> to write bytes to (possibly
	     * overwriting).
	     *
	     * @throws IOException if <code>source</code> does not exist, <code>destination</code> cannot be
	     * written to, or an IO error occurs during copying.
	     *
	     * @throws FileNotFoundException if <code>destination</code> is a directory
	     * (use {@link #copyFileToDirectory}).
	     */
	    public static void copyFile(final File source, final File destination) throws IOException {
	        //check source exists
	        if (!source.exists()) {
	            final String message = "File " + source + " does not exist";
	            throw new IOException(message);
	        }

	        //does destinations directory exist ?
	        if (destination.getParentFile() != null && !destination.getParentFile().exists()) {
	            destination.mkdirs();
	        }

	        //make sure we can write to destination
	        if (destination.exists() && !destination.canWrite()) {
	            final String message = "Unable to open file " + destination + " for writing.";
	            throw new IOException(message);
	        }

	        final FileInputStream input = new FileInputStream(source);
	        final FileOutputStream output = new FileOutputStream(destination);
	        IOUtility.copy(input, output);
	        IOUtility.shutdownStream(input);
	        IOUtility.shutdownStream(output);

	        if (source.length() != destination.length()) {
	            final String message = "Failed to copy full contents from " + source + " to " + destination;
	            throw new IOException(message);
	        }
	    }

	    /**
	     * Copies bytes from the URL <code>source</code> to a file <code>destination</code>.
	     * The directories up to <code>destination</code> will be created if they don't already exist.
	     * <code>destination</code> will be overwritten if it already exists.
	     *
	     * @param source A <code>URL</code> to copy bytes from.
	     * @param destination A non-directory <code>File</code> to write bytes to (possibly
	     * overwriting).
	     *
	     * @throws IOException if
	     * <ul>
	     *  <li><code>source</code> URL cannot be opened</li>
	     *  <li><code>destination</code> cannot be written to</li>
	     *  <li>an IO error occurs during copying</li>
	     * </ul>
	     */
	    public static void copyURLToFile(final URL source, final File destination) throws IOException {
	        //does destination directory exist ?
	        if (destination.getParentFile() != null && !destination.getParentFile().exists()) {
	            destination.mkdirs();
	        }

	        //make sure we can write to destination
	        if (destination.exists() && !destination.canWrite()) {
	            final String message = "Unable to open file " + destination + " for writing.";
	            throw new IOException(message);
	        }

	        final InputStream input = source.openStream();
	        final FileOutputStream output = new FileOutputStream(destination);
	        IOUtility.copy(input, output);
	        IOUtility.shutdownStream(input);
	        IOUtility.shutdownStream(output);
	    }

	    /**
	     * Normalize a path.
	     * Eliminates "/../" and "/./" in a string. Returns <code>null</code> if the ..'s went past the
	     * root.
	     * Eg:
	     * <pre>
	     * /foo//               -->     /foo/
	     * /foo/./              -->     /foo/
	     * /foo/../bar          -->     /bar
	     * /foo/../bar/         -->     /bar/
	     * /foo/../bar/../baz   -->     /baz
	     * //foo//./bar         -->     /foo/bar
	     * /../                 -->     null
	     * </pre>
	     *
	     * @param path the path to normalize
	     * @return the normalized String, or <code>null</code> if too many ..'s.
	     */
	    public static String normalize(final String path) {
	        String normalized = path;
	        // Resolve occurrences of "//" in the normalized path
	        while (true) {
	            int index = normalized.indexOf("//");
	            if (index < 0)
	                break;
	            normalized = normalized.substring(0, index) + normalized.substring(index + 1);
	        }

	        // Resolve occurrences of "/./" in the normalized path
	        while (true) {
	            int index = normalized.indexOf("/./");
	            if (index < 0)
	                break;
	            normalized = normalized.substring(0, index) + normalized.substring(index + 2);
	        }

	        // Resolve occurrences of "/../" in the normalized path
	        while (true) {
	            int index = normalized.indexOf("/../");
	            if (index < 0)
	                break;
	            if (index == 0)
	                return null; // Trying to go outside our context
	            int index2 = normalized.lastIndexOf('/', index - 1);
	            normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
	        }

	        // Return the normalized path that we have completed
	        return normalized;
	    }

	    /**
	     * Will concatenate 2 paths, dealing with <code>..</code>
	     * <p>Eg.,<br />
	     * <code>/a/b/c</code> + <code>d</code> = <code>/a/b/d</code><br />
	     * <code>/a/b/c</code> + <code>../d</code> = <code>/a/d</code><br />
	     * </p>
	     *
	     * Thieved from Tomcat sources...
	     *
	     * @return The concatenated paths, or null if error occurs
	     */
	    public static String catPath(final String lookupPath, final String path) {
	        // Cut off the last slash and everything beyond
	        int index = lookupPath.lastIndexOf("/");
	        String lookup = lookupPath.substring(0, index);
	        String pth = path;

	        // Deal with .. by chopping dirs off the lookup path
	        while (pth.startsWith("../")) {
	            if (lookup.length() > 0) {
	                index = lookup.lastIndexOf("/");
	                lookup = lookup.substring(0, index);
	            }
	            else {
	                // More ..'s than dirs, return null
	                return null;
	            }

	            index = pth.indexOf("../") + 3;
	            pth = pth.substring(index);
	        }

	        return new StringBuffer(lookup).append("/").append(pth).toString();
	    }

	    /**
	     * Resolve a file <code>filename</code> to it's canonical form. If <code>filename</code> is
	     * relative (doesn't start with <code>/</code>), it will be resolved relative to
	     * <code>baseFile</code>, otherwise it is treated as a normal root-relative path.
	     *
	     * @param baseFile Where to resolve <code>filename</code> from, if <code>filename</code> is
	     * relative.
	     * @param filename Absolute or relative file path to resolve.
	     * @return The canonical <code>File</code> of <code>filename</code>.
	     */
	    public static File resolveFile(final File baseFile, String filename) {
	        String filenm = null;
	        if ('/' != File.separatorChar) {
	            filenm = filename.replace('/', File.separatorChar);
	        }

	        if ('\\' != File.separatorChar) {
	            filenm = filename.replace('\\', File.separatorChar);
	        }

	        // deal with absolute files
	        if (filenm.startsWith(File.separator)) {
	            File file = new File(filenm);

	            try {
	                file = file.getCanonicalFile();
	            }
	            catch (final IOException ioe) {}

	            return file;
	        }
	        
	        // FIXME: I'm almost certain this // removal is unnecessary, as getAbsoluteFile() strips
	        // them. However, I'm not sure about this UNC stuff. (JT)
	        final char[] chars = filename.toCharArray();
	        final StringBuffer sb = new StringBuffer();

	        //remove duplicate file separators in succession - except
	        //on win32 at start of filename as UNC filenames can
	        //be \\AComputer\AShare\myfile.txt
	        int start = 0;
	        if ('\\' == File.separatorChar) {
	            sb.append(filenm.charAt(0));
	            start++;
	        }

	        for (int i = start; i < chars.length; i++) {
	            final boolean doubleSeparator = File.separatorChar == chars[i] && File.separatorChar == chars[i - 1];

	            if (!doubleSeparator) {
	                sb.append(chars[i]);
	            }
	        }

	        filenm = sb.toString();

	        //must be relative
	        File file = (new File(baseFile, filenm)).getAbsoluteFile();

	        try {
	            file = file.getCanonicalFile();
	        }
	        catch (final IOException ioe) {}

	        return file;
	    }

	    /**
	     * Delete a file. If file is directory delete it and all sub-directories.
	     */
	    public static void forceDelete(final String file) throws IOException {
	        forceDelete(new File(file));
	    }

	    /**
	     * Delete a file. If file is directory delete it and all sub-directories.
	     */
	    public static void forceDelete(final File file) throws IOException {
	        if (file.isDirectory()) {
	            deleteDirectory(file);
	        }
	        else {
	            if (false == file.delete()) {
	                final String message = "File " + file + " unable to be deleted.";
	                throw new IOException(message);
	            }
	        }
	    }

	    /**
	     * Recursively delete a directory.
	     */
	    public static void deleteDirectory(final String directory) throws IOException {
	        deleteDirectory(new File(directory));
	    }

	    /**
	     * Recursively delete a directory.
	     */
	    public static void deleteDirectory(final File directory) throws IOException {
	        if (!directory.exists()) {
	            return;
	        }

	        cleanDirectory(directory);
	        if (false == directory.delete()) {
	            throw new IOException("Directory " + directory + " unable to be deleted.");
	        }
	    }

	    /**
	     * Clean a directory without deleting it.
	     */
	    public static void cleanDirectory(final String directory) throws IOException {
	        cleanDirectory(new File(directory));
	    }

	    /**
	     * Clean a directory without deleting it.
	     */
	    public static void cleanDirectory(final File directory) throws IOException {
	        if (!directory.exists()) {
	            final String message = directory + " does not exist";
	            throw new IllegalArgumentException(message);
	        }

	        if (!directory.isDirectory()) {
	            final String message = directory + " is not a directory";
	            throw new IllegalArgumentException(message);
	        }

	        final File[] files = directory.listFiles();

	        for (int i = 0; i < files.length; i++) {
	            final File file = files[i];

	            FileUtility.forceDelete(file);
	        }
	    }

	    /**
	     * Replace substrings of one string with another string and return altered string.
	     *
	     * @param original input string
	     * @param oldString the substring section to replace
	     * @param newString the new substring replacing old substring section
	     * @return converted string
	     */
	    private static String replaceSubString(final String original, final String oldString, final String newString) {
	        final StringBuffer sb = new StringBuffer();

	        int end = original.indexOf(oldString);
	        int start = 0;
	        final int stringSize = oldString.length();

	        while (end != -1) {
	            sb.append(original.substring(start, end));
	            sb.append(newString);
	            start = end + stringSize;
	            end = original.indexOf(oldString, start);
	        }

	        end = original.length();
	        sb.append(original.substring(start, end));

	        return sb.toString();
	    }

	    /**
	     * Recursively count size of a directory.
	     *
	     * @return size of directory in bytes.
	     */
	    public static long sizeOfDirectory(final String directory) {
	        return sizeOfDirectory(new File(directory));
	    }

	    /**
	     * Recursively count size of a directory.
	     *
	     * @return size of directory in bytes.
	     */
	    public static long sizeOfDirectory(final File directory) {
	        if (!directory.exists()) {
	            final String message = directory + " does not exist";
	            throw new IllegalArgumentException(message);
	        }

	        if (!directory.isDirectory()) {
	            final String message = directory + " is not a directory";
	            throw new IllegalArgumentException(message);
	        }

	        long size = 0;

	        final File[] files = directory.listFiles();
	        for (int i = 0; i < files.length; i++) {
	            final File file = files[i];

	            if (file.isDirectory()) {
	                size += sizeOfDirectory(file);
	            }
	            else {
	                size += file.length();
	            }
	        }

	        return size;
	    }

	    /**
	     * Get an unique path file
	     * @param basePath base path
	     * @param subPath sub directory name
	     * @return an unique path file that not exists
	     */
	    public static File getUniquePath(String basePath, String subPath) {
	        File resultPath = new File(basePath, subPath);
	        int counter = 1;
	        while (resultPath.exists()) {
	            resultPath = new File(basePath, subPath + counter);
	            counter++;
	        }

	        return resultPath;
	    }

	    /**
	     * Get an unique file
	     * @param dir the path where the file in
	     * @param originFile origin file name
	     * @return an unique file that not exists
	     */
	    public static File getUniqueFile(String dir, String originFile) {
	        String originFileName = removeExtension(originFile);
	        String originFileExt = getExtension(originFile);
	        File resultFile = new File(dir, originFile);
	        int counter = 1;
	        while (resultFile.exists()) {
	            resultFile = new File(dir, new StringBuffer(originFileName).append(counter).append(".").append(originFileExt).toString());
	            counter++;
	        }
	        return resultFile;
	    }
	    /**
	     * create a new dircetory.
	     * @param String pathName
	     */
	    public static void createDir(String pathName)
	    {
	        File file = new File(pathName);
	        if(!file.exists())
	            file.mkdirs();
	    }
	   
	}
