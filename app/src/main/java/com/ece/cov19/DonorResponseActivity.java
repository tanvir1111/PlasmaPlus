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
import com.ece.cov19.RecyclerViews.DonorResponseAlphaAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PASS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PHONE;

public class DonorResponseActivity extends AppCompatActivity {


    private RecyclerView RecyclerView;
    private ProgressBar ProgressBar;
    private TextView donorResponseTextView, noResponseTextView;
    private ImageView backbtn;
    private Button pendingbtn, succcessfulBtn, failedBtn,allBtn;
    private String status,requestTypeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_response);

        donorResponseTextView = findViewById(R.id.donor_response_textview);
        RecyclerView = findViewById(R.id.donor_response_recyclerview);
        ProgressBar=findViewById(R.id.donor_response_progress_bar);
        backbtn=findViewById(R.id.donor_response_back_button);
        succcessfulBtn =findViewById(R.id.donor_response_show_successful_requests);
        pendingbtn=findViewById(R.id.donor_response_show_pending_requests);
        failedBtn =findViewById(R.id.donor_response_show_failed_requests);

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
                allBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                allBtn.setEnabled(false);
                succcessfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                succcessfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                succcessfulBtn.setEnabled(false);
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
                requestTypeText=getResources().getString(R.string.pending_responses);
                donorResponseTextView.setText(requestTypeText);
                pendingbtn.setBackgroundResource(R.drawable.tabstyleselected);
                pendingbtn.setTextColor(getResources().getColor(R.color.textColorDark));
                pendingbtn.setEnabled(false);
                succcessfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                succcessfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                succcessfulBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                failedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                failedBtn.setEnabled(false);
                myPatientsSearch(status);

            }
        });

        succcessfulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.accepted_responses);
                status="Successful";
                donorResponseTextView.setText(requestTypeText);
                succcessfulBtn.setBackgroundResource(R.drawable.tabstyleselected);
                succcessfulBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                succcessfulBtn.setEnabled(false);
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
                requestTypeText=getResources().getString(R.string.donated_responses);
                donorResponseTextView.setText(requestTypeText);
                failedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                failedBtn.setEnabled(false);
                succcessfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                succcessfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                succcessfulBtn.setEnabled(false);
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
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_donor_response);

        donorResponseTextView = findViewById(R.id.donor_response_textview);
        RecyclerView = findViewById(R.id.donor_response_recyclerview);
        ProgressBar=findViewById(R.id.donor_response_progress_bar);
        backbtn=findViewById(R.id.donor_response_back_button);
        succcessfulBtn =findViewById(R.id.donor_response_show_successful_requests);
        pendingbtn=findViewById(R.id.donor_response_show_pending_requests);
        failedBtn =findViewById(R.id.donor_response_show_failed_requests);

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
                allBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                allBtn.setEnabled(false);
                succcessfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                succcessfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                succcessfulBtn.setEnabled(false);
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
                requestTypeText=getResources().getString(R.string.pending_responses);
                donorResponseTextView.setText(requestTypeText);
                pendingbtn.setBackgroundResource(R.drawable.tabstyleselected);
                pendingbtn.setTextColor(getResources().getColor(R.color.textColorDark));
                pendingbtn.setEnabled(false);
                succcessfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                succcessfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                succcessfulBtn.setEnabled(false);
                allBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                allBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                allBtn.setEnabled(false);
                failedBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                failedBtn.setEnabled(false);
                myPatientsSearch(status);

            }
        });

        succcessfulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTypeText=getResources().getString(R.string.accepted_responses);
                status="Successful";
                donorResponseTextView.setText(requestTypeText);
                succcessfulBtn.setBackgroundResource(R.drawable.tabstyleselected);
                succcessfulBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                succcessfulBtn.setEnabled(false);
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
                requestTypeText=getResources().getString(R.string.donated_responses);
                donorResponseTextView.setText(requestTypeText);
                failedBtn.setBackgroundResource(R.drawable.tabstyleselected);
                failedBtn.setTextColor(getResources().getColor(R.color.textColorDark));
                failedBtn.setEnabled(false);
                succcessfulBtn.setBackgroundResource(R.drawable.tabstyle_not_selected);
                succcessfulBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                succcessfulBtn.setEnabled(false);
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
        ProgressBar.setVisibility(View.VISIBLE);

        ArrayList<PatientDataModel> patientDataModels;
        DonorResponseAlphaAdapter donorResponseAlphaAdapter;
        patientDataModels = new ArrayList<>();
        donorResponseAlphaAdapter = new DonorResponseAlphaAdapter(getApplicationContext(), patientDataModels, status);

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> ownPatients = retroInterface.responsesFromDonorsAlpha(LoggedInUserData.loggedInUserPhone, status);
        ownPatients.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {

                ProgressBar.setVisibility(View.GONE);
                pendingbtn.setEnabled(true);
                succcessfulBtn.setEnabled(true);
                allBtn.setEnabled(true);
                failedBtn.setEnabled(true);
                if(status.equals("any")){
                    allBtn.setEnabled(false);
                }
                if(status.equals("Pending")){
                    pendingbtn.setEnabled(false);
                }
                if(status.equals("Successful")){
                    succcessfulBtn.setEnabled(false);
                }
                if(status.equals("Failed")){
                    failedBtn.setEnabled(false);
                }



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


                        for (PatientDataModel initialDataModel : initialModels) {

                            patientDataModels.add(initialDataModel);
                        }
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
                pendingbtn.setEnabled(true);
                succcessfulBtn.setEnabled(true);
                allBtn.setEnabled(true);
                failedBtn.setEnabled(true);
                if(status.equals("any")){
                    allBtn.setEnabled(false);
                }
                if(status.equals("Pending")){
                    pendingbtn.setEnabled(false);
                }
                if(status.equals("Successful")){
                    succcessfulBtn.setEnabled(false);
                }
                if(status.equals("Failed")){
                    failedBtn.setEnabled(false);
                }


            }
        });
    }




}