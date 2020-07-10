package com.ece.cov19;

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
import com.ece.cov19.Functions.ToastCreator;
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
    private String status="Pending",requestTypeText="Pending Requests";
    private TextView requestTypeTextView, noRequestTextView;
    private ProgressBar progressBar;
    private int buttonSelector = 0;

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
        noRequestTextView = findViewById(R.id.donor_requests_norecordtextview);

        patientDataModels = new ArrayList<>();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        getRequests("Pending");


        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelector = 1;
                status="Pending";
                requestTypeText="Pending Requests";
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setVisibility(View.GONE);
                acceptedBtn.setVisibility(View.VISIBLE);
                getRequests(status);

            }
        });

        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelector = 2;
                requestTypeText="Accepted Requests";
                status="Accepted";
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setVisibility(View.VISIBLE);
                acceptedBtn.setVisibility(View.GONE);
                getRequests(status);

            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_donor_requests);
        recyclerView = findViewById(R.id.donor_requests_recyclerview);
        backbtn=findViewById(R.id.donor_requests_back_button);
        acceptedBtn=findViewById(R.id.donor_requests_show_accepted_requests);
        pendingbtn=findViewById(R.id.donor_requests_show_pending_requests);
        requestTypeTextView=findViewById(R.id.donor_requests_type_textView);
        progressBar = findViewById(R.id.donor_requests_progressBar);
        noRequestTextView = findViewById(R.id.donor_requests_norecordtextview);


        if(buttonSelector == 1) {
            pendingbtn.setVisibility(View.VISIBLE);
            acceptedBtn.setVisibility(View.GONE);
        }
        else if(buttonSelector == 2){
            pendingbtn.setVisibility(View.GONE);
            acceptedBtn.setVisibility(View.VISIBLE);
        }

        patientDataModels = new ArrayList<>();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getRequests("Pending");


        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelector = 1;
                status="Pending";
                requestTypeText="Pending Requests";
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setVisibility(View.GONE);
                acceptedBtn.setVisibility(View.VISIBLE);
                getRequests(status);

            }
        });

        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelector = 2;
                requestTypeText="Accepted Requests";
                status="Accepted";
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setVisibility(View.VISIBLE);
                acceptedBtn.setVisibility(View.GONE);
                getRequests(status);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    private void getRequests(String status) {
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
                        if(initialDataModel.getServerMsg().equals("No Record")){
                            patientDataModels.clear();
                            requestTypeTextView.setText(requestTypeText+" (" +0+")");
                            noRequestTextView.setVisibility(View.VISIBLE);
                            break;
                        }
                        else if(initialDataModel.getNeed().equals("Blood") || initialDataModel.getNeed().equals("Plasma")){
                            patientDataModels.add(initialDataModel);
                        }
                    }
                    donorRequestsAdapter = new DonorRequestsAdapter(getApplicationContext(), patientDataModels);
                    recyclerView.setAdapter(donorRequestsAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
                else {
                    ToastCreator.toastCreatorRed(DonorRequestsActivity.this,"No Response");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                ToastCreator.toastCreatorRed(DonorRequestsActivity.this,"Error : " +t.getMessage());
            }
        });

    }
}
