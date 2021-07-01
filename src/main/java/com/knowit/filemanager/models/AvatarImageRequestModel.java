package com.knowit.filemanager.models;

import org.springframework.web.multipart.MultipartFile;

public class AvatarImageRequestModel {

    private MultipartFile multipartFile;

    private String userId;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
