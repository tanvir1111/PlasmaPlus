package com.ece.cov19;

import android.content.Intent;
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
import com.ece.cov19.RecyclerViews.SearchDonorAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.FindPatientData.findPatientAge;
import static com.ece.cov19.DataModels.FindPatientData.findPatientBloodGroup;
import static com.ece.cov19.DataModels.FindPatientData.findPatientName;
import static com.ece.cov19.DataModels.FindPatientData.findPatientPhone;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class SearchDonorActivity extends AppCompatActivity {


    private ArrayList<UserDataModel> userDataModels;
    private SearchDonorAdapter searchDonorAdapter;
    private RecyclerView recyclerView;
    private Spinner bloodgrpSpinner;
    private EditText districtEditText;
    private ProgressBar progressBar;
    private ImageView backbtn;
    private TextView filterResult;
    private String filterResultText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findPatientName="";
        findPatientAge="";
        findPatientPhone="";
        findPatientBloodGroup="any";

        setContentView(R.layout.activity_search_donor);
        recyclerView = findViewById(R.id.search_donor_recyclerview);
        bloodgrpSpinner = findViewById(R.id.search_donor_bld_grp);
        districtEditText = findViewById(R.id.search_donor_district_edittext);
        progressBar = findViewById(R.id.search_donor_progress_bar);
        backbtn = findViewById(R.id.search_donor_back_button);
        filterResult = findViewById(R.id.search_donor_filter_result);

        filterResultText = filterResult.getText().toString();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });


        userDataModels = new ArrayList<>();

        bloodgrpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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

        Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(intent);
        finishAffinity();
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
        Call<ArrayList<UserDataModel>> searchDonor = retroInterface.findDonor(bloodgroup, district, loggedInUserPhone);
        searchDonor.enqueue(new Callback<ArrayList<UserDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserDataModel>> call, Response<ArrayList<UserDataModel>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    ArrayList<UserDataModel> initialModels = response.body();
                    if (initialModels.size() == 0) {
                        filterResult.setText("No Donors Found");
                    } else {
                        filterResult.setText(filterResultText + " (" + initialModels.size() + ")");
                    }

                    for (UserDataModel initialDataModel : initialModels) {
                        if (initialDataModel.getDonor().equals("Blood") || initialDataModel.getDonor().equals("Plasma")) {
                            userDataModels.add(initialDataModel);
                        }
                    }

                    searchDonorAdapter = new SearchDonorAdapter(getApplicationContext(), userDataModels);

                    recyclerView.setAdapter(searchDonorAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                } else {
                    Toast.makeText(SearchDonorActivity.this, "No Response", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserDataModel>> call, Throwable t) {
                Toast.makeText(SearchDonorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
