package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

    private EditText phoneNumberEditText, passwordEditText;
    private ImageView showPasswordIcon;
    private Button signUpButton, signInbtn;
    public static final String LOGIN_SHARED_PREFS="login_pref";
    public static final String LOGIN_USER_PHONE="login_phone";
    public static final String LOGIN_USER_PASS="login_pass";

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

//      showing and hiding password
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

                    Toast.makeText(LoginActivity.this, "Welcome "+response.body().getName(), Toast.LENGTH_SHORT).show();

//                  Storing phone and password to shared preferences
                    SharedPreferences loginSharedPrefs=getSharedPreferences(LOGIN_SHARED_PREFS,MODE_PRIVATE);
                    SharedPreferences.Editor loginPrefsEditor = loginSharedPrefs.edit();
                    loginPrefsEditor.putString(LOGIN_USER_PHONE,response.body().getPhone());
                    loginPrefsEditor.putString(LOGIN_USER_PASS,response.body().getPassword());
                    loginPrefsEditor.apply();



//              setting all logged in info
                    new LoggedInUserData(response.body().getName(),response.body().getPhone(),
                            response.body().getGender(),response.body().getBloodGroup(),
                            response.body().getDivision(),response.body().getDistrict(),
                            response.body().getThana(),response.body().getAge(),response.body().getDonor());


//                  going to Dashboard
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
                Toast.makeText(LoginActivity.this, "An error occurred! Check your connection and try again", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
