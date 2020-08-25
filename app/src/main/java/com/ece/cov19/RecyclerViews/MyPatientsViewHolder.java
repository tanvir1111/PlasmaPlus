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

public class MyPatientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTextView, donateTextView, typeTextView, bloodTextView, locationTextView, dateTextView;
    ImageView patientImageView;
    PatientDataModel patientDataModel;
    ArrayList<PatientDataModel> patientDataModels;
    int pos;


    public MyPatientsViewHolder(@NonNull View itemView, ArrayList<PatientDataModel> patientDataModels) {
        super(itemView);
        this.patientDataModels = patientDataModels;

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
        pos = getAdapterPosition();
        Context c = view.getContext();

        patientDataModel = patientDataModels.get(pos);
        Intent intent = new Intent(view.getContext(), ViewPatientProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra("name",patientDataModel.getName());
        intent.putExtra("phone",patientDataModel.getPhone());
        intent.putExtra("blood_group",patientDataModel.getBloodGroup());
        intent.putExtra("hospital",patientDataModel.getHospital());
        intent.putExtra("age",patientDataModel.getAge());
        intent.putExtra("need",patientDataModel.getNeed());
        intent.putExtra("gender",patientDataModel.getGender());
        intent.putExtra("date",patientDataModel.getDate());
        intent.putExtra("district",patientDataModel.getDistrict());
        intent.putExtra("division",patientDataModel.getDivision());
        intent.putExtra("amountOfBloodNeeded",patientDataModel.getAmountOfBloodNeeded());
        intent.putExtra("activity","MyPatientsActivity");

        c.startActivity(intent);
    }
    }
}
