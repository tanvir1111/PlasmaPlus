package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ece.cov19.DataModels.PatientDataModel;

import com.ece.cov19.R;


import java.util.ArrayList;

public class FindPatientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTextView, phoneTextView, typeTextView, bloodTextView, locationTextView;
    ImageView patientImageView;
    PatientDataModel patientDataModel;
    ArrayList<PatientDataModel> patientDataModels;
    FindPatientViewHolderViewClickListener findPatientViewHolderViewClickListener;

    public interface FindPatientViewHolderViewClickListener {
        void onClicked(View v, int position);
    }

    public FindPatientViewHolder(@NonNull View itemView, ArrayList<PatientDataModel> patientDataModels, FindPatientViewHolderViewClickListener findPatientViewHolderViewClickListener) {
        super(itemView);
        this.patientDataModels = patientDataModels;
        this.findPatientViewHolderViewClickListener = findPatientViewHolderViewClickListener;

        nameTextView = itemView.findViewById(R.id.seeking_help_name);
        phoneTextView = itemView.findViewById(R.id.seeking_help_child_phone);
        typeTextView = itemView.findViewById(R.id.seeking_help_type);
        bloodTextView = itemView.findViewById(R.id.seeking_help_child_bld_grp);
        locationTextView = itemView.findViewById(R.id.seeking_help_location);

        patientImageView = itemView.findViewById(R.id.seeking_help_child_profile_image);


        itemView.setOnClickListener(this);
        phoneTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        findPatientViewHolderViewClickListener.onClicked(view, getAdapterPosition());
    }
}
