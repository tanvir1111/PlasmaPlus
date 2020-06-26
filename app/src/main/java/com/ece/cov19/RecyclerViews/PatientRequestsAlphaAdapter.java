package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.R;

import java.util.ArrayList;

public class PatientRequestsAlphaAdapter extends RecyclerView.Adapter<PatientRequestsAlphaViewHolder> {

    public Context context;
    public PatientDataModel patientDataModel;
    public ArrayList<PatientDataModel> patientDataModels;

    public PatientRequestsAlphaAdapter(Context context, ArrayList<PatientDataModel> patientDataModels) {
        this.context = context;
        this.patientDataModels = patientDataModels;
    }

    @NonNull
    @Override
    public PatientRequestsAlphaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.seeking_help_child, parent, false);
        PatientRequestsAlphaViewHolder patientRequestsAlphaViewHolder = new PatientRequestsAlphaViewHolder(view, patientDataModels);
        return patientRequestsAlphaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientRequestsAlphaViewHolder holder, int position) {

        patientDataModel = patientDataModels.get(position);

        holder.donateTextView.setVisibility(View.VISIBLE);
        holder.donateTextView.setText("View Requests");

        holder.nameTextView.setText(patientDataModel.getName());
        holder.typeTextView.setText(patientDataModel.getNeed());
        holder.bloodTextView.setText(patientDataModel.getBloodGroup());
        holder.locationTextView.setText(patientDataModel.getHospital());

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
