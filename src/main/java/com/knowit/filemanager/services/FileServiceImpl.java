package com.knowit.filemanager.services;

import com.knowit.filemanager.models.AvatarImageResponseModel;
import com.knowit.filemanager.models.ImageRequestModel;
import com.knowit.filemanager.models.ImageResponseModel;
import com.knowit.filemanager.utils.ThumbnailUtils;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class FileServiceImpl implements FileService {

    private final static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    private final StreamBridge streamBridge;
    private final String contestsPath;
    private final String avatarPath;

    public FileServiceImpl(
            StreamBridge streamBridge,
            @Value("${contests.file.path}") String contestsPath,
            @Value("${avatar.file.path}") String avatarPath) {
        this.streamBridge = streamBridge;
        this.contestsPath = contestsPath;
        this.avatarPath = avatarPath;
    }

    public void uploadLectureImage(byte[] image, String contestsId, String name, String format) throws IOException {

        Path path = Paths.get(contestsPath.concat("/").concat(contestsId));

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        ByteArrayInputStream bytes = new ByteArrayInputStream(image);
        BufferedImage bufferedImage = ImageIO.read(bytes);
        File file = new File(path.toString(), name.concat(".").concat(format));
        ImageIO.write(bufferedImage, format, file);
        Path url = Path.of(file.toURI());

        ImageResponseModel imageResponse = new ImageResponseModel();
        imageResponse.setId(contestsId);
        imageResponse.setImageUrl(url.toString());
        imageResponse.setThumbUrl(ThumbnailUtils.thumbnailImage(file, path.toString(), format));

        this.streamBridge.send("sendContestImageResponseModel-out-0", imageResponse);

    }

    @Override
    public AvatarImageResponseModel uploadAvatarImage(
            MultipartFile multipartFile,
            String userId,
            String format
    ) throws IOException {
        String fileName = StringUtils.getFilename(multipartFile.getOriginalFilename());

        File file = new File(this.getDirectoryPath(avatarPath, userId).toString(), fileName);
        multipartFile.transferTo(file);

        AvatarImageResponseModel avatarImage = new AvatarImageResponseModel();
        avatarImage.setPngUrl(Path.of(file.toURI()).toString());
        avatarImage.setThumbUrl(ThumbnailUtils.thumbnailImage
                (file, this.getDirectoryPath(avatarPath, userId).toString(), format));
        avatarImage.setUserId(userId);

        return avatarImage;

    }

    @Bean
    public Supplier<Mono<ImageResponseModel>> sendImage() {
        return () -> Mono.just(new ImageResponseModel());
    }

    @Bean
    public Consumer<KStream<String, ImageRequestModel>> receiveImage() {
        return input -> input.foreach((k, v) -> {
            if (v.getId() == null) {
                return;
            }

            try {
                this.uploadLectureImage(v.getImage(), v.getId(), v.getName(), v.getImageFormat());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private Path getDirectoryPath(String path, String id) throws IOException {
        Path storagePath = Paths.get(path.concat("/").concat(id));

        if (!Files.exists(storagePath)) {
            Files.createDirectories(Path.of(path.concat("/").concat(id)));
        }

        return Paths.get(storagePath.toString());
    }

}
