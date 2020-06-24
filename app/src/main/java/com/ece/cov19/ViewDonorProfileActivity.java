package com.ece.cov19;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.DataModels.RequestDataModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.FindPatientData.findPatientAge;
import static com.ece.cov19.DataModels.FindPatientData.findPatientBloodGroup;
import static com.ece.cov19.DataModels.FindPatientData.findPatientName;
import static com.ece.cov19.DataModels.FindPatientData.findPatientPhone;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPass;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class ViewDonorProfileActivity extends AppCompatActivity {

    private TextView nameTextView, phoneTextView, bloodGroupTextView, addressTextView, ageTextView, donorInfoTextView;
    private ImageView genderImageView;
    private Button askForHelpBtn;
    private ImageView backbtn;
    String donorphone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donor_profile);
        Intent intent = getIntent();

        donorphone=intent.getStringExtra("phone");



        nameTextView = findViewById(R.id.donor_profile_name);
        phoneTextView = findViewById(R.id.donor_profile_phone);
        bloodGroupTextView = findViewById(R.id.donor_profile_blood_group);
        addressTextView = findViewById(R.id.donor_profile_hospital);
        ageTextView = findViewById(R.id.donor_profile_age);
        donorInfoTextView = findViewById(R.id.donor_profile_type);
        genderImageView = findViewById(R.id.donor_profile_gender_icon);
        askForHelpBtn =findViewById(R.id.donor_ask_for_help_button);
        backbtn=findViewById(R.id.donor_profile_back_button);




        nameTextView.setText(intent.getStringExtra("name"));
        phoneTextView.setText(donorphone);
        bloodGroupTextView.setText(intent.getStringExtra("blood"));
        addressTextView.setText(intent.getStringExtra("address"));
        ageTextView.setText(intent.getStringExtra("age"));
        donorInfoTextView.setText(intent.getStringExtra("donorinfo"));
        if(intent.getStringExtra("gender").equals("male")){
            genderImageView.setImageResource(R.drawable.profile_icon_male);
        }
        else {
            genderImageView.setImageResource(R.drawable.profile_icon_female);

        }
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        askForHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passWordAlertDialog();




            }
        });

    }

    private void passWordAlertDialog() {

//                asking password with alertdialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewDonorProfileActivity.this);
        builder.setMessage("Enter Password");

// Set up the input
        final EditText pass = new EditText(getApplicationContext());

        float density = getResources().getDisplayMetrics().density;
        int paddingDp = (int)(12* density);
        pass.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);
        pass.setHint("******");
        pass.setBackgroundResource(R.drawable.edit_text_dark);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(pass);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(pass.getText().toString().equals(loggedInUserPass)){

                    sendRequest();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();;
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void sendRequest(){

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<RequestDataModel> requestFromPatient = retroInterface.sendRequest(donorphone,findPatientName,findPatientAge,findPatientPhone,findPatientBloodGroup,"patient");
        Toast.makeText(this, donorphone+findPatientName+findPatientAge+findPatientPhone+findPatientBloodGroup, Toast.LENGTH_SHORT).show();
        requestFromPatient.enqueue(new Callback<RequestDataModel>() {
            @Override
            public void onResponse(Call<RequestDataModel> call, Response<RequestDataModel> response) {
                if(response.body().getServerMsg().equals("Success")){
                    Toast.makeText(ViewDonorProfileActivity.this, "Request Sent! Wait For dono's Response", Toast.LENGTH_SHORT).show();



                }
                else {
                    Toast.makeText(ViewDonorProfileActivity.this, response.body().getServerMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestDataModel> call, Throwable t) {
                Toast.makeText(ViewDonorProfileActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });



    }

}
