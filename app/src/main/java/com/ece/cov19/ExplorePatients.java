package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.RecyclerViews.DonorAdapter;
import com.ece.cov19.RecyclerViews.PatientAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExplorePatients extends AppCompatActivity {

    ArrayList<PatientDataModel> patientDataModels;
    private RecyclerView recyclerView;
    private Spinner bloodgrpSpinner;
    private EditText districtEditText;
    private ProgressBar progressBar;
    private PatientAdapter patientAdapter;
    private ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_patients);
        recyclerView = findViewById(R.id.explore_patients_recyclerview);
        bloodgrpSpinner=findViewById(R.id.explore_patients_bld_grp);
        districtEditText=findViewById(R.id.explore_patients_district_edittext);
        progressBar=findViewById(R.id.explore_patients_progress_bar);
        backbtn=findViewById(R.id.explore_patients_back_button);
        patientDataModels = new ArrayList<>();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bloodgrpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                patientSearch();
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
                patientSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }

    private void patientSearch() {
        progressBar.setVisibility(View.VISIBLE);
        patientDataModels.clear();

        String bloodgroup = bloodgrpSpinner.getSelectedItem().toString();
        String district;

        if(districtEditText.getText().toString().isEmpty()){
            district="any";
        }
        else{
            district=districtEditText.getText().toString();
        }

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> searchDonor = retroInterface.searchPatients(bloodgroup,district);
        searchDonor.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    ArrayList<PatientDataModel> initialModels = response.body();
                    for(PatientDataModel initialDataModel : initialModels){

                            patientDataModels.add(initialDataModel);

                    }

                    patientAdapter = new PatientAdapter(getApplicationContext(), patientDataModels);

                    recyclerView.setAdapter(patientAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                }

                else{
                    Toast.makeText(ExplorePatients.this, "No Response", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                Toast.makeText(ExplorePatients.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}