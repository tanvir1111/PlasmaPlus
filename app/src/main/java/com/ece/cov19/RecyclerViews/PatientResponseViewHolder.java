package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.Functions.ClickTimeChecker;
import com.ece.cov19.R;
import com.ece.cov19.ViewPatientProfileActivity;

import java.util.ArrayList;

public class PatientResponseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTextView, typeTextView, bloodTextView, locationTextView, acceptButton, declineButton, dateTextView;
    ImageView patientImageView;
    PatientDataModel patientDataModel;
    ArrayList<PatientDataModel> patientDataModels;
    int pos;

    public PatientResponseViewHolder(@NonNull View itemView, ArrayList<PatientDataModel> patientDataModels) {
        super(itemView);
        this.patientDataModels = patientDataModels;

        nameTextView = itemView.findViewById(R.id.request_patient_name);
        typeTextView = itemView.findViewById(R.id.request_patient_type);
        bloodTextView = itemView.findViewById(R.id.request_patient_child_bld_grp);
        locationTextView = itemView.findViewById(R.id.request_patient_location);
        patientImageView = itemView.findViewById(R.id.request_patient_child_profile_image);
        acceptButton = itemView.findViewById(R.id.request_patient_accept_btn);
        declineButton = itemView.findViewById(R.id.request_patient_decline_btn);
        dateTextView = itemView.findViewById(R.id.request_patient_date);

        acceptButton.setOnClickListener(this);
        declineButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(ClickTimeChecker.clickTimeChecker()) {
            Context c = view.getContext();
            pos = getAdapterPosition();
            patientDataModel = patientDataModels.get(pos);


            Intent intent = new Intent(view.getContext(), ViewPatientProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.putExtra("name", patientDataModel.getName());
            intent.putExtra("age", patientDataModel.getAge());
            intent.putExtra("gender", patientDataModel.getGender());
            intent.putExtra("blood_group", patientDataModel.getBloodGroup());
            intent.putExtra("hospital", patientDataModel.getHospital());
            intent.putExtra("division", patientDataModel.getDivision());
            intent.putExtra("district", patientDataModel.getDistrict());
            intent.putExtra("date", patientDataModel.getDate());
            intent.putExtra("need", patientDataModel.getNeed());
            intent.putExtra("phone", patientDataModel.getPhone());
            intent.putExtra("amountOfBloodNeeded",patientDataModel.getAmountOfBloodNeeded());
            intent.putExtra("activity", "PatientResponseActivity");

            c.startActivity(intent);
        }
    }

}
