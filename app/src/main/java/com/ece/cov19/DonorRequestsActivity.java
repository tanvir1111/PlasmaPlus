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
import com.ece.cov19.RecyclerViews.DonorRequestsAdapter;
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

public class DonorRequestsActivity extends AppCompatActivity {

    private Button addPatientBtn;
    private PatientDataModel patientDataModel;
    private ArrayList<PatientDataModel> patientDataModels;
    private DonorRequestsAdapter donorRequestsAdapter;
    private RecyclerView recyclerView;
    private ImageView backbtn;
    private Button pendingbtn,acceptedBtn;
    private String status="Pending",requestTypeText;
    private TextView requestTypeTextView, noRequestTextView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_requests);
        requestTypeText=getString(R.string.donor_requests_pending_requests);
        recyclerView = findViewById(R.id.donor_requests_recyclerview);
        backbtn=findViewById(R.id.donor_requests_back_button);
        acceptedBtn=findViewById(R.id.donor_requests_show_accepted_requests);
        pendingbtn=findViewById(R.id.donor_requests_show_pending_requests);
        requestTypeTextView=findViewById(R.id.donor_requests_type_textView);
        progressBar = findViewById(R.id.donor_requests_progressBar);
        noRequestTextView = findViewById(R.id.donor_requests_norecordtextview);

        requestTypeText = getResources().getString(R.string.donor_requests_pending_requests);

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
        getRequests("Pending");


        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                requestTypeText=getResources().getString(R.string.donor_requests_pending_requests);
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setBackgroundResource(R.drawable.button_style_white);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.button_style_colored);
                acceptedBtn.setTextColor(getColor(R.color.textColorWhite));
                acceptedBtn.setEnabled(true);
                getRequests(status);

            }
        });

        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.accepted_requests);
                status="Accepted";
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setBackgroundResource(R.drawable.button_style_colored);
                pendingbtn.setTextColor(getColor(R.color.textColorWhite));
                pendingbtn.setEnabled(true);
                acceptedBtn.setBackgroundResource(R.drawable.button_style_white);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(false);
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

        requestTypeText = getResources().getString(R.string.donor_requests_pending_requests);
        if(LoginUser.checkLoginStat().equals("failed")){
            SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
            String phone,password;

            if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {
                phone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
                password= sharedPreferences.getString(LOGIN_USER_PASS, "");

                LoginUser.loginUser(this,phone,password,DonorRequestsActivity.class);
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
        getRequests("Pending");
        pendingbtn.setEnabled(false);


        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                requestTypeText=getResources().getString(R.string.donor_requests_pending_requests);
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setBackgroundResource(R.drawable.button_style_white);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.button_style_colored);
                acceptedBtn.setTextColor(getColor(R.color.textColorWhite));
                acceptedBtn.setEnabled(true);
                getRequests(status);

            }
        });

        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestTypeText=getResources().getString(R.string.accepted_requests);
                status="Accepted";
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setBackgroundResource(R.drawable.button_style_colored);
                pendingbtn.setTextColor(getColor(R.color.textColorWhite));
                pendingbtn.setEnabled(true);
                acceptedBtn.setBackgroundResource(R.drawable.button_style_white);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(false);
                getRequests(status);

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
                    ToastCreator.toastCreatorRed(DonorRequestsActivity.this,getResources().getString(R.string.connection_failed_try_again));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                ToastCreator.toastCreatorRed(DonorRequestsActivity.this, getResources().getString(R.string.connection_error));
            }
        });

    }
}
