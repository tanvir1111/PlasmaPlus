package com.ece.cov19;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.LoginUser;
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
import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PASS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PHONE;

public class SplashActivity extends AppCompatActivity {

    Button tryAgain;
    ProgressBar progressBar;
    SharedPreferences langPrefs;
    String lang="not set";
    public static final String Language_pref="Language";
    public static final String Selected_language="Selected Language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.splash_progress_bar);
        tryAgain = findViewById(R.id.splash_retry);

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              langPrefs=getSharedPreferences(Language_pref,MODE_PRIVATE);
                if(langPrefs.contains(Selected_language)){
                    setLocale(langPrefs.getString(Selected_language,""));

                }else {
                    languageAlertDialog();
                }
            }
        },1000);
    }

    private void setLocale(String selected_language) {
        Locale myLocale = new Locale(selected_language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        langPrefs=getSharedPreferences(Language_pref,MODE_PRIVATE);
        SharedPreferences.Editor langPrefsEditor = langPrefs.edit();
        langPrefsEditor.putString(Selected_language, selected_language);
        langPrefsEditor.apply();

        loginUser();

    }

    private void languageAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater=LayoutInflater.from(this);
        View langDialogView=  inflater.inflate(R.layout.language_dialog,null);
        TextView english=langDialogView.findViewById(R.id.language_dialog_english);
        TextView bengali=langDialogView.findViewById(R.id.language_dialog_bengali);
        builder.setCancelable(false);
        builder.setView(langDialogView);

        AlertDialog alertDialog=builder.create();
        alertDialog.show();



        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                english.setBackgroundResource(R.drawable.button_style_colored);
                english.setTextColor(Color.WHITE);
                bengali.setBackgroundResource(0);
                bengali.setTextColor(getResources().getColor(R.color.colorAccent));

                lang="en";
                setLocale(lang);

                alertDialog.dismiss();


            }
        });
        bengali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bengali.setBackgroundResource(R.drawable.button_style_colored);
                bengali.setTextColor(Color.WHITE);
                english.setBackgroundResource(0);
                english.setTextColor(getResources().getColor(R.color.colorAccent));

                lang="bn";
                setLocale(lang);

                alertDialog.dismiss();



            }
        });




    }

    private void loginUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
        String phone,password;
        if (sharedPreferences.contains(LOGIN_USER_PHONE) && sharedPreferences.contains(LOGIN_USER_PASS)) {
          phone = sharedPreferences.getString(LOGIN_USER_PHONE, "");
          password= sharedPreferences.getString(LOGIN_USER_PASS, "");

            LoginUser.loginUser(this,phone,password,DashboardActivity.class);
        } else {
            Intent login = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }



    }
}
