package com.ece.cov19.RecyclerViews;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.FindPatientData;
import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.R;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonorResponseAlphaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTextView, donateTextView, typeTextView, bloodTextView, locationTextView, dateTextView;
    ImageView patientImageView;
    ProgressBar progressBar;
    RecyclerView requestDonorRecyclerView;
    PatientDataModel patientDataModel;
    ArrayList<PatientDataModel> patientDataModels;
    String status;

    int pos;
    boolean visibility = true;


    public DonorResponseAlphaViewHolder(@NonNull View itemView, ArrayList<PatientDataModel> patientDataModels, String status) {
        super(itemView);
        this.patientDataModels = patientDataModels;
        this.status = status;

        nameTextView = itemView.findViewById(R.id.seeking_help_name);
        donateTextView = itemView.findViewById(R.id.seeking_help_donate_btn);
        typeTextView = itemView.findViewById(R.id.seeking_help_type);
        bloodTextView = itemView.findViewById(R.id.seeking_help_child_bld_grp);
        locationTextView = itemView.findViewById(R.id.seeking_help_location);
        patientImageView = itemView.findViewById(R.id.seeking_help_child_profile_image);
        progressBar = itemView.findViewById(R.id.seeking_help_child_progressBar);
        requestDonorRecyclerView = itemView.findViewById(R.id.seeking_help_child_recyclerview);
        dateTextView = itemView.findViewById(R.id.seeking_help_date);



        //donateTextView.setBackgroundColor(R.drawable.button_style_green);
        //itemView.setOnClickListener(this);
        donateTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {



        if(visibility == true){
           // ToastCreator.toastCreator(view.getContext(),"Pressed",Toast.LENGTH_SHORT).show();
            visibility = false;
            pos = getAdapterPosition();

            patientDataModel = patientDataModels.get(pos);

            FindPatientData.findPatientPosition = pos;
            FindPatientData.findPatientBloodGroup = patientDataModels.get(pos).getBloodGroup();
            FindPatientData.findPatientName=patientDataModels.get(pos).getName();
            FindPatientData.findPatientAge=patientDataModels.get(pos).getAge();
            FindPatientData.findPatientDate=patientDataModels.get(pos).getDate();
            FindPatientData.findPatientPhone=patientDataModels.get(pos).getPhone();
            FindPatientData.findPatientNeed=patientDataModels.get(pos).getNeed();


            UserDataModel userDataModel;
            ArrayList<UserDataModel> userDataModels;
            userDataModels = new ArrayList<>();

            progressBar.setVisibility(View.VISIBLE);
            RetroInterface retroInterface = RetroInstance.getRetro();
            Call<ArrayList<UserDataModel>> incomingResponse = retroInterface.responsesFromDonorsBeta(patientDataModel.getName(),patientDataModel.getAge(),patientDataModel.getBloodGroup(),patientDataModel.getPhone(),status);
            incomingResponse.enqueue(new Callback<ArrayList<UserDataModel>>() {
                @Override
                public void onResponse(Call<ArrayList<UserDataModel>> call, Response<ArrayList<UserDataModel>> response) {
                    progressBar.setVisibility(View.GONE);
                    userDataModels.clear();

                    if(response.isSuccessful()){
                        ArrayList<UserDataModel> initialModels = response.body();
                        for(UserDataModel initialDataModel : initialModels){

                            userDataModels.add(initialDataModel);

                        }
                        donateTextView.setText(R.string.hide_response);
                        //donateTextView.setBackgroundColor(R.drawable.button_style_colored);
                        requestDonorRecyclerView.setVisibility(View.VISIBLE);
                        DonorResponseBetaAdapter donorResponseBetaAdapter = new DonorResponseBetaAdapter(itemView.getContext(), userDataModels);
                        requestDonorRecyclerView.setAdapter(donorResponseBetaAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
                        requestDonorRecyclerView.setLayoutManager(linearLayoutManager);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        donateTextView.setText(R.string.no_response_found);
                    }


                }

                @Override
                public void onFailure(Call<ArrayList<UserDataModel>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    donateTextView.setText(R.string.no_requests_found);
                }
            });

        }
        else{

            visibility = true;
            donateTextView.setText(R.string.show_response);
            //donateTextView.setBackgroundColor(R.drawable.button_style_green);
            requestDonorRecyclerView.setVisibility(View.GONE);
        }

    }
}
