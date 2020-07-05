package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.LoggedInUserData;
import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.R;

import java.util.ArrayList;

public class ExplorePatientsAdapter extends RecyclerView.Adapter<ExplorePatientsViewHolder> {

    public Context context;
    public PatientDataModel patientDataModel;
    public ArrayList<PatientDataModel> patientDataModels;

    public ExplorePatientsAdapter(Context context, ArrayList<PatientDataModel> patientDataModels) {
        this.context = context;
        this.patientDataModels = patientDataModels;
    }

    @NonNull
    @Override
    public ExplorePatientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.seeking_help_child, parent, false);
        ExplorePatientsViewHolder explorePatientsViewHolder = new ExplorePatientsViewHolder(view, patientDataModels);
        return explorePatientsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExplorePatientsViewHolder holder, int position) {

        patientDataModel = patientDataModels.get(position);


        if((LoggedInUserData.loggedInUserDonorInfo.equals("Blood") || LoggedInUserData.loggedInUserDonorInfo.equals("Plasma")) && patientDataModel.getBloodGroup().equals(LoggedInUserData.loggedInUserBloodGroup)){
            holder.donateTextView.setVisibility(View.VISIBLE);
            holder.donateTextView.setText("Donate to Help");
        }
        else
        {
            holder.donateTextView.setVisibility(View.GONE);
        }


        holder.nameTextView.setText(patientDataModel.getName());
        holder.typeTextView.setText(patientDataModel.getNeed());
        holder.bloodTextView.setText(patientDataModel.getBloodGroup());
        holder.locationTextView.setText(patientDataModel.getHospital());
        holder.dateTextView.setText("Last date of donation              "+patientDataModel.getDate());

        if(patientDataModel.getGender().equals("male")) {
            holder.patientImageView.setImageResource(R.drawable.profile_icon_male);
        } else {
            holder.patientImageView.setImageResource(R.drawable.profile_icon_female);
        }



    }

    @Override
    public int getItemCount() {
        return patientDataModels.size();
    }
}
