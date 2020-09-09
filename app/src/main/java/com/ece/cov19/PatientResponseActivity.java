package com.ece.cov19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private Button pendingbtn, successfulBtn, failedBtn,allBtn;
    private String status,responseTypeText;
    private TextView patientResponseTextView, noResponseTextView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_response);
        recyclerView = findViewById(R.id.patients_response_recyclerview);
        backbtn=findViewById(R.id.patients_response_back_button);
        successfulBtn =findViewById(R.id.patients_response_show_successful_requests);
        pendingbtn=findViewById(R.id.patients_response_show_pending_requests);
        failedBtn =findViewById(R.id.patients_response_show_failed_requests);

        allBtn=findViewById(R.id.patients_response_show_All_requests);
        patientResponseTextView=findViewById(R.id.patients_response_type_textView);
        progressBar = findViewById(R.id.patients_response_progressBar);
        noResponseTextView = findViewById(R.id.patients_response_norecordtextview);

        responseTypeText = getResources().getString(R.string.all_requests);
        
        patientResponseSearch("any");
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

        allBtn.setEnabled(false);
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="any";
                responseTypeText=getResources().getString(R.string.all_responses);
                patientResponseTextView.setText(responseTypeText);
                allBtn.setBackgroundResource(R.drawable.tabstyleselected);
                allBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                allBtn.setEnabled(false);
                successfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                successfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                successfulBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                failedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                failedBtn.setEnabled(false);
                patientResponseSearch(status);

            }
        });

        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                responseTypeText=getResources().getString(R.string.pending_responses);
                patientResponseTextView.setText(responseTypeText);
                pendingbtn.setBackgroundResource(R.drawable.tabstyleselected);
                pendingbtn.setTextColor(getResources().getColor(R.color.textColorDark));
                pendingbtn.setEnabled(false);
                successfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                successfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                successfulBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                failedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                failedBtn.setEnabled(false);
                patientResponseSearch(status);

            }
        });

        successfulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseTypeText=getResources().getString(R.string.accepted_responses);
                status="Successful";
                patientResponseTextView.setText(responseTypeText);
                successfulBtn.setBackgroundResource(R.drawable.tabstyleselected);
                successfulBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                successfulBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                failedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                failedBtn.setEnabled(false);
                patientResponseSearch(status);


            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Failed";
                responseTypeText=getResources().getString(R.string.donated_responses);
                patientResponseTextView.setText(responseTypeText);
                failedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                failedBtn.setEnabled(false);
                successfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                successfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                successfulBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                patientResponseSearch(status);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_patient_response);
        recyclerView = findViewById(R.id.patients_response_recyclerview);
        backbtn=findViewById(R.id.patients_response_back_button);
        successfulBtn =findViewById(R.id.patients_response_show_successful_requests);
        pendingbtn=findViewById(R.id.patients_response_show_pending_requests);

        failedBtn =findViewById(R.id.patients_response_show_failed_requests);
        allBtn=findViewById(R.id.patients_response_show_All_requests);
        patientResponseTextView=findViewById(R.id.patients_response_type_textView);
        progressBar = findViewById(R.id.patients_response_progressBar);
        noResponseTextView = findViewById(R.id.patients_response_norecordtextview);

        responseTypeText = getResources().getString(R.string.all_responses);
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

        patientResponseSearch("any");
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

        allBtn.setEnabled(false);
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="any";
                responseTypeText=getResources().getString(R.string.all_responses);
                patientResponseTextView.setText(responseTypeText);
                allBtn.setBackgroundResource(R.drawable.tabstyleselected);
                allBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                allBtn.setEnabled(false);
                successfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                successfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                successfulBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                failedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                failedBtn.setEnabled(false);
                patientResponseSearch(status);

            }
        });

        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                responseTypeText=getResources().getString(R.string.pending_responses);
                patientResponseTextView.setText(responseTypeText);
                pendingbtn.setBackgroundResource(R.drawable.tabstyleselected);
                pendingbtn.setTextColor(getResources().getColor(R.color.textColorDark));
                pendingbtn.setEnabled(false);
                successfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                successfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                successfulBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                failedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                failedBtn.setEnabled(false);
                patientResponseSearch(status);

            }
        });

        successfulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseTypeText=getResources().getString(R.string.accepted_responses);
                status="Successful";
                patientResponseTextView.setText(responseTypeText);
                successfulBtn.setBackgroundResource(R.drawable.tabstyleselected);
                successfulBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                successfulBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                failedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                failedBtn.setEnabled(false);
                patientResponseSearch(status);


            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Failed";
                responseTypeText=getResources().getString(R.string.donated_responses);
                patientResponseTextView.setText(responseTypeText);
                failedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                failedBtn.setEnabled(false);
                successfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                successfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                successfulBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                patientResponseSearch(status);

            }
        });

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


    private void patientResponseSearch(String status){
        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();

        Call <ArrayList<PatientDataModel>> incomingResponse = retroInterface.responsesFromPatients(loggedInUserPhone, status);
        incomingResponse.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {
                progressBar.setVisibility(View.GONE);
                pendingbtn.setEnabled(true);
                successfulBtn.setEnabled(true);
                allBtn.setEnabled(true);
                failedBtn.setEnabled(true);
                if(status.equals("any")){
                    allBtn.setEnabled(false);
                }
                if(status.equals("Pending")){
                    pendingbtn.setEnabled(false);
                }
                if(status.equals("Successful")){
                    successfulBtn.setEnabled(false);
                }
                if(status.equals("Failed")){
                    failedBtn.setEnabled(false);
                }


                if(response.isSuccessful()) {
                    patientDataModels.clear();
                    ArrayList<PatientDataModel> initialModels = response.body();


                    for(PatientDataModel initialDataModel : initialModels){

                        if(initialDataModel.getServerMsg().toLowerCase().equals("no record")){
                            patientDataModels.clear();
                            noResponseTextView.setVisibility(View.VISIBLE);
                            patientResponseTextView.setText(responseTypeText+" (0)");
                            break;
                        }
                        else if(initialDataModel.getNeed().toLowerCase().equals("blood") || initialDataModel.getNeed().toLowerCase().equals("plasma")){
                            noResponseTextView.setVisibility(View.GONE);
                            patientDataModels.add(initialDataModel);
                        }
                    }
                    patientResponseTextView.setText(responseTypeText+" (" +patientDataModels.size()+")");

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
                pendingbtn.setEnabled(true);
                successfulBtn.setEnabled(true);
                allBtn.setEnabled(true);
                failedBtn.setEnabled(true);
                if(status.equals("any")){
                    allBtn.setEnabled(false);
                }
                if(status.equals("Pending")){
                    pendingbtn.setEnabled(false);
                }
                if(status.equals("Successful")){
                    successfulBtn.setEnabled(false);
                }
                if(status.equals("Failed")){
                    failedBtn.setEnabled(false);
                }


                ToastCreator.toastCreatorRed(PatientResponseActivity.this,getResources().getString(R.string.connection_error));
            }
        });
    }
}
