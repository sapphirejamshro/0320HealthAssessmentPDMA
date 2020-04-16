package com.sapphire.HealthAssessmentPDMA.bean;

public class OtherUserListingRVAdapterBean {

    private String userId;
    private String name;
    private String phoneNo;
    private String userType;

    public OtherUserListingRVAdapterBean(String userId, String name, String phoneNo, String userType) {
        this.userId = userId;
        this.name = name;
        this.phoneNo = phoneNo;
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
