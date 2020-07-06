package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneVerificationActivity1 extends AppCompatActivity {
    TextView labelPhone,countryCode;
    EditText phoneInput;
    Button getOtpBtn;
    ImageView backBtn;
    String verification, phonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification_1);

        backBtn=findViewById(R.id.phn_ver_back_button);
        labelPhone=findViewById(R.id.phn_ver_phone_text);
        countryCode=findViewById(R.id.phn_ver_countryCode);
        getOtpBtn=findViewById(R.id.phn_ver_get_otp_button);
        phoneInput=findViewById(R.id.phn_ver_phone_input);

        Intent intent = getIntent();
        verification = intent.getStringExtra("verification");

       if(verification.equals("forgotpass")){
            getOtpBtn.setText("Next");
        }

        getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber=phoneInput.getText().toString();
                if(phonenumber.length() == 11){


                        checkUser();
                } else {
                    phoneInput.setError("Invalid Phone Number");
                    phoneInput.requestFocus();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void checkUser() {

        RetroInterface retroInterface = RetroInstance.getRetro();

        Call<UserDataModel> sendingData = retroInterface.checkUser(phonenumber);
        sendingData.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                if(verification.equals("forgotpass")) {
                    if (response.body().getServerMsg().equals("record exists")) {
                        Intent intent = new Intent(PhoneVerificationActivity1.this, UpdatePasswordActivity.class);
                        intent.putExtra("phone", phonenumber);
                        intent.putExtra("verification", verification);
                        startActivity(intent);
                        finish();
                    }
                    else if(response.body().getServerMsg().equals("record doesn't exist")){
                        Toast.makeText(PhoneVerificationActivity1.this, "This Phone number is not registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PhoneVerificationActivity1.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(PhoneVerificationActivity1.this,  response.body().getServerMsg(), Toast.LENGTH_SHORT).show();
                    }
                }


                else if(verification.equals("signup")){
                    if (response.body().getServerMsg().equals("record exists")) {
                        Toast.makeText(PhoneVerificationActivity1.this, "This phone number is  already registered", Toast.LENGTH_SHORT).show();

                    }
                    else if(response.body().getServerMsg().equals("record doesn't exist")){


                        Intent nextIntent = new Intent(PhoneVerificationActivity1.this, RegistrationActivity.class);
                        nextIntent.putExtra("phone",phonenumber);
                        nextIntent.putExtra("verification",verification);
                        startActivity(nextIntent);
                        finish();

                    }
                    else {
                        Toast.makeText(PhoneVerificationActivity1.this,  response.body().getServerMsg(), Toast.LENGTH_SHORT).show();
                    }


                }



            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                Toast.makeText(PhoneVerificationActivity1.this, "An error occurred! Check your connection and try again", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
