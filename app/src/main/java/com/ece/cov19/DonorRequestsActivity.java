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
    private Button pendingbtn,acceptedBtn,declinedBtn,donatedBtn,notDonatedBtn,allBtn;
    private String status,requestTypeText;
    private TextView requestTypeTextView, noRequestTextView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_requests);
        requestTypeText=getString(R.string.all);
        recyclerView = findViewById(R.id.donor_requests_recyclerview);
        backbtn=findViewById(R.id.donor_requests_back_button);
        acceptedBtn=findViewById(R.id.donor_requests_show_accepted_requests);
        pendingbtn=findViewById(R.id.donor_requests_show_pending_requests);
        declinedBtn=findViewById(R.id.donor_requests_show_declined_requests);
        donatedBtn=findViewById(R.id.donor_requests_show_donated_requests);
        notDonatedBtn=findViewById(R.id.donor_requests_show_not_donated_requests);
        allBtn=findViewById(R.id.donor_requests_show_All_requests);
        requestTypeTextView=findViewById(R.id.donor_requests_type_textView);
        progressBar = findViewById(R.id.donor_requests_progressBar);
        noRequestTextView = findViewById(R.id.donor_requests_norecordtextview);

        requestTypeText = getResources().getString(R.string.all_requests);

        getRequests("any");
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
                requestTypeText=getResources().getString(R.string.all_requests);
                requestTypeTextView.setText(requestTypeText);
                allBtn.setBackgroundResource(R.drawable.tabstyleselected);
                allBtn.setTextColor(getColor(R.color.textColorDark));
                allBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(true);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(true);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(true);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(true);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(true);
                getRequests(status);

            }
        });

        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                requestTypeText=getResources().getString(R.string.donor_requests_pending_requests);
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setBackgroundResource(R.drawable.tabstyleselected);
                pendingbtn.setTextColor(getColor(R.color.textColorDark));
                pendingbtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(true);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(true);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(true);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(true);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(true);
                getRequests(status);

            }
        });

        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.accepted_requests);
                status="Accepted";
                requestTypeTextView.setText(requestTypeText);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                acceptedBtn.setTextColor(getColor(R.color.textColorDark));
                acceptedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(true);
                declinedBtn.setEnabled(true);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(true);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(true);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(true);
                getRequests(status);


            }
        });
        declinedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.declined_requests);
                status="Declined";
                requestTypeTextView.setText(requestTypeText);
                declinedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                declinedBtn.setTextColor(getColor(R.color.textColorDark));
                declinedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(true);
                acceptedBtn.setEnabled(true);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(true);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(true);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(true);
                getRequests(status);


            }
        });

        donatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Donated";
                requestTypeText=getResources().getString(R.string.donated_requests);
                requestTypeTextView.setText(requestTypeText);
                donatedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                donatedBtn.setTextColor(getColor(R.color.textColorDark));
                donatedBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(true);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(true);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(true);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(true);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(true);
                getRequests(status);

            }
        });
        notDonatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Not Donated";
                requestTypeText=getResources().getString(R.string.not_donated_requests);
                requestTypeTextView.setText(requestTypeText);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorDark));
                notDonatedBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(true);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(true);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(true);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(true);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(true);
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
        declinedBtn=findViewById(R.id.donor_requests_show_declined_requests);
        donatedBtn=findViewById(R.id.donor_requests_show_donated_requests);
        notDonatedBtn=findViewById(R.id.donor_requests_show_not_donated_requests);
        allBtn=findViewById(R.id.donor_requests_show_All_requests);
        requestTypeTextView=findViewById(R.id.donor_requests_type_textView);
        progressBar = findViewById(R.id.donor_requests_progressBar);
        noRequestTextView = findViewById(R.id.donor_requests_norecordtextview);

        requestTypeText = getResources().getString(R.string.all_requests);

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



        getRequests("any");
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
                requestTypeText=getResources().getString(R.string.all_requests);
                requestTypeTextView.setText(requestTypeText);
                allBtn.setBackgroundResource(R.drawable.tabstyleselected);
                allBtn.setTextColor(getColor(R.color.textColorDark));
                allBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(true);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(true);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(true);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(true);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(true);
                getRequests(status);

            }
        });

        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                requestTypeText=getResources().getString(R.string.donor_requests_pending_requests);
                requestTypeTextView.setText(requestTypeText);
                pendingbtn.setBackgroundResource(R.drawable.tabstyleselected);
                pendingbtn.setTextColor(getColor(R.color.textColorDark));
                pendingbtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(true);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(true);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(true);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(true);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(true);
                getRequests(status);

            }
        });

        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status="Accepted";
                requestTypeText=getResources().getString(R.string.accepted_requests);
                requestTypeTextView.setText(requestTypeText);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                acceptedBtn.setTextColor(getColor(R.color.textColorDark));
                acceptedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(true);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(true);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(true);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(true);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(true);
                getRequests(status);


            }
        });
        declinedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.declined_requests);
                status="Declined";
                requestTypeTextView.setText(requestTypeText);
                declinedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                declinedBtn.setTextColor(getColor(R.color.textColorDark));
                declinedBtn.setEnabled(false);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(true);
                acceptedBtn.setEnabled(true);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(true);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(true);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(true);
                getRequests(status);


            }
        });

        donatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Donated";
                requestTypeText=getResources().getString(R.string.donated_requests);
                requestTypeTextView.setText(requestTypeText);
                donatedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                donatedBtn.setTextColor(getColor(R.color.textColorDark));
                donatedBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(true);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(true);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(true);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(true);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorGrey));
                notDonatedBtn.setEnabled(true);
                getRequests(status);

            }
        });
        notDonatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Not Donated";
                requestTypeText=getResources().getString(R.string.not_donated_requests);
                requestTypeTextView.setText(requestTypeText);
                notDonatedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                notDonatedBtn.setTextColor(getColor(R.color.textColorDark));
                notDonatedBtn.setEnabled(false);
                acceptedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                acceptedBtn.setTextColor(getColor(R.color.textColorGrey));
                acceptedBtn.setEnabled(true);
                declinedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                declinedBtn.setTextColor(getColor(R.color.textColorGrey));
                declinedBtn.setEnabled(true);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getColor(R.color.textColorGrey));
                allBtn.setEnabled(true);
                donatedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                donatedBtn.setTextColor(getColor(R.color.textColorGrey));
                donatedBtn.setEnabled(true);
                pendingbtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                pendingbtn.setTextColor(getColor(R.color.textColorGrey));
                pendingbtn.setEnabled(true);
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

        Call <ArrayList<PatientDataModel>> incomingResponse = retroInterface.requestsFromPatients(loggedInUserPhone,status);
        incomingResponse.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {
                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){

                    patientDataModels.clear();
                    ArrayList<PatientDataModel> initialModels = response.body();
                    requestTypeTextView.setText(requestTypeText+" (" +initialModels.size()+")");

                    for(PatientDataModel initialDataModel : initialModels){

                        if(initialDataModel.getServerMsg().equals("No Record")){
                            patientDataModels.clear();
                            requestTypeTextView.setText(requestTypeText+" (0)");
                            noRequestTextView.setVisibility(View.VISIBLE);
                            break;
                        }
                        else if(initialDataModel.getNeed().equals("Blood") || initialDataModel.getNeed().equals("Plasma")){
                            noRequestTextView.setVisibility(View.GONE);
                            patientDataModels.add(initialDataModel);
                        }
                    }
                    donorRequestsAdapter = new DonorRequestsAdapter(getApplicationContext(), patientDataModels);
                    recyclerView.setAdapter(donorRequestsAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    ToastCreator.toastCreatorRed(DonorRequestsActivity.this,getResources().getString(R.string.connection_failed_try_again));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ToastCreator.toastCreatorRed(DonorRequestsActivity.this, getResources().getString(R.string.connection_error));
            }
        });

    }
}
