package com.sapphire.HealthAssessmentPDMA.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UserSpecificListingRVAdapterBean implements Parcelable {


    private String userName;
    private int surveyNo;
    private String  assessmentDate;
    private String assessmentResult;
    private String assessmentId;

    public UserSpecificListingRVAdapterBean() {
    }

    public UserSpecificListingRVAdapterBean(String userName, int surveyNo, String assessmentDate, String assessmentResult, String assessmentId) {
        this.userName = userName;
        this.surveyNo = surveyNo;
        this.assessmentDate = assessmentDate;
        this.assessmentResult = assessmentResult;
        this.assessmentId = assessmentId;
    }

    protected UserSpecificListingRVAdapterBean(Parcel in) {
        userName = in.readString();
        surveyNo = in.readInt();
        assessmentDate = in.readString();
        assessmentResult = in.readString();
        assessmentId = in.readString();
    }

    public static final Creator<UserSpecificListingRVAdapterBean> CREATOR = new Creator<UserSpecificListingRVAdapterBean>() {
        @Override
        public UserSpecificListingRVAdapterBean createFromParcel(Parcel in) {
            return new UserSpecificListingRVAdapterBean(in);
        }

        @Override
        public UserSpecificListingRVAdapterBean[] newArray(int size) {
            return new UserSpecificListingRVAdapterBean[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSurveyNo() {
        return surveyNo;
    }

    public void setSurveyNo(int surveyNo) {
        this.surveyNo = surveyNo;
    }

    public String getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(String assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public String getAssessmentResult() {
        return assessmentResult;
    }

    public void setAssessmentResult(String assessmentResult) {
        this.assessmentResult = assessmentResult;
    }

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeInt(surveyNo);
        dest.writeString(assessmentDate);
        dest.writeString(assessmentResult);
        dest.writeString(assessmentId);
    }
}
