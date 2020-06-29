package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.RecyclerViews.PatientsResponseAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class PatientResponseActivity extends AppCompatActivity {

    private PatientDataModel patientDataModel;
    private ArrayList<PatientDataModel> patientDataModels;
    private PatientsResponseAdapter patientsResponseAdapter;
    private RecyclerView recyclerView;
    private ImageView backbtn;


    private TextView requestTypeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_response);


        recyclerView = findViewById(R.id.patients_response_recyclerview);
        backbtn=findViewById(R.id.patients_response_back_button);

        requestTypeTextView=findViewById(R.id.patients_response_type_textView);
       // progressBar = findViewById(R.id.donor_requests_progressBar);

        patientDataModels = new ArrayList<>();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBackIntent = new Intent(PatientResponseActivity.this,DashboardActivity.class);
                startActivity(goBackIntent);
                finishAffinity();
            }
        });
        getRequests();





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent goBackIntent = new Intent(PatientResponseActivity.this,DashboardActivity.class);
        startActivity(goBackIntent);
        finishAffinity();
    }

    private void getRequests() {
//        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();

        Call<ArrayList<PatientDataModel>> incomingResponse = retroInterface.checkRequestByDonor(loggedInUserPhone);
        incomingResponse.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {

                if(response.isSuccessful()) {
                    patientDataModels.clear();
                    ArrayList<PatientDataModel> initialModels = response.body();
//                    Toast.makeText(PatientResponseActivity.this, response.body().get(0).get, Toast.LENGTH_SHORT).show();

                    for(PatientDataModel initialDataModel : initialModels){
                        if(initialDataModel.getNeed().equals("Blood") || initialDataModel.getNeed().equals("Plasma")){
                            //Toast.makeText(RequestsActivity.this, initialDataModel.getName(), Toast.LENGTH_SHORT).show();
                            patientDataModels.add(initialDataModel);
                        }
                    }
                   patientsResponseAdapter=new PatientsResponseAdapter(PatientResponseActivity.this,patientDataModels);
                 recyclerView.setAdapter(patientsResponseAdapter);
               LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
              recyclerView.setLayoutManager(linearLayoutManager);
                }
                else {
                    Toast.makeText(PatientResponseActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                Toast.makeText(PatientResponseActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
