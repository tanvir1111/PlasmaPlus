package com.ece.cov19;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.RecyclerViews.DonorRequestsAdapter;
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
    private DonorRequestsAdapter donorRequestsAdapter;
    private RecyclerView recyclerView;
    private ImageView backbtn;
    private Button pendingbtn,acceptedBtn;
    private String status="pending",requestTypeText="Pending Requests";
    private TextView requestTypeTextView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_requests);
        recyclerView = findViewById(R.id.donor_requests_recyclerview);
        backbtn=findViewById(R.id.donor_requests_back_button);
        acceptedBtn=findViewById(R.id.donor_requests_show_accepted_requests);
        pendingbtn=findViewById(R.id.donor_requests_show_pending_requests);
        requestTypeTextView=findViewById(R.id.donor_requests_type_textView);
        progressBar = findViewById(R.id.donor_requests_progressBar);

        patientDataModels = new ArrayList<>();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        getRequests();


        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                requestTypeText="Pending Requests";
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setVisibility(View.GONE);
                acceptedBtn.setVisibility(View.VISIBLE);
                getRequests();

            }
        });

        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestTypeText="Accepted Requests";
                status="Accepted";
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setVisibility(View.VISIBLE);
                acceptedBtn.setVisibility(View.GONE);
                getRequests();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void getRequests() {
        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();

        Call <ArrayList<PatientDataModel>> incomingResponse = retroInterface.checkDonorRequest(loggedInUserPhone,status);
        incomingResponse.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    patientDataModels.clear();
                    ArrayList<PatientDataModel> initialModels = response.body();
                    requestTypeTextView.setText(requestTypeText+" (" +initialModels.size()+")");
                    for(PatientDataModel initialDataModel : initialModels){
                        if(initialDataModel.getNeed().equals("Blood") || initialDataModel.getNeed().equals("Plasma")){
                            //Toast.makeText(RequestsActivity.this, initialDataModel.getName(), Toast.LENGTH_SHORT).show();
                            patientDataModels.add(initialDataModel);
                        }
                    }
                    donorRequestsAdapter = new DonorRequestsAdapter(getApplicationContext(), patientDataModels);
                    recyclerView.setAdapter(donorRequestsAdapter);
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
