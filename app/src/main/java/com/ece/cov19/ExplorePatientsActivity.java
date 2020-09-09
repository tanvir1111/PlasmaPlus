package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.Functions.LoginUser;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RecyclerViews.ExplorePatientsAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.FindPatientData.findPatientAge;
import static com.ece.cov19.DataModels.FindPatientData.findPatientBloodGroup;
import static com.ece.cov19.DataModels.FindPatientData.findPatientDistrict;
import static com.ece.cov19.DataModels.FindPatientData.findPatientDivision;
import static com.ece.cov19.DataModels.FindPatientData.findPatientName;
import static com.ece.cov19.DataModels.FindPatientData.findPatientNeed;
import static com.ece.cov19.DataModels.FindPatientData.findPatientPhone;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDistrict;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDivision;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;
import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PASS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PHONE;

public class ExplorePatientsActivity extends AppCompatActivity {


    private RecyclerView explorePatientsRecyclerView;
    private Spinner bloodgrpSpinner;
    private EditText districtEditText;
    private ProgressBar progressBar;
    private TextView explorePatientsTextView, noRecordTextView;
    private ImageView backbtn;
    private String otherPatientsText,myPatientsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_patients);

        findPatientName="";
        findPatientAge="";
        findPatientPhone="";
        findPatientBloodGroup="any";
        findPatientDistrict="";
        findPatientDivision="";
        findPatientNeed="";

        explorePatientsTextView =findViewById(R.id.explore_patients_textview);
        explorePatientsRecyclerView = findViewById(R.id.explore_patients_recyclerview);
        bloodgrpSpinner=findViewById(R.id.explore_patients_bld_grp);
        districtEditText=findViewById(R.id.explore_patients_district_edittext);
        progressBar=findViewById(R.id.explore_patients_progress_bar);
        backbtn=findViewById(R.id.explore_patients_back_button);
        noRecordTextView=findViewById(R.id.explore_patients_norecordtextview);
        otherPatientsText= explorePatientsTextView.getText().toString();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bloodgrpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodgrpSpinner.setEnabled(false);
                allPatientsSearch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        districtEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                allPatientsSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        setContentView(R.layout.activity_explore_patients);

        findPatientName="";
        findPatientAge="";
        findPatientPhone="";
        findPatientBloodGroup="any";
        findPatientNeed="";
        if(LoginUser.checkLoginStat().equals("failed")){
            SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
            String phone,password;

            if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {
                phone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
                password= sharedPreferences.getString(LOGIN_USER_PASS, "");

                LoginUser.loginUser(this,phone,password,ExplorePatientsActivity.class);
            }
            else {
                ToastCreator.toastCreatorRed(this,getString(R.string.login_failed));
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
        explorePatientsTextView =findViewById(R.id.explore_patients_textview);
        explorePatientsRecyclerView = findViewById(R.id.explore_patients_recyclerview);
        bloodgrpSpinner=findViewById(R.id.explore_patients_bld_grp);
        districtEditText=findViewById(R.id.explore_patients_district_edittext);
        progressBar=findViewById(R.id.explore_patients_progress_bar);
        backbtn=findViewById(R.id.explore_patients_back_button);
        noRecordTextView=findViewById(R.id.explore_patients_norecordtextview);
        otherPatientsText= explorePatientsTextView.getText().toString();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bloodgrpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodgrpSpinner.setEnabled(false);
                allPatientsSearch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        districtEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                allPatientsSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }


    private void allPatientsSearch() {
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<PatientDataModel> patientDataModels;
        ExplorePatientsAdapter explorePatientsAdapter;
        patientDataModels = new ArrayList<>();
        explorePatientsAdapter = new ExplorePatientsAdapter(getApplicationContext(), patientDataModels);

        String bloodgroup = bloodgrpSpinner.getSelectedItem().toString();
        String district;

        if(districtEditText.getText().toString().isEmpty()){
            district="any";
        }
        else{
            district=districtEditText.getText().toString();
        }

        patientDataModels.clear();
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> searchDonor = retroInterface.searchPatients(bloodgroup,district,loggedInUserPhone,loggedInUserDistrict,loggedInUserDivision);
        searchDonor.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {
                progressBar.setVisibility(View.GONE);
                bloodgrpSpinner.setEnabled(true);

                if(response.isSuccessful()){
                    ArrayList<PatientDataModel> initialModels = response.body();
                    explorePatientsTextView.setText(otherPatientsText+" (" +initialModels.size() + ")");
                    if(initialModels.size() == 0){
                        noRecordTextView.setVisibility(View.VISIBLE);
                    }
                    else {
                        noRecordTextView.setVisibility(View.GONE);
                        for (PatientDataModel initialDataModel : initialModels) {

                            patientDataModels.add(initialDataModel);

                        }
                    }



                    explorePatientsRecyclerView.setAdapter(explorePatientsAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    explorePatientsRecyclerView.setLayoutManager(linearLayoutManager);
                }

                else{
                    progressBar.setVisibility(View.GONE);
                    ToastCreator.toastCreatorRed(ExplorePatientsActivity.this,getResources().getString(R.string.connection_failed_try_again));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                bloodgrpSpinner.setEnabled(true);
                ToastCreator.toastCreatorRed(ExplorePatientsActivity.this,getResources().getString(R.string.connection_error));
            }
        });
    }
}