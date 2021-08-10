package com.knowit.filemanager.controllers;

import com.knowit.filemanager.models.FileResponseModel;
import com.knowit.filemanager.services.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String listAllFiles(Model model) {

        model.addAttribute("files", this.fileService.loadAll().map(
                path -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/download/")
                        .path(path.getFileName().toString())
                        .toUriString())
                .collect(Collectors.toList()));

        return model.getAttribute("files").toString();
    }

    @PostMapping("/upload-file")
    @ResponseBody
    public FileResponseModel uploadFile(@RequestParam("file") MultipartFile file) {
        String name = this.fileService.store(file);

        String uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(name)
                .toUriString();

        return new FileResponseModel(name, uri, file.getContentType(), file.getSize());
    }

//    @GetMapping("/download/{filename}")
//    @ResponseBody
//    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
//
//        Resource resource = this.fileService.loadAsResource(filename);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }

    @GetMapping("/download/{filename}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getFile(@PathVariable String filename) throws FileNotFoundException {

        File file = this.fileService.loadAsFile(filename);

        InputStream inputStream = new FileInputStream(file.getPath());
        HttpHeaders headers = new HttpHeaders();

        headers.set("Accept-Ranges", "bytes");
        headers.set("Content-Type", "video/mp4");
        headers.set("Content-Range", "bytes 50-1025/17839845");
        headers.set("Content-Length", String.valueOf(file.length()));

        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }
}
