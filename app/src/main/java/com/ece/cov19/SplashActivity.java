package com.ece.cov19;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
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
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PASS;
import static com.ece.cov19.LoginActivity.LOGIN_USER_PHONE;

public class SplashActivity extends AppCompatActivity {

    public static Button tryAgain;
    public static ProgressBar progressBar;
    SharedPreferences langPrefs;
    String lang="not set";
    public static final String Language_pref="Language";
    public static final String Selected_language="Selected Language";
    String currentVersion, latestVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



    }

    @Override
    protected void onResume(){
        super.onResume();

        progressBar = findViewById(R.id.splash_progress_bar);
        tryAgain = findViewById(R.id.splash_retry);

        checkForUpdate();

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForUpdate();
            }
        });
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

    private void checkForUpdate(){
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<UserDataModel> versionCheck = retroInterface.latestVersion();
        versionCheck.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {

                if(response.body().getServerMsg() != null) {
                    latestVersion = response.body().getServerMsg();
                    currentVersion = getCurrentVersion();
                    if (!currentVersion.equals(latestVersion)){
                        showUpdateDialog();
                    }
                    else{
                        checkSharedPref();
                    }

                }

            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tryAgain.setVisibility(View.VISIBLE);
            }
        });
    }

    private String getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;
        String ver;
        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ver = pInfo.versionName;

        return ver;

    }



    private void checkSharedPref(){
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                langPrefs = getSharedPreferences(Language_pref, MODE_PRIVATE);
                if (langPrefs.contains(Selected_language)) {
                    setLocale(langPrefs.getString(Selected_language, ""));

                } else {
                    languageAlertDialog();
                }
            }
        }, 1000);

    }

    private void showUpdateDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater=LayoutInflater.from(this);
        View langDialogView=  inflater.inflate(R.layout.update_dialog,null);
        TextView exit=langDialogView.findViewById(R.id.update_dialog_exit);
        TextView update=langDialogView.findViewById(R.id.update_dialog_update);
        builder.setCancelable(false);
        builder.setView(langDialogView);

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    finishAffinity();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri =  Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(goToMarket);


            }
        });

    }
}
