package com.sapphire.HealthAssessmentPDMA.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.helper.CustomTypefaceSpan;
import com.sapphire.HealthAssessmentPDMA.helper.WorkaroundMapFragment;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ViewUserDetailsFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    private Activity activity;
    private TextView tvMobileNo,tvAge,tvGender,tvName,tvAddress,tvCNIC,tvTaluka,tvDistrict,
            tvMobileNoValue,tvAgeValue,tvGenderValue,tvNameValue,tvAddressValue,tvCNICValue,
            tvTalukaValue,tvDistrictValue;
    public UserSession session;
    public ProgressDialog progressDialog;
    String nameString="", mobNoString="",ageString="",genderString="",districtString="",
            talukaString="",addressString="",cnicString="", latitudeString="",longitudeString="";
    private WorkaroundMapFragment mapFragment;
    private CommonCode commonCode;
    GoogleMap mMap;

    public ViewUserDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_view_user_details, container, false);
        activity = getActivity();
        commonCode = new CommonCode(activity);
        session = new UserSession(activity);
        progressDialog = new ProgressDialog(activity);
        //set Map in fragment
        mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
        init();
        try {
            JSONObject jsonObject = new JSONObject(getArguments().getString("userObj"));
            getUserValues(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setCustomFonts();
        setValues();
        return view;
    }
    //onMapReady ready function starts
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String title="";
        // Add a marker in Jamshore, Sindh, and move the camera.
        LatLng jamshoro = new LatLng(Double.valueOf(latitudeString), Double.valueOf(longitudeString));

        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(Double.valueOf(latitudeString),Double.valueOf(longitudeString),1);
            if(addressList!=null && addressList.size()>0){
                title = addressList.get(0).getAddressLine(0);
            }

        } catch (IOException e) {
            System.out.println("===============exception "+e);
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().position(jamshoro).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jamshoro,15));



            LatLng latLng = new LatLng(Double.valueOf(latitudeString),Double.valueOf(longitudeString));
            //mMap.addMarker(new MarkerOptions().position(latLng).title(arrayList.get(i).getTitle()));
            //createMarker(arrayList.get(i).getLatitude(), arrayList.get(i).getLongitude(), arrayList.get(i).getTitle());




    }//onMapReady function ends


    private void getUserValues(JSONObject object){
        if (object != null && object.length()>0){

                try {
                    if (!object.isNull("name")) {
                        nameString= object.getString("name");
                    }
                    if (!object.isNull("mobile")) {
                       mobNoString= object.getString("mobile");
                    }
                    if (!object.isNull("age")) {
                        ageString= object.getString("age");
                    }
                    if (!object.isNull("gender")) {
                        genderString  = object.getString("gender");
                    }
                    if (!object.isNull("district_name")) {
                       districtString = object.getString("district_name");
                    }
                    if (!object.isNull("taluka_name")) {
                       talukaString= object.getString("taluka_name");
                    }
                    if (!object.isNull("address")) {
                        addressString= object.getString("address");
                    }
                    if (!object.isNull("cnic")) {
                        cnicString= object.getString("cnic");
                    }
                    if (!object.isNull("latitude")) {
                       latitudeString= object.getString("latitude");
                    }
                    if (!object.isNull("longitude")) {
                        longitudeString = object.getString("longitude");
                    }
                    setValues();
                } catch (JSONException e) {


                }
            }
    }


    private void init() {
        tvMobileNo = view.findViewById(R.id.tv_mobileNo_user_detTab);
        tvAge = view.findViewById(R.id.tv_age_user_detTab);
        tvGender = view.findViewById(R.id.tv_gender_user_detTab);
        tvName = view.findViewById(R.id.tv_name_user_detTab);
        tvAddress = view.findViewById(R.id.tv_address_user_detTab);
        tvCNIC = view.findViewById(R.id.tv_cnicNo_user_detTab);
        tvTaluka = view.findViewById(R.id.tv_taluka_user_detTab);
        tvDistrict = view.findViewById(R.id.tv_district_user_detTab);

        tvMobileNoValue = view.findViewById(R.id.tv_mobileNo_Value_user_detTab);
        tvAgeValue = view.findViewById(R.id.tv_age_Value_user_detTab);
        tvGenderValue = view.findViewById(R.id.tv_gender_Value_user_detTab);
        tvNameValue = view.findViewById(R.id.tv_nameNo_Value_user_detTab);
        tvAddressValue = view.findViewById(R.id.tv_addressNo_Value_user_detTab);
        tvCNICValue = view.findViewById(R.id.tv_cnicNo_Value_user_detTab);
        tvTalukaValue = view.findViewById(R.id.tv_taluka_Value_user_detTab);
        tvDistrictValue = view.findViewById(R.id.tv_district_Value_user_detTab);

    }
    private void setCustomFonts() {

        Typeface fontEng = Typeface.createFromAsset(getActivity().getAssets(),"myriad_pro_regular.ttf");
        Typeface fontUrdu = Typeface.createFromAsset(getActivity().getAssets(),"notonastaliqurdu_regular.ttf");

        // for mobile No
        SpannableString spanStringMob = new SpannableString(tvMobileNo.getText().toString());
        spanStringMob.setSpan(new CustomTypefaceSpan("",fontEng),0,6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanStringMob.setSpan(new CustomTypefaceSpan("",fontUrdu),10,spanStringMob.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvMobileNo.setText(spanStringMob);

        // for Age
        SpannableString spanStringAge = new SpannableString(tvAge.getText().toString());
        spanStringAge.setSpan(new CustomTypefaceSpan("",fontEng),0,3, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanStringAge.setSpan(new CustomTypefaceSpan("",fontUrdu),5,spanStringAge.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvAge.setText(spanStringAge);

        // for Gender
        SpannableString spanStringGender = new SpannableString(tvGender.getText().toString());
        spanStringGender.setSpan(new CustomTypefaceSpan("",fontEng),0,6, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanStringGender.setSpan(new CustomTypefaceSpan("",fontUrdu),8,spanStringGender.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvGender.setText(spanStringGender);

        //for Name
        SpannableString spanStringName = new SpannableString(tvName.getText().toString());
        spanStringName.setSpan(new CustomTypefaceSpan("",fontEng),0,4,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanStringName.setSpan(new CustomTypefaceSpan("",fontUrdu),5,spanStringName.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvName.setText(spanStringName);

        //for Address
        SpannableString spanStringAddress = new SpannableString(tvAddress.getText().toString());
        spanStringAddress.setSpan(new CustomTypefaceSpan("",fontEng),0,7,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanStringAddress.setSpan(new CustomTypefaceSpan("",fontUrdu),8,spanStringAddress.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvAddress.setText(spanStringAddress);

        // for CNIC
        SpannableString spanStringCNIC = new SpannableString(tvCNIC.getText().toString());
        spanStringCNIC.setSpan(new CustomTypefaceSpan("",fontEng),0,4,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanStringCNIC.setSpan(new CustomTypefaceSpan("",fontUrdu),6,spanStringCNIC.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvCNIC.setText(spanStringCNIC);

        // for Taluka
        SpannableString spanStringTaluka = new SpannableString(tvTaluka.getText().toString());
        spanStringTaluka.setSpan(new CustomTypefaceSpan("",fontEng),0,6,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanStringTaluka.setSpan(new CustomTypefaceSpan("",fontUrdu),7,spanStringTaluka.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvTaluka.setText(spanStringTaluka);

        //for District
        SpannableString spanStringDistrict = new SpannableString(tvDistrict.getText().toString());
        spanStringDistrict.setSpan(new CustomTypefaceSpan("",fontEng),0,8,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spanStringDistrict.setSpan(new CustomTypefaceSpan("",fontUrdu),10,spanStringDistrict.length()-1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvDistrict.setText(spanStringDistrict);
    }

    private void setValues(){
        tvNameValue.setText(nameString);
        tvMobileNoValue.setText(mobNoString);
        tvAgeValue.setText(ageString);
        tvGenderValue.setText(genderString);
        tvDistrictValue.setText(districtString);
        tvTalukaValue.setText(talukaString);
        tvAddressValue.setText(addressString);
        tvCNICValue.setText(cnicString);
    }
}
