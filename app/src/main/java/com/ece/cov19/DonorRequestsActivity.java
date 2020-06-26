package com.ece.cov19;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.RecyclerViews.ExplorePatientsBetaAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class DonorRequestsActivity extends AppCompatActivity {

    private Button addPatientBtn;
    private PatientDataModel patientDataModel;
    private ArrayList<PatientDataModel> patientDataModels;
    private ExplorePatientsBetaAdapter explorePatientsBetaAdapter;
    private RecyclerView recyclerView;
    private ImageView backbtn;
    private Button pendingbtn,acceptedBtn;
    private String status="pending";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_requests);
        recyclerView = findViewById(R.id.donor_requests_recyclerview);
        backbtn=findViewById(R.id.donor_requests_back_button);
        acceptedBtn=findViewById(R.id.donor_requests_show_accepted_requests);
        pendingbtn=findViewById(R.id.donor_requests_show_pending_requests);


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getRequests();



        patientDataModels = new ArrayList<>();

        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                pendingbtn.setVisibility(View.GONE);
                acceptedBtn.setVisibility(View.VISIBLE);
                getRequests();

            }
        });
        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Accepted";
                pendingbtn.setVisibility(View.VISIBLE);
                acceptedBtn.setVisibility(View.GONE);
                getRequests();

            }
        });


    }

    private void getRequests() {
        RetroInterface retroInterface = RetroInstance.getRetro();

        Call <ArrayList<PatientDataModel>> incomingResponse = retroInterface.checkDonorRequest(loggedInUserPhone,status);
        incomingResponse.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {

                if(response.isSuccessful()) {
                    patientDataModels.clear();
                    ArrayList<PatientDataModel> initialModels = response.body();
                    for(PatientDataModel initialDataModel : initialModels){
                        if(initialDataModel.getNeed().equals("Blood") || initialDataModel.getNeed().equals("Plasma")){
                            //Toast.makeText(RequestsActivity.this, initialDataModel.getName(), Toast.LENGTH_SHORT).show();
                            patientDataModels.add(initialDataModel);
                        }
                    }
                    explorePatientsBetaAdapter = new ExplorePatientsBetaAdapter(getApplicationContext(), patientDataModels);
                    recyclerView.setAdapter(explorePatientsBetaAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
                else {
                    Toast.makeText(DonorRequestsActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                Toast.makeText(DonorRequestsActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
