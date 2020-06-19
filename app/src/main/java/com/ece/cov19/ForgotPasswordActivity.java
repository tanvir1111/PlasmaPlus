package com.ece.cov19;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class ForgotPasswordActivity extends AppCompatActivity {

    private Button finishButton;
    private EditText passText, confPassText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        passText = findViewById(R.id.current_password_text);
        confPassText = findViewById(R.id.new_password_text);
        finishButton = findViewById(R.id.reset_button);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, DashBoard.class);
                startActivity(intent);
            }
        });



    }



}

