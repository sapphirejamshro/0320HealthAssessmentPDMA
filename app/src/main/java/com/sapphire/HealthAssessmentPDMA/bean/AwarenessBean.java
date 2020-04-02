package com.sapphire.HealthAssessmentPDMA.bean;

public class AwarenessBean {
    private String imageName;
    private int imageId;

    public AwarenessBean(String imageName, int imageId) {
        this.imageName = imageName;
        this.imageId = imageId;
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

}
