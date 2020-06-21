package com.ece.cov19;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewPatientProfileActivity extends AppCompatActivity {

    private TextView nameTextView, phoneTextView, bloodGroupTextView, hospitalTextView, ageTextView, typeTextView;
    private ImageView genderImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_profile);

        nameTextView = findViewById(R.id.profile_name);
        phoneTextView = findViewById(R.id.profile_phone);
        bloodGroupTextView = findViewById(R.id.profile_blood_group);
        hospitalTextView = findViewById(R.id.profile_hospital);
        ageTextView = findViewById(R.id.profile_age);
        typeTextView = findViewById(R.id.profile_type);
        genderImageView = findViewById(R.id.profile_gender_icon);


        Intent intent = getIntent();

        nameTextView.setText(intent.getStringExtra("name"));
        phoneTextView.setText(intent.getStringExtra("phone"));
        bloodGroupTextView.setText(intent.getStringExtra("blood"));
        hospitalTextView.setText(intent.getStringExtra("hospital"));
        ageTextView.setText(intent.getStringExtra("age"));
        typeTextView.setText(intent.getStringExtra("type"));
        if(intent.getStringExtra("gender").equals("male")){
            genderImageView.setImageResource(R.drawable.male_icon);
        }
        else {
            genderImageView.setImageResource(R.drawable.female_icon);

        }

    }
}
