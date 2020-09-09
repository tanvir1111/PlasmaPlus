package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.cov19.DataModels.DashBoardNumberModel;
import com.ece.cov19.DataModels.FindPatientData;
import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.LoginUser;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RecyclerViews.FindDonorBetaAdapter;
import com.ece.cov19.RecyclerViews.FindDonorAlphaAdapter;
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

public class FindDonorActivity extends AppCompatActivity {


    private RecyclerView patientRecyclerView, donorRecyclerView;
    private EditText districtEditText;
    private ProgressBar patientProgressBar, donorProgressBar;
    private TextView patientTextView, donorTextView, filterTextView, noMatchTextView, numberOfPatients, numberOfPatientsTextView;
    private ImageView backbtn;
    private String myPatients,availableDonors;
    private Button addPatientBtn;
    private CardView addPatientCardView;

    String bloodGroup, noOfMyPatients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_donor);
        findPatientName="";
        findPatientAge="";
        findPatientPhone="";
        findPatientBloodGroup="any";
        findPatientDistrict="";
        findPatientDivision="";
        findPatientNeed="";

        addPatientCardView = findViewById(R.id.add_patient_cardView);
        numberOfPatients = findViewById(R.id.find_donor_number_of_patients);
        numberOfPatientsTextView = findViewById(R.id.find_donor_number_of_patients_textview);
        addPatientBtn = findViewById(R.id.find_donor_add_patient_btn);

        patientRecyclerView = findViewById(R.id.find_donor_forpatients_recyclerview);
        patientProgressBar = findViewById(R.id.find_donor_forpatients_progress_bar);
        patientTextView = findViewById(R.id.find_donor_forpatients_textview);

        donorRecyclerView = findViewById(R.id.find_donor_fordonors_recyclerview);
        donorTextView = findViewById(R.id.find_donor_fordonors_textview);
        noMatchTextView = findViewById(R.id.find_donor_fordonors_nomatchtextview);
        filterTextView = findViewById(R.id.find_donor_fordonors_label_district);
        districtEditText=findViewById(R.id.find_donor_fordonors_district_edittext);
        donorProgressBar =findViewById(R.id.find_donor_fordonors_progress_bar);
        backbtn=findViewById(R.id.find_donor_fordonors_back_button);

        myPatients=patientTextView.getText().toString();
        availableDonors=donorTextView.getText().toString();

        donorTextView.setVisibility(View.GONE);
        filterTextView.setVisibility(View.GONE);
        districtEditText.setVisibility(View.GONE);



        findPatient();
        findDonor();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        districtEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               findDonor();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        setContentView(R.layout.activity_find_donor);

        addPatientCardView = findViewById(R.id.add_patient_cardView);
        numberOfPatients = findViewById(R.id.find_donor_number_of_patients);
        numberOfPatientsTextView = findViewById(R.id.find_donor_number_of_patients_textview);
        addPatientBtn = findViewById(R.id.find_donor_add_patient_btn);


        patientRecyclerView = findViewById(R.id.find_donor_forpatients_recyclerview);
        patientProgressBar = findViewById(R.id.find_donor_forpatients_progress_bar);
        patientTextView = findViewById(R.id.find_donor_forpatients_textview);

        donorRecyclerView = findViewById(R.id.find_donor_fordonors_recyclerview);
        donorTextView = findViewById(R.id.find_donor_fordonors_textview);
        noMatchTextView = findViewById(R.id.find_donor_fordonors_nomatchtextview);
        filterTextView = findViewById(R.id.find_donor_fordonors_label_district);
        districtEditText=findViewById(R.id.find_donor_fordonors_district_edittext);
        donorProgressBar =findViewById(R.id.find_donor_fordonors_progress_bar);
        backbtn=findViewById(R.id.find_donor_fordonors_back_button);

        findPatientName="";
        findPatientAge="";
        findPatientPhone="";
        findPatientBloodGroup="any";
        findPatientDistrict="";
        findPatientDivision="";
        findPatientNeed="";
        if(LoginUser.checkLoginStat().equals("failed")){
            SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
            String phone,password;

            if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {
                phone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
                password= sharedPreferences.getString(LOGIN_USER_PASS, "");

                LoginUser.loginUser(this,phone,password,FindDonorActivity.class);
            }
            else {
                ToastCreator.toastCreatorRed(this,getString(R.string.login_failed));
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
        myPatients=patientTextView.getText().toString();
        availableDonors=donorTextView.getText().toString();

        donorTextView.setVisibility(View.GONE);
        filterTextView.setVisibility(View.GONE);
        districtEditText.setVisibility(View.GONE);


        FindPatientData.findPatientBloodGroup = "any";
        findPatient();
        findDonor();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),BloodRequestFormActivity.class);
                startActivity(intent);
            }
        });



        districtEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                findDonor();
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

    private void findPatient(){
        patientProgressBar.setVisibility(View.VISIBLE);

        ArrayList<PatientDataModel> patientDataModels;
        FindDonorAlphaAdapter findDonorAlphaAdapter;
        patientDataModels = new ArrayList<>();
        findDonorAlphaAdapter = new FindDonorAlphaAdapter(getApplicationContext(), patientDataModels, new FindDonorAlphaAdapter.RecyclerViewClickListener() {
        @Override
            public void onClicked(View v, int position) {

                findDonor();
            }
        });

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> searchDonor = retroInterface.ownPatients(loggedInUserPhone);
        searchDonor.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {




                patientProgressBar.setVisibility(View.GONE);

                patientDataModels.clear();
                if(response.isSuccessful()){
                    ArrayList<PatientDataModel> initialModels = response.body();
                    patientTextView.setText(myPatients+" (" + initialModels.size() + ")");

                    if(initialModels.size() == 0){
                        addPatientCardView.setVisibility(View.VISIBLE);
                    }
                    for(PatientDataModel initialDataModel : initialModels){

                        patientDataModels.add(initialDataModel);

                    }


                    if(patientDataModels.size() > 0){
                        patientTextView.setVisibility(View.VISIBLE);

                    }
                    patientRecyclerView.setAdapter(findDonorAlphaAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
                    patientRecyclerView.setLayoutManager(linearLayoutManager);
                }

                else{
                    patientProgressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                patientProgressBar.setVisibility(View.GONE);
            }
        });

    }




    private void findDonor() {
        donorProgressBar.setVisibility(View.VISIBLE);
        donorTextView.setVisibility(View.VISIBLE);
        filterTextView.setVisibility(View.VISIBLE);
        districtEditText.setVisibility(View.VISIBLE);
        noMatchTextView.setVisibility(View.GONE);

        ArrayList<UserDataModel> userDataModels;
        FindDonorBetaAdapter findDonorBetaAdapter;
        userDataModels = new ArrayList<>();
        findDonorBetaAdapter = new FindDonorBetaAdapter(getApplicationContext(),userDataModels);

        bloodGroup = FindPatientData.findPatientBloodGroup;
        String district;

        if(districtEditText.getText().toString().isEmpty()){
            district="any";
        }
        else{
            district=districtEditText.getText().toString();
        }

        userDataModels.clear();
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<UserDataModel>> findDonor = retroInterface.findDonor(bloodGroup,district,loggedInUserPhone,findPatientDistrict,findPatientDivision);
        findDonor.enqueue(new Callback<ArrayList<UserDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserDataModel>> call, Response<ArrayList<UserDataModel>> response) {
                donorProgressBar.setVisibility(View.GONE);



                if(response.isSuccessful()){
                    ArrayList<UserDataModel> initialModels = response.body();


                    if(initialModels.size() == 0){
                        noMatchTextView.setVisibility(View.VISIBLE);
                    }
                    else {
                        noMatchTextView.setVisibility(View.GONE);
                    }
                    for(UserDataModel initialDataModel : initialModels){
                        if(findPatientNeed.toLowerCase().equals("plasma")){
                            if(initialDataModel.getDonor().toLowerCase().equals("plasma")|| initialDataModel.getDonor().toLowerCase().equals("blood and plasma")){
                                userDataModels.add(initialDataModel);
                            }
                        }
                        else {
                            if(initialDataModel.getDonor().toLowerCase().equals("blood")|| initialDataModel.getDonor().toLowerCase().equals("blood and plasma")) {


                                userDataModels.add(initialDataModel);
                            }
                        }


                    }

                    if(userDataModels.size() == 0){
                        donorTextView.setVisibility(View.GONE);
                        filterTextView.setVisibility(View.GONE);
                        districtEditText.setVisibility(View.GONE);
                        noMatchTextView.setVisibility(View.VISIBLE);
                    }
                    else {
                        donorTextView.setText(availableDonors+ " (" +userDataModels.size()+ ")");
                    }

                    donorRecyclerView.setAdapter(findDonorBetaAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    donorRecyclerView.setLayoutManager(linearLayoutManager);
                }

                else{
                    donorProgressBar.setVisibility(View.GONE);
                    ToastCreator.toastCreatorRed(FindDonorActivity.this,getResources().getString(R.string.connection_failed_try_again));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserDataModel>> call, Throwable t) {
                donorProgressBar.setVisibility(View.GONE);
                ToastCreator.toastCreatorRed(FindDonorActivity.this,getResources().getString(R.string.connection_error));
            }
        });
    }
}