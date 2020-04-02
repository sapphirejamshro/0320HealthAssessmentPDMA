package com.sapphire.HealthAssessmentPDMA.bean;

public class DashboardBean {
    private String imageName;
    private int imageId;
    private int colorId;

    public DashboardBean(String imageName, int imageId, int colorId) {
        this.imageName = imageName;
        this.imageId = imageId;
        this.colorId = colorId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }
}
