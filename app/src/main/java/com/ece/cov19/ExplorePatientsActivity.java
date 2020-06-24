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
import android.widget.TextView;
import android.widget.Toast;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.RecyclerViews.ExplorePatientAdapter;
import com.ece.cov19.RecyclerViews.MyPatientAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class ExplorePatientsActivity extends AppCompatActivity {


    private RecyclerView yourPatientsRecyclerView, explorePatientsRecyclerView;
    private Spinner bloodgrpSpinner;
    private EditText districtEditText;
    private ProgressBar yourPatientsProgressBar, progressBar;
    private TextView yourPatientsTextView;
    private ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_patients);

        yourPatientsRecyclerView = findViewById(R.id.your_patients_recyclerview);
        yourPatientsProgressBar = findViewById(R.id.your_patients_progress_bar);
        yourPatientsTextView = findViewById(R.id.your_patients_textview);

        explorePatientsRecyclerView = findViewById(R.id.explore_patients_recyclerview);
        bloodgrpSpinner=findViewById(R.id.explore_patients_bld_grp);
        districtEditText=findViewById(R.id.explore_patients_district_edittext);
        progressBar=findViewById(R.id.explore_patients_progress_bar);
        backbtn=findViewById(R.id.explore_patients_back_button);


        myPatientsSearch();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bloodgrpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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

    private void myPatientsSearch(){
        yourPatientsProgressBar.setVisibility(View.VISIBLE);

        ArrayList<PatientDataModel> patientDataModels;
        MyPatientAdapter myPatientAdapter;
        patientDataModels = new ArrayList<>();
        myPatientAdapter = new MyPatientAdapter(getApplicationContext(), patientDataModels);

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> searchDonor = retroInterface.ownPatients(loggedInUserPhone);
        searchDonor.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {




                yourPatientsProgressBar.setVisibility(View.GONE);

                patientDataModels.clear();
                if(response.isSuccessful()){
                    ArrayList<PatientDataModel> initialModels = response.body();
                    for(PatientDataModel initialDataModel : initialModels){

                        patientDataModels.add(initialDataModel);

                    }


                    if(patientDataModels.size() > 0){
                        yourPatientsTextView.setVisibility(View.VISIBLE);

                    }
                    yourPatientsRecyclerView.setAdapter(myPatientAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    yourPatientsRecyclerView.setLayoutManager(linearLayoutManager);
                }

                else{
                    Toast.makeText(ExplorePatientsActivity.this, "No Response", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                Toast.makeText(ExplorePatientsActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void allPatientsSearch() {
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<PatientDataModel> patientDataModels;
        ExplorePatientAdapter explorePatientAdapter;
        patientDataModels = new ArrayList<>();
        explorePatientAdapter = new ExplorePatientAdapter(getApplicationContext(), patientDataModels);

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
        Call<ArrayList<PatientDataModel>> searchDonor = retroInterface.searchPatients(bloodgroup,district,loggedInUserPhone);
        searchDonor.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {
                progressBar.setVisibility(View.GONE);



                if(response.isSuccessful()){
                    ArrayList<PatientDataModel> initialModels = response.body();
                    for(PatientDataModel initialDataModel : initialModels){

                        patientDataModels.add(initialDataModel);

                    }



                    explorePatientsRecyclerView.setAdapter(explorePatientAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    explorePatientsRecyclerView.setLayoutManager(linearLayoutManager);
                }

                else{
                    Toast.makeText(ExplorePatientsActivity.this, "No Response", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                Toast.makeText(ExplorePatientsActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}