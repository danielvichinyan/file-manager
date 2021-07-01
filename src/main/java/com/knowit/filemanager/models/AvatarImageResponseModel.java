package com.knowit.filemanager.models;

public class AvatarImageResponseModel {

    private String pngUrl;

    private String thumbUrl;

    private String userId;

    public String getPngUrl() {
        return pngUrl;
    }

    public void setPngUrl(String pngUrl) {
        this.pngUrl = pngUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
