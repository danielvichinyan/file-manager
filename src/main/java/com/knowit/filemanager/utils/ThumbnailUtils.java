package com.knowit.filemanager.utils;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class ThumbnailUtils {

    private ThumbnailUtils() {
    }

    public static String thumbnailImage(File file, String dirPath, String formatName) throws IOException {
        UUID uuid = UUID.randomUUID();

        String newImageName = uuid.toString().concat("_thumb.").concat(formatName);
        File files = new File(dirPath, newImageName);
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(file);

        BufferedImage img = ImageIO.read(imageInputStream);
        BufferedImage thumbImg = null;
        thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 100, Scalr.OP_ANTIALIAS);
        ImageIO.write(thumbImg, formatName, files);

        return Path.of(files.toURI()).toString();
    }

}
