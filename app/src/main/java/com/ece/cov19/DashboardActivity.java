package com.ece.cov19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.cov19.DataModels.DashBoardNumberModel;
import com.ece.cov19.DataModels.LoggedInUserData;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.content.ContentValues.TAG;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserName;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;


public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{
    private String[] nameSplit;
    private Button profileBtn;
    private CardView findDonorCardView, addPatientCardView, requestsCardView, responsesCardView, fromDonorsCardView,
            fromPatientsCardView, exploreCardView, myPatientsCardView, allDonorsCardView, allPatientsCardView;
    private TextView findDonorText, addPatientText, requestsText, responsesText,fromDonorsText,fromPatientsText, exploreText,
            myPatientsText,allDonorsText,allPatientsText;
    private ImageView findDonorImage, addPatientImage, requestsImage, responsesImage,fromDonorsImage,fromPatientsImage, exploreImage,
            myPatientsImage,allDonorsImage,allPatientsImage;
    private TextView dashboard, numberOfPatients,numberOfDonors,numberOfPatientsText,numberOfDonorsText,numberOfRequestsFromDonors,
            numberOfRequestsFromPatients,numberOfRequestsFromDonorsText,numberOfRequestsFromPatientsText;
    private ProgressBar progressBar;
    private ConstraintLayout loadingView;

    public int backCounter;
    public int requestResponseSwitcher;
    public int requestResponseCardViewSwitcher;
    public int exploreSwitcher;


    private String noOfDonors, noOfPatients, noOfRequests, noOfResponses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        loadingView=findViewById(R.id.loadingView);
        dashboard=findViewById(R.id.dashboard_header);
        progressBar=findViewById(R.id.dashboard_progress_bar);
        profileBtn=findViewById(R.id.dashboard_profile_btn);

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
                Toast.makeText(DashboardActivity.this, "Failed to Update Dashboard", Toast.LENGTH_SHORT).show();

            }
        });

        profileBtn.setText(nameSplit[0]);
        profileBtn.setOnClickListener(this);
        dashboard.setOnClickListener(this);

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


        profileBtn.setText(nameSplit[0]);
        profileBtn.setOnClickListener(this);
        dashboard.setOnClickListener(this);

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
    public void onBackPressed() {

        backCounter++;
        if(backCounter == 1) {
            Toast.makeText(DashboardActivity.this,"Press back one more time to exit", Toast.LENGTH_SHORT).show();
        }
        if(backCounter == 2) {
            finish();
        }
    }

    public void slideUp(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight()-300,  // fromYDelta
                -300);                // toYDelta
        animate.setDuration(250);
        animate.setFillAfter(false);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -300,                 // fromYDelta
                view.getHeight()-300); // toYDelta
        animate.setDuration(250);
        animate.setFillAfter(false);
        view.startAnimation(animate);
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.dashboard_profile_btn:
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
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fromDonorsCardView.setVisibility(View.GONE);
                            fromPatientsCardView.setVisibility(View.GONE);
                        }
                    }, 250);

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
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fromDonorsCardView.setVisibility(View.GONE);
                            fromPatientsCardView.setVisibility(View.GONE);
                        }
                    }, 250);
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
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            allDonorsCardView.setVisibility(View.GONE);
                            allPatientsCardView.setVisibility(View.GONE);
                        }
                    }, 250);
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
}
