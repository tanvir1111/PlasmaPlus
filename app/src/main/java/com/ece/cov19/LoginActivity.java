package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.Locale;

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
import static com.ece.cov19.Functions.LoginUser.loginUser;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneNumberEditText, passwordEditText;
    private TextView forgotPasswordTextView;
    private ImageView showPasswordIcon, loginMenuIcon;
    private Button signUpButton, signInbtn;
    private ProgressBar progressBar;
    public static final String LOGIN_SHARED_PREFS = "login_pref";
    public static final String LOGIN_USER_PHONE = "login_phone";
    public static final String LOGIN_USER_PASS = "login_pass";

    public static final String Language_pref="Language";
    public static final String Selected_language="Selected Language";

    private int backCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumberEditText = findViewById(R.id.login_phone_number_edittext);
        passwordEditText = findViewById(R.id.login_password_edittext);
        showPasswordIcon = findViewById(R.id.show_password_icon);
        loginMenuIcon = findViewById(R.id.login_menu_icon);
        signUpButton = findViewById(R.id.login_sign_up_button);
        signInbtn = findViewById(R.id.login_sign_in_button);
        progressBar = findViewById(R.id.login_progressBar);
        forgotPasswordTextView = findViewById(R.id.login_forgot_password_textview);


        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInbtn.setEnabled(false);
                verifyData();


            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent phonever = new Intent(LoginActivity.this, PhoneVerificationActivity1.class);
                phonever.putExtra("verification","signup");
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

        loginMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), loginMenuIcon);
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.activity_login_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.english) {
                            setLocale("en");
                        }
                        else if(id == R.id.bangla){
                            setLocale("bn");
                        }

                        return true;
                    }
                });
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPassIntent = new Intent(LoginActivity.this,PhoneVerificationActivity1.class);

                forgotPassIntent.putExtra("verification","forgotpass");
                startActivity(forgotPassIntent);
            }
        });


    }



    @Override
    public void onBackPressed() {

        backCounter++;
        if(backCounter == 1) {

            ToastCreator.toastCreatorRed(LoginActivity.this,getResources().getString(R.string.press_one_more_time));
        }
        if(backCounter == 2) {
            finishAffinity();
        }
    }

    private void verifyData() {

        String phone, password, emptyfield = "all ok";

        password = passwordEditText.getText().toString();

        phone = phoneNumberEditText.getText().toString();


//        checking empty Fields

        if (phone.isEmpty()) {
            phoneNumberEditText.setError(getResources().getString(R.string.login_activity_phone_number_edittext));
            phoneNumberEditText.requestFocus();
            emptyfield = "phone number";
            signInbtn.setEnabled(true);
        }

        if (password.isEmpty()) {
            passwordEditText.setError(getResources().getString(R.string.login_activity_password_edittext));
            passwordEditText.requestFocus();
            emptyfield = "password";
            signInbtn.setEnabled(true);
        }

        if (emptyfield.toLowerCase().equals("all ok")) {
           loginUser(phone, password);

        }


    }


    private void loginUser(String phone, String password) {


        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<UserDataModel> sendingData = retroInterface.loginRetroMethod(phone, password);
        sendingData.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                if (response.body().getServerMsg().toLowerCase().equals("success")) {
                    progressBar.setVisibility(View.GONE);

                    ToastCreator.toastCreatorGreen(LoginActivity.this, getResources().getString(R.string.welcome)+" " + response.body().getName());

//                  Storing phone and password to shared preferences
                    SharedPreferences loginSharedPrefs = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor loginPrefsEditor = loginSharedPrefs.edit();
                    loginPrefsEditor.putString(LOGIN_USER_PHONE, response.body().getPhone());
                    loginPrefsEditor.putString(LOGIN_USER_PASS, response.body().getPassword());
                    loginPrefsEditor.apply();

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


//                  going to Dashboard
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);

                    startActivity(intent);

                    finish();
                }
                else if(response.body().getServerMsg().toLowerCase().equals("wrong phone or password")){
                    progressBar.setVisibility(View.GONE);
                    ToastCreator.toastCreatorRed(LoginActivity.this,getResources().getString(R.string.login_activity_wrong));
                    signInbtn.setEnabled(true);
                }

                else {
                    progressBar.setVisibility(View.GONE);

                    ToastCreator.toastCreatorRed(LoginActivity.this,getResources().getString(R.string.connection_error));
                    signInbtn.setEnabled(true);
                }

            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                signInbtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);


                ToastCreator.toastCreatorRed(LoginActivity.this,getResources().getString(R.string.connection_error));
            }
        });

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        SharedPreferences langPrefs=getSharedPreferences(Language_pref,MODE_PRIVATE);
        SharedPreferences.Editor langPrefsEditor = langPrefs.edit();
        langPrefsEditor.putString(Selected_language, lang);
        langPrefsEditor.apply();


        Intent refresh = new Intent(this, LoginActivity.class);
        startActivity(refresh);
        finish();
    }
}
