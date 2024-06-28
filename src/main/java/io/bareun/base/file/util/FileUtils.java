package io.bareun.base.file.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

@Component
public class FileUtils {

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static void upload(MultipartFile file, String fullPath) {
        File destFile = new File(fullPath);
        File parentDirectory = destFile.getParentFile();

        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new IllegalStateException("Fail upload ", e);
        }
    }

    public static Resource getResource(String filePath) {
        try {
            return new UrlResource("file:" + filePath);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Fail get resource ", e);
        }
    }
}
