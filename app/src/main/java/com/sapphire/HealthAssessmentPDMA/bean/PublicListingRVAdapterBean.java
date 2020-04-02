package com.sapphire.HealthAssessmentPDMA.bean;

public class PublicListingRVAdapterBean {
    private String userId;
    private String name;
    private int surveryNo;
    private String  date;
    private String result;

    public PublicListingRVAdapterBean(String userId, String name, int surveryNo, String date, String result) {
        this.userId = userId;
        this.name = name;
        this.surveryNo = surveryNo;
        this.date = date;
        this.result = result;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSurveryNo() {
        return surveryNo;
    }

    public void setSurveryNo(int surveryNo) {
        this.surveryNo = surveryNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
