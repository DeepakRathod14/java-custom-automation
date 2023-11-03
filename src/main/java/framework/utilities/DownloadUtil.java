package framework.utilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DownloadUtil {

    private static final Logger LOGGER = LogManager.getLogger(DownloadUtil.class);

    private DownloadUtil() {
        //empty constructor
    }

    /**
     * Download file by URL in new file path.
     *
     * @param url      - URL to file
     * @param filePath - new file path location
     */
    public static File downloadFromResources(String url, String filePath) {
        LOGGER.debug("Download file from resource: {} --> to: {}", url, filePath);
        URL downloadUrl = null;
        try {
            downloadUrl = new URL(url);
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage());
        }
        File downloadedFile = new File(filePath);
        try {
            FileUtils.copyURLToFile(Objects.requireNonNull(downloadUrl), downloadedFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return downloadedFile;
    }

    /**
     * Download file by URL keep origin file name to folder location.
     *
     * @param url        - URL to file
     * @param folderPath - folder file location
     */
    public static File downloadFileFromResources(String url, String folderPath) {
        URL downloadUrl = null;
        try {
            downloadUrl = new URL(url);
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage());
        }
        String fileName = FilenameUtils.getName(Objects.requireNonNull(downloadUrl).getPath());
        return downloadFromResources(url, new File(folderPath, fileName).getPath());
    }


}
