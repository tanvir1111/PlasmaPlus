package com.ece.cov19;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.RecyclerViews.PatientAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestsActivity extends AppCompatActivity {

    private Button addRequestsBtn;
    private PatientDataModel patientDataModel;
    private ArrayList<PatientDataModel> patientDataModels;
    private PatientAdapter patientAdapter;
    private RecyclerView recyclerView;
    private ImageView backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        recyclerView = findViewById(R.id.requests_recyclerview);
        addRequestsBtn=findViewById(R.id.requsts_add_requests_btn);
        backbtn=findViewById(R.id.requests_back_button);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addRequestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bldReqIntent=new Intent(RequestsActivity.this, BloodRequestFormActivity.class);
                startActivity(bldReqIntent);
            }
        });

        patientDataModels = new ArrayList<>();

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call <ArrayList<PatientDataModel>> seeRequest = retroInterface.seeRequest();
        seeRequest.enqueue(new Callback<ArrayList<PatientDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PatientDataModel>> call, Response<ArrayList<PatientDataModel>> response) {

                if(response.isSuccessful()) {
                    ArrayList<PatientDataModel> initialModels = response.body();
                    for(PatientDataModel initialDataModel : initialModels){
                        if(initialDataModel.getNeed().equals("blood") || initialDataModel.getNeed().equals("plasma")){
                            //Toast.makeText(RequestsActivity.this, initialDataModel.getName(), Toast.LENGTH_SHORT).show();
                            patientDataModels.add(initialDataModel);
                        }
                    }
                    patientAdapter = new PatientAdapter(getApplicationContext(), patientDataModels);
                    recyclerView.setAdapter(patientAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
                else {
                    Toast.makeText(RequestsActivity.this, "No Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PatientDataModel>> call, Throwable t) {
                Toast.makeText(RequestsActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
