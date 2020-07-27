package com.ece.cov19;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.ece.cov19.Functions.LoginUser;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RecyclerViews.PatientRequestsAlphaAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PASS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PHONE;

public class PatientRequestsActivity extends AppCompatActivity {


    private RecyclerView myPatientRequestRecyclerView;
    private ProgressBar myPatientRequestProgressBar;
    private ImageView backbtn;
    private TextView noRequestTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_requests);

        myPatientRequestRecyclerView = findViewById(R.id.patient_requests_recyclerview);
        myPatientRequestProgressBar=findViewById(R.id.patient_requests_progress_bar);
        backbtn=findViewById(R.id.patient_requests_back_button);
        noRequestTextView=findViewById(R.id.patient_requests_norecordtextview);

        myPatientsSearch();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String status = intent.getStringExtra("notification");

                if(status == null){
                    finish();
                }
                else if(status.equals("yes")){
                    Intent goBackIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(goBackIntent);
                    finish();
                }
            }
        });


       
    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_patient_requests);

        myPatientRequestRecyclerView = findViewById(R.id.patient_requests_recyclerview);
        myPatientRequestProgressBar=findViewById(R.id.patient_requests_progress_bar);
        backbtn=findViewById(R.id.patient_requests_back_button);
        noRequestTextView=findViewById(R.id.patient_requests_norecordtextview);
        if(LoginUser.checkLoginStat().equals("failed")){
            SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
            String phone,password;

            if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {
                phone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
                password= sharedPreferences.getString(LOGIN_USER_PASS, "");

                LoginUser.loginUser(this,phone,password,PatientRequestsActivity.class);
            }
            else {
                ToastCreator.toastCreatorRed(this,getString(R.string.login_failed));
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        myPatientsSearch();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String status = intent.getStringExtra("notification");

                if(status == null){
                    finish();
                }
                else if(status.equals("yes")){
                    Intent goBackIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(goBackIntent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

        Intent intent = getIntent();
        String status = intent.getStringExtra("notification");

        if(status == null){
            finish();
        }
        else if(status.equals("yes")){
            Intent goBackIntent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(goBackIntent);
            finish();
        }
    }

    private void myPatientsSearch(){
        myPatientRequestProgressBar.setVisibility(View.VISIBLE);

        ArrayList<PatientDataModel> patientDataModels;
        PatientRequestsAlphaAdapter patientRequestsAlphaAdapter;
        patientDataModels = new ArrayList<>();
        patientRequestsAlphaAdapter = new PatientRequestsAlphaAdapter(getApplicationContext(), patientDataModels);

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> ownPatients = retroInterface.requestsFromDonors(LoggedInUserData.loggedInUserPhone);
        ownPatients.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {


                myPatientRequestProgressBar.setVisibility(View.GONE);

                patientDataModels.clear();
                if(response.isSuccessful()){

                    ArrayList<PatientDataModel> initialModels = response.body();
                    if(initialModels.size() == 0){
                        noRequestTextView.setVisibility(View.VISIBLE);
                    }
                    for(PatientDataModel initialDataModel : initialModels){

                        patientDataModels.add(initialDataModel);

                    }


                    if(patientDataModels.size() > 0){

                    }
                    myPatientRequestRecyclerView.setAdapter(patientRequestsAlphaAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    myPatientRequestRecyclerView.setLayoutManager(linearLayoutManager);

                }

                else{

                    myPatientRequestProgressBar.setVisibility(View.GONE);
                    ToastCreator.toastCreatorRed(PatientRequestsActivity.this,getResources().getString(R.string.connection_failed_try_again));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                myPatientRequestProgressBar.setVisibility(View.GONE);
                ToastCreator.toastCreatorRed(PatientRequestsActivity.this,getResources().getString(R.string.connection_error));
            }
        });

    }
    
}