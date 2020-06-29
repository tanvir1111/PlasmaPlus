package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.R;

import java.util.ArrayList;

public class DonorResponseBetaAdapter extends RecyclerView.Adapter<DonorResponseBetaViewHolder>{

    public Context context;
    public UserDataModel userDataModel;
    public ArrayList<UserDataModel> userDataModels;

    public DonorResponseBetaAdapter(Context context, ArrayList<UserDataModel> userDataModels) {
        this.context = context;
        this.userDataModels = userDataModels;
    }


    @NonNull
    @Override
    public DonorResponseBetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.request_donor_child, parent, false);
        DonorResponseBetaViewHolder donorResponseBetaViewHolder = new DonorResponseBetaViewHolder(view, userDataModels);
        return donorResponseBetaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DonorResponseBetaViewHolder holder, int position) {

        userDataModel = userDataModels.get(position);

        holder.nameTextView.setText(userDataModel.getName());
        holder.locationTextView.setText(userDataModel.getDistrict());
        holder.bloodTextView.setText(userDataModel.getBloodGroup());
        holder.donorType.setText(userDataModel.getDonor());

        if(userDataModel.getServerMsg().equals("Pending")) {
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setText("Pending");
            holder.declineButton.setVisibility(View.GONE);
        }
        else if(userDataModel.getServerMsg().equals("Accepted")){
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setText("Accepted");
            holder.declineButton.setVisibility(View.GONE);
        }
        else if(userDataModel.getServerMsg().equals("Declined")){
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setText("Accepted");
            holder.declineButton.setVisibility(View.GONE);
        }


        //Toast.makeText(holder.itemView.getContext(), userDataModel.getServerMsg(), Toast.LENGTH_SHORT).show();

            if(userDataModel.getGender().equals("male")) {
            holder.donorImageView.setImageResource(R.drawable.profile_icon_male);
        } else {
            holder.donorImageView.setImageResource(R.drawable.profile_icon_female);
        }
        holder.locationImageView.setImageResource(R.drawable.location_icon);

    }

    @Override
    public int getItemCount() {
        return userDataModels.size();
    }
}
