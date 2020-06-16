package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserAge;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserBloodGroup;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDistrict;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDonorInfo;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserGender;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserName;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserThana;

public class ProfileActivity extends AppCompatActivity {
    private TextView nameTextView, phoneTextView, bloodGroupTextView, addressTextView, ageTextView, donorInfoTextView;
    private ImageView genderImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = findViewById(R.id.profile_name);
        phoneTextView = findViewById(R.id.profile_phone);
        bloodGroupTextView = findViewById(R.id.profile_blood_group);
        addressTextView = findViewById(R.id.profile_address);
        ageTextView = findViewById(R.id.profile_age);
        donorInfoTextView = findViewById(R.id.profile_donor_role);
        genderImageView = findViewById(R.id.profile_gender_icon);


        
//      setting logged in user info
        nameTextView.setText(loggedInUserName);
        phoneTextView.setText(loggedInUserPhone);
        bloodGroupTextView.setText(loggedInUserBloodGroup);
        addressTextView.setText(String.format("%s,%s", loggedInUserThana, loggedInUserDistrict));
        ageTextView.setText(loggedInUserAge);
        donorInfoTextView.setText(loggedInUserDonorInfo);

        if (loggedInUserGender.toLowerCase().equals("male")) {
            genderImageView.setImageResource(R.drawable.profile_icon_male);
        } else if (loggedInUserGender.toLowerCase().equals("male")) {
            genderImageView.setImageResource(R.drawable.profile_icon_female);
        }


    }
}
