package com.kts.facebookapp.dtos;

public class ShareGameLinkDTO {
    private String link;
    private String iconURL;
    private String pictureURL;
    private String name;
    private String description;

    public ShareGameLinkDTO(String link, String iconURL, String pictureURL, String name, String description) {
        this.link = link;
        this.iconURL = iconURL;
        this.pictureURL = pictureURL;
        this.name = name;
        this.description = description;
    }

    
    public String getDescription() {
        return description;
    }

    public String getIconURL() {
        return iconURL;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getPictureURL() {
        return pictureURL;
    }
    
}
