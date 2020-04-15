package com.sapphire.HealthAssessmentPDMA.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.android.volley.VolleyError;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.Gson;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.adapter.NavigationDrawerAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.NavigationDrawerBean;
import com.sapphire.HealthAssessmentPDMA.bean.NavigationDrawerBean;
import com.sapphire.HealthAssessmentPDMA.bean.OtherUserListingRVAdapterBean;
import com.sapphire.HealthAssessmentPDMA.fragment.AwarenessFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.DashboardFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.InformationTabFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.NovelCoronaVirusDetails;
import com.sapphire.HealthAssessmentPDMA.fragment.OtherAssessmentFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.OtherUserInformationFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.OtherUserListingFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.PublicListingFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.PublicSurveyFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.PublicSurveyResultFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.UserInformationFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.UserSpecificListingFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.VerifyOTPFragment;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.interfaces.LockDrawer;
import com.sapphire.HealthAssessmentPDMA.interfaces.VolleyCallback;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;
import com.sapphire.HealthAssessmentPDMA.webService.UserService;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LockDrawer {

    private Context context;
    private ConstraintLayout  headerTextContConsLayout;
    private AppBarLayout mainAppbarLayout;
    private NavigationView navigationView;
    private ListView listView;
    private List<NavigationDrawerBean> navigationBeanList;
    private NavigationDrawerAdapter adapter;
    private DrawerLayout drawer;
    private ImageButton imgBtnMenu;
    public static TextView tvHeading;
    Bundle bundle;
    String screenName="", mobileNo="";
    private AlertDialog logoutAlert;
    private ImageButton imageButtonHome,imageButtonProfile,
            imageButtonNotifications,imageButtonMessages;
    public static LinearLayout layoutBottomButtons;
    public static KeyboardView keyboardview;
    public static Boolean isBackPressForKeyboard = false;
    String selectedLanguage ="";
    private CommonCode commonCode;
    private Activity activity;
    private long lastClickTime = 0;
    /*public static ExpandableHeightGridView bottomGridview;
    public static List<BottomGridviewBean> bottomGridviewBeanList;
    private BottomGridviewAdapter bottomGridviewAdapter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        this.context = this;
        activity = this;
        init();
        //layoutBottomButtons.setVisibility(View.GONE);
        bundle = new Bundle();
        commonCode = new CommonCode(context);
        selectedLanguage = new UserSession(getApplicationContext()).getSelectedLanguage();
        /*bottomGridviewBeanList = new ArrayList<>();
        bottomGridviewBeanList.add(new BottomGridviewBean(R.mipmap.home_icon));
        bottomGridviewBeanList.add(new BottomGridviewBean(R.mipmap.profile_icon));
        bottomGridviewBeanList.add(new BottomGridviewBean(R.mipmap.notification_icon));
        bottomGridviewBeanList.add(new BottomGridviewBean(R.mipmap.messge_icon));

        bottomGridviewAdapter = new BottomGridviewAdapter(this,bottomGridviewBeanList,true);
        bottomGridview.setAdapter(bottomGridviewAdapter);
        bottomGridview.setExpanded(true);

        bottomGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = (ImageView) view;

            }
        });*/
        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentByTag(OtherAssessmentFragment.class.getSimpleName());
                if(f != null && f.isVisible()){
                    showLogoutDialog("Are you sure, you want to go Home Screen?","Confirmation","HomeScreen");
                }else{
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName());
                    if(fragment != null && !fragment.isVisible()){
                        CommonCode.updateDisplay(fragment,getSupportFragmentManager());
                    }else{
                        CommonCode.updateDisplay(new DashboardFragment(),getSupportFragmentManager());
                    }
                    //Removed by Kamran on 3 April 2020
                    /*if (fragment!=null && !fragment.isVisible()){
                        CommonCode.updateDisplay(new DashboardFragment(),getSupportFragmentManager());
                    }*/
                }
            }
        });
        imageButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment f = getSupportFragmentManager().findFragmentByTag(OtherAssessmentFragment.class.getSimpleName());
                if(f != null && f.isVisible()){
                    showLogoutDialog("Are you sure, you want to go to Track Assessment Screen?","Confirmation","TrackAssessmentScreen");
                }else{
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(PublicListingFragment.class.getSimpleName());
                    if(fragment != null ){
                        if(!fragment.isVisible()){
                            String currentFragment = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                            if(currentFragment.equalsIgnoreCase(UserSpecificListingFragment.class.getSimpleName()) ||
                                    currentFragment.equalsIgnoreCase(InformationTabFragment.class.getSimpleName())){
                                getSupportFragmentManager().popBackStack(PublicListingFragment.class.getSimpleName(),0);
                            }else{
                                CommonCode.updateDisplay(fragment,getSupportFragmentManager());
                            }
                        }
                    }else{
                        CommonCode.updateDisplay(new PublicListingFragment(),getSupportFragmentManager());
                    }
                    //Removed by Kamran on 3 April 2020
                    /*if (fragment==null || !fragment.isVisible()) {
                        CommonCode.updateDisplay(new PublicListingFragment(), getSupportFragmentManager());
                    }*/
                }
            }
        });
        imageButtonNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime()-lastClickTime<3000){
                    return;
                }
                lastClickTime =  SystemClock.elapsedRealtime();
                Toast.makeText(getApplicationContext(),"In Progress",Toast.LENGTH_SHORT).show();
            }
        });
        imageButtonMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime()-lastClickTime<3000){
                    return;
                }
                lastClickTime =  SystemClock.elapsedRealtime();
                Toast.makeText(getApplicationContext(),"In Progress",Toast.LENGTH_SHORT).show();
            }
        });
        if (getIntent().getExtras()!=null) {
            bundle = getIntent().getExtras();
            if (bundle.getString("screen") != null) {
                screenName = bundle.getString("screen");
            }
            if (bundle.getString("mobileNo") != null){
                mobileNo = bundle.getString("mobileNo");
            }
        }
        if (screenName!=null && screenName.equalsIgnoreCase("SignInActivity")){
//            CommonCode.updateDisplay(new DashboardFragment(),getSupportFragmentManager());
            //For Testing SMS API
            CommonCode.updateDisplay(new VerifyOTPFragment(),getSupportFragmentManager());

        }else if (screenName!=null && (screenName.equalsIgnoreCase("SignInActivityNotVerified") ||
                screenName.equalsIgnoreCase("VerifiedLogin"))){

            VerifyOTPFragment verifyOTPFragment = new VerifyOTPFragment();
            verifyOTPFragment.setArguments(bundle);
            //Removed for Testing
            CommonCode.updateDisplay(verifyOTPFragment,getSupportFragmentManager());
            //Added for testing
           //CommonCode.updateDisplay(new DashboardFragment(),getSupportFragmentManager());

        }else {
            CommonCode.updateDisplay(new UserInformationFragment(), getSupportFragmentManager());
        }
        navigationBeanList = new ArrayList<>();

        if (selectedLanguage.equalsIgnoreCase("Sindhi")){
            navigationBeanList = new ArrayList<>();
            navigationBeanList.add(new NavigationDrawerBean("مینیو",R.drawable.ic_dashboard));
            navigationBeanList.add(new NavigationDrawerBean(getResources().getString(R.string.corona_awareness_sindhi),R.drawable.ic_colored_awarness));
            navigationBeanList.add(new NavigationDrawerBean(getResources().getString(R.string.assessment_for_self_sindhi),R.drawable.ic_colored_self_assessment));
            navigationBeanList.add(new NavigationDrawerBean(getResources().getString(R.string.assessment_for_other_sindhi),R.drawable.ic_colored_other_assessment));
            navigationBeanList.add(new NavigationDrawerBean(getResources().getString(R.string.track_assessment_sindhi),R.drawable.ic_colored_previous_assessment));
            navigationBeanList.add(new NavigationDrawerBean("لاگ آوٽ",R.drawable.ic_logout));
        }else if(selectedLanguage.equalsIgnoreCase("Urdu")){
            navigationBeanList = new ArrayList<>();
            navigationBeanList.add(new NavigationDrawerBean("مینیو",R.drawable.ic_dashboard));

            navigationBeanList.add(new NavigationDrawerBean("   "+getResources().getString(R.string.corona_awareness_urdu),R.drawable.ic_colored_awarness));
            navigationBeanList.add(new NavigationDrawerBean(getResources().getString(R.string.assessment_for_self_urdu),R.drawable.ic_colored_self_assessment));
            navigationBeanList.add(new NavigationDrawerBean(getResources().getString(R.string.assessment_for_other_urdu),R.drawable.ic_colored_other_assessment));
            navigationBeanList.add(new NavigationDrawerBean(getResources().getString(R.string.track_assessment_urdu),R.drawable.ic_colored_previous_assessment));
            navigationBeanList.add(new NavigationDrawerBean("لاگ آوٹ",R.drawable.ic_logout));

        }else if(selectedLanguage.equalsIgnoreCase("en")){
            navigationBeanList = new ArrayList<>();
            navigationBeanList.add(new NavigationDrawerBean("MENU",R.drawable.ic_dashboard));

            navigationBeanList.add(new NavigationDrawerBean(getResources().getString(R.string.corona_awareness),R.drawable.ic_colored_awarness));
            navigationBeanList.add(new NavigationDrawerBean(getResources().getString(R.string.assessment_for_self),R.drawable.ic_colored_self_assessment));
            navigationBeanList.add(new NavigationDrawerBean(getResources().getString(R.string.assessment_for_other),R.drawable.ic_colored_other_assessment));
            navigationBeanList.add(new NavigationDrawerBean(getResources().getString(R.string.track_assessment),R.drawable.ic_colored_previous_assessment));
            navigationBeanList.add(new NavigationDrawerBean("Logout",R.drawable.ic_logout));
        }

        adapter = new NavigationDrawerAdapter(getApplicationContext(),navigationBeanList,selectedLanguage);
        listView.setAdapter(adapter);
        addListViewListener();


        imgBtnMenu.setFocusable(true);
        imgBtnMenu.setFocusableInTouchMode(true);
        imgBtnMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imgBtnMenu.requestFocus();
                return false;
            }
        });
        imgBtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(GravityCompat.END))
                {drawer.closeDrawer(GravityCompat.END);}
                else
                {drawer.openDrawer(GravityCompat.END);}
            }
        });


        drawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                drawer.requestFocus();
                return false;
            }
        });

    }

    private void init(){
        headerTextContConsLayout = findViewById(R.id.header_background);
        listView = findViewById(R.id.lstNdMenu);
        drawer =  findViewById(R.id.drawer_layout);
        tvHeading = findViewById(R.id.tv_heading);
        imgBtnMenu = findViewById(R.id.imgBtnMenuND);
        //bottomGridview = findViewById(R.id.gridView_Bottom);
        imageButtonHome = findViewById(R.id.imgBtnHome);
        imageButtonProfile = findViewById(R.id.imgBtnProfile);
        imageButtonNotifications = findViewById(R.id.imgBtnNotification);
        imageButtonMessages = findViewById(R.id.imgBtnMessages);
        layoutBottomButtons = findViewById(R.id.layoutBottomButtons);
        keyboardview = (KeyboardView) findViewById(R.id.keyboard);
        navigationView = findViewById(R.id.nav_view);
    }

    @Override
    public void onBackPressed() {
        Fragment publicSurveyResFrag = getSupportFragmentManager().findFragmentByTag(PublicSurveyResultFragment.class.getSimpleName());
        if(!isBackPressForKeyboard){
            if(drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            }
            else {
                int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
                String currentFrag = getSupportFragmentManager().getBackStackEntryAt(fragmentCount - 1).getName();
                if (isBackPressForKeyboard) {
                    isBackPressForKeyboard = false;
                }else if(currentFrag.equalsIgnoreCase(VerifyOTPFragment.class.getSimpleName())){
                    getSupportFragmentManager().popBackStackImmediate(0,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    finish();
                } else if(currentFrag.equalsIgnoreCase(OtherAssessmentFragment.class.getSimpleName())){
                    // Added by hina on 14-Apr-2020
                    boolean isPreFragOAssess = true;
                    String preFrag = "";
                    int i = 2;
                    while (isPreFragOAssess){
                         preFrag = getSupportFragmentManager().getBackStackEntryAt(fragmentCount - i).getName();
                         if(preFrag!=null && preFrag.equalsIgnoreCase(OtherAssessmentFragment.class.getSimpleName())){

                             i++;
                         }
                         else{
                             isPreFragOAssess = false;
                             break;
                         }
                    }

                    if(preFrag.equalsIgnoreCase(OtherUserInformationFragment.class.getSimpleName())){
                        showLogoutDialog("Are you sure, you want to go back to Dashboard Screen?","Confirmation","OtherUserInformationScreen");
                    }else if(preFrag.equalsIgnoreCase(OtherUserListingFragment.class.getSimpleName())){
                        showLogoutDialog("Are you sure, you want to go back to Other Users Screen?","Confirmation","OtherUserListingScreen");
                    }
                }else if (getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName())!=null
                        && getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName()).isAdded()
                        && getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName()).isVisible()){
                    //finish();
                    closeApp();
                }else if (fragmentCount == 1 && !isBackPressForKeyboard) {
                    finish();
//                    closeApp();
                }
                // Added on 13-April-2020 by Hina
                else if(publicSurveyResFrag!=null && publicSurveyResFrag.isVisible()){
                    if(PublicSurveyResultFragment.isComeFromOtherAssess){
                        // remove Survey ResultFragment
                        getSupportFragmentManager().beginTransaction().remove(publicSurveyResFrag).commit();
                        getSupportFragmentManager().popBackStack();
                        Fragment otherUserFrag = getSupportFragmentManager().findFragmentByTag(OtherUserInformationFragment.class.getSimpleName());
                        if(otherUserFrag!=null){
                            getSupportFragmentManager().beginTransaction().remove(publicSurveyResFrag).commit();
                            getSupportFragmentManager().popBackStack();
                        }
                        // Now Check Listing Fragment
                        Fragment otherUserListingFarg = getSupportFragmentManager().findFragmentByTag(OtherUserListingFragment.class.getSimpleName());
                        if(otherUserListingFarg==null){
                            CommonCode.updateDisplay(new OtherUserListingFragment(),getSupportFragmentManager());
                        }

                    }
                    else{
                        super.onBackPressed();
                    }
                }
                else {
                    super.onBackPressed();
                }
            }
        }else{
            isBackPressForKeyboard = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void addListViewListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    Fragment homeFragment = getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName())  ;
                    if(homeFragment!=null && homeFragment.isVisible()){
                        drawer.closeDrawer(GravityCompat.END);
                    }
                    else{
                        drawer.closeDrawer(GravityCompat.END);
                        CommonCode.updateDisplay(new DashboardFragment(),getSupportFragmentManager());
                    }
                }else if (position ==1){
                    Fragment awarenessFragment = getSupportFragmentManager().findFragmentByTag(AwarenessFragment.class.getSimpleName())  ;
                    if(awarenessFragment!=null && awarenessFragment.isVisible()){
                        drawer.closeDrawer(GravityCompat.END);
                    }
                    else{
                        drawer.closeDrawer(GravityCompat.END);
                        CommonCode.updateDisplay(new AwarenessFragment(),getSupportFragmentManager());
                    }
                }else if (position == 2){
                    Fragment assessmentFragment = getSupportFragmentManager().findFragmentByTag(PublicSurveyFragment.class.getSimpleName())  ;
                    if(assessmentFragment!=null && assessmentFragment.isVisible()){
                        drawer.closeDrawer(GravityCompat.END);
                    }
                    else{
                        drawer.closeDrawer(GravityCompat.END);
                        checkAssessmentAllowedMethod(Integer.valueOf(new UserSession(activity).getUserId()));
                        //CommonCode.updateDisplay(new PublicSurveyFragment(),getSupportFragmentManager());
                    }
                }else if (position == 3){
                    Fragment otherAssessment = getSupportFragmentManager().findFragmentByTag(OtherUserInformationFragment.class.getSimpleName())  ;
                    if(otherAssessment!=null && otherAssessment.isVisible()){
                        drawer.closeDrawer(GravityCompat.END);
                    }
                    else{
                        drawer.closeDrawer(GravityCompat.END);
                        //CommonCode.updateDisplay(new OtherUserInformationFragment(),getSupportFragmentManager());
                        getUserHavingOtherAssessments(Integer.valueOf(new UserSession(activity).getUserId()));
                    }
                }else if (position == 4){
                    Fragment publicListingFrag = getSupportFragmentManager().findFragmentByTag(PublicListingFragment.class.getSimpleName())  ;
                    if(publicListingFrag!=null && publicListingFrag.isVisible()){
                        drawer.closeDrawer(GravityCompat.END);
                    }
                    else{
                        drawer.closeDrawer(GravityCompat.END);
                        Fragment publicListing = new PublicListingFragment();
                        Bundle b = new Bundle();
                        b.putString("preScreen","Dashboard");
                        publicListing.setArguments(b);
                        CommonCode.updateDisplay(publicListing,getSupportFragmentManager());
//                        CommonCode.updateDisplay(new InformationTabFragment(),getSupportFragmentManager());
                    }
                }else if (position == 5){

                    drawer.closeDrawer(GravityCompat.END);
                    showLogoutDialog("Are you sure you want to logout?","Logout","logout");
                }
            }
        });
    }

    private void showLogoutDialog(String message, String title, final String type){
        final AlertDialog.Builder logoutAlertDialog = new AlertDialog.Builder(NavigationDrawerActivity.this,R.style.LogoutDialogTheme);
        logoutAlertDialog.setTitle("Logout");
        LayoutInflater inflater = getLayoutInflater();
        View viewTitle=inflater.inflate(R.layout.logout_title_bar, null);
        if(!type.equalsIgnoreCase("logout")){
            ImageView icon = viewTitle.findViewById(R.id.imgV_icon_logout_title_bar);
            icon.setVisibility(View.GONE);
        }
        ((TextView)viewTitle.findViewById(R.id.tv_title_logout_title_bar)).setText(title);
        logoutAlertDialog.setCustomTitle(viewTitle);
        logoutAlertDialog.setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new UserSession(getApplicationContext()).logout();


                        if(type.equalsIgnoreCase("logout")){
                            Intent intent = new Intent(NavigationDrawerActivity.this,SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }else if(type.equalsIgnoreCase("HomeScreen")){
                            Fragment fragment = getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName());
                            if (fragment!=null && !fragment.isVisible()){
                                CommonCode.updateDisplay(fragment,getSupportFragmentManager());
                            }else{
                                CommonCode.updateDisplay(new DashboardFragment(),getSupportFragmentManager());
                            }
                        }else if(type.equalsIgnoreCase("TrackAssessmentScreen")){
                            Fragment fragment = getSupportFragmentManager().findFragmentByTag(PublicListingFragment.class.getSimpleName());
                            if (fragment !=null && !fragment.isVisible()) {
                                CommonCode.updateDisplay(fragment,getSupportFragmentManager());
                            }else{
                                CommonCode.updateDisplay(new PublicListingFragment(), getSupportFragmentManager());
                            }
                        }else if(type.equalsIgnoreCase("OtherUserInformationScreen")){
                            getSupportFragmentManager().popBackStack(DashboardFragment.class.getSimpleName(),0);
                        }else if(type.equalsIgnoreCase("OtherUserListingScreen")){
                            Fragment fragment = getSupportFragmentManager().findFragmentByTag(OtherAssessmentFragment.class.getSimpleName());
                            if(fragment!=null){
                                getSupportFragmentManager().popBackStackImmediate(OtherAssessmentFragment.class.getSimpleName(),FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                            else{
                                getSupportFragmentManager().popBackStack();
                            }

                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutAlert.dismiss();
                    }
                });
        logoutAlert = logoutAlertDialog.show();
        TextView textView = logoutAlert.findViewById(android.R.id.message);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"myriad_pro_regular.ttf");
        textView.setTypeface(typeface);
        logoutAlert.setCanceledOnTouchOutside(false);
    }

    @Override
    public void lockDrawer(){
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,GravityCompat.END);
        imgBtnMenu.setVisibility(View.GONE);
        layoutBottomButtons.setVisibility(View.GONE);
        //bottomGridview.setVisibility(View.GONE);
//        toggle.setDrawerIndicatorEnabled(false);
    }


    @Override
    public void unLockDrawer(){
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,GravityCompat.END);
        imgBtnMenu.setVisibility(View.VISIBLE);
        layoutBottomButtons.setVisibility(View.VISIBLE);
        //bottomGridview.setVisibility(View.VISIBLE);
//        toggle.setDrawerIndicatorEnabled(true);
    }

    private void closeApp(){

        AlertDialog closeAlert;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NavigationDrawerActivity.this,R.style.AlertDialogTheme);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Are you sure you want to exit?")
                .setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                NavigationDrawerActivity.this.finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
                System.exit(0);
            }
        });
        alertDialog.setNegativeButton("No", null);
        closeAlert = alertDialog.show();
        closeAlert.setCanceledOnTouchOutside(false);

    }

    private void checkAssessmentAllowedMethod(Integer userId){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new UserService(context).checkAssessmentAllowed(userId, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                Boolean isResponded= false, isAllowed = false, isAssessmentAvailable = false;
                if (response != null && response.length()>0){
                    try {
                        String statusDescription = response.getString("statusDescription");
                        JSONObject jsonObject = response.getJSONObject("data");
                        if (jsonObject.length()>0){
                            if (!jsonObject.isNull("is_assessment_available") &&
                                    jsonObject.getString("is_assessment_available").equalsIgnoreCase("1")){
                                isAssessmentAvailable = true;
                            }
                            if (!jsonObject.isNull("time_to_allow_assessment") &&
                                    jsonObject.getString("time_to_allow_assessment").equalsIgnoreCase("1")){
                                isAllowed = true;
                            }
                            if (!jsonObject.isNull("is_user_responded") &&
                                    jsonObject.getString("is_user_responded").equalsIgnoreCase("1")){
                                isResponded = true;
                            }

                            if (!isAssessmentAvailable){
                                CommonCode.updateDisplay(new PublicSurveyFragment(), getSupportFragmentManager());
                            }else if (isAllowed && !isResponded){
                                CommonCode.updateDisplay(new PublicSurveyFragment(), getSupportFragmentManager());
                            }else if (isResponded){
                                commonCode.showErrorORSuccessAlert(activity,"error","Sorry, your Assessment is already available and is assigned to a concerned team. Please have some patience",getSupportFragmentManager(),null,true);
                            }else {
                                commonCode.showErrorORSuccessAlert(activity,"error","Sorry, your Assessment is already available and is in process, please wait for sometime.",getSupportFragmentManager(),null,true);
                            }
                        }
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getSupportFragmentManager(),null,false);
                    }
                }else {
                    progressDialog.dismiss();
                    commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getSupportFragmentManager(),null,false);
                }
            }
            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getSupportFragmentManager(),null,false);
            }
        });
    }

    private void getUserHavingOtherAssessments(final Integer userId){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        final List<OtherUserListingRVAdapterBean> beanList = new ArrayList<>();

        new UserService(activity).getUsersHavingAssessments(userId,"other",new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                String publicUserId="",userType="",userMobileNumber="", userName="";
                if(response != null && response.length() > 0 && !response.isNull("statusDescription")){
                    try {
                        String status = response.getString("statusDescription");
                        if(status != null && status.length() > 0 && status.equalsIgnoreCase("Success") ){
                            JSONObject data = response.getJSONObject("data");
                            int count=0;
                            if(data != null && data.length() > 0){
                                for (int i=1; i<=data.length();i++){
                                    count++;
                                    if (!data.isNull(""+count)){
                                        JSONObject innerObj = data.getJSONObject(""+count);
                                        //for (int j=0; j<innerObj.length();j++){
                                        if (!innerObj.isNull("public_user_id")) {
                                            publicUserId = innerObj.getString("public_user_id");
                                        }
                                        if (!innerObj.isNull("name")) {
                                            userName = innerObj.getString("name");
                                        }
                                        if (!innerObj.isNull("user_type")) {
                                            userType = innerObj.getString("user_type");
                                        }
                                        if (!innerObj.isNull("mobile")){
                                            userMobileNumber = innerObj.getString("mobile");
                                        }

                                        beanList.add(new OtherUserListingRVAdapterBean(publicUserId,userName,userMobileNumber,userType));
                                    }else {
                                        i--;
                                    }

                                }
                                if (beanList.size()>0){
                                    Bundle bundle = new Bundle();
                                    bundle.putString("dataList",new Gson().toJson(beanList));
                                    Fragment fragmentOtherUserListing = new OtherUserListingFragment();
                                    fragmentOtherUserListing.setArguments(bundle);
                                    CommonCode.updateDisplay(fragmentOtherUserListing,getSupportFragmentManager());
                                }else {
                                    CommonCode.updateDisplay(new OtherUserInformationFragment(), getSupportFragmentManager());
                                }
                            }else{
                                progressDialog.dismiss();
                                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getSupportFragmentManager(),null,false);
                            }
                        }
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        if (e.getMessage() != null && e.getMessage().equalsIgnoreCase("Value [] at data of type org.json.JSONArray cannot be converted to JSONObject")){
                            CommonCode.updateDisplay(new OtherUserInformationFragment(), getSupportFragmentManager());
                        }else{
                            commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getSupportFragmentManager(),null,false);
                        }

                    }

                }else{
                    progressDialog.dismiss();
                    commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please try again!",getSupportFragmentManager(),null,false);
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                commonCode.showErrorORSuccessAlert(activity,"error","Something went wrong, please check your internet connection.",getSupportFragmentManager(),null,false);
            }
        });
    }

}
