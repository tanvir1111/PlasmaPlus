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

import com.ece.cov19.DataModels.LoggedInUserData;
import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.RecyclerViews.PatientRequestsAlphaAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientRequestsActivity extends AppCompatActivity {


    private RecyclerView myPatientRequestRecyclerView;
    private ProgressBar myPatientRequestProgressBar;
    private TextView myPatientRequestTextView;
    private ImageView backbtn;
    private Button addPatientBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_requests);

        myPatientRequestRecyclerView = findViewById(R.id.patient_requests_recyclerview);
        myPatientRequestProgressBar=findViewById(R.id.patient_requests_progress_bar);
        myPatientRequestTextView=findViewById(R.id.patient_requests_show_textview);
        backbtn=findViewById(R.id.patient_requests_back_button);
        addPatientBtn =findViewById(R.id.patient_requests_add_patient_btn);

        addPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bldReqIntent=new Intent(PatientRequestsActivity.this, BloodRequestFormActivity.class);
                startActivity(bldReqIntent);
            }
        });
        myPatientsSearch();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       
    }

    private void myPatientsSearch(){
        myPatientRequestProgressBar.setVisibility(View.VISIBLE);

        ArrayList<PatientDataModel> patientDataModels;
        PatientRequestsAlphaAdapter patientRequestsAlphaAdapter;
        patientDataModels = new ArrayList<>();
        patientRequestsAlphaAdapter = new PatientRequestsAlphaAdapter(getApplicationContext(), patientDataModels);

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> ownPatients = retroInterface.ownPatients(LoggedInUserData.loggedInUserPhone);
        ownPatients.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {


                myPatientRequestProgressBar.setVisibility(View.GONE);

                patientDataModels.clear();
                if(response.isSuccessful()){

                    ArrayList<PatientDataModel> initialModels = response.body();
                    for(PatientDataModel initialDataModel : initialModels){

                        patientDataModels.add(initialDataModel);

                    }


                    if(patientDataModels.size() > 0){
                        myPatientRequestTextView.setVisibility(View.VISIBLE);

                    }
                    myPatientRequestRecyclerView.setAdapter(patientRequestsAlphaAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    myPatientRequestRecyclerView.setLayoutManager(linearLayoutManager);

                }

                else{
                    Toast.makeText(PatientRequestsActivity.this, "No Response", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                Toast.makeText(PatientRequestsActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    
}