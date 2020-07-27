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

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.Functions.LoginUser;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RecyclerViews.PatientResponseAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;
import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PASS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PHONE;

public class PatientResponseActivity extends AppCompatActivity {

    private ArrayList<PatientDataModel> patientDataModels;
    private PatientResponseAdapter PatientResponseAdapter;
    private RecyclerView recyclerView;
    private ImageView backbtn;
    private String status="pending",responseTypeText;
    private TextView responseTypeTextView, noResponseTextView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_response);
        recyclerView = findViewById(R.id.patients_response_recyclerview);
        backbtn=findViewById(R.id.patients_response_back_button);
        responseTypeTextView=findViewById(R.id.patients_response_type_textView);
        progressBar = findViewById(R.id.patients_response_progressBar);
        noResponseTextView = findViewById(R.id.patients_response_norecordtextview);

        responseTypeText = getResources().getString(R.string.patients_response_type_textView);

        patientDataModels = new ArrayList<>();

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

        patientResponseSearch();



    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_patient_response);
        recyclerView = findViewById(R.id.patients_response_recyclerview);
        backbtn=findViewById(R.id.patients_response_back_button);
        responseTypeTextView=findViewById(R.id.patients_response_type_textView);
        progressBar = findViewById(R.id.patients_response_progressBar);
        noResponseTextView = findViewById(R.id.patients_response_norecordtextview);
        responseTypeText = getResources().getString(R.string.patients_response_type_textView);
        if(LoginUser.checkLoginStat().equals("failed")){
            SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
            String phone,password;

            if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {
                phone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
                password= sharedPreferences.getString(LOGIN_USER_PASS, "");

                LoginUser.loginUser(this,phone,password,PatientResponseActivity.class);
            }
            else {
                ToastCreator.toastCreatorRed(this,getString(R.string.login_failed));
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }


        patientDataModels = new ArrayList<>();

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

        patientResponseSearch();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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


    private void patientResponseSearch(){
        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();

        Call <ArrayList<PatientDataModel>> incomingResponse = retroInterface.checkPatientResponse(loggedInUserPhone);
        incomingResponse.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    patientDataModels.clear();
                    ArrayList<PatientDataModel> initialModels = response.body();
                    responseTypeTextView.setText(responseTypeText+" (" +initialModels.size()+")");
                    if(initialModels.size() == 0){
                        noResponseTextView.setVisibility(View.VISIBLE);
                    }

                    for(PatientDataModel initialDataModel : initialModels){

                        if(initialDataModel.getServerMsg().toLowerCase().equals("no record")){
                            patientDataModels.clear();
                            responseTypeTextView.setText(responseTypeText+" (" +0+")");
                            break;
                        }
                        else if(initialDataModel.getNeed().toLowerCase().equals("blood") || initialDataModel.getNeed().toLowerCase().equals("plasma")){

                            patientDataModels.add(initialDataModel);
                        }
                    }

                    PatientResponseAdapter = new PatientResponseAdapter(getApplicationContext(), patientDataModels);
                    recyclerView.setAdapter(PatientResponseAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    ToastCreator.toastCreatorRed(PatientResponseActivity.this,getResources().getString(R.string.connection_failed_try_again));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ToastCreator.toastCreatorRed(PatientResponseActivity.this,getResources().getString(R.string.connection_error));
            }
        });
    }
}
