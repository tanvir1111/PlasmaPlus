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
    private ImageView backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donor_profile);

        nameTextView = findViewById(R.id.donor_profile_name);
        phoneTextView = findViewById(R.id.donor_profile_phone);
        bloodGroupTextView = findViewById(R.id.donor_profile_blood_group);
        addressTextView = findViewById(R.id.donor_profile_hospital);
        ageTextView = findViewById(R.id.donor_profile_age);
        donorInfoTextView = findViewById(R.id.donor_profile_type);
        genderImageView = findViewById(R.id.donor_profile_gender_icon);
        updateInfoButton=findViewById(R.id.donor_ask_for_help_button);
        backbtn=findViewById(R.id.donor_profile_back_button);

        Intent intent = getIntent();

        nameTextView.setText(intent.getStringExtra("name"));
        phoneTextView.setText(intent.getStringExtra("phone"));
        bloodGroupTextView.setText(intent.getStringExtra("blood"));
        addressTextView.setText(intent.getStringExtra("address"));
        ageTextView.setText(intent.getStringExtra("age"));
        donorInfoTextView.setText(intent.getStringExtra("donorinfo"));
        if(intent.getStringExtra("gender").equals("male")){
            genderImageView.setImageResource(R.drawable.profile_icon_male);
        }
        else {
            genderImageView.setImageResource(R.drawable.profile_icon_female);

        }
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
