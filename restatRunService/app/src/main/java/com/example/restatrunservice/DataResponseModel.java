package com.example.restatrunservice;

import com.google.gson.annotations.SerializedName;

public class DataResponseModel {

    @SerializedName("Status")
    private boolean checkplay;

    @SerializedName("Link")
    private String link;

    @SerializedName("Volume")
    private int volumeConfig;

    public DataResponseModel() {

    }

    public boolean isCheckplay() {
        return checkplay;
    }

    public void setCheckplay(boolean checkplay) {
        this.checkplay = checkplay;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getVolumeConfig() {
        return volumeConfig;
    }

    public void setVolumeConfig(int volumeConfig) {
        this.volumeConfig = volumeConfig;
    }
}
