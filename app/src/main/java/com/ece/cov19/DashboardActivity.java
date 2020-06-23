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

public class DashboardActivity extends AppCompatActivity {
    private Button profileBtn,seeRequestBtn,findDonorBtn, seePatientsbtn;
    private String[] nameSplit;
    private TextView numberOfPatients,numberOfDonors,numberOfRequests;
    private ProgressBar progressBar;
    private ConstraintLayout loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        progressBar=findViewById(R.id.dashboard_progress_bar);

        profileBtn=findViewById(R.id.dashboard_profile_btn);
        seeRequestBtn=findViewById(R.id.dashboard_see_requests_btn);
        findDonorBtn=findViewById(R.id.dashboard_find_donor_btn);
        seePatientsbtn =findViewById(R.id.dashboard_see_patients_btn);
        numberOfDonors=findViewById(R.id.dashboard_number_of_donors);
        numberOfPatients=findViewById(R.id.dashboard_number_of_patients);
        numberOfRequests=findViewById(R.id.dashboard_number_of_requests);
        nameSplit = loggedInUserName.split("");
        loadingView=findViewById(R.id.loadingView);

        profileBtn.setText(nameSplit[0]);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent=new Intent(DashboardActivity.this, ViewUserProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        seeRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeRequstsIntent=new Intent(DashboardActivity.this,RequestsActivity.class);
                startActivity(seeRequstsIntent);
            }
        });
        findDonorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchDonorIntent=new Intent(DashboardActivity.this,SearchDonorActivity.class);
                startActivity(searchDonorIntent);
            }
        });

        seePatientsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent seePatientsIntent=new Intent(DashboardActivity.this, ExplorePatientsActivity.class);
                startActivity(seePatientsIntent);
            }
        });
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<DashBoardNumberModel> dashBoardNumbers = retroInterface.getDashBoardNumbers();
        dashBoardNumbers.enqueue(new Callback<DashBoardNumberModel>() {
            @Override
            public void onResponse(Call<DashBoardNumberModel> call, Response<DashBoardNumberModel> response) {
                loadingView.setVisibility(View.GONE);
                numberOfDonors.setText(response.body().getNumberOfDonors());
                numberOfPatients.setText(response.body().getNumberOfPatients());
                numberOfRequests.setText(response.body().getNumberOfRequests());
            }

            @Override
            public void onFailure(Call<DashBoardNumberModel> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "failed to get numbers", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
