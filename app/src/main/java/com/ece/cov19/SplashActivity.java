package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

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
import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PASS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PHONE;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.splash_progress_bar);
        final Handler handler = new Handler();

handler.postDelayed(new Runnable() {
    @Override
    public void run() {
        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
        if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {
            String userPhone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
            String userPass = sharedPreferences.getString(LOGIN_USER_PASS, "");

            loginUser(userPhone, userPass);

        } else {
            Intent login = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }
    }
},1000);


    }

    private void loginUser(String phone, String password) {

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<UserDataModel> sendingData = retroInterface.loginRetroMethod(phone, password);
        sendingData.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                if (response.body().getServerMsg().equals("Success")) {

                    progressBar.setVisibility(View.GONE);



//              setting all logged in info
                    loggedInUserName = response.body().getName();
                    loggedInUserPhone = response.body().getPhone();
                    loggedInUserGender = response.body().getGender();
                    loggedInUserBloodGroup = response.body().getBloodGroup();
                    loggedInUserDivision = response.body().getDivision();
                    loggedInUserDistrict = response.body().getDistrict();
                    loggedInUserThana = response.body().getThana();
                    loggedInUserAge = response.body().getAge();
                    loggedInUserDonorInfo = response.body().getDonor();
                    loggedInUserPass = response.body().getPassword();
                    Toast.makeText(SplashActivity.this, "Welcome " + loggedInUserName, Toast.LENGTH_SHORT).show();

//                  going to Dashboard
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SplashActivity.this, response.body().getServerMsg(), Toast.LENGTH_SHORT).show();

//                   going to Login
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }

            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "An error occurred! Check your connection and try again", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
