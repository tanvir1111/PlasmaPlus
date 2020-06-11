package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    EditText passwordEditText;
    ImageView showPasswordIcon;
    Button signUpButton,signInbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        passwordEditText=findViewById(R.id.login_password_edittext);
        showPasswordIcon=findViewById(R.id.show_password_icon);
        signUpButton=findViewById(R.id.login_sign_up_button);
        signInbtn=findViewById(R.id.login_sign_in_button);
        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile=new Intent(LoginActivity.this, DashBoard.class);
                startActivity(profile);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phonever=new Intent(LoginActivity.this,Phone_verification.class);
                startActivity(phonever);
            }
        });

        showPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (passwordEditText.getTransformationMethod() == null) {
                        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
                    } else {
                        passwordEditText.setTransformationMethod(null);
                    }
                }

        });


    }
}
