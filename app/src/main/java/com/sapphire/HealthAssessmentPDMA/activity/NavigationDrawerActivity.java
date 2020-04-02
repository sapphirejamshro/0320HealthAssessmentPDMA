package com.sapphire.HealthAssessmentPDMA.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.sapphire.HealthAssessmentPDMA.R;
import com.sapphire.HealthAssessmentPDMA.adapter.NavigationDrawerAdapter;
import com.sapphire.HealthAssessmentPDMA.bean.NavigationDrawerBean;
import com.sapphire.HealthAssessmentPDMA.bean.NavigationDrawerBean;
import com.sapphire.HealthAssessmentPDMA.fragment.AwarenessFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.DashboardFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.OtherUserInformationFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.PublicListingFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.PublicSurveyFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.UserInformationFragment;
import com.sapphire.HealthAssessmentPDMA.fragment.VerifyOTPFragment;
import com.sapphire.HealthAssessmentPDMA.helper.CommonCode;
import com.sapphire.HealthAssessmentPDMA.interfaces.LockDrawer;
import com.sapphire.HealthAssessmentPDMA.sessionManagement.UserSession;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LockDrawer {

    private Context context;
    private ConstraintLayout  headerTextContConsLayout;
    private AppBarLayout mainAppbarLayout;
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
    /*public static ExpandableHeightGridView bottomGridview;
    public static List<BottomGridviewBean> bottomGridviewBeanList;
    private BottomGridviewAdapter bottomGridviewAdapter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        this.context = this;
        init();
        //layoutBottomButtons.setVisibility(View.GONE);
        bundle = new Bundle();
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
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName());
                if (fragment!=null && !fragment.isVisible()){
                    CommonCode.updateDisplay(new DashboardFragment(),getSupportFragmentManager());
                }
            }
        });
        imageButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(PublicListingFragment.class.getSimpleName());
                System.out.println("==============fragment "+fragment);
                if (fragment==null || !fragment.isVisible()) {
                    CommonCode.updateDisplay(new PublicListingFragment(), getSupportFragmentManager());
                }
            }
        });
        imageButtonNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"In Progress",Toast.LENGTH_SHORT).show();
            }
        });
        imageButtonMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            CommonCode.updateDisplay(new DashboardFragment(),getSupportFragmentManager());
            //For Testing SMS API
           // CommonCode.updateDisplay(new VerifyOTPFragment(),getSupportFragmentManager());

        }else if (screenName!=null && screenName.equalsIgnoreCase("SignInActivityNotVerified")){

            VerifyOTPFragment verifyOTPFragment = new VerifyOTPFragment();
            verifyOTPFragment.setArguments(bundle);
            CommonCode.updateDisplay(verifyOTPFragment,getSupportFragmentManager());

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

        imgBtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawer.isDrawerOpen(GravityCompat.END))
                {drawer.closeDrawer(GravityCompat.END);}
                else
                {drawer.openDrawer(GravityCompat.END);}
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
    }

    @Override
    public void onBackPressed() {
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
                } else if (getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName())!=null
                        && getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName()).isAdded()
                        && getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName()).isVisible()){
                    //finish();
                    closeApp();
                }else if (fragmentCount == 1 && !isBackPressForKeyboard) {
                    finish();
//                    closeApp();
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
                        CommonCode.updateDisplay(new PublicSurveyFragment(),getSupportFragmentManager());
                    }
                }else if (position == 3){
                    Fragment otherAssessment = getSupportFragmentManager().findFragmentByTag(OtherUserInformationFragment.class.getSimpleName())  ;
                    if(otherAssessment!=null && otherAssessment.isVisible()){
                        drawer.closeDrawer(GravityCompat.END);
                    }
                    else{
                        drawer.closeDrawer(GravityCompat.END);
                        CommonCode.updateDisplay(new OtherUserInformationFragment(),getSupportFragmentManager());
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
                    showLogoutDialog("Are you sure you want to logout?","Logout");
                }
            }
        });
    }

    private void showLogoutDialog(String message,String title){
        final AlertDialog.Builder logoutAlertDialog = new AlertDialog.Builder(NavigationDrawerActivity.this,R.style.LogoutDialogTheme);
        logoutAlertDialog.setTitle("Logout");
        LayoutInflater inflater = getLayoutInflater();
        View viewTitle=inflater.inflate(R.layout.logout_title_bar, null);
        ((TextView)viewTitle.findViewById(R.id.tv_title_logout_title_bar)).setText(title);
        logoutAlertDialog.setCustomTitle(viewTitle);
        logoutAlertDialog.setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new UserSession(getApplicationContext()).logout();

                        Intent intent = new Intent(NavigationDrawerActivity.this,SignInActivity.class);
                        startActivity(intent);
                        finish();
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
                // NavigationDrawer.this.finish();
                finish();
            }
        });
        alertDialog.setNegativeButton("No", null);
        closeAlert = alertDialog.show();
        closeAlert.setCanceledOnTouchOutside(false);

    }
}
