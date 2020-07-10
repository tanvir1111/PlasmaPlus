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
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RecyclerViews.DonorResponseAlphaAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonorResponseActivity extends AppCompatActivity {


    private RecyclerView RecyclerView;
    private ProgressBar ProgressBar;
    private TextView TextView, noResponseTextView;
    private ImageView backbtn;
    private Button addPatientBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_response);

        TextView = findViewById(R.id.donor_response_textview);
        RecyclerView = findViewById(R.id.donor_response_recyclerview);
        ProgressBar=findViewById(R.id.donor_response_progress_bar);
        backbtn=findViewById(R.id.donor_response_back_button);
        noResponseTextView = findViewById(R.id.donor_response_norecordtextview);


        myPatientsSearch();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_donor_response);

        TextView = findViewById(R.id.donor_response_textview);
        RecyclerView = findViewById(R.id.donor_response_recyclerview);
        ProgressBar=findViewById(R.id.donor_response_progress_bar);
        backbtn=findViewById(R.id.donor_response_back_button);
        noResponseTextView = findViewById(R.id.donor_response_norecordtextview);


        myPatientsSearch();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    private void myPatientsSearch(){
        ProgressBar.setVisibility(View.VISIBLE);

        ArrayList<PatientDataModel> patientDataModels;
        DonorResponseAlphaAdapter donorResponseAlphaAdapter;
        patientDataModels = new ArrayList<>();
        donorResponseAlphaAdapter = new DonorResponseAlphaAdapter(getApplicationContext(), patientDataModels);

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> ownPatients = retroInterface.ownPatients(LoggedInUserData.loggedInUserPhone);
        ownPatients.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {


                ProgressBar.setVisibility(View.GONE);

                patientDataModels.clear();
                if(response.isSuccessful()){

                    ArrayList<PatientDataModel> initialModels = response.body();
                    if(initialModels.size() == 0){
                        noResponseTextView.setVisibility(View.VISIBLE);
                    }
                    for(PatientDataModel initialDataModel : initialModels){

                        patientDataModels.add(initialDataModel);

                    }


                    if(patientDataModels.size() > 0){
                        TextView.setVisibility(View.VISIBLE);

                    }
                    RecyclerView.setAdapter(donorResponseAlphaAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    RecyclerView.setLayoutManager(linearLayoutManager);

                }

                else{

                    ToastCreator.toastCreatorRed(DonorResponseActivity.this,"No Response");

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                ToastCreator.toastCreatorRed(DonorResponseActivity.this,"Error : " +t.getMessage());
            }
        });
    }
    
}