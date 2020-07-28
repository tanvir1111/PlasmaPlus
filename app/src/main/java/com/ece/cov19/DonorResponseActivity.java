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
import com.ece.cov19.DataModels.RequestDataModel;
import com.ece.cov19.Functions.LoginUser;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RecyclerViews.DonorResponseAlphaAdapter;
import com.ece.cov19.RecyclerViews.DonorResponseBetaAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserBloodGroup;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserEligibility;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;
import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PASS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PHONE;

public class DonorResponseActivity extends AppCompatActivity {


    private RecyclerView RecyclerView;
    private ProgressBar ProgressBar;
    private TextView donorResponseTextView, noResponseTextView;
    private ImageView backbtn;
    private Button pendingbtn,acceptedBtn,declinedBtn,donatedBtn,notDonatedBtn,allBtn;
    private String status="Pending",requestTypeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_response);

        donorResponseTextView = findViewById(R.id.donor_response_textview);
        RecyclerView = findViewById(R.id.donor_response_recyclerview);
        ProgressBar=findViewById(R.id.donor_response_progress_bar);
        backbtn=findViewById(R.id.donor_response_back_button);
        acceptedBtn=findViewById(R.id.donor_response_show_accepted_requests);
        pendingbtn=findViewById(R.id.donor_response_show_pending_requests);
        declinedBtn=findViewById(R.id.donor_response_show_declined_requests);
        donatedBtn=findViewById(R.id.donor_response_show_donated_requests);
        notDonatedBtn=findViewById(R.id.donor_response_show_not_donated_requests);
        allBtn=findViewById(R.id.donor_response_show_All_requests);
        noResponseTextView = findViewById(R.id.donor_response_norecordtextview);

        requestTypeText = getResources().getString(R.string.all_responses);


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
                requestTypeText=getResources().getString(R.string.all_responses);
                donorResponseTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });

        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                requestTypeText=getResources().getString(R.string.pending_responses);
                donorResponseTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });

        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.accepted_responses);
                status="Accepted";
                donorResponseTextView.setText(requestTypeText);
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
                myPatientsSearch(status);


            }
        });
        declinedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.declined_responses);
                status="Declined";
                donorResponseTextView.setText(requestTypeText);
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
                myPatientsSearch(status);


            }
        });


        donatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Donated";
                requestTypeText=getResources().getString(R.string.donated_responses);
                donorResponseTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });
        notDonatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Not Donated";
                requestTypeText=getResources().getString(R.string.not_donated_responses);
                donorResponseTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_donor_response);

        donorResponseTextView = findViewById(R.id.donor_response_textview);
        RecyclerView = findViewById(R.id.donor_response_recyclerview);
        ProgressBar=findViewById(R.id.donor_response_progress_bar);
        backbtn=findViewById(R.id.donor_response_back_button);
        acceptedBtn=findViewById(R.id.donor_response_show_accepted_requests);
        pendingbtn=findViewById(R.id.donor_response_show_pending_requests);
        declinedBtn=findViewById(R.id.donor_response_show_declined_requests);
        donatedBtn=findViewById(R.id.donor_response_show_donated_requests);
        notDonatedBtn=findViewById(R.id.donor_response_show_not_donated_requests);
        allBtn=findViewById(R.id.donor_response_show_All_requests);
        noResponseTextView = findViewById(R.id.donor_response_norecordtextview);

        requestTypeText = getResources().getString(R.string.all_responses);

        if(LoginUser.checkLoginStat().equals("failed")){
            SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
            String phone,password;

            if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {
                phone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
                password= sharedPreferences.getString(LOGIN_USER_PASS, "");

                LoginUser.loginUser(this,phone,password,DonorResponseActivity.class);
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

        allBtn.setEnabled(false);
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="any";
                requestTypeText=getResources().getString(R.string.all_responses);
                donorResponseTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });

        pendingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Pending";
                requestTypeText=getResources().getString(R.string.pending_responses);
                donorResponseTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });

        acceptedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.accepted_responses);
                status="Accepted";
                donorResponseTextView.setText(requestTypeText);
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
                myPatientsSearch(status);


            }
        });
        declinedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.declined_responses);
                status="Declined";
                donorResponseTextView.setText(requestTypeText);
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
                myPatientsSearch(status);


            }
        });


        donatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Donated";
                requestTypeText=getResources().getString(R.string.donated_responses);
                donorResponseTextView.setText(requestTypeText);
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
                myPatientsSearch(status);

            }
        });
        notDonatedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                status="Not Donated";
                requestTypeText=getResources().getString(R.string.not_donated_responses);
                donorResponseTextView.setText(requestTypeText);
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
        ProgressBar.setVisibility(View.VISIBLE);

        ArrayList<PatientDataModel> patientDataModels;
        DonorResponseAlphaAdapter donorResponseAlphaAdapter;
        patientDataModels = new ArrayList<>();
        donorResponseAlphaAdapter = new DonorResponseAlphaAdapter(getApplicationContext(), patientDataModels);

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> ownPatients = retroInterface.responsesFromDonorsAlpha(LoggedInUserData.loggedInUserPhone, status);
        ownPatients.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {
                ProgressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){

                    patientDataModels.clear();
                    ArrayList<PatientDataModel> initialModels = response.body();
                    donorResponseTextView.setText(requestTypeText+" (" +initialModels.size()+")");

                    if(initialModels.size() == 0){
                        noResponseTextView.setVisibility(View.VISIBLE);
                        donorResponseTextView.setText(requestTypeText+" (0)");

                    }
                    else {
                        noResponseTextView.setVisibility(View.GONE);
                    }

                    for(PatientDataModel initialDataModel : initialModels) {

                        patientDataModels.add(initialDataModel);
                    }

                    RecyclerView.setAdapter(donorResponseAlphaAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    RecyclerView.setLayoutManager(linearLayoutManager);

                }

                else{
                    ProgressBar.setVisibility(View.GONE);
                    ToastCreator.toastCreatorRed(DonorResponseActivity.this,getResources().getString(R.string.connection_failed_try_again));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                ProgressBar.setVisibility(View.GONE);
                ToastCreator.toastCreatorRed(DonorResponseActivity.this,getResources().getString(R.string.connection_error));
            }
        });
    }




}