package com.example.restatrunservice;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("Status")
  private boolean checkplay;

    @SerializedName("Link")
  private String link;

    @SerializedName("Volume")
    private int volumeConfig;

  private boolean checkSenLink;

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

    public boolean isCheckSenLink() {
        return checkSenLink;
    }

    public void setCheckSenLink(boolean checkSenLink) {
        this.checkSenLink = checkSenLink;
    }

    public User() {
    }

    public User(boolean checkplay, String link, boolean checkSenLink) {
        this.checkplay = checkplay;
        this.link = link;
        this.checkSenLink = checkSenLink;
    }
}
