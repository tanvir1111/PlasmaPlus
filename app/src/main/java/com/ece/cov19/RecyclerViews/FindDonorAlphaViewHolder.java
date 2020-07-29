package com.ece.cov19.RecyclerViews;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ece.cov19.DataModels.PatientDataModel;

import com.ece.cov19.Functions.ClickTimeChecker;
import com.ece.cov19.R;


import java.util.ArrayList;

public class FindDonorAlphaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTextView, donateTextView, typeTextView, bloodTextView, locationTextView, dateTextView;
    ImageView patientImageView;
    PatientDataModel patientDataModel;
    ArrayList<PatientDataModel> patientDataModels;
    FindPatientViewHolderViewClickListener findPatientViewHolderViewClickListener;
    int pos;

    public interface FindPatientViewHolderViewClickListener {
        void onClicked(View v, int position);
    }

    public FindDonorAlphaViewHolder(@NonNull View itemView, ArrayList<PatientDataModel> patientDataModels, FindPatientViewHolderViewClickListener findPatientViewHolderViewClickListener) {
        super(itemView);
        this.patientDataModels = patientDataModels;
        this.findPatientViewHolderViewClickListener = findPatientViewHolderViewClickListener;

        nameTextView = itemView.findViewById(R.id.seeking_help_name);
        donateTextView = itemView.findViewById(R.id.seeking_help_donate_btn);
        typeTextView = itemView.findViewById(R.id.seeking_help_type);
        bloodTextView = itemView.findViewById(R.id.seeking_help_child_bld_grp);
        locationTextView = itemView.findViewById(R.id.seeking_help_location);
        patientImageView = itemView.findViewById(R.id.seeking_help_child_profile_image);
        dateTextView = itemView.findViewById(R.id.seeking_help_date);


        itemView.setOnClickListener(this);
        donateTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(ClickTimeChecker.clickTimeChecker()) {
            findPatientViewHolderViewClickListener.onClicked(view, getAdapterPosition());
        }

    }
}
