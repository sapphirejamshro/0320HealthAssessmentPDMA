package com.sapphire.HealthAssessmentPDMA.bean;



public class SymptomsInfoBean {
    private String title;
    private String subTitle;
    private String infoText;
    private Boolean isBottomVisible;

    public SymptomsInfoBean() {
    }

    public SymptomsInfoBean(String title, String subTitle, String infoText) {
        this.title = title;
        this.subTitle = subTitle;
        this.infoText = infoText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public Boolean getIsBottomVisible() {
        return isBottomVisible;
    }

    public void setIsBottomVisible(Boolean isBottomVisible) {
        this.isBottomVisible = isBottomVisible;
    }
}
