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
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneVerificationActivity1 extends AppCompatActivity {
    TextView labelPhone,countryCode,headerTextView;
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
        headerTextView=findViewById(R.id.phn_ver_header_textview);


        Intent intent = getIntent();
        verification = intent.getStringExtra("verification");

       if(verification.toLowerCase().equals("forgotpass")){
            getOtpBtn.setText(R.string.next);
            headerTextView.setText(R.string.forgot_password);
        }

        getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber=phoneInput.getText().toString();
                if(phonenumber.length() == 11){


                        checkUser();
                } else {
                    phoneInput.setError(getResources().getString(R.string.phn_ver_activity_invalid_phone));
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

                if(verification.toLowerCase().equals("forgotpass")) {
                    if (response.body().getServerMsg().toLowerCase().equals("record exists")) {

                        Intent intent = new Intent(PhoneVerificationActivity1.this, PhoneVerificationActivity2.class);

                        intent.putExtra("phone", phonenumber);
                        intent.putExtra("verification", verification);
                        startActivity(intent);
                        finish();
                    }
                    else if(response.body().getServerMsg().toLowerCase().equals("record doesn't exist")){
                        ToastCreator.toastCreatorRed(PhoneVerificationActivity1.this, getResources().getString(R.string.phn_ver_activity_not_registered));
                        Intent intent = new Intent(PhoneVerificationActivity1.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        ToastCreator.toastCreatorRed(PhoneVerificationActivity1.this,  getResources().getString(R.string.connection_failed_try_again));
                    }
                }


                else if(verification.toLowerCase().equals("signup")){
                    if (response.body().getServerMsg().toLowerCase().equals("record exists")) {
                        ToastCreator.toastCreatorRed(PhoneVerificationActivity1.this, getResources().getString(R.string.phn_ver_activity_already_registered));

                    }
                    else if(response.body().getServerMsg().toLowerCase().equals("record doesn't exist")){


                        Intent nextIntent = new Intent(PhoneVerificationActivity1.this, PhoneVerificationActivity2.class);
                        nextIntent.putExtra("phone",phonenumber);
                        nextIntent.putExtra("verification",verification);
                        startActivity(nextIntent);
                        finish();

                    }
                    else {
                        ToastCreator.toastCreatorRed(PhoneVerificationActivity1.this,  getResources().getString(R.string.connection_failed_try_again));
                    }


                }



            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(PhoneVerificationActivity1.this, getResources().getString(R.string.connection_error));
            }
        });

    }


}
