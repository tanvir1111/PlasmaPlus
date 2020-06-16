package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ece.cov19.DataModels.LoggedInUserData;
import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserName;

public class LoginActivity extends AppCompatActivity {

    EditText phoneNumberEditText, passwordEditText;
    ImageView showPasswordIcon;
    Button signUpButton, signInbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumberEditText = findViewById(R.id.login_phone_number_edittext);
        passwordEditText = findViewById(R.id.login_password_edittext);
        showPasswordIcon = findViewById(R.id.show_password_icon);
        signUpButton = findViewById(R.id.login_sign_up_button);
        signInbtn = findViewById(R.id.login_sign_in_button);


        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyData();


            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent phonever = new Intent(LoginActivity.this, Phone_verification.class);
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

    private void verifyData() {
        String phone, password, emptyfield = "all ok";

        password = passwordEditText.getText().toString();

        phone = phoneNumberEditText.getText().toString();


//        checking empty Fields

        if (phone.isEmpty()) {
            emptyfield = "name";
        } else if (password.isEmpty()) {
            emptyfield = "password";
        }

        if (emptyfield.equals("all ok")) {
            loginUser(phone, password);
        } else {
            Toast.makeText(this, "Enter " + emptyfield, Toast.LENGTH_SHORT).show();
        }


    }

//    database operations

    private void loginUser(String phone, String password) {

        RetroInterface retroinstance = RetroInstance.getRetro();
        Call<UserDataModel> sendingData = retroinstance.loginRetroMethod(phone, password);
        sendingData.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                if (response.body().getServerMsg().equals("Success")) {

                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

//              setting all logged in info and going to Dashboard
                    new LoggedInUserData(response.body().getName(),response.body().getPhone(),
                            response.body().getGender(),response.body().getBloodGroup(),
                            response.body().getDivision(),response.body().getDistrict(),
                            response.body().getThana(),response.body().getAge(),response.body().getDonor());

                    Intent intent = new Intent(LoginActivity.this, DashBoard.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, response.body().getServerMsg(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
