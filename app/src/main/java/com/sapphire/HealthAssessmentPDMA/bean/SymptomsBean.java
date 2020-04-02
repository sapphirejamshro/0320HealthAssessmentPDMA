package com.sapphire.HealthAssessmentPDMA.bean;

public class SymptomsBean {
    private int imageName;
    private int imageId;

    public SymptomsBean(int imageName, int imageId) {
        this.imageName = imageName;
        this.imageId = imageId;
    }

    public int getImageName() {
        return imageName;
    }

    public void setImageName(int imageName) {
        this.imageName = imageName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

}
