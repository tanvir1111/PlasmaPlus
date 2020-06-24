package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.FindPatientData;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.R;

import java.util.ArrayList;

public class FindDonorAdapter extends RecyclerView.Adapter<FindDonorViewHolder>{

    public Context context;
    public UserDataModel userDataModel;
    public ArrayList<UserDataModel> userDataModels;

    public FindDonorAdapter(Context context, ArrayList<UserDataModel> userDataModels) {
        this.context = context;
        this.userDataModels = userDataModels;
    }


    @NonNull
    @Override
    public FindDonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.search_donor_child, parent, false);
        FindDonorViewHolder findDonorViewHolder = new FindDonorViewHolder(view, userDataModels);
        return findDonorViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FindDonorViewHolder holder, int position) {

        userDataModel = userDataModels.get(position);

        holder.nameTextView.setText(userDataModel.getName());
        holder.locationTextView.setText(userDataModel.getDistrict());
        holder.bloodTextView.setText(userDataModel.getBloodGroup());
        holder.donortype.setText(userDataModel.getDonor());

        if(userDataModel.getGender().equals("male")) {
            holder.donorImageView.setImageResource(R.drawable.profile_icon_male);
        } else {
            holder.donorImageView.setImageResource(R.drawable.profile_icon_female);
        }
        holder.locationImageView.setImageResource(R.drawable.location_icon);

        if(userDataModel.getBloodGroup().equals(FindPatientData.findPatientBloodGroup)){
            holder.askButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return userDataModels.size();
    }
}
