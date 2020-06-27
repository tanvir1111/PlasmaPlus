package com.ece.cov19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVerificationActivity extends AppCompatActivity {
    TextView labelPhone,otp,countryCode;
    EditText otpInput,phoneInput;
    Button verifyBtn,getOtpBtn;
    ImageView backBtn;
    FirebaseAuth mAuth;
    String verificationId;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        backBtn=findViewById(R.id.phn_ver_back_button);
        labelPhone=findViewById(R.id.phn_ver_phone_text);
        countryCode=findViewById(R.id.phn_ver_countryCode);
        getOtpBtn=findViewById(R.id.phn_ver_get_otp_button);
        phoneInput=findViewById(R.id.phn_ver_phone_input);
        progressBar = findViewById(R.id.phn_ver_progressBar);
        otp=findViewById(R.id.phn_ver_otp_textview);
        verifyBtn=findViewById(R.id.phn_ver_verify_button);
        otpInput=findViewById(R.id.phn_ver_otp_edittext);

        mAuth = FirebaseAuth.getInstance();

        getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeOtpDialogeVisible();
                String phonenumber = phoneInput.getText().toString();
                //Toast.makeText(PhoneVerificationActivity.this,"Phone: "+countryCode.getText().toString()+phonenumber,Toast.LENGTH_SHORT).show();
                sendVerificationCode(countryCode.getText().toString()+phonenumber);

            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = otpInput.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    otpInput.setError("Enter code...");
                    otpInput.requestFocus();
                } else {
                    verifyCode(code);
                    Intent registration=new Intent(PhoneVerificationActivity.this,RegistrationActivity.class);
                    registration.putExtra("phone",phoneInput.getText().toString());
                    startActivity(registration);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makPhoneInputVisible();
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
        if(phoneInput.getText().toString().length()!=11){
            phoneInput.setError("invalid number");
        }
        else {
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

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(PhoneVerificationActivity.this, RegistrationActivity
                                    .class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(PhoneVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otpInput.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}
