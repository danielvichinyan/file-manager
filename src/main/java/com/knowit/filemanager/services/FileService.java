package com.knowit.filemanager.services;

import com.knowit.filemanager.models.AvatarImageResponseModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    AvatarImageResponseModel uploadAvatarImage(MultipartFile multipartFile, String userId, String format) throws IOException;
}
