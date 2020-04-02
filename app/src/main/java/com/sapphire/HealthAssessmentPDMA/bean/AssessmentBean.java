package com.sapphire.HealthAssessmentPDMA.bean;

public class AssessmentBean {
    private String question;
    private String answer;
    private String flightNo;
    private String countryName;
    private String passportNo;
    private String contactedPersonName;
    private String contactedPersonMobNo;

    public AssessmentBean(String question, String answer, String flightNo, String countryName, String passportNo, String contactedPersonName, String contactedPersonMobNo) {
        this.question = question;
        this.answer = answer;
        this.flightNo = flightNo;
        this.countryName = countryName;
        this.passportNo = passportNo;
        this.contactedPersonName = contactedPersonName;
        this.contactedPersonMobNo = contactedPersonMobNo;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getContactedPersonName() {
        return contactedPersonName;
    }

    public void setContactedPersonName(String contactedPersonName) {
        this.contactedPersonName = contactedPersonName;
    }

    public String getContactedPersonMobNo() {
        return contactedPersonMobNo;
    }

    public void setContactedPersonMobNo(String contactedPersonMobNo) {
        this.contactedPersonMobNo = contactedPersonMobNo;
    }
}
