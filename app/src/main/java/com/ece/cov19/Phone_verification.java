package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Phone_verification extends AppCompatActivity {
    TextView labelPhone,otp,countryCode;
    EditText otpInput,phoneInput;
    Button verifyBtn,getOtpBtn;
    ImageView backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        backBtn=findViewById(R.id.phn_ver_back_button);
        //hello
        //there
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
                makeOtpDialogeVisible();
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
                Intent registration=new Intent(Phone_verification.this,RegistrationActivity.class);
                registration.putExtra("phone",phoneInput.getText().toString());
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
