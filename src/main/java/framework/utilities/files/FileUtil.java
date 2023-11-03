package framework.utilities.files;

import static java.nio.file.Files.copy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

public class FileUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Load file content by file path from resources.
     *
     * @param path path to a file
     * @return String file contents
     */
    public static String loadFileAsString(String path) {
        LOGGER.info("Load file as String from resources: {}", path);
        String fileContents = null;
        try (InputStream inputStream = FileUtil.class.getResourceAsStream(path)) {
            fileContents = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return fileContents;
    }

    /**
     * Retrieve file name from path.
     *
     * @param filePath input path
     * @return String filename
     */
    public static String getFileName(String filePath) {
        Path path = Paths.get(filePath);
        Path fileName = path.getFileName();
        if (null != fileName) {
            return fileName.toString();
        }
        return Strings.EMPTY;
    }

    /**
     * Read file content by relative path to file.
     *
     * @return - return file content as string
     */
    public static String readFile(String pathToFile) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(new FileInputStream(pathToFile), StandardCharsets.UTF_8))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                result.append(currentLine).append("\n");
            }

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return result.toString();
    }

    /**
     * This method parse file line by line and create list of Strings from lines.
     *
     * @param filePath path
     * @return List of strings
     */
    public static List<String> getFileAsStringsList(String filePath) {
        List<String> list = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath), StandardCharsets.UTF_8.name())
            .useDelimiter(System.lineSeparator())) {
            while (scanner.hasNextLine()) {
                list.add(scanner.next());
            }
        } catch (FileNotFoundException e1) {
            LOGGER.error(e1.getMessage());
        }
        return list;
    }

    /**
     * get resource by specified path. if resource was not found, assume path variable is absolute already.
     *
     * @param path path to file
     */
    public static String getAbsPath(String path) {
        return FileUtil.class.getResource(path) != null
            ? FileUtil.class.getResource(path).getPath() : path;
    }

    public static void deleteFolder(String folderPath) {
        deleteDirectoryIfExist(folderPath);
    }

    /**
     * Delete directory if exists.
     *
     * @param folderPath directory to delete
     */
    private static void deleteDirectoryIfExist(String folderPath) {
        boolean isFolderExists = isDirectoryExists(folderPath);
        LOGGER.debug("Delete Folder if exists: {} Folder: {}", isFolderExists, folderPath);
        try {
            if (isFolderExists) {
                FileUtils.forceDelete(new File(folderPath));
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Create directory.
     *
     * @param filePath path to directory.
     * @return File
     */
    public static File mkdir(String filePath) {
        File file = new File(filePath);
        try {
            FileUtils.forceMkdirParent(file);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return file;
    }

    /**
     * Check if file exists.
     *
     * @param filePath path to file
     * @return true if exists
     */
    public static boolean isExists(String filePath) {
        File f = new File(filePath);
        return f.exists() && f.isFile();
    }

    /**
     * Check if directory exists.
     *
     * @param filePath path to directory
     * @return true if exists
     */
    public static boolean isDirectoryExists(String filePath) {
        File f = new File(filePath);
        return f.exists() && f.isDirectory();
    }

    /**
     * Wait for file.
     *
     * @param filePath       path to file
     * @param timeoutSeconds - time to wait in seconds
     * @return true if file was found
     */
    public static boolean waitFor(String filePath, int timeoutSeconds) {
        File file = new File(filePath);
        return FileUtils.waitFor(file, timeoutSeconds);
    }

    /**
     * Delete a file.
     *
     * @param filePath path to file to be deleted
     */
    public static void deleteFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            LOGGER.debug("Deleting file if exists: {} File: {}", isExists(filePath), path);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Recursively search for files.
     *
     * @param folderPath path to folder
     * @return List of located files
     */
    public static List<String> reverseSearchedFilesList(String folderPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            return paths
                .filter(elm -> elm.toFile().isFile())
                .map(Path::toString)
                .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.warn("{} : No such file or directory", folderPath);
        }
        return Collections.emptyList();
    }

    /**
     * Recursively Delete empty folders by given path.
     * @param folderPath directory path
     */
    public static void deleteEmptyFolders(String folderPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .filter(File::isDirectory)
                .forEach(file -> {
                    try {
                        Files.delete(file.toPath());
                    } catch (IOException e) {
                        LOGGER.debug("Failed to remove a folder {}", file.getAbsolutePath());
                    }
                });
        } catch (IOException e) {
            LOGGER.error("Error occurred clearing {}", folderPath);
        }
    }

    /**
     * Retrieve list of files and directories in folder location.
     *
     * @param pathToSearch path to directory
     * @return List of files and folders
     */
    public static List<String> searchFilesAndDirectories(String pathToSearch) {
        if (null == pathToSearch) {
            throw new NullPointerException("Path to files cannot be null");
        }
        List<String> sunFoldersList = new ArrayList<>();
        String[] items = new File(pathToSearch).list();
        if (null == items) {
            throw new IllegalStateException("Path is invalid");
        }
        Stream.of(items)
            .forEach(name -> sunFoldersList.add(pathToSearch + File.separator + name));
        return sunFoldersList;
    }

    /**
     * Search for files in directory using filter.
     *
     * @param folderPath path to directory
     * @param filter     a filter to search for, for example .cab
     * @return List of found files
     */
    public static List<String> filteredFilesList(String folderPath, String filter) {
        List<String> filteredList = new ArrayList<>();
        if (!reverseSearchedFilesList(folderPath).isEmpty()) {
            reverseSearchedFilesList(folderPath).stream()
                .filter(s -> s.contains(filter))
                .forEach(filteredList::add);
        }
        return filteredList;
    }

    /**
     * Gets an input stream from a file.
     *
     * @param filePath    - path to file
     * @param configClass - loader
     * @return InputStream of located file
     * @throws FileNotFoundException in case if file was not found
     */
    public static InputStream getFileInputStream(String filePath, Class<?> configClass) throws FileNotFoundException {
        InputStream inputStream = configClass.getResourceAsStream(filePath);
        if (inputStream == null) {
            inputStream = new FileInputStream(filePath);
        }
        return inputStream;
    }

    /**
     * Replace file.
     *
     * @param pathFrom - source
     * @param pathTo   - target
     */
    public static void replaceFile(String pathFrom, String pathTo) {
        Path from = Paths.get(pathFrom); // convert from String to Path
        Path to = Paths.get(pathTo);
        try {
            copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Replace file from input stream to Path.
     *
     * @param inputStream input stream of a file
     * @param pathTo      Path to file
     */
    public static void replaceFile(InputStream inputStream, Path pathTo) {
        try {
            copy(inputStream, pathTo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Relocate file from resources to real path location.
     *
     * @param resourceFilePath source file
     * @param realPath         target file
     * @return
     */
    public static String relocateFile(String resourceFilePath, String realPath) {
        try (InputStream inputStream = FileUtil.class.getResourceAsStream(resourceFilePath)) {
            String fileName = FilenameUtils.getName(resourceFilePath);
            File targetFile = getCreatedFile(realPath, fileName);
            replaceFile(inputStream, targetFile.toPath());
            return targetFile.getAbsolutePath();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return "";
    }

    /**
     * Create a directory.
     *
     * @param dirPath path to directory
     * @return true if created
     */
    public static boolean createDirectory(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            LOGGER.debug("Create new directory: {}", dirPath);
            return directory.mkdir();
        }
        return false;
    }

    /**
     * Create directories and intermediate directories.
     *
     * @param dirPath path to directory
     * @return true if operation was successful
     */
    public static boolean createDirectories(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            LOGGER.debug("Create new directory: {}", dirPath);
            return directory.mkdirs();
        }
        return false;
    }

    /**
     * Create a file.
     *
     * @param filePath path to new file
     * @return true if operation was successful
     */
    public static boolean createFile(String filePath) {
        LOGGER.debug("Create new file: {}", filePath);
        try {
            return new File(filePath).createNewFile();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    /**
     * Remove file.
     *
     * @param filePath file path
     */
    public static void removeFile(String filePath) {
        File file = new File(filePath);
        LOGGER.debug("Remove file: {} if exist: {}", filePath, file.exists());
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Copy file from filePath to newFilePath.
     *
     * @param filePath    source file path
     * @param newFilePath target file path
     */
    public static void copyFile(String filePath, String newFilePath) {
        LOGGER.debug("Copy file: {} -> {}", filePath, newFilePath);
        try {
            FileUtils.copyFile(new File(filePath), new File(newFilePath));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Delete directory by directory path.
     *
     * @param folderPath path to directory to remove
     */
    public static void removeDirectory(String folderPath) {
        LOGGER.debug("Remove directory: {}", folderPath);
        try {
            FileUtils.deleteDirectory(new File(folderPath));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private static File getCreatedFile(String realPath, String fileName) {
        File targetFile = new File(realPath + File.separator + fileName);
        if (targetFile.mkdirs()) {
            return targetFile;
        } else {
            throw new IllegalStateException("File was not created");
        }
    }

    /**
     * Write to file.
     *
     * @param filePath path to file
     * @param content  content to write
     */
    public static void writeToFile(String filePath, String content) {
        File file = FileUtil.mkdir(filePath);
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            out.write(content);
            LOGGER.debug("File created: {}", filePath);
        } catch (IOException e) {
            LOGGER.error("Cannot find file: [{}]. Please specify valid path to file", filePath);
            LOGGER.catching(e);
        }
    }

    /**
     * Append to a file.
     *
     * @param filePath path to file
     * @param content  content to append
     */
    public static void writeToFileWithAppend(String filePath, String content) {
        LOGGER.debug("File created: {}", filePath);
        try {
            Files.write(
                Paths.get(filePath),
                content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOGGER.error("Cannot find file: [{}]. Please specify valid path to file", filePath);
            LOGGER.catching(e);
        }
    }

    /**
     * Calculate common checksum  with MD5 type for file with MD5.
     *
     * @param filePath - full path to file
     * @return
     */
    public static String getFileMD5Checksum(String filePath) {
        return getFileChecksum(filePath, "MD5");
    }

    /**
     * Calculate checksum for file.
     *
     * @param filePath     - full path to file
     * @param checksumType - example "MD5"
     * @return
     */
    public static String getFileChecksum(String filePath, String checksumType) {
        byte[] buffer;
        byte[] hashSumForFile = new byte[0];
        try {
            buffer = Files.readAllBytes(Paths.get(filePath));
            hashSumForFile = MessageDigest.getInstance(checksumType).digest(buffer);
        } catch (NoSuchAlgorithmException | IOException e) {
            LOGGER.error("Cannot calculate checksum for file: [{}] with type: [{}] ", filePath, checksumType);
            LOGGER.catching(e);
        }
        return DatatypeConverter.printHexBinary(hashSumForFile);
    }


    /**
     * Relocate file from resources to real location.
     *
     * @param classWithResources resource
     * @param resourceFilePath   file path
     * @return String
     */
    public static String readFileFromResources(Class<?> classWithResources, String resourceFilePath) {
        try (InputStream inputStream = classWithResources.getResourceAsStream(resourceFilePath)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return "";
    }

    /**
     * Read file from resource.
     *
     * @param resourceFilePath path to file
     * @return String
     */
    public String readFileFromResources(String resourceFilePath) {
        String result = "";
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(resourceFilePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return result;
    }

    /**
     * This method is created to get file as File object from resource folder.
     *
     * @param resourceFilePath - path to file location in resource folder.
     * @return File object
     * @throws IOException in case if file cannot be created
     */
    public File getFileFromResources(String resourceFilePath) throws IOException {
        File file = new File(FileUtils.getTempDirectoryPath().concat(resourceFilePath));
        if (!file.getParentFile().exists() && !file.exists()) {
            boolean isCreated = file.getParentFile().mkdirs() && file.createNewFile();
            if (!isCreated) {
                LOGGER.warn("Resource {} was not created", resourceFilePath);
            }
        }
        InputStream inputStream = getClass().getResourceAsStream(resourceFilePath);
        copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        IOUtils.closeQuietly(inputStream);
        return file;
    }

}
