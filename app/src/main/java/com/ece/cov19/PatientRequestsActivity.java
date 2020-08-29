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
    private Button pendingbtn,acceptedBtn,declinedBtn,donatedBtn,notDonatedBtn,allBtn;
    private ImageView backbtn;
    private TextView noRequestTextView, patientRequestsTextView;
    private String status,requestTypeText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_requests);

        myPatientRequestRecyclerView = findViewById(R.id.patient_requests_recyclerview);
        myPatientRequestProgressBar=findViewById(R.id.patient_requests_progress_bar);
        backbtn=findViewById(R.id.patient_requests_back_button);
        acceptedBtn=findViewById(R.id.patient_requests_show_accepted_requests);
        pendingbtn=findViewById(R.id.patient_requests_show_pending_requests);
        declinedBtn=findViewById(R.id.patient_requests_show_declined_requests);
        donatedBtn=findViewById(R.id.patient_requests_show_donated_requests);
        notDonatedBtn=findViewById(R.id.patient_requests_show_not_donated_requests);
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
                allBtn.setTextColor(getColor(R.color.textColorDark));
                allBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(false);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(false);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(false);
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
                pendingbtn.setTextColor(getColor(R.color.textColorDark));
                pendingbtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(false);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(false);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(false);
                myPatientsSearch(status);

            }
        });

        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.accepted_requests);
                status="Accepted";
                patientRequestsTextView.setText(requestTypeText);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                acceptedBtn.setTextColor(getColor(R.color.textColorDark));
                acceptedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                declinedBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(false);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(false);
                myPatientsSearch(status);


            }
        });
        declinedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.declined_requests);
                status="Declined";
                patientRequestsTextView.setText(requestTypeText);
                declinedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                declinedBtn.setTextColor(getColor(R.color.textColorDark));
                declinedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                acceptedBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(false);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(false);
                myPatientsSearch(status);


            }
        });


        donatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Donated";
                requestTypeText=getResources().getString(R.string.donated_requests);
                patientRequestsTextView.setText(requestTypeText);
                donatedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                donatedBtn.setTextColor(getColor(R.color.textColorDark));
                donatedBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(false);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(false);
                myPatientsSearch(status);

            }
        });
        notDonatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Not_Donated";
                requestTypeText=getResources().getString(R.string.not_donated_requests);
                patientRequestsTextView.setText(requestTypeText);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorDark));
                notDonatedBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(false);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                myPatientsSearch(status);

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
        acceptedBtn=findViewById(R.id.patient_requests_show_accepted_requests);
        pendingbtn=findViewById(R.id.patient_requests_show_pending_requests);
        declinedBtn=findViewById(R.id.patient_requests_show_declined_requests);
        donatedBtn=findViewById(R.id.patient_requests_show_donated_requests);
        notDonatedBtn=findViewById(R.id.patient_requests_show_not_donated_requests);
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

                LoginUser.loginUser(this,phone,password,PatientRequestsActivity.class);
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
                allBtn.setTextColor(getColor(R.color.textColorDark));
                allBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(false);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(false);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(false);
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
                pendingbtn.setTextColor(getColor(R.color.textColorDark));
                pendingbtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(false);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(false);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(false);
                myPatientsSearch(status);

            }
        });

        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.accepted_requests);
                status="Accepted";
                patientRequestsTextView.setText(requestTypeText);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                acceptedBtn.setTextColor(getColor(R.color.textColorDark));
                acceptedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                declinedBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(false);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(false);
                myPatientsSearch(status);


            }
        });
        declinedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.declined_requests);
                status="Declined";
                patientRequestsTextView.setText(requestTypeText);
                declinedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                declinedBtn.setTextColor(getColor(R.color.textColorDark));
                declinedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                acceptedBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(false);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(false);
                myPatientsSearch(status);


            }
        });


        donatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Donated";
                requestTypeText=getResources().getString(R.string.donated_requests);
                patientRequestsTextView.setText(requestTypeText);
                donatedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                donatedBtn.setTextColor(getColor(R.color.textColorDark));
                donatedBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(false);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(false);
                myPatientsSearch(status);

            }
        });
        notDonatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Not_Donated";
                requestTypeText=getResources().getString(R.string.not_donated_requests);
                patientRequestsTextView.setText(requestTypeText);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorDark));
                notDonatedBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(false);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
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
        PatientRequestsAlphaAdapter patientRequestsAlphaAdapter;
        patientDataModels = new ArrayList<>();
        patientRequestsAlphaAdapter = new PatientRequestsAlphaAdapter(getApplicationContext(), patientDataModels, status);

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> incomingResponse = retroInterface.requestsFromDonorsAlpha(LoggedInUserData.loggedInUserPhone, status);
        incomingResponse.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {
                pendingbtn.setEnabled(true);
                acceptedBtn.setEnabled(true);
                notDonatedBtn.setEnabled(true);
                declinedBtn.setEnabled(true);
                allBtn.setEnabled(true);
                donatedBtn.setEnabled(true);
                if(status.equals("any")){
                    allBtn.setEnabled(false);
                }
                if(status.equals("Pending")){
                    pendingbtn.setEnabled(false);
                }
                if(status.equals("Accepted")){
                    acceptedBtn.setEnabled(false);
                }
                if(status.equals("Donated")){
                    donatedBtn.setEnabled(false);
                }

                if(status.equals("Not_Donated")){
                    notDonatedBtn.setEnabled(false);
                }

                if(status.equals("Declined")){
                    declinedBtn.setEnabled(false);
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
                pendingbtn.setEnabled(true);
                acceptedBtn.setEnabled(true);
                notDonatedBtn.setEnabled(true);
                declinedBtn.setEnabled(true);
                allBtn.setEnabled(true);
                donatedBtn.setEnabled(true);
                if(status.equals("any")){
                    allBtn.setEnabled(false);
                }
                if(status.equals("Pending")){
                    pendingbtn.setEnabled(false);
                }
                if(status.equals("Accepted")){
                    acceptedBtn.setEnabled(false);
                }
                if(status.equals("Donated")){
                    donatedBtn.setEnabled(false);
                }

                if(status.equals("Not_Donated")){
                    notDonatedBtn.setEnabled(false);
                }

                if(status.equals("Declined")){
                    declinedBtn.setEnabled(false);
                }

                myPatientRequestProgressBar.setVisibility(View.GONE);
                ToastCreator.toastCreatorRed(PatientRequestsActivity.this,getResources().getString(R.string.connection_error));
            }
        });

    }
    
}