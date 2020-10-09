package com.ece.cov19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ece.cov19.DataModels.DashBoardNumberModel;
import com.ece.cov19.DataModels.ImageDataModel;
import com.ece.cov19.DataModels.LoggedInUserData;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.ClickTimeChecker;
import com.ece.cov19.Functions.LoginUser;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.ContentValues.TAG;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDonorInfo;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserEligibility;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserGender;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;
import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PASS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PHONE;
import static com.ece.cov19.SplashActivity.Language_pref;
import static com.ece.cov19.SplashActivity.Selected_language;


public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    private CardView findDonorCardView, addPatientCardView, requestsCardView, responsesCardView, fromDonorsCardView,
            fromPatientsCardView, exploreCardView, myPatientsCardView, allDonorsCardView, allPatientsCardView,
            exploreCCardView, exploreDCardView, exploreECardView, exploreFCardView;

    private ImageView dashboardDrawerBtn, dashboardGenderIcon;
    private TextView dashboard, numberOfPatients,numberOfDonors,numberOfRequestsFromDonors,
            numberOfRequestsFromPatients,numberOfRequestsFromDonorsText,numberOfRequestsFromPatientsText, exploreCText, exploreDText,
            exploreEText, exploreFText;

    private ConstraintLayout loadingView;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ReviewInfo reviewInfo;
    private ReviewManager manager;

    private Button xRay;

    public int backCounter;
    public int requestResponseSwitcher;
    public int requestResponseCardViewSwitcher;
    public int exploreSwitcher;

    private String noOfDonors, noOfPatients, noOfRequestsFromDonors, noOfRequestsFromPatients, noOfResponsesFromDonors, noOfResponsesFromPatients, nameOfActivity;

    Bitmap insertBitmap;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        drawerLayout =findViewById(R.id.drawer_layout);
        navigationView =findViewById(R.id.nav_view);
        loadingView=findViewById(R.id.loadingView);
        dashboard=findViewById(R.id.dashboard_header);

        dashboardGenderIcon=findViewById(R.id.dashboard_gender_icon);
        dashboardDrawerBtn=findViewById(R.id.dashboard_drawer_btn);

        findDonorCardView=findViewById(R.id.cardView_findDonor);
        addPatientCardView=findViewById(R.id.cardView_addPatient);
        requestsCardView=findViewById(R.id.cardView_requests);
        responsesCardView=findViewById(R.id.cardView_responses);
        fromDonorsCardView=findViewById(R.id.cardView_requestA);
        fromPatientsCardView=findViewById(R.id.cardView_requestB);
        exploreCardView=findViewById(R.id.cardView_explore);
        myPatientsCardView=findViewById(R.id.cardView_myPatients);
        allDonorsCardView=findViewById(R.id.cardView_exploreA);
        allPatientsCardView=findViewById(R.id.cardView_exploreB);
        exploreCCardView=findViewById(R.id.cardView_exploreC);
        exploreDCardView=findViewById(R.id.cardView_exploreD);
        exploreECardView=findViewById(R.id.cardView_exploreE);
        exploreFCardView=findViewById(R.id.cardView_exploreF);


        numberOfDonors=findViewById(R.id.dashboard_no_of_donors);
        numberOfPatients=findViewById(R.id.dashboard_no_of_patients);
        numberOfRequestsFromDonors=findViewById(R.id.dashboard_no_of_requests_from_donors);
        numberOfRequestsFromDonorsText=findViewById(R.id.dashboard_text_no_of_requests_from_donors);
        numberOfRequestsFromPatients=findViewById(R.id.dashboard_no_of_requests_from_patients);
        numberOfRequestsFromPatientsText=findViewById(R.id.dashboard_text_no_of_requests_from_patients);
        exploreCText=findViewById(R.id.dashboard_text_exploreC);
        exploreDText=findViewById(R.id.dashboard_text_exploreD);
        exploreEText=findViewById(R.id.dashboard_text_exploreE);
        exploreFText=findViewById(R.id.dashboard_text_exploreF0);

        xRay=findViewById(R.id.dashboard_button_x_ray);



        loadingView.setVisibility(View.VISIBLE);

        fromDonorsCardView.setVisibility(View.GONE);
        fromPatientsCardView.setVisibility(View.GONE);
        allDonorsCardView.setVisibility(View.GONE);
        allPatientsCardView.setVisibility(View.GONE);
        exploreCCardView.setVisibility(View.GONE);
        exploreDCardView.setVisibility(View.GONE);
        exploreECardView.setVisibility(View.GONE);
        exploreFCardView.setVisibility(View.GONE);



        downloadImage(loggedInUserPhone);

        xRay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashboardActivity.this,XRayActivity.class);
                startActivity(intent);
            }
        });



        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
//To do//
                            return;
                        }

// Get the Instance ID token//
                        String token = task.getResult().getToken();
                        String msg = getString(R.string.fcm_token, token);
                        Log.d(TAG, msg);

                        RetroInterface retroInterface = RetroInstance.getRetro();
                        Call<UserDataModel> incomingResponse = retroInterface.sendToken(LoggedInUserData.loggedInUserPhone,token);
                        incomingResponse.enqueue(new Callback<UserDataModel>() {
                            @Override
                            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                            }

                            @Override
                            public void onFailure(Call<UserDataModel> call, Throwable t) {

                            }
                        });

                    }
                });


        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<DashBoardNumberModel> dashBoardNumbers = retroInterface.getDashBoardNumbers(loggedInUserPhone);
        dashBoardNumbers.enqueue(new Callback<DashBoardNumberModel>() {
            @Override
            public void onResponse(Call<DashBoardNumberModel> call, Response<DashBoardNumberModel> response) {
                loadingView.setVisibility(View.GONE);


                noOfDonors = response.body().getNumberOfDonors();
                noOfPatients = response.body().getNumberOfPatients();
                noOfRequestsFromDonors = response.body().getNumberOfRequestsFromDonors();
                noOfRequestsFromPatients = response.body().getNumberOfRequestsFromPatients();
                noOfResponsesFromDonors = response.body().getNumberOfResponsesFromDonors();
                noOfResponsesFromPatients = response.body().getNumberOfResponsesFromPatients();

            }

            @Override
            public void onFailure(Call<DashBoardNumberModel> call, Throwable t) {
                loadingView.setVisibility(View.GONE);


            }
        });

        retroInterface = RetroInstance.getRetro();
        Call<DashBoardNumberModel> donorEligibility = retroInterface.eligibilityCheck(loggedInUserPhone);
        donorEligibility.enqueue(new Callback<DashBoardNumberModel>() {
            @Override
            public void onResponse(Call<DashBoardNumberModel> call, Response<DashBoardNumberModel> response) {

                if(response.body().getServerMsg().toLowerCase().equals("true")) {
                    if (response.body().getEligibility().toLowerCase().equals("eligible")) {
                        loggedInUserEligibility = "eligible";
                    } else if (response.body().getEligibility().toLowerCase().equals("not_eligible")) {
                        loggedInUserEligibility = "not_eligible";
                    }
                }
            }

            @Override
            public void onFailure(Call<DashBoardNumberModel> call, Throwable t) {
            }
        });



        retroInterface = RetroInstance.getRetro();
        Call<UserDataModel> checkNotification = retroInterface.checkNotification(loggedInUserPhone);
        checkNotification.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                if(response.body().getServerMsg().equals("No Notifications")){

                }
                else{
                    nameOfActivity = response.body().getServerMsg();


                    if(nameOfActivity.equals("RequestsFromPatientsActivity")) {
                        Intent intent = new Intent(getApplicationContext(), RequestsFromPatientsActivity.class);
                        startActivity(intent);
                    }
                    if(nameOfActivity.equals("RequestsFromDonorsActivity")) {
                        Intent intent = new Intent(getApplicationContext(), RequestsFromDonorsActivity.class);
                        startActivity(intent);
                    }
                    if(nameOfActivity.equals("DonorResponseActivity")) {
                        Intent intent = new Intent(getApplicationContext(), DonorResponseActivity.class);
                        startActivity(intent);
                    }
                    if(nameOfActivity.equals("PatientResponseActivity")) {
                        Intent intent = new Intent(getApplicationContext(), PatientResponseActivity.class);
                        startActivity(intent);
                    }
                    if(nameOfActivity.equals("MyPatientsActivity")) {
                        Intent intent = new Intent(getApplicationContext(), MyPatientsActivity.class);
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
            }
        });




        navigationView.setNavigationItemSelectedListener(this);
        dashboardGenderIcon.setOnClickListener(this);
        dashboard.setOnClickListener(this);
        dashboardDrawerBtn.setOnClickListener(this);
        findDonorCardView.setOnClickListener(this);
        addPatientCardView.setOnClickListener(this);
        requestsCardView.setOnClickListener(this);
        responsesCardView.setOnClickListener(this);
        fromDonorsCardView.setOnClickListener(this);
        fromPatientsCardView.setOnClickListener(this);
        exploreCardView.setOnClickListener(this);
        myPatientsCardView.setOnClickListener(this);
        allDonorsCardView.setOnClickListener(this);
        allPatientsCardView.setOnClickListener(this);
        exploreCCardView.setOnClickListener(this);
        exploreDCardView.setOnClickListener(this);
        exploreECardView.setOnClickListener(this);
        exploreFCardView.setOnClickListener(this);
        exploreCText.setOnClickListener(this);
        exploreDText.setOnClickListener(this);
        exploreEText.setOnClickListener(this);
        exploreFText.setOnClickListener(this);




    }

    @Override
    protected void onResume() {
        super.onResume();
if(LoginUser.checkLoginStat().equals("failed")){
    SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
    String phone,password;

    if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {
        phone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
        password= sharedPreferences.getString(LOGIN_USER_PASS, "");

        LoginUser.loginUser(this,phone,password,DashboardActivity.class);
    }
    else {
        ToastCreator.toastCreatorRed(this,getString(R.string.login_failed));
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}



        loadingView.setVisibility(View.VISIBLE);




        downloadImage(loggedInUserPhone);

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<DashBoardNumberModel> dashBoardNumbers = retroInterface.getDashBoardNumbers(loggedInUserPhone);
        dashBoardNumbers.enqueue(new Callback<DashBoardNumberModel>() {
            @Override
            public void onResponse(Call<DashBoardNumberModel> call, Response<DashBoardNumberModel> response) {
                loadingView.setVisibility(View.GONE);
                noOfDonors = response.body().getNumberOfDonors();
                noOfPatients = response.body().getNumberOfPatients();
                noOfRequestsFromDonors = response.body().getNumberOfRequestsFromDonors();
                noOfRequestsFromPatients = response.body().getNumberOfRequestsFromPatients();
                noOfResponsesFromDonors = response.body().getNumberOfResponsesFromDonors();
                noOfResponsesFromPatients = response.body().getNumberOfResponsesFromPatients();

                numberOfDonors.setText(noOfDonors);
                numberOfPatients.setText(noOfPatients);

                if(requestResponseCardViewSwitcher == 1) {
                    if(requestResponseSwitcher == 1) {
                        requestResponseCardViewSwitcher = 1;
                        requestResponseSwitcher = 1;
                        fromDonorsCardView.setVisibility(View.VISIBLE);
                        fromPatientsCardView.setVisibility(View.VISIBLE);
                        numberOfRequestsFromDonorsText.setText(getResources().getString(R.string.requests));
                        numberOfRequestsFromPatientsText.setText(getResources().getString(R.string.requests));
                        numberOfRequestsFromDonors.setText(noOfRequestsFromDonors);
                        numberOfRequestsFromPatients.setText(noOfRequestsFromPatients);
                    }

                    if(requestResponseSwitcher == 2){
                        requestResponseCardViewSwitcher = 1;
                        requestResponseSwitcher = 2;
                        fromDonorsCardView.setVisibility(View.VISIBLE);
                        fromPatientsCardView.setVisibility(View.VISIBLE);
                        numberOfRequestsFromDonorsText.setText(getResources().getString(R.string.responses));
                        numberOfRequestsFromPatientsText.setText(getResources().getString(R.string.responses));
                        numberOfRequestsFromDonors.setText(noOfResponsesFromDonors);
                        numberOfRequestsFromPatients.setText(noOfResponsesFromPatients);
                    }
                }
                else if(requestResponseCardViewSwitcher == 0) {
                    requestResponseCardViewSwitcher = 0;
                    requestResponseSwitcher = 0;
                    fromDonorsCardView.setVisibility(View.GONE);
                    fromPatientsCardView.setVisibility(View.GONE);
                }


                if(exploreSwitcher==1) {
                    exploreSwitcher=1;
                    allDonorsCardView.setVisibility(View.VISIBLE);
                    allPatientsCardView.setVisibility(View.VISIBLE);
                    exploreCCardView.setVisibility(View.VISIBLE);
                    exploreDCardView.setVisibility(View.VISIBLE);
                    exploreECardView.setVisibility(View.VISIBLE);
                    exploreFCardView.setVisibility(View.VISIBLE);

                }
                else if(exploreSwitcher==0) {
                    exploreSwitcher=0;
                    allDonorsCardView.setVisibility(View.GONE);
                    allPatientsCardView.setVisibility(View.GONE);
                    exploreCCardView.setVisibility(View.GONE);
                    exploreDCardView.setVisibility(View.GONE);
                    exploreECardView.setVisibility(View.GONE);
                    exploreFCardView.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<DashBoardNumberModel> call, Throwable t) {

                loadingView.setVisibility(View.GONE);

            }
        });

        retroInterface = RetroInstance.getRetro();
        Call<DashBoardNumberModel> donorEligibility = retroInterface.eligibilityCheck(loggedInUserPhone);
        donorEligibility.enqueue(new Callback<DashBoardNumberModel>() {
            @Override
            public void onResponse(Call<DashBoardNumberModel> call, Response<DashBoardNumberModel> response) {

                if(response.body().getServerMsg().toLowerCase().equals("true")) {
                    if (response.body().getEligibility().toLowerCase().equals("eligible")) {
                        loggedInUserEligibility = "eligible";
                    } else if (response.body().getEligibility().toLowerCase().equals("not_eligible")) {
                        loggedInUserEligibility = "not_eligible";
                    }


                }
                else if(response.body().getServerMsg().toLowerCase().equals("false")){
                }
            }

            @Override
            public void onFailure(Call<DashBoardNumberModel> call, Throwable t) {
            }
        });




        dashboardGenderIcon.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        dashboard.setOnClickListener(this);
        dashboardDrawerBtn.setOnClickListener(this);
        findDonorCardView.setOnClickListener(this);
        addPatientCardView.setOnClickListener(this);
        requestsCardView.setOnClickListener(this);
        responsesCardView.setOnClickListener(this);
        fromDonorsCardView.setOnClickListener(this);
        fromPatientsCardView.setOnClickListener(this);
        exploreCardView.setOnClickListener(this);
        myPatientsCardView.setOnClickListener(this);
        allDonorsCardView.setOnClickListener(this);
        allPatientsCardView.setOnClickListener(this);
        exploreCCardView.setOnClickListener(this);
        exploreDCardView.setOnClickListener(this);
        exploreECardView.setOnClickListener(this);
        exploreFCardView.setOnClickListener(this);
        exploreCText.setOnClickListener(this);
        exploreDText.setOnClickListener(this);
        exploreEText.setOnClickListener(this);
        exploreFText.setOnClickListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.profile){
            Intent profileIntent=new Intent(DashboardActivity.this, ViewUserProfileActivity.class);
            startActivity(profileIntent);
        }
        else if (id == R.id.english) {
            languageAlertDialog("en");
        }
        else if (id == R.id.bangla) {
            languageAlertDialog("bn");
        }
        else if (id == R.id.shareApp){
            Intent shareIntent=new Intent((Intent.ACTION_SEND));
            shareIntent.setType("text/plain");
            String shareBody="Blood and Plasma Banking solution app: Plasma+\n http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
            String shareSub="Plasma+";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
            shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
            startActivity(Intent.createChooser(shareIntent,"Share Using"));

        }
        else if (id == R.id.emailUs){

            String email = "ece.lab.ruet@gmail.com";
            String subject = "Contact Us";
            String body = "Please share your valuable thoughts with us.";
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, body);
            startActivityForResult(intent,200);
        }

        else if(id == R.id.sendReview){
            Uri uri = Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSd_PgXtE8sYdaxCIp4pPXM6IqU7ZvoA963iBksFejGIOUYH6g/viewform?usp=sf_link");
            Intent review = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(review);
        }

        else if(id == R.id.rateApp){

            Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(goToMarket);
//            try {
//                startActivity(goToMarket);
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
//            }

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            backCounter++;
            if (backCounter == 1) {

                ToastCreator.toastCreatorRed(DashboardActivity.this,getResources().getString(R.string.press_one_more_time));
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backCounter--;
                    }
                }, 3000);
            }
            if (backCounter == 2) {
                finishAffinity();

            }

        }
    }

//    private void inAppReview(){
//
//        manager = ReviewManagerFactory.create(this);
//        com.google.android.play.core.tasks.Task<ReviewInfo> request = manager.requestReviewFlow()
//                .addOnCompleteListener(new com.google.android.play.core.tasks.OnCompleteListener<ReviewInfo>() {
//            @Override
//            public void onComplete(@NonNull com.google.android.play.core.tasks.Task<ReviewInfo> task) {
//                if(task.isSuccessful()){
//                    reviewInfo = task.getResult();
//
//                    manager.launchReviewFlow(DashboardActivity.this, reviewInfo)
//                            .addOnCompleteListener(new com.google.android.play.core.tasks.OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull com.google.android.play.core.tasks.Task<Void> task) {
//                                    ToastCreator.toastCreatorGreen(getApplicationContext(),getResources().getString(R.string.rating_successful));
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(Exception e) {
//                            ToastCreator.toastCreatorRed(getApplicationContext(),getResources().getString(R.string.rating_failed));
//                        }
//                    });
//                }
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(Exception e) {
//                        ToastCreator.toastCreatorRed(getApplicationContext(),getResources().getString(R.string.rating_failed));
//                    }
//                });
//    }




    private void languageAlertDialog(String lang){
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setMessage(getResources().getString(R.string.are_you_sure));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.change_language), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               setLocale(lang);
            }
        })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        SharedPreferences langPrefs=getSharedPreferences(Language_pref,MODE_PRIVATE);
        SharedPreferences.Editor langPrefsEditor = langPrefs.edit();
        langPrefsEditor.putString(Selected_language, lang);
        langPrefsEditor.apply();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, DashboardActivity.class);
        startActivity(refresh);
        finish();
    }


    public void slideUp(View view) {
            view.animate()
                    .translationY(-view.getHeight())
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.GONE);
                        }
                    });

    }


    // slide the view from its current position to below itself
    public void slideDown(View view) {


            view.setVisibility(View.VISIBLE);


// Start the animation
            view.animate()
                    .translationY(0)
                    .alpha(1)
                    .setListener(null);

    }

    // slide the view from its current position to below itself
    public void slideDownInactive(CardView view) {


        view.setVisibility(View.VISIBLE);
        view.setEnabled(false);
        view.setCardBackgroundColor(Color.LTGRAY);

// Start the animation
        view.animate()
                .translationY(0)
                .alpha(1)
                .setListener(null);

    }





    @Override
    public void onClick(View view) {
        if(ClickTimeChecker.clickTimeChecker()) {
        switch(view.getId()){
            case R.id.dashboard_drawer_btn:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;

            case R.id.dashboard_gender_icon:
                Intent profileIntent=new Intent(DashboardActivity.this, ViewUserProfileActivity.class);
                startActivity(profileIntent);
                break;

            case R.id.dashboard_text_find_donor:
            case R.id.dashboard_img_find_donor:
            case R.id.cardView_findDonor:
                Intent findDonorIntent=new Intent(DashboardActivity.this,FindDonorActivity.class);
                startActivity(findDonorIntent);
                break;
            case R.id.dashboard_text_add_patient:
            case R.id.dashboard_img_add_patient:
            case R.id.cardView_addPatient:
                Intent addPatientIntent=new Intent(DashboardActivity.this, BloodRequestFormActivity.class);
                startActivity(addPatientIntent);
                break;
            case R.id.dashboard_text_requests:
            case R.id.dashboard_img_requests:
            case R.id.cardView_requests:
                if(requestResponseCardViewSwitcher == 0) {
                    requestResponseCardViewSwitcher = 1;
                    requestResponseSwitcher = 1;
                    slideDown(fromDonorsCardView);
                    if(!loggedInUserDonorInfo.toLowerCase().equals("none")) {
                        slideDown(fromPatientsCardView);
                    } else {
                        slideDownInactive(fromPatientsCardView);
                    }
                    numberOfRequestsFromDonorsText.setText(getResources().getString(R.string.requests));
                    numberOfRequestsFromPatientsText.setText(getResources().getString(R.string.requests));
                    numberOfRequestsFromDonors.setText(noOfRequestsFromDonors);
                    numberOfRequestsFromPatients.setText(noOfRequestsFromPatients);

                    break;
                }
                if(requestResponseCardViewSwitcher ==1) {
                    requestResponseCardViewSwitcher = 0;
                    requestResponseSwitcher = 0;
                    slideUp(fromDonorsCardView);
                    slideUp(fromPatientsCardView);
                    break;
                }
                break;
            case R.id.dashboard_text_responses:
            case R.id.dashboard_img_responses:
            case R.id.cardView_responses:
                if(requestResponseCardViewSwitcher ==0) {
                    requestResponseCardViewSwitcher = 1;
                    requestResponseSwitcher = 2;
                    slideDown(fromDonorsCardView);
                    if(!loggedInUserDonorInfo.toLowerCase().equals("none")) {
                        slideDown(fromPatientsCardView);
                    } else {
                        slideDownInactive(fromPatientsCardView);
                    }
                    numberOfRequestsFromDonorsText.setText(getResources().getString(R.string.responses));
                    numberOfRequestsFromPatientsText.setText(getResources().getString(R.string.responses));
                    numberOfRequestsFromDonors.setText(noOfResponsesFromDonors);
                    numberOfRequestsFromPatients.setText(noOfResponsesFromPatients);
                    break;
                }
                if(requestResponseCardViewSwitcher ==1) {
                    requestResponseCardViewSwitcher = 0;
                    requestResponseSwitcher = 0;
                    slideUp(fromDonorsCardView);
                    slideUp(fromPatientsCardView);
                    break;
                }
                break;

            case R.id.dashboard_text_requestA:
            case R.id.dashboard_img_requestA:
            case R.id.cardView_requestA:
                if(requestResponseSwitcher == 1) {
                    Intent checkPatientRequestsIntent = new Intent(DashboardActivity.this, RequestsFromDonorsActivity.class);
                    startActivity(checkPatientRequestsIntent);
                    break;
                }
                if (requestResponseSwitcher == 2) {
                    Intent viewDonorResIntent=new Intent(DashboardActivity.this, DonorResponseActivity.class);
                    startActivity(viewDonorResIntent);
                    break;
                }
                break;
            case R.id.dashboard_text_requestB:
            case R.id.dashboard_img_requestB:
            case R.id.cardView_requestB:
                if(requestResponseSwitcher == 1) {
                    Intent checkDonorRequestsIntent = new Intent(DashboardActivity.this, RequestsFromPatientsActivity.class);
                    startActivity(checkDonorRequestsIntent);
                    break;

                }
                if (requestResponseSwitcher == 2) {
                    Intent viewPatientResIntent=new Intent(DashboardActivity.this, PatientResponseActivity.class);
                    startActivity(viewPatientResIntent);
                    break;


                }
                break;

            case R.id.dashboard_text_explore:
            case R.id.dashboard_img_explore:
            case R.id.cardView_explore:
                if(exploreSwitcher==0) {
                    exploreSwitcher=1;
                    slideDown(allDonorsCardView);
                    slideDown(allPatientsCardView);
                    slideDown(exploreCCardView);
                    slideDown(exploreDCardView);
                    slideDown(exploreECardView);
                    slideDown(exploreFCardView);
                    numberOfDonors.setText(noOfDonors);
                    numberOfPatients.setText(noOfPatients);
                    break;
                }
                if(exploreSwitcher==1) {
                    exploreSwitcher=0;
                    slideUp(allDonorsCardView);
                    slideUp(allPatientsCardView);
                    slideUp(exploreCCardView);
                    slideUp(exploreDCardView);
                    slideUp(exploreECardView);
                    slideUp(exploreFCardView);
                    break;
                }
                break;
            case R.id.dashboard_text_explore_all_donors:
            case R.id.dashboard_img_explore_all_donors:
            case R.id.cardView_exploreA:
                Intent allDonorsIntent = new Intent(DashboardActivity.this,SearchDonorActivity.class);
                startActivity(allDonorsIntent);
                break;

            case R.id.dashboard_text_explore_all_patients:
            case R.id.dashboard_img_explore_all_patients:
            case R.id.cardView_exploreB:
                Intent allPatientsIntent=new Intent(DashboardActivity.this, ExplorePatientsActivity.class);
                startActivity(allPatientsIntent);
                break;
            case R.id.cardView_exploreC:
            case R.id.dashboard_text_exploreC:
                Intent exploreCIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://corona.gov.bd"));
                startActivity(exploreCIntent);
                break;
            case R.id.cardView_exploreD:
            case R.id.dashboard_text_exploreD:
                Intent exploreDIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.worldometers.info/coronavirus"));
                startActivity(exploreDIntent);
                break;
            case R.id.cardView_exploreE:
            case R.id.dashboard_text_exploreE:
                Intent exploreEIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://covid19.who.int"));
                startActivity(exploreEIntent);
                break;
            case R.id.cardView_exploreF:
            case R.id.dashboard_text_exploreF0:
                Intent exploreFIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:10655"));
                startActivity(exploreFIntent);
                break;

            case R.id.dashboard_text_my_patients:
            case R.id.dashboard_img_my_patients:
            case R.id.cardView_myPatients:
                Intent viewMyPatientsIntent=new Intent(DashboardActivity.this, MyPatientsActivity.class);
                startActivity(viewMyPatientsIntent);
                break;

//            case R.id.dashboard_header:
//                RetroInterface retroInterface = RetroInstance.getRetro();
//                Call<UserDataModel> incomingResponse = retroInterface.sendNotification(loggedInUserPhone,getResources().getString(R.string.dashboard_header),getResources().getString(R.string.dashboard_welcome_to),"DashboardActivity","");
//                incomingResponse.enqueue(new Callback<UserDataModel>() {
//                    @Override
//                    public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserDataModel> call, Throwable t) {
//
//                    }
//                });

        }
    }
    }

    private Bitmap scaleImage(Bitmap bitmap) {


        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(150);


        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return scaledBitmap;

    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }




    private void showImage(ImageView view, Bitmap bitmap, int drawable) {

        BitmapDrawable result = new BitmapDrawable(bitmap);
        view.setImageDrawable(result);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
        Bitmap bmp = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                drawable, o);
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        params.width = 2*width;
        params.height = 2*height;
        view.setLayoutParams(params);
    }

    private void showDrawable(ImageView view, int drawable) {
        view.setImageResource(drawable);

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
        Bitmap bmp = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                drawable, o);
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        params.width = 2*width;
        params.height = 2*height;
        view.setLayoutParams(params);
    }


    private void downloadImage(String title) {
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ImageDataModel> incomingResponse = retroInterface.downloadImage(title);
        incomingResponse.enqueue(new Callback<ImageDataModel>() {
            @Override
            public void onResponse(Call<ImageDataModel> call, Response<ImageDataModel> response) {

                if(response.body().getServerMsg().toLowerCase().equals("true")){
                    String image = response.body().getImage();
                    byte[] imageByte = Base64.decode(image, Base64.DEFAULT);
                    insertBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                    insertBitmap = scaleImage(insertBitmap);
                    showImage(dashboardGenderIcon, insertBitmap, R.drawable.profile_icon_male);
                }

                else if(response.body().getServerMsg().toLowerCase().equals("false")) {

                    if (loggedInUserGender.toLowerCase().equals("male")) {
                        showDrawable(dashboardGenderIcon,R.drawable.profile_icon_male);
                    } else if (loggedInUserGender.toLowerCase().equals("female")) {
                        showDrawable(dashboardGenderIcon,R.drawable.profile_icon_female);
                    }
                }

            }

            @Override
            public void onFailure(Call<ImageDataModel> call, Throwable t) {


                if (loggedInUserGender.toLowerCase().equals("male")) {
                    dashboardGenderIcon.setImageResource(R.drawable.profile_icon_male);
                } else if (loggedInUserGender.toLowerCase().equals("female")) {
                    dashboardGenderIcon.setImageResource(R.drawable.profile_icon_female);
                }
            }
        });

    }


}



