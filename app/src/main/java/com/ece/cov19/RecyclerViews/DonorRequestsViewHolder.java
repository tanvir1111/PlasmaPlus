package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.DonorRequestsActivity;
import com.ece.cov19.R;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;
import com.ece.cov19.SearchDonorActivity;
import com.ece.cov19.ViewPatientProfileActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DonorRequestsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTextView, typeTextView, bloodTextView, locationTextView, acceptButton, declineButton;
    ImageView patientImageView;
    PatientDataModel patientDataModel;
    ArrayList<PatientDataModel> patientDataModels;
    int pos;

    public DonorRequestsViewHolder(@NonNull View itemView, ArrayList<PatientDataModel> patientDataModels) {
        super(itemView);
        this.patientDataModels = patientDataModels;

        nameTextView = itemView.findViewById(R.id.request_patient_name);
        typeTextView = itemView.findViewById(R.id.request_patient_type);
        bloodTextView = itemView.findViewById(R.id.request_patient_child_bld_grp);
        locationTextView = itemView.findViewById(R.id.request_patient_location);
        patientImageView = itemView.findViewById(R.id.request_patient_child_profile_image);
        acceptButton = itemView.findViewById(R.id.request_patient_accept_btn);
        declineButton = itemView.findViewById(R.id.request_patient_decline_btn);

        acceptButton.setVisibility(View.VISIBLE);
        acceptButton.setText("View Profile");
        declineButton.setVisibility(View.GONE);

        acceptButton.setOnClickListener(this);
        declineButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

                Context c = view.getContext();
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
                intent.putExtra("activity","DonorRequestsActivity");

                c.startActivity(intent);
                //((DonorRequestsActivity)c).finish();

    }

}
