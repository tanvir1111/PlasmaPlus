package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.FindPatientData;
import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.ClickTimeChecker;
import com.ece.cov19.R;
import com.ece.cov19.ViewDonorProfileActivity;

import java.util.ArrayList;


public class DonorResponseBetaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView nameTextView, locationTextView, bloodTextView,donorType, acceptButton, declineButton;
    ImageView donorImageView, locationImageView;
    UserDataModel userDataModel;
    ArrayList<UserDataModel> userDataModels;
    int pos;



    public DonorResponseBetaViewHolder(@NonNull View itemView, ArrayList<UserDataModel> userDataModels) {
        super(itemView);
        this.userDataModels = userDataModels;


        nameTextView = itemView.findViewById(R.id.request_donor_name);
        locationTextView = itemView.findViewById(R.id.request_donor_location);
        bloodTextView = itemView.findViewById(R.id.request_donor_bld_grp);
        donorImageView = itemView.findViewById(R.id.request_donor_profile_image);
        locationImageView = itemView.findViewById(R.id.request_donor_location_image);
        donorType=itemView.findViewById(R.id.request_donor_donor_type);
        acceptButton = itemView.findViewById(R.id.request_donor_accept_btn);
        declineButton = itemView.findViewById(R.id.request_donor_decline_btn);

        //itemView.setOnClickListener(this);
        acceptButton.setVisibility(View.VISIBLE);
        acceptButton.setText(R.string.view_profile);
        declineButton.setVisibility(View.GONE);


        acceptButton.setOnClickListener(this);
        declineButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (ClickTimeChecker.clickTimeChecker()) {
            pos = getAdapterPosition();
            Context c = view.getContext();

            userDataModel = userDataModels.get(pos);

            Intent intent = new Intent(view.getContext(), ViewDonorProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.putExtra("name", userDataModel.getName());
            intent.putExtra("phone", userDataModel.getPhone());
            intent.putExtra("blood", userDataModel.getBloodGroup());
            intent.putExtra("address", userDataModel.getThana() + ", " + userDataModel.getDistrict());
            intent.putExtra("age", userDataModel.getAge());
            intent.putExtra("donorinfo", userDataModel.getDonor());
            intent.putExtra("gender", userDataModel.getGender());
            intent.putExtra("activity", "DonorResponseActivity");

            c.startActivity(intent);

        }
    }
}
