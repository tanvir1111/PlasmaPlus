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

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.RecyclerViews.ExplorePatientsAlphaAdapter;
import com.ece.cov19.RecyclerViews.ExplorePatientsBetaAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.FindPatientData.findPatientAge;
import static com.ece.cov19.DataModels.FindPatientData.findPatientBloodGroup;
import static com.ece.cov19.DataModels.FindPatientData.findPatientName;
import static com.ece.cov19.DataModels.FindPatientData.findPatientNeed;
import static com.ece.cov19.DataModels.FindPatientData.findPatientPhone;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class MyPatientsActivity extends AppCompatActivity {


    private RecyclerView myPatientsRecyclerView;

    private ProgressBar myPatientsProgressBar;
    private TextView myPatientsTextView;
    private ImageView backbtn;
    private String myPatientsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patients);

        findPatientName="";
        findPatientAge="";
        findPatientPhone="";
        findPatientBloodGroup="any";
        findPatientNeed="";

        myPatientsRecyclerView = findViewById(R.id.my_patients_recyclerview);
        myPatientsProgressBar = findViewById(R.id.my_patients_progress_bar);
        myPatientsTextView = findViewById(R.id.my_patients_textview);

        backbtn=findViewById(R.id.my_patients_back_button);
        myPatientsText=myPatientsTextView.getText().toString();


        myPatientsSearch();


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    private void myPatientsSearch(){
        myPatientsProgressBar.setVisibility(View.VISIBLE);

        ArrayList<PatientDataModel> patientDataModels;
        ExplorePatientsAlphaAdapter explorePatientsAlphaAdapter;
        patientDataModels = new ArrayList<>();
        explorePatientsAlphaAdapter = new ExplorePatientsAlphaAdapter(getApplicationContext(), patientDataModels);
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<ArrayList<PatientDataModel>> viewPatients = retroInterface.ownPatients(loggedInUserPhone);
        viewPatients.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {




                myPatientsProgressBar.setVisibility(View.GONE);

                patientDataModels.clear();
                if(response.isSuccessful()){
                    ArrayList<PatientDataModel> initialModels = response.body();
                    myPatientsTextView.setText(myPatientsText+" ("+Integer.toString(initialModels.size())+")");
                    for(PatientDataModel initialDataModel : initialModels){

                        patientDataModels.add(initialDataModel);

                    }


                    if(patientDataModels.size() > 0){
                        myPatientsTextView.setVisibility(View.VISIBLE);

                    }
                    myPatientsRecyclerView.setAdapter(explorePatientsAlphaAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                    myPatientsRecyclerView.setLayoutManager(linearLayoutManager);
                }

                else{
                    Toast.makeText(MyPatientsActivity.this, "No Response", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                Toast.makeText(MyPatientsActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}