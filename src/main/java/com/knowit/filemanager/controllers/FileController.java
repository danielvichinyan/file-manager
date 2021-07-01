package com.knowit.filemanager.controllers;

import com.knowit.filemanager.models.AvatarImageResponseModel;
import com.knowit.filemanager.services.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/image")
    public AvatarImageResponseModel uploadFile(
            @RequestParam("image") MultipartFile multipartFile,
            @RequestParam("userId") String userId,
            @RequestParam("typeFormat") String typeFormat
    ) throws IOException {
        return this.fileService.uploadAvatarImage(multipartFile, userId, typeFormat);
    }
}
