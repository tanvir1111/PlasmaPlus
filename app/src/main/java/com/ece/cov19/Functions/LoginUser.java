package com.ece.cov19.Functions;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.LoginActivity;
import com.ece.cov19.R;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;
import com.ece.cov19.SplashActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
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
import static com.ece.cov19.SplashActivity.progressBar;
import static com.ece.cov19.SplashActivity.tryAgain;

public class LoginUser {


    public static String checkLoginStat(){
        if(loggedInUserPhone.equals("Didn't Load") || loggedInUserName.equals("Didn't Load") || loggedInUserAge.equals("Didn't Load")
                || loggedInUserBloodGroup.equals("Didn't Load") || loggedInUserDistrict.equals("Didn't Load") || loggedInUserDivision.equals("Didn't Load")
                || loggedInUserGender.equals("Didn't Load") || loggedInUserDonorInfo.equals("Didn't Load")||loggedInUserThana.equals("Didn't Load")
        ){
            return "failed";
        }
        return "success";
    }
    public static void loginUser(Context ctx, String phone, String password,Class<?> toActivity){


        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<UserDataModel> sendingData = retroInterface.loginRetroMethod(phone, password);
        sendingData.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                if (response.body().getServerMsg().toLowerCase().equals("success")) {
                    ToastCreator.toastCreatorGreen(ctx, ctx.getResources().getString(R.string.welcome)+" " + response.body().getName());

//                  Storing phone and password to shared preferences
                    SharedPreferences loginSharedPrefs = ctx.getSharedPreferences(LOGIN_SHARED_PREFS, MODE_PRIVATE);
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
                    if(ctx.getClass()!=toActivity) {
                        Intent intent = new Intent(ctx, toActivity);


//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ctx.startActivity(intent);
                    }

                }
                else if(response.body().getServerMsg().toLowerCase().equals("wrong phone or password")){
                    if(ctx.getClass()== SplashActivity.class){
                        Intent intent=new Intent(ctx,LoginActivity.class);
                        ctx.startActivity(intent);
                        ((SplashActivity) ctx).finishAffinity();
                    }
                    else {
                        ToastCreator.toastCreatorRed(ctx, ctx.getResources().getString(R.string.login_activity_wrong));
                    }
                }

                else {

                    ToastCreator.toastCreatorRed(ctx,ctx.getResources().getString(R.string.connection_error));
                    progressBar.setVisibility(View.GONE);
                    tryAgain.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {

                ToastCreator.toastCreatorRed(ctx,ctx.getResources().getString(R.string.connection_error));
                progressBar.setVisibility(View.GONE);
                tryAgain.setVisibility(View.VISIBLE);
            }
        });


    }

}
