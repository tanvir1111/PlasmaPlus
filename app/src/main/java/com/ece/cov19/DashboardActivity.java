package com.ece.cov19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.cov19.DataModels.DashBoardNumberModel;
import com.ece.cov19.DataModels.ImageDataModel;
import com.ece.cov19.DataModels.LoggedInUserData;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.ContentValues.TAG;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserGender;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserName;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;
import static com.ece.cov19.SplashActivity.Language_pref;
import static com.ece.cov19.SplashActivity.Selected_language;


public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{
    private String[] nameSplit;
    private CardView findDonorCardView, addPatientCardView, requestsCardView, responsesCardView, fromDonorsCardView,
            fromPatientsCardView, exploreCardView, myPatientsCardView, allDonorsCardView, allPatientsCardView;
    private TextView findDonorText, addPatientText, requestsText, responsesText,fromDonorsText,fromPatientsText, exploreText,
            myPatientsText,allDonorsText,allPatientsText;
    private ImageView dashboardDrawerBtn, dashboardGenderIcon, findDonorImage, addPatientImage, requestsImage, responsesImage,fromDonorsImage,fromPatientsImage, exploreImage,
            myPatientsImage,allDonorsImage,allPatientsImage;
    private TextView dashboard, numberOfPatients,numberOfDonors,numberOfPatientsText,numberOfDonorsText,numberOfRequestsFromDonors,
            numberOfRequestsFromPatients,numberOfRequestsFromDonorsText,numberOfRequestsFromPatientsText;
    private ProgressBar progressBar;
    private ConstraintLayout loadingView;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public int backCounter;
    public int requestResponseSwitcher;
    public int requestResponseCardViewSwitcher;
    public int exploreSwitcher;


    private String noOfDonors, noOfPatients, noOfRequests, noOfResponses;

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
        progressBar=findViewById(R.id.dashboard_progress_bar);
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

        numberOfDonors=findViewById(R.id.dashboard_no_of_donors);
        numberOfDonorsText=findViewById(R.id.dashboard_text_no_of_donors);
        numberOfPatients=findViewById(R.id.dashboard_no_of_patients);
        numberOfPatientsText=findViewById(R.id.dashboard_text_no_of_patients);
        numberOfRequestsFromDonors=findViewById(R.id.dashboard_no_of_requests_from_donors);
        numberOfRequestsFromDonorsText=findViewById(R.id.dashboard_text_no_of_requests_from_donors);
        numberOfRequestsFromPatients=findViewById(R.id.dashboard_no_of_requests_from_patients);
        numberOfRequestsFromPatientsText=findViewById(R.id.dashboard_text_no_of_requests_from_patients);


        nameSplit = loggedInUserName.split("");
        loadingView.setVisibility(View.VISIBLE);

        fromDonorsCardView.setVisibility(View.GONE);
        fromPatientsCardView.setVisibility(View.GONE);
        allDonorsCardView.setVisibility(View.GONE);
        allPatientsCardView.setVisibility(View.GONE);

        downloadImage(loggedInUserPhone);


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
                noOfRequests = response.body().getNumberOfRequests();
                noOfResponses = response.body().getNumberOfResponses();

            }

            @Override
            public void onFailure(Call<DashBoardNumberModel> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, R.string.dashboard_error_message, Toast.LENGTH_SHORT).show();

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


    }

    @Override
    protected void onResume() {
        super.onResume();


        nameSplit = loggedInUserName.split("");
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
                noOfRequests = response.body().getNumberOfRequests();
                noOfResponses = response.body().getNumberOfResponses();

                numberOfDonors.setText(noOfDonors);
                numberOfPatients.setText(noOfPatients);

                if(requestResponseCardViewSwitcher == 1) {
                    if(requestResponseSwitcher == 1) {
                        requestResponseCardViewSwitcher = 1;
                        requestResponseSwitcher = 1;
                        fromDonorsCardView.setVisibility(View.VISIBLE);
                        fromPatientsCardView.setVisibility(View.VISIBLE);
                        numberOfRequestsFromDonorsText.setText("Requests");
                        numberOfRequestsFromPatientsText.setText("Requests");
                        numberOfRequestsFromDonors.setText(noOfResponses);
                        numberOfRequestsFromPatients.setText(noOfRequests);
                    }

                    if(requestResponseSwitcher == 2){
                        requestResponseCardViewSwitcher = 1;
                        requestResponseSwitcher = 2;
                        fromDonorsCardView.setVisibility(View.VISIBLE);
                        fromPatientsCardView.setVisibility(View.VISIBLE);
                        numberOfRequestsFromDonorsText.setText("Responses");
                        numberOfRequestsFromPatientsText.setText("Responses");
                        numberOfRequestsFromDonors.setText(noOfRequests);
                        numberOfRequestsFromPatients.setText(noOfResponses);
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

                }
                else if(exploreSwitcher==0) {
                    exploreSwitcher=0;
                    allDonorsCardView.setVisibility(View.GONE);
                    allPatientsCardView.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<DashBoardNumberModel> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "Failed to Update Dashboard", Toast.LENGTH_SHORT).show();

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
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
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
            //intent.setData(Uri.parse("mailto:"+id+"?cc="+"&subject="+Uri.encode(subject)+"&body="+Uri.encode(body)));
            startActivityForResult(intent,200);
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
                Toast.makeText(DashboardActivity.this, "Press back one more time to exit", Toast.LENGTH_SHORT).show();
            }
            if (backCounter == 2) {
                finish();
            }
        }
    }


    private void languageAlertDialog(String lang){
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Change Language", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
               setLocale(lang);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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





    @Override
    public void onClick(View view) {
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
                    slideDown(fromPatientsCardView);
                    numberOfRequestsFromDonorsText.setText("Requests");
                    numberOfRequestsFromPatientsText.setText("Requests");
                    numberOfRequestsFromDonors.setText(noOfResponses);
                    numberOfRequestsFromPatients.setText(noOfRequests);

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
                    slideDown(fromPatientsCardView);
                    numberOfRequestsFromDonorsText.setText("Responses");
                    numberOfRequestsFromPatientsText.setText("Responses");
                    numberOfRequestsFromDonors.setText(noOfRequests);
                    numberOfRequestsFromPatients.setText(noOfResponses);
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
                    Intent checkPatientRequestsIntent = new Intent(DashboardActivity.this, PatientRequestsActivity.class);
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
                    Intent checkDonorRequestsIntent = new Intent(DashboardActivity.this, DonorRequestsActivity.class);
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
                    numberOfDonors.setText(noOfDonors);
                    numberOfPatients.setText(noOfPatients);
                    break;
                }
                if(exploreSwitcher==1) {
                    exploreSwitcher=0;
                    slideUp(allDonorsCardView);
                    slideUp(allPatientsCardView);
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

            case R.id.dashboard_text_my_patients:
            case R.id.dashboard_img_my_patients:
            case R.id.cardView_myPatients:
                Intent viewMyPatientsIntent=new Intent(DashboardActivity.this, MyPatientsActivity.class);
                startActivity(viewMyPatientsIntent);
                break;

            case R.id.dashboard_header:
                RetroInterface retroInterface = RetroInstance.getRetro();
                Call<UserDataModel> incomingResponse = retroInterface.sendNotification(loggedInUserPhone,"Dashboard","Welcome  to Dashboard");
                incomingResponse.enqueue(new Callback<UserDataModel>() {
                    @Override
                    public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                    }

                    @Override
                    public void onFailure(Call<UserDataModel> call, Throwable t) {

                    }
                });

        }
    }

    private Bitmap scaleImage(Bitmap bitmap) {


        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = dpToPx(150);


        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return scaledBitmap;

    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }




    private void showImage(ImageView view, Bitmap bitmap) {

        BitmapDrawable result = new BitmapDrawable(bitmap);
        view.setImageDrawable(result);

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        params.width = 150;
        params.height = 150;
        view.setLayoutParams(params);
    }








    private void downloadImage(String title) {
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ImageDataModel> incomingResponse = retroInterface.downloadImage(title);
        incomingResponse.enqueue(new Callback<ImageDataModel>() {
            @Override
            public void onResponse(Call<ImageDataModel> call, Response<ImageDataModel> response) {

                if(response.body().getServerMsg().equals("true")){
                    String image = response.body().getImage();
                    byte[] imageByte = Base64.decode(image, Base64.DEFAULT);
                    insertBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                    insertBitmap = scaleImage(insertBitmap);
                    showImage(dashboardGenderIcon, insertBitmap);
                }

                else if(response.body().getServerMsg().equals("false")) {

                    if (loggedInUserGender.toLowerCase().equals("male")) {
                        dashboardGenderIcon.setImageResource(R.drawable.profile_icon_male);
                    } else if (loggedInUserGender.toLowerCase().equals("male")) {
                        dashboardGenderIcon.setImageResource(R.drawable.profile_icon_female);
                    }
                }

            }

            @Override
            public void onFailure(Call<ImageDataModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Profile Image retrieve failed. " + t.getMessage(), Toast.LENGTH_SHORT).show();


                if (loggedInUserGender.toLowerCase().equals("male")) {
                    dashboardGenderIcon.setImageResource(R.drawable.profile_icon_male);
                } else if (loggedInUserGender.toLowerCase().equals("male")) {
                    dashboardGenderIcon.setImageResource(R.drawable.profile_icon_female);
                }
            }
        });

    }

}
