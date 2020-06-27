package com.ece.cov19;

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

import com.ece.cov19.DataModels.RequestDataModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

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

    private TextView nameTextView, phoneTextView, bloodGroupTextView, addressTextView, ageTextView, donorInfoTextView, sendRequestSuggestion;
    private ImageView genderImageView;
    private Button askForHelpBtn, acceptBtn, declineBtn;
    private ImageView backbtn;
    String name, age, bloodGroup, donorphone, donorInfo, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donor_profile);


        nameTextView = findViewById(R.id.donor_profile_name);
        phoneTextView = findViewById(R.id.donor_profile_phone);
        bloodGroupTextView = findViewById(R.id.donor_profile_blood_group);
        addressTextView = findViewById(R.id.donor_profile_hospital);
        ageTextView = findViewById(R.id.donor_profile_age);
        donorInfoTextView = findViewById(R.id.donor_profile_type);
        genderImageView = findViewById(R.id.donor_profile_gender_icon);
        askForHelpBtn = findViewById(R.id.donor_profile_ask_for_help_button);
        acceptBtn = findViewById(R.id.donor_profile_accept_button);
        declineBtn = findViewById(R.id.donor_profile_decline_button);
        backbtn = findViewById(R.id.donor_profile_back_button);
        sendRequestSuggestion = findViewById(R.id.donor_profile_send_request_suggestion);


        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        bloodGroup = intent.getStringExtra("blood");
        donorphone = intent.getStringExtra("phone");
        donorInfo = intent.getStringExtra("donorinfo");
        address = intent.getStringExtra("address");

        nameTextView.setText(name);
        if (donorphone.equals(loggedInUserPhone)) {
            phoneTextView.setText(donorphone);

        } else {
            phoneTextView.setText("Not Permitted!");
        }
        bloodGroupTextView.setText(bloodGroup);
        addressTextView.setText(address);
        ageTextView.setText(age);
        donorInfoTextView.setText(donorInfo);

        if (findPatientName.equals("") && findPatientAge.equals("") && findPatientPhone.equals("") && findPatientBloodGroup.equals("any")) {
            askForHelpBtn.setVisibility(View.GONE);
            sendRequestSuggestion.setVisibility(View.VISIBLE);

        }
        else{
            requestsOperation("getStatus");
        }

        if (intent.getStringExtra("gender").equals("male")) {
            genderImageView.setImageResource(R.drawable.profile_icon_male);
        } else {
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
                if(askForHelpBtn.getText().toString().toLowerCase().equals("ask for help")) {
                    passWordAlertDialog();
                }


            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestsOperation("accept");

            }
        });

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestsOperation("decline");

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
        int paddingDp = (int) (12 * density);
        pass.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        pass.setHint("******");
        pass.setBackgroundResource(R.drawable.edit_text_dark);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(pass);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (pass.getText().toString().equals(loggedInUserPass)) {

                    sendRequest();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                    ;
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

    private void sendRequest() {

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<RequestDataModel> requestFromPatient = retroInterface.sendRequest(donorphone, findPatientName, findPatientAge, findPatientPhone, findPatientBloodGroup, "patient");
        requestFromPatient.enqueue(new Callback<RequestDataModel>() {
            @Override
            public void onResponse(Call<RequestDataModel> call, Response<RequestDataModel> response) {
                if (response.body().getServerMsg().equals("Success")) {
                    Toast.makeText(ViewDonorProfileActivity.this, "Request Sent! Wait For donor's Response", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(ViewDonorProfileActivity.this, response.body().getServerMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestDataModel> call, Throwable t) {
                Toast.makeText(ViewDonorProfileActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void requestsOperation(String operation) {
        RetroInterface retroInterface = RetroInstance.getRetro();
        Toast.makeText(this, donorphone + findPatientName + findPatientAge + findPatientBloodGroup + findPatientPhone, Toast.LENGTH_SHORT).show();
        Call<RequestDataModel> lookforRequestFromPatient = retroInterface.requestsOperation(donorphone, findPatientName, findPatientAge, findPatientBloodGroup, findPatientPhone, "donor",operation);
        lookforRequestFromPatient.enqueue(new Callback<RequestDataModel>() {
            @Override
            public void onResponse(Call<RequestDataModel> call, Response<RequestDataModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ViewDonorProfileActivity.this, response.body().getServerMsg(), Toast.LENGTH_SHORT).show();
                    if(response.body().getServerMsg().equals("no requests")) {
                        if(donorphone.equals(loggedInUserPhone)){
                            askForHelpBtn.setVisibility(View.GONE);
                            acceptBtn.setVisibility(View.GONE);
                            declineBtn.setVisibility(View.GONE);
                        } else {
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText("Ask for Help");
                            acceptBtn.setVisibility(View.GONE);
                            declineBtn.setVisibility(View.GONE);
                        }
                    }
                    else if (response.body().getServerMsg().equals("Pending")) {
                        askForHelpBtn.setVisibility(View.GONE);
                        acceptBtn.setVisibility(View.VISIBLE);
                        acceptBtn.setText("Accept Request");
                        declineBtn.setVisibility(View.VISIBLE);
                        declineBtn.setText("Decline Request");

                    }
                    else if (response.body().getServerMsg().equals("Accepted")) {
//                        show accepted textView
                        askForHelpBtn.setVisibility(View.VISIBLE);
                        askForHelpBtn.setText("Accepted");
                        acceptBtn.setVisibility(View.GONE);
                        declineBtn.setVisibility(View.GONE);
                        phoneTextView.setText(donorphone);

                    }
                    else if (response.body().getServerMsg().equals("Declined")) {
//                        show accepted textView
                            askForHelpBtn.setVisibility(View.VISIBLE);
                            askForHelpBtn.setText("Declined");
                            acceptBtn.setVisibility(View.GONE);
                            declineBtn.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onFailure(Call<RequestDataModel> call, Throwable t) {
                Toast.makeText(ViewDonorProfileActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
