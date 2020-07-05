package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.R;

import java.util.ArrayList;

public class PatientResponseAdapter extends RecyclerView.Adapter<PatientResponseViewHolder> {

    public Context context;
    public PatientDataModel patientDataModel;
    public ArrayList<PatientDataModel> patientDataModels;

    public PatientResponseAdapter(Context context, ArrayList<PatientDataModel> patientDataModels) {
        this.context = context;
        this.patientDataModels = patientDataModels;
    }

    @NonNull
    @Override
    public PatientResponseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.request_patient_child, parent, false);
        PatientResponseViewHolder patientResponseViewHolder = new PatientResponseViewHolder(view, patientDataModels);
        return patientResponseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientResponseViewHolder holder, int position) {

        patientDataModel = patientDataModels.get(position);

        holder.nameTextView.setText(patientDataModel.getName());
        holder.typeTextView.setText(patientDataModel.getNeed());
        holder.bloodTextView.setText(patientDataModel.getBloodGroup());
        holder.locationTextView.setText(patientDataModel.getDistrict());
        holder.dateTextView.setText("Last date of donation              "+patientDataModel.getDate());
        if(patientDataModel.getGender().equals("male")) {
            holder.patientImageView.setImageResource(R.drawable.profile_icon_male);
        } else {
            holder.patientImageView.setImageResource(R.drawable.profile_icon_female);
        }

        if(patientDataModel.getServerMsg().equals("Pending")){
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setText("Pending");
            holder.acceptButton.setBackgroundResource(R.drawable.button_style_orange);
            holder.acceptButton.setTextColor(Color.parseColor("#FFFFFF"));
            holder.declineButton.setVisibility(View.GONE);
        }
        else if(patientDataModel.getServerMsg().equals("Accepted")){
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setText("Accepted");
            holder.acceptButton.setBackgroundResource(R.drawable.button_style_green);
            holder.acceptButton.setTextColor(Color.parseColor("#FFFFFF"));
            holder.declineButton.setVisibility(View.GONE);
        }
        else if(patientDataModel.getServerMsg().equals("Declined")){
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setText("Declined");
            holder.acceptButton.setBackgroundResource(R.drawable.button_style_red);
            holder.acceptButton.setTextColor(Color.parseColor("#FFFFFF"));
            holder.declineButton.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return patientDataModels.size();
    }
}
