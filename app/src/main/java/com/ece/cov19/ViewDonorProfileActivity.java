package com.ece.cov19;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewDonorProfileActivity extends AppCompatActivity {

    private TextView nameTextView, phoneTextView, bloodGroupTextView, addressTextView, ageTextView, donorInfoTextView;
    private ImageView genderImageView;
    private Button updateInfoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donor_profile);

        nameTextView = findViewById(R.id.profile_name);
        phoneTextView = findViewById(R.id.profile_phone);
        bloodGroupTextView = findViewById(R.id.profile_blood_group);
        addressTextView = findViewById(R.id.profile_hospital);
        ageTextView = findViewById(R.id.profile_age);
        donorInfoTextView = findViewById(R.id.profile_type);
        genderImageView = findViewById(R.id.profile_gender_icon);
        updateInfoButton=findViewById(R.id.go_back_btn);

        Intent intent = getIntent();

        nameTextView.setText(intent.getStringExtra("name"));
        phoneTextView.setText(intent.getStringExtra("phone"));
        bloodGroupTextView.setText(intent.getStringExtra("blood"));
        addressTextView.setText(intent.getStringExtra("address"));
        ageTextView.setText(intent.getStringExtra("age"));
        donorInfoTextView.setText(intent.getStringExtra("donorinfo"));
        if(intent.getStringExtra("gender").equals("male")){
            genderImageView.setImageResource(R.drawable.male_icon);
        }
        else {
            genderImageView.setImageResource(R.drawable.female_icon);

        }

        updateInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
