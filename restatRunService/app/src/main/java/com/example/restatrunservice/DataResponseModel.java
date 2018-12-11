package com.example.restatrunservice;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DataResponseModel {

    @SerializedName("AppId")
    private int appId;

    @SerializedName("Name")
    private String name;

    @SerializedName("Voltage")
    private String voltage;

    @SerializedName("Resistant")
    private String resistant;

    @SerializedName("Tempurature")
    private String tempurature;

    @SerializedName("LastTime")
    private String lastTime;

    @SerializedName("MinusVolume")
    private int minusVolume;

    @SerializedName("Volume")
    private int volume;

    @SerializedName("AddVolume")
    private int addvolume;

    @SerializedName("Link")
    private String link;

    public DataResponseModel() {
        // Create
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getResistant() {
        return resistant;
    }

    public void setResistant(String resistant) {
        resistant = resistant;
    }

    public String getTempurature() {
        return tempurature;
    }

    public void setTempurature(String tempurature) {
        this.tempurature = tempurature;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public int getMinusVolume() {
        return minusVolume;
    }

    public void setMinusVolume(int minusVolume) {
        this.minusVolume = minusVolume;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getAddvolume() {
        return addvolume;
    }

    public void setAddvolume(int addvolume) {
        this.addvolume = addvolume;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
