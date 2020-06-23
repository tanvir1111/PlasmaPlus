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

public class ExplorePatientAdapter extends RecyclerView.Adapter<ExplorePatientViewHolder> {

    public Context context;
    public PatientDataModel patientDataModel;
    public ArrayList<PatientDataModel> patientDataModels;

    public ExplorePatientAdapter(Context context, ArrayList<PatientDataModel> patientDataModels) {
        this.context = context;
        this.patientDataModels = patientDataModels;
    }

    @NonNull
    @Override
    public ExplorePatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.seeking_help_child, parent, false);
        ExplorePatientViewHolder explorePatientViewHolder = new ExplorePatientViewHolder(view, patientDataModels);
        return explorePatientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExplorePatientViewHolder holder, int position) {

        patientDataModel = patientDataModels.get(position);

        holder.nameTextView.setText(patientDataModel.getName());
        holder.phoneTextView.setText(patientDataModel.getPhone());
        holder.typeTextView.setText(patientDataModel.getNeed());
        holder.bloodTextView.setText(patientDataModel.getBloodGroup());
        holder.locationTextView.setText(patientDataModel.getDistrict());

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
