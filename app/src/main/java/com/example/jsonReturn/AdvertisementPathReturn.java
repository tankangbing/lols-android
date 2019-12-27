package com.example.jsonReturn;

/**
 * Created by DELL on 2017/9/7.
 */

public class AdvertisementPathReturn {

    boolean success;
    String photoAdPath;
    String videoAdPath;
    String adVideoTime;
    boolean hasAd;
    public String getPhotoAdPath() {
        return photoAdPath;
    }

    public void setPhotoAdPath(String photoAdPath) {
        this.photoAdPath = photoAdPath;
    }

    public String getVideoAdPath() {
        return videoAdPath;
    }

    public void setVideoAdPath(String videoAdPath) {
        this.videoAdPath = videoAdPath;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAdVideoTime() {
        return adVideoTime;
    }

    public void setAdVideoTime(String adVideoTime) {
        this.adVideoTime = adVideoTime;
    }

    public boolean isHasAd() {
        return hasAd;
    }

    public void setHasAd(boolean hasAd) {
        this.hasAd = hasAd;
    }
}
