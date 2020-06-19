package com.ece.cov19;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PhoneVerificationActivity1 extends AppCompatActivity {
    TextView labelPhone,otp,countryCode;
    EditText otpInput,phoneInput;
    Button verifyBtn,getOtpBtn;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification_1);

        backBtn=findViewById(R.id.phn_ver_back_button);
        labelPhone=findViewById(R.id.phn_ver_phone_text);
        countryCode=findViewById(R.id.phn_ver_countryCode);
        getOtpBtn=findViewById(R.id.phn_ver_get_otp_button);
        phoneInput=findViewById(R.id.phn_ver_phone_input);
        otp=findViewById(R.id.phn_ver_otp_textview);
        verifyBtn=findViewById(R.id.phn_ver_verify_button);
        otpInput=findViewById(R.id.phn_ver_otp_edittext);

        getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //makeOtpDialogeVisible();
                Intent registration=new Intent(PhoneVerificationActivity1.this, PhoneVerificationActivity2.class);
                registration.putExtra("phone",countryCode.getText().toString()+phoneInput.getText().toString());
                startActivity(registration);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makPhoneInputVisible();
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registration=new Intent(PhoneVerificationActivity1.this, PhoneVerificationActivity2.class);
                registration.putExtra("phone",countryCode.getText().toString()+phoneInput.getText().toString());
                startActivity(registration);
            }
        });
    }

    private void makPhoneInputVisible() {
        labelPhone.setVisibility(View.VISIBLE);
        countryCode.setVisibility(View.VISIBLE);
        getOtpBtn.setVisibility(View.VISIBLE);
        phoneInput.setVisibility(View.VISIBLE);
        otp.setVisibility(View.INVISIBLE);
        verifyBtn.setVisibility(View.INVISIBLE);
        otpInput.setVisibility(View.INVISIBLE);
    }

    private void makeOtpDialogeVisible() {
        labelPhone.setVisibility(View.INVISIBLE);
        countryCode.setVisibility(View.INVISIBLE);
        getOtpBtn.setVisibility(View.INVISIBLE);
        phoneInput.setVisibility(View.INVISIBLE);
        otp.setVisibility(View.VISIBLE);
        verifyBtn.setVisibility(View.VISIBLE);
        otpInput.setVisibility(View.VISIBLE);
        otpInput.setText("");
    }
}
