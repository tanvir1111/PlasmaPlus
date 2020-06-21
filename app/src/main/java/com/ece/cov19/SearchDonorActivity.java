package com.ece.cov19;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.RecyclerViews.DonorAdapter;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchDonorActivity extends AppCompatActivity {

    UserDataModel userDataModel;
    ArrayList<UserDataModel> userDataModels;
    DonorAdapter donorAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donor);
        recyclerView = findViewById(R.id.search_donor_recyclerview);
        userDataModels = new ArrayList<>();

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call <ArrayList<UserDataModel>> searchDonor = retroInterface.searchDonor();
        searchDonor.enqueue(new Callback<ArrayList<UserDataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserDataModel>> call, Response<ArrayList<UserDataModel>> response) {
                if(response.isSuccessful()){
                    ArrayList<UserDataModel> initialModels = response.body();
                    for(UserDataModel initialDataModel : initialModels){
                        if(initialDataModel.getDonor().equals("Blood") || initialDataModel.getDonor().equals("Plasma")){
                            userDataModels.add(initialDataModel);
                        }
                    }
                    donorAdapter = new DonorAdapter(getApplicationContext(), userDataModels);
                    recyclerView.setAdapter(donorAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                }

                else{
                    Toast.makeText(SearchDonorActivity.this, "No Response", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserDataModel>> call, Throwable t) {
                Toast.makeText(SearchDonorActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
