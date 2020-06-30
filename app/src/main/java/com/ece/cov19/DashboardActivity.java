package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.cov19.DataModels.DashBoardNumberModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserName;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{
    private Button profileBtn,seeRequestBtn,seePatientRequestBtn, findDonorBtn, allDonorBtn, seePatientsbtn, seePatientResponseBtn, seeDonorResponseBtn, addPatientBtn;
    private String[] nameSplit;
    private TextView numberOfPatients,numberOfDonors,numberOfRequests,numberofPatientRequests,numberofDonorRequests;
    private ProgressBar progressBar;
    private ConstraintLayout loadingView;
    private int backCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        progressBar=findViewById(R.id.dashboard_progress_bar);
        numberOfDonors=findViewById(R.id.dashboard_number_of_donors);
        numberOfPatients=findViewById(R.id.dashboard_number_of_patients);
        numberOfRequests=findViewById(R.id.dashboard_number_of_requests);
        nameSplit = loggedInUserName.split("");
        loadingView=findViewById(R.id.loadingView);

        profileBtn=findViewById(R.id.dashboard_profile_btn);
        seeRequestBtn=findViewById(R.id.dashboard_see_requests_btn);
        seePatientsbtn =findViewById(R.id.dashboard_view_patients_btn);
        seePatientRequestBtn = findViewById(R.id.dashboard_check_requests_btn);
        findDonorBtn=findViewById(R.id.dashboard_find_donor_btn);
        allDonorBtn=findViewById(R.id.dashboard_all_donor_btn);
        numberofPatientRequests=findViewById(R.id.dashboard_number_of_responses);
        seePatientResponseBtn=findViewById(R.id.dashboard_view_patient_response_btn);
        seeDonorResponseBtn=findViewById(R.id.dashboard_view_donor_response_btn);
        addPatientBtn=findViewById(R.id.dashboard_add_patient_btn);

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<DashBoardNumberModel> dashBoardNumbers = retroInterface.getDashBoardNumbers(loggedInUserPhone);
        dashBoardNumbers.enqueue(new Callback<DashBoardNumberModel>() {
            @Override
            public void onResponse(Call<DashBoardNumberModel> call, Response<DashBoardNumberModel> response) {
                loadingView.setVisibility(View.GONE);
                numberOfDonors.setText(response.body().getNumberOfDonors());
                numberOfPatients.setText(response.body().getNumberOfPatients());

                if(response.body().getNumberOfRequests().equals("0")){
                    numberOfRequests.setText("No");
                    seeRequestBtn.setVisibility(View.GONE);
                }
                else {
                    numberOfRequests.setText(response.body().getNumberOfRequests());
                }

            }

            @Override
            public void onFailure(Call<DashBoardNumberModel> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "failed to get numbers", Toast.LENGTH_SHORT).show();

            }
        });


        profileBtn.setText(nameSplit[0]);
        profileBtn.setOnClickListener(this);
        seeRequestBtn.setOnClickListener(this);
        findDonorBtn.setOnClickListener(this);
        allDonorBtn.setOnClickListener(this);
        seePatientRequestBtn.setOnClickListener(this);
        seePatientsbtn.setOnClickListener(this);
        seePatientResponseBtn.setOnClickListener(this);
        seeDonorResponseBtn.setOnClickListener(this);
        addPatientBtn.setOnClickListener(this);

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


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.dashboard_profile_btn:
                Intent profileIntent=new Intent(DashboardActivity.this, ViewUserProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.dashboard_see_requests_btn:
                Intent checkDonorRequestsIntent=new Intent(DashboardActivity.this, DonorRequestsActivity.class);
                startActivity(checkDonorRequestsIntent);
                break;
            case R.id.dashboard_find_donor_btn:
                Intent findDonorIntent=new Intent(DashboardActivity.this,FindDonorActivity.class);
                startActivity(findDonorIntent);
                break;
            case R.id.dashboard_all_donor_btn:
                Intent allDonorsIntent = new Intent(DashboardActivity.this,SearchDonorActivity.class);
                startActivity(allDonorsIntent);
                break;
            case R.id.dashboard_check_requests_btn:
                Intent checkPatientRequestsIntent = new Intent(DashboardActivity.this, PatientRequestsActivity.class);
                startActivity(checkPatientRequestsIntent);
                break;
            case R.id.dashboard_view_patients_btn:
                Intent allPatientsIntent=new Intent(DashboardActivity.this, ExplorePatientsActivity.class);
                startActivity(allPatientsIntent);
                break;
            case R.id.dashboard_view_patient_response_btn:
                Intent viewPatientResIntent=new Intent(DashboardActivity.this, PatientResponseActivity.class);
                startActivity(viewPatientResIntent);
                break;
            case R.id.dashboard_view_donor_response_btn:
                Intent viewDonorResIntent=new Intent(DashboardActivity.this, DonorResponseActivity.class);
                startActivity(viewDonorResIntent);
                break;
            case R.id.dashboard_add_patient_btn:
                Intent addPatientIntent=new Intent(DashboardActivity.this, BloodRequestFormActivity.class);
                startActivity(addPatientIntent);
                break;

        }
    }
}
