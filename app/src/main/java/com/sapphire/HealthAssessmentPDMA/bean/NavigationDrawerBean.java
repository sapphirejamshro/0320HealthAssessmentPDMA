package com.sapphire.HealthAssessmentPDMA.bean;

public class NavigationDrawerBean {

    private String imgName;
    private int imageId;

    public NavigationDrawerBean(String imgName, int imageId) {
        this.imgName = imgName;
        this.imageId = imageId;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
