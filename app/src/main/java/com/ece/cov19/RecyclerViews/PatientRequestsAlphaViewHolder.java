package com.ece.cov19.RecyclerViews;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.R;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientRequestsAlphaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTextView, donateTextView, typeTextView, bloodTextView, locationTextView;
    ImageView patientImageView;
    RecyclerView requestDonorRecyclerView;
    PatientDataModel patientDataModel;
    ArrayList<PatientDataModel> patientDataModels;
    int pos;
    boolean visibility = true;

    public PatientRequestsAlphaViewHolder(@NonNull View itemView, ArrayList<PatientDataModel> patientDataModels) {
        super(itemView);
        this.patientDataModels = patientDataModels;

        nameTextView = itemView.findViewById(R.id.seeking_help_name);
        donateTextView = itemView.findViewById(R.id.seeking_help_donate_btn);
        typeTextView = itemView.findViewById(R.id.seeking_help_type);
        bloodTextView = itemView.findViewById(R.id.seeking_help_child_bld_grp);
        locationTextView = itemView.findViewById(R.id.seeking_help_location);
        patientImageView = itemView.findViewById(R.id.seeking_help_child_profile_image);
        requestDonorRecyclerView = itemView.findViewById(R.id.seeking_help_child_recyclerview);


        //donateTextView.setBackgroundColor(R.drawable.button_style_green);
        //itemView.setOnClickListener(this);
        donateTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(visibility == true){
            visibility = false;
            pos = getAdapterPosition();

            patientDataModel = patientDataModels.get(pos);
            UserDataModel userDataModel;
            ArrayList<UserDataModel> userDataModels;
            userDataModels = new ArrayList<>();


            RetroInterface retroInterface = RetroInstance.getRetro();
            Call<ArrayList<UserDataModel>> incomingResponse = retroInterface.checkPatientRequest(patientDataModel.getName(),patientDataModel.getAge(),patientDataModel.getBloodGroup(),patientDataModel.getPhone());

            incomingResponse.enqueue(new Callback<ArrayList<UserDataModel>>() {
                @Override
                public void onResponse(Call<ArrayList<UserDataModel>> call, Response<ArrayList<UserDataModel>> response) {

                    userDataModels.clear();

                    if(response.isSuccessful()){
                        ArrayList<UserDataModel> initialModels = response.body();
                        for(UserDataModel initialDataModel : initialModels){

                            userDataModels.add(initialDataModel);

                        }
                        donateTextView.setText("Hide Requests");
                        //donateTextView.setBackgroundColor(R.drawable.button_style_colored);
                        requestDonorRecyclerView.setVisibility(View.VISIBLE);
                        PatientRequestsBetaAdapter patientRequestsBetaAdapter = new PatientRequestsBetaAdapter(itemView.getContext(), userDataModels);
                        requestDonorRecyclerView.setAdapter(patientRequestsBetaAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
                        requestDonorRecyclerView.setLayoutManager(linearLayoutManager);
                    }


                }

                @Override
                public void onFailure(Call<ArrayList<UserDataModel>> call, Throwable t) {

                }
            });

        }
        else{
            visibility = true;
            donateTextView.setText("View Requests");
            //donateTextView.setBackgroundColor(R.drawable.button_style_green);
            requestDonorRecyclerView.setVisibility(View.GONE);
        }

    }
}
