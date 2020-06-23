package com.ece.cov19.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ece.cov19.DataModels.FindPatientData;
import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.R;

import java.util.ArrayList;

public class FindPatientAdapter extends RecyclerView.Adapter<FindPatientViewHolder>{

    public Context context;
    public PatientDataModel patientDataModel;

    public ArrayList<PatientDataModel> patientDataModels;
    private RecyclerViewClickListener recyclerViewClickListener;

    public interface RecyclerViewClickListener {
        void onClicked(View v, int position);
    }

    public FindPatientAdapter(Context context, ArrayList<PatientDataModel> patientDataModels, RecyclerViewClickListener recyclerViewClickListener) {
        this.context = context;
        this.patientDataModels = patientDataModels;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public FindPatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.seeking_help_child, parent, false);
        FindPatientViewHolder findPatientViewHolder = new FindPatientViewHolder(view, patientDataModels, new FindPatientViewHolder.FindPatientViewHolderViewClickListener() {

        @Override
            public void onClicked(View v, int position) {
                FindPatientData.findPatientPosition = position;
                FindPatientData.findPatientBloodGroup = patientDataModels.get(position).getBloodGroup();
                recyclerViewClickListener.onClicked(v,position);
            }
        });
        return findPatientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FindPatientViewHolder holder, int position) {

        patientDataModel = patientDataModels.get(position);
        FindPatientData.findPatientPosition = holder.getAdapterPosition();
        holder.nameTextView.setText(patientDataModel.getName());
        holder.phoneTextView.setText(patientDataModel.getPhone());
        holder.typeTextView.setText(patientDataModel.getNeed());
        holder.bloodTextView.setText(patientDataModel.getBloodGroup());
        holder.locationTextView.setText(patientDataModel.getDistrict());
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
