package com.sapphire.HealthAssessmentPDMA.bean;

public class PreventionRVAdapterBean {
    private int img;
    private int text;

    public PreventionRVAdapterBean(int img, int text) {
        this.img = img;
        this.text = text;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getText() {
        return text;
    }

    public void setText(int text) {
        this.text = text;
    }
}
