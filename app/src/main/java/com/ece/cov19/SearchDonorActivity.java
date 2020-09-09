package com.ece.cov19;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.LoginUser;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RecyclerViews.SearchDonorAdapter;
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

public class SearchDonorActivity extends AppCompatActivity {


    private ArrayList<UserDataModel> userDataModels;
    private SearchDonorAdapter searchDonorAdapter;
    private RecyclerView recyclerView;
    private Spinner bloodgrpSpinner;
    private EditText districtEditText;
    private ProgressBar progressBar;
    private ImageView backbtn;
    private TextView filterResult, noRecordTextView;
    private String filterResultText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_donor);
        recyclerView = findViewById(R.id.search_donor_recyclerview);
        bloodgrpSpinner = findViewById(R.id.search_donor_bld_grp);
        districtEditText = findViewById(R.id.search_donor_district_edittext);
        progressBar = findViewById(R.id.search_donor_progress_bar);
        backbtn = findViewById(R.id.search_donor_back_button);
        filterResult = findViewById(R.id.search_donor_filter_result);
        noRecordTextView = findViewById(R.id.search_donor_norecordtextview);

        filterResultText = filterResult.getText().toString();

        findPatientName="";
        findPatientAge="";
        findPatientPhone="";
        findPatientBloodGroup="any";

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        userDataModels = new ArrayList<>();

        bloodgrpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodgrpSpinner.setEnabled(false);
                donorSearch();
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
                donorSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        setContentView(R.layout.activity_search_donor);
        recyclerView = findViewById(R.id.search_donor_recyclerview);
        bloodgrpSpinner = findViewById(R.id.search_donor_bld_grp);
        districtEditText = findViewById(R.id.search_donor_district_edittext);
        progressBar = findViewById(R.id.search_donor_progress_bar);
        backbtn = findViewById(R.id.search_donor_back_button);
        filterResult = findViewById(R.id.search_donor_filter_result);
        noRecordTextView = findViewById(R.id.search_donor_norecordtextview);

        filterResultText = filterResult.getText().toString();

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

                LoginUser.loginUser(this,phone,password,SearchDonorActivity.class);
            }
            else {
                ToastCreator.toastCreatorRed(this,getString(R.string.login_failed));
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        userDataModels = new ArrayList<>();

        bloodgrpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bloodgrpSpinner.setEnabled(false);
                donorSearch();
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
                donorSearch();
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

    private void donorSearch() {
        progressBar.setVisibility(View.VISIBLE);
        userDataModels.clear();

        String bloodgroup = bloodgrpSpinner.getSelectedItem().toString();
        String district;

        if (districtEditText.getText().toString().isEmpty()) {
            district = "any";
        } else {
            district = districtEditText.getText().toString();
        }


        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<UserDataModel>> searchDonor = retroInterface.findDonor(bloodgroup, district, loggedInUserPhone,loggedInUserDistrict,loggedInUserDivision);
        searchDonor.enqueue(new Callback<ArrayList<UserDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserDataModel>> call, Response<ArrayList<UserDataModel>> response) {
                bloodgrpSpinner.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    ArrayList<UserDataModel> initialModels = response.body();


                    for (UserDataModel initialDataModel : initialModels) {
                        if (initialDataModel.getDonor().toLowerCase().equals("blood") || initialDataModel.getDonor().toLowerCase().equals("plasma")|| initialDataModel.getDonor().toLowerCase().equals("blood and plasma")) {
                            userDataModels.add(initialDataModel);
                        }
                    }
                    if (initialModels.size() == 0) {
                        filterResult.setText(filterResultText + " (" + userDataModels.size() + ")");
                        noRecordTextView.setVisibility(View.VISIBLE);
                    } else {
                        filterResult.setText(filterResultText + " (" + userDataModels.size() + ")");
                        noRecordTextView.setVisibility(View.GONE);
                    }

                    searchDonorAdapter = new SearchDonorAdapter(getApplicationContext(), userDataModels);

                    recyclerView.setAdapter(searchDonorAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                } else {
                    progressBar.setVisibility(View.GONE);
                    ToastCreator.toastCreatorRed(SearchDonorActivity.this,getResources().getString(R.string.connection_failed_try_again));

                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserDataModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                bloodgrpSpinner.setEnabled(true);
                ToastCreator.toastCreatorRed(SearchDonorActivity.this,getResources().getString(R.string.connection_error));
            }
        });
    }
}
