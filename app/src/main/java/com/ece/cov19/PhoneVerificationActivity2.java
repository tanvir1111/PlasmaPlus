package com.ece.cov19;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

//import android.support.annotation.NonNull;

public class PhoneVerificationActivity2 extends AppCompatActivity {


    private String verificationId;
    private FirebaseAuth mAuth;
    private Button button;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification_2);
        editText = findViewById(R.id.phn_ver_otp);
        button = findViewById(R.id.phn_ver_otp_button);
        mAuth = FirebaseAuth.getInstance();


        String phonenumber = getIntent().getStringExtra("phone");
        sendVerificationCode(phonenumber);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText.getText().toString();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Invalid code.");
                    editText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(PhoneVerificationActivity2.this, RegistrationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            String phonenumber = getIntent().getStringExtra("phone");
                            intent.putExtra("phone",phonenumber);
                            Toast.makeText(PhoneVerificationActivity2.this, "Verification Successful!", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(PhoneVerificationActivity2.this, "Verification Failed! Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }




    private void sendVerificationCode(String number) {
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
                editText.setText(code);
                verifyCode(code);
                Toast.makeText(PhoneVerificationActivity2.this, "Automatic Verification", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneVerificationActivity2.this, "Automatic Verification Failed! Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}

