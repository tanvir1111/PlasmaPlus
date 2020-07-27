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
    private TextView TextView, noResponseTextView;
    private ImageView backbtn;

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
    protected void onResume() {
        super.onResume();

        setContentView(R.layout.activity_donor_response);

        TextView = findViewById(R.id.donor_response_textview);
        RecyclerView = findViewById(R.id.donor_response_recyclerview);
        ProgressBar=findViewById(R.id.donor_response_progress_bar);
        backbtn=findViewById(R.id.donor_response_back_button);
        noResponseTextView = findViewById(R.id.donor_response_norecordtextview);
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
        ProgressBar.setVisibility(View.VISIBLE);

        ArrayList<PatientDataModel> patientDataModels;
        DonorResponseAlphaAdapter donorResponseAlphaAdapter;
        patientDataModels = new ArrayList<>();
        donorResponseAlphaAdapter = new DonorResponseAlphaAdapter(getApplicationContext(), patientDataModels);

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> ownPatients = retroInterface.responsesFromDonors(LoggedInUserData.loggedInUserPhone);
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

                    for(PatientDataModel initialDataModel : initialModels) {

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