package com.knowit.filemanager.services;

import com.knowit.filemanager.models.FileResponseModel;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileService {

//    FileResponseModel uploadFile(MultipartFile multipartFile, String userId) throws IOException;

//    List<String> uploadFiles(List<MultipartFile> multipartFiles) throws IOException;

    void init();

    String store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
}
