package com.ece.cov19;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserAge;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserBloodGroup;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDistrict;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDivision;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDonorInfo;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserGender;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserName;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPass;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserThana;

//import android.support.annotation.NonNull;

public class PhoneVerificationActivity2 extends AppCompatActivity {


    private String verificationId;
    private FirebaseAuth mAuth;
    private Button button;
    private EditText editText;
    private ImageView backButton;
    private ProgressBar progressBar;
    String verification, phonenumber;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification_2);
        editText = findViewById(R.id.phn_ver2_otp);
        button = findViewById(R.id.phn_ver2_otp_button);
        backButton = findViewById(R.id.phn_ver2_back_button);
        progressBar = findViewById(R.id.phn_ver2_progress_bar);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        verification = intent.getStringExtra("verification");
        phonenumber = intent.getStringExtra("phone");
        sendVerificationCode("+88"+phonenumber);

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
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(verification.toLowerCase().equals("signup")) {
                                Intent intent = new Intent(PhoneVerificationActivity2.this, RegistrationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("phone", phonenumber);
                                ToastCreator.toastCreatorGreen(PhoneVerificationActivity2.this, getResources().getString(R.string.phn_ver_activity_verification_success));
                                startActivity(intent);
                                finish();
                            }

                            else if(verification.toLowerCase().equals("forgotpass")){
                                Intent intent = new Intent(PhoneVerificationActivity2.this, UpdatePasswordActivity.class);
                                intent.putExtra("phone", phonenumber);
                                intent.putExtra("verification", verification);
                                ToastCreator.toastCreatorGreen(PhoneVerificationActivity2.this, getResources().getString(R.string.phn_ver_activity_verification_success));
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            progressBar.setVisibility(View.GONE);
                            ToastCreator.toastCreatorRed(PhoneVerificationActivity2.this, getResources().getString(R.string.phn_ver_activity_verification_failed));
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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
                verifyCode(code);
                progressBar.setVisibility(View.GONE);
                ToastCreator.toastCreatorGreen(PhoneVerificationActivity2.this, getResources().getString(R.string.phn_ver_activity_verification_success));
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            ToastCreator.toastCreatorRed(PhoneVerificationActivity2.this, getResources().getString(R.string.phn_ver_activity_verification_failed));
        }
    };


}

