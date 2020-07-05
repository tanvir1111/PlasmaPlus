package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.FindPatientData;
import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.R;

import java.util.ArrayList;

public class FindDonorAlphaAdapter extends RecyclerView.Adapter<FindDonorAlphaViewHolder>{

    public Context context;
    public PatientDataModel patientDataModel;

    public ArrayList<PatientDataModel> patientDataModels;
    private RecyclerViewClickListener recyclerViewClickListener;

    public interface RecyclerViewClickListener {
        void onClicked(View v, int position);
    }

    public FindDonorAlphaAdapter(Context context, ArrayList<PatientDataModel> patientDataModels, RecyclerViewClickListener recyclerViewClickListener) {
        this.context = context;
        this.patientDataModels = patientDataModels;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public FindDonorAlphaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.seeking_help_child, parent, false);
        FindDonorAlphaViewHolder findDonorAlphaViewHolder = new FindDonorAlphaViewHolder(view, patientDataModels, new FindDonorAlphaViewHolder.FindPatientViewHolderViewClickListener() {

        @Override
            public void onClicked(View v, int position) {
                FindPatientData.findPatientPosition = position;
                FindPatientData.findPatientBloodGroup = patientDataModels.get(position).getBloodGroup();
                FindPatientData.findPatientName=patientDataModels.get(position).getName();
                FindPatientData.findPatientAge=patientDataModels.get(position).getAge();
                FindPatientData.findPatientPhone=patientDataModels.get(position).getPhone();
                FindPatientData.findPatientNeed=patientDataModels.get(position).getNeed();
                recyclerViewClickListener.onClicked(v,position);
            }
        });
        return findDonorAlphaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FindDonorAlphaViewHolder holder, int position) {

        //FindPatientData.findPatientPosition = holder.getAdapterPosition();
        patientDataModel = patientDataModels.get(position);

        holder.donateTextView.setVisibility(View.VISIBLE);
        holder.donateTextView.setText("Select Patient");
        holder.nameTextView.setText(patientDataModel.getName());
        holder.typeTextView.setText(patientDataModel.getNeed());
        holder.bloodTextView.setText(patientDataModel.getBloodGroup());
        holder.locationTextView.setText(patientDataModel.getHospital());
        holder.dateTextView.setText("Last date of donation              "+patientDataModel.getDate());
        if(patientDataModel.getGender().equals("male")) {
            holder.patientImageView.setImageResource(R.drawable.profile_icon_male);
        } else {
            holder.patientImageView.setImageResource(R.drawable.profile_icon_female);
        }


    }

    @Override
    public int getItemCount() {
        return patientDataModels.size();
    }
}
