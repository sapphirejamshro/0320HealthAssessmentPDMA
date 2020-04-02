package com.sapphire.HealthAssessmentPDMA.sessionManagement;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {

    private static final String FILE_NAME = "PUBLIC-COVID19";
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isLogin = false;

    public UserSession(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String selectedLanguage){
        editor.putString("selectedLanguage",selectedLanguage);
        editor.commit();
    }
    public void createUserSession(String userId,String name, String mobileNo,String age,
                                  String gender, String district, String tehsil, String address,
                                  String cnic,String latitude, String longitude,String userType){
        editor.putString("userId",userId);
        editor.putString("name",name);
        editor.putString("mobileNo",mobileNo);
        editor.putString("age",age);
        editor.putString("gender",gender);
        editor.putString("district",district);
        editor.putString("tehsil",tehsil);
        editor.putString("address",address);
        editor.putString("cnic",cnic);
        editor.putString("latitude",latitude);
        editor.putString("longitude",longitude);
        editor.putString("userType",userType);
        editor.commit();
    }

    public String getSelectedLanguage(){
        return sharedPreferences.getString("selectedLanguage","");
    }
    public String getName(){
        return sharedPreferences.getString("name","");
    }
    public String getMobileNo(){
        return sharedPreferences.getString("mobileNo","");
    }
    public String getAge(){
        return sharedPreferences.getString("age","");
    }
    public String getGender(){
        return sharedPreferences.getString("gender","");
    }
    public String getDistrict(){
        return sharedPreferences.getString("district","");
    }
    public String getTehsil(){
        return sharedPreferences.getString("tehsil","");
    }
    public String getAddress(){
        return sharedPreferences.getString("address","");
    }
    public String getCnic(){
        return sharedPreferences.getString("cnic","");
    }

    public String getUserId(){
        return sharedPreferences.getString("userId","");
    }

    public void setMobileNo(String mobileNo){
        editor.putString("mobileNo",mobileNo);
        editor.commit();
    }
    public void removeSelectedLanguage() {
        editor.remove("selectedLanguage");
    }

    public void logout(){
        editor.remove("userId");
        editor.remove("name");
        editor.remove("mobileNo");
        editor.remove("age");
        editor.remove("gender");
        editor.remove("district");
        editor.remove("tehsil");
        editor.remove("address");
        editor.remove("cnic");
        editor.remove("latitude");
        editor.remove("longitude");
        editor.remove("userType");
    }

    // Added By Hina on 26-March-2020
    public void addOtherUserInfo(String otherUserId,String otherUserType){
        editor.putString("otherUserId",otherUserId);
        editor.putString("otherUserType",otherUserType);
        editor.commit();
    }
    public String getOtherUserId(){
        return sharedPreferences.getString("otherUserId","");
    }
    public String getOtherUserType(){
        return sharedPreferences.getString("otherUserType","");
    }
}
