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

import com.ece.cov19.DataModels.LoggedInUserData;
import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.Functions.LoginUser;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RecyclerViews.RequestsFromDonorsAlphaAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PASS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PHONE;

public class RequestsFromDonorsActivity extends AppCompatActivity {


    private RecyclerView myPatientRequestRecyclerView;
    private ProgressBar myPatientRequestProgressBar;
    private Button pendingbtn, successfulBtn, failedBtn,allBtn;
    private ImageView backbtn;
    private TextView noRequestTextView, patientRequestsTextView;
    private String status,requestTypeText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_from_donors);

        myPatientRequestRecyclerView = findViewById(R.id.patient_requests_recyclerview);
        myPatientRequestProgressBar=findViewById(R.id.patient_requests_progress_bar);
        backbtn=findViewById(R.id.patient_requests_back_button);
        successfulBtn =findViewById(R.id.patient_requests_show_successful_requests);
        pendingbtn=findViewById(R.id.patient_requests_show_pending_requests);

        failedBtn =findViewById(R.id.patient_requests_show_failed_requests);

        allBtn=findViewById(R.id.patient_requests_show_All_requests);
        noRequestTextView=findViewById(R.id.patient_requests_norecordtextview);
        patientRequestsTextView=findViewById(R.id.patient_requests_textview);

        requestTypeText = getResources().getString(R.string.all_requests);


        myPatientsSearch("any");

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
                requestTypeText=getResources().getString(R.string.all_requests);
                patientRequestsTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });

        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                requestTypeText=getResources().getString(R.string.donor_requests_pending_requests);
                patientRequestsTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });

        successfulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.accepted_requests);
                status="Successful";
                patientRequestsTextView.setText(requestTypeText);
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
                myPatientsSearch(status);


            }
        });
        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Failed";
                requestTypeText=getResources().getString(R.string.donated_requests);
                patientRequestsTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });




    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_requests_from_donors);

        myPatientRequestRecyclerView = findViewById(R.id.patient_requests_recyclerview);
        myPatientRequestProgressBar=findViewById(R.id.patient_requests_progress_bar);
        backbtn=findViewById(R.id.patient_requests_back_button);
        successfulBtn =findViewById(R.id.patient_requests_show_successful_requests);
        pendingbtn=findViewById(R.id.patient_requests_show_pending_requests);

        failedBtn =findViewById(R.id.patient_requests_show_failed_requests);

        allBtn=findViewById(R.id.patient_requests_show_All_requests);
        noRequestTextView=findViewById(R.id.patient_requests_norecordtextview);
        patientRequestsTextView=findViewById(R.id.patient_requests_textview);


        requestTypeText = getResources().getString(R.string.all_requests);
        
        if(LoginUser.checkLoginStat().equals("failed")){
            SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
            String phone,password;

            if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {
                phone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
                password= sharedPreferences.getString(LOGIN_USER_PASS, "");

                LoginUser.loginUser(this,phone,password, RequestsFromDonorsActivity.class);
            }
            else {
                ToastCreator.toastCreatorRed(this,getString(R.string.login_failed));
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        myPatientsSearch("any");
        

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

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="any";
                requestTypeText=getResources().getString(R.string.all_requests);
                patientRequestsTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });

        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                requestTypeText=getResources().getString(R.string.donor_requests_pending_requests);
                patientRequestsTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });

        successfulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.accepted_requests);
                status="Successful";
                patientRequestsTextView.setText(requestTypeText);
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
                myPatientsSearch(status);


            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Failed";
                requestTypeText=getResources().getString(R.string.donated_requests);
                patientRequestsTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

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

    private void myPatientsSearch(String status){
        myPatientRequestProgressBar.setVisibility(View.VISIBLE);

        ArrayList<PatientDataModel> patientDataModels;
        RequestsFromDonorsAlphaAdapter requestsFromDonorsAlphaAdapter;
        patientDataModels = new ArrayList<>();
        requestsFromDonorsAlphaAdapter = new RequestsFromDonorsAlphaAdapter(getApplicationContext(), patientDataModels, status);

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> incomingResponse = retroInterface.requestsFromDonorsAlpha(LoggedInUserData.loggedInUserPhone, status);
        incomingResponse.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {
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





                myPatientRequestProgressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){

                    patientDataModels.clear();
                    ArrayList<PatientDataModel> initialModels = response.body();
                    patientRequestsTextView.setText(requestTypeText+" (" +initialModels.size()+")");

                    if(initialModels.size() == 0){
                        noRequestTextView.setVisibility(View.VISIBLE);
                        patientRequestsTextView.setText(requestTypeText+" (0)");

                    }
                    else {
                        noRequestTextView.setVisibility(View.GONE);
                    }
                    for(PatientDataModel initialDataModel : initialModels){

                        patientDataModels.add(initialDataModel);

                    }

                    myPatientRequestRecyclerView.setAdapter(requestsFromDonorsAlphaAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    myPatientRequestRecyclerView.setLayoutManager(linearLayoutManager);

                }

                else{

                    myPatientRequestProgressBar.setVisibility(View.GONE);
                    ToastCreator.toastCreatorRed(RequestsFromDonorsActivity.this,getResources().getString(R.string.connection_failed_try_again));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
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


                myPatientRequestProgressBar.setVisibility(View.GONE);
                ToastCreator.toastCreatorRed(RequestsFromDonorsActivity.this,getResources().getString(R.string.connection_error));
            }
        });

    }
    
}