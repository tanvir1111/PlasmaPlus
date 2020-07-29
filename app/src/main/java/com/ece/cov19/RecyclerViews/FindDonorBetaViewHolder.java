package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.ClickTimeChecker;
import com.ece.cov19.R;
import com.ece.cov19.ViewDonorProfileActivity;

import java.util.ArrayList;


public class FindDonorBetaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView nameTextView, locationTextView, bloodTextView, donortype, askButton;
    ImageView donorImageView, locationImageView;
    UserDataModel userDataModel;
    ArrayList<UserDataModel> userDataModels;
    int pos;

    public FindDonorBetaViewHolder(@NonNull View itemView, ArrayList<UserDataModel> userDataModels) {
        super(itemView);
        this.userDataModels = userDataModels;
        nameTextView = itemView.findViewById(R.id.donor_name);
        locationTextView = itemView.findViewById(R.id.donor_location);
        bloodTextView = itemView.findViewById(R.id.donor_child_bld_grp);
        donorImageView = itemView.findViewById(R.id.donor_child_profile_image);
        locationImageView = itemView.findViewById(R.id.donor_child_location_image);
        donortype = itemView.findViewById(R.id.donor_child_donor_type);
        askButton = itemView.findViewById(R.id.donor_child_details_btn);

        itemView.setOnClickListener(this);
        askButton.setOnClickListener(this);

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
            intent.putExtra("activity", "FindDonorActivity");

            c.startActivity(intent);
            //((FindDonorActivity)c).finish();
        }
    }
}