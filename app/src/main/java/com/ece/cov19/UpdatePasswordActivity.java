package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;
import static com.ece.cov19.LoginActivity.LOGIN_SHARED_PREFS;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText passwordEditText,confPasswordEditText;
    private String password,phone;
    private Button updatePassBtn;
    private ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        passwordEditText=findViewById(R.id.update_password_password_edittext);
        confPasswordEditText=findViewById(R.id.update_password_confirm_password_edittext);
        updatePassBtn=findViewById(R.id.update_password_update_btn);
        backbtn=findViewById(R.id.update_password_back_button);


        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        updatePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassBtn.setEnabled(false);
                password=passwordEditText.getText().toString();

                if (password.length() < 6) {

                    ToastCreator.toastCreatorRed(UpdatePasswordActivity.this,getResources().getString(R.string.reg_activity_password_length));
                    updatePassBtn.setEnabled(true);
                }
                else {
                    if(password.equals(confPasswordEditText.getText().toString())){
                        updatePassword(password);
                    }
                    else {

                        ToastCreator.toastCreatorRed(UpdatePasswordActivity.this,getResources().getString(R.string.reg_activity_password_no_match));
                        updatePassBtn.setEnabled(true);
                    }
                }
            }
        });

    }

    private void updatePassword(String password) {

        RetroInterface retroInterface= RetroInstance.getRetro();
        Call<UserDataModel> updatePass = retroInterface.updatePassword(phone, password);
        updatePass.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                updatePassBtn.setEnabled(true);
                String serverMsg = response.body().getServerMsg();
                if(serverMsg.toLowerCase().equals("success")){
                    ToastCreator.toastCreatorGreen(UpdatePasswordActivity.this,getResources().getString(R.string.update_password_update_success));

//                    clearing Shared Prefs
                    SharedPreferences sharedPreferences=getSharedPreferences(LOGIN_SHARED_PREFS,MODE_PRIVATE);
                    sharedPreferences.edit().clear().apply();
//                    going to login Activity
                    Intent intent=new Intent(UpdatePasswordActivity.this,LoginActivity.class);
                    finishAffinity();
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(UpdatePasswordActivity.this,getResources().getString(R.string.update_password_update_failed));
                updatePassBtn.setEnabled(true);
            }
        });

    }
}