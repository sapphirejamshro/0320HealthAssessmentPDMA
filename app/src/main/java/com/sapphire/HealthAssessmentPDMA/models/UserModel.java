package com.sapphire.HealthAssessmentPDMA.models;

public class UserModel {

    private String name;
    private String mobileNumber;
    private String age;
    private String gender;
    private String district;
    private String tehsil;
    private String unionCouncil;
    private String cnic;
    private String address;
    private String latitude;
    private String longitude;

    public UserModel() {
    }

    public UserModel(String name, String mobileNumber, String age, String gender, String district, String tehsil, String unionCouncil, String cnic, String address, String latitude, String longitude) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.age = age;
        this.gender = gender;
        this.district = district;
        this.tehsil = tehsil;
        this.unionCouncil = unionCouncil;
        this.cnic = cnic;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public String getUnionCouncil() {
        return unionCouncil;
    }

    public void setUnionCouncil(String unionCouncil) {
        this.unionCouncil = unionCouncil;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
