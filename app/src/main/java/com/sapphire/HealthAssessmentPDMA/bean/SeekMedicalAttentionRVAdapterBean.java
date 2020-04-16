package com.sapphire.HealthAssessmentPDMA.bean;

public class SeekMedicalAttentionRVAdapterBean {
    private int img;
    private int text;

    public SeekMedicalAttentionRVAdapterBean(int img) {
        this.img = img;
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
