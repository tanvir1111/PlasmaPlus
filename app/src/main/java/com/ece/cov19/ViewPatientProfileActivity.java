package com.ece.cov19;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ece.cov19.DataModels.PatientDataModel;
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
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserBloodGroup;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPass;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class ViewPatientProfileActivity extends AppCompatActivity {

    private TextView nameTextView, phoneTextView, bloodGroupTextView, hospitalTextView, ageTextView, needTextView,dateTextView;
    private ImageView genderImageView,backbtn;
    private ProgressBar progressBar;
    Button donateToHelpButton, updateButton, deleteButton;
    String name, age, gender, bloodGroup, hospital, division, district, date, need, phone,requestedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient_profile);

        nameTextView = findViewById(R.id.patient_profile_name);
        phoneTextView = findViewById(R.id.patient_profile_phone);
        bloodGroupTextView = findViewById(R.id.patient_profile_blood_group);
        hospitalTextView = findViewById(R.id.patient_profile_hospital);
        ageTextView = findViewById(R.id.patient_profile_age);
        needTextView = findViewById(R.id.patient_profile_need);
        genderImageView = findViewById(R.id.patient_profile_gender_icon);
        backbtn=findViewById(R.id.patient_profile_back_button);
        updateButton = findViewById(R.id.patient_profile_update_button);
        deleteButton = findViewById(R.id.patient_profile_delete_button);
        donateToHelpButton = findViewById(R.id.patient_profile_donate_button);
        dateTextView=findViewById(R.id.patient_profile_date);
        progressBar = findViewById(R.id.patient_profile_progressBar);


        Intent intent = getIntent();


        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        gender = intent.getStringExtra("gender");
        bloodGroup = intent.getStringExtra("blood_group");
        hospital = intent.getStringExtra("hospital");
        division = intent.getStringExtra("division");
        district = intent.getStringExtra("district");
        date = intent.getStringExtra("date");
        need = intent.getStringExtra("need");
        phone = intent.getStringExtra("phone");
        if(intent.hasExtra("activity")){
            if(intent.getStringExtra("activity").equals("DonorRequestsActivity")){
                requestedBy="patient";
            }
            else{
                requestedBy="donor";
            }
        }

        nameTextView.setText(name);
        if(phone.equals(loggedInUserPhone)){
            phoneTextView.setText(phone);

        }
        else {
            phoneTextView.setText("Not Permitted!");
        }
        bloodGroupTextView.setText(bloodGroup);
        hospitalTextView.setText(hospital);
        ageTextView.setText(age);
        needTextView.setText(need);
        dateTextView.setText(date);


        if(intent.getStringExtra("gender").equals("male")){
            genderImageView.setImageResource(R.drawable.profile_icon_male);
        }
        else {
            genderImageView.setImageResource(R.drawable.profile_icon_female);

        }


        requestsOperation("getStatus");

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();

                if(intent.getStringExtra("activity").equals("PatientRequestsActivity")){
                    Intent goBackIntent = new Intent(ViewPatientProfileActivity.this,PatientRequestsActivity.class);
                    startActivity(goBackIntent);
                    finish();

                } else if(intent.getStringExtra("activity").equals("ExplorePatientsActivity")){
                    Intent goBackIntent = new Intent(ViewPatientProfileActivity.this,ExplorePatientsActivity.class);
                    startActivity(goBackIntent);
                    finish();
                } else if(intent.getStringExtra("activity").equals("PatientResponseActivity")) {
                    Intent goBackIntent = new Intent(ViewPatientProfileActivity.this, PatientResponseActivity.class);
                    startActivity(goBackIntent);
                    finish();
                }
            }
        });

        donateToHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(donateToHelpButton.getText().toString().toLowerCase().equals("donate to help")) {
                    passWordAlertDialog();
                }
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateButton.getText().toString().toLowerCase().equals("update profile")) {
                    Intent intent = new Intent(ViewPatientProfileActivity.this, UpdatePatientProfileActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("age", age);
                    intent.putExtra("gender", gender);
                    intent.putExtra("blood_group", bloodGroup);
                    intent.putExtra("hospital", hospital);
                    intent.putExtra("division", division);
                    intent.putExtra("district", district);
                    intent.putExtra("date", date);
                    intent.putExtra("need", need);
                    intent.putExtra("phone", phone);
                    updateAlertDialog(intent);
                }
                else if(updateButton.getText().toString().toLowerCase().equals("accept request")){
                    Toast.makeText(ViewPatientProfileActivity.this, "accept ops", Toast.LENGTH_SHORT).show();
                    requestsOperation("accept");
                }
                else if(updateButton.getText().toString().toLowerCase().equals("call patient")){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phone));
                    startActivity(intent);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteButton.getText().toString().toLowerCase().equals("delete profile")) {
                    Intent intent = new Intent(ViewPatientProfileActivity.this, ExplorePatientsActivity.class);
                    deleteAlertDialog(intent);
                }
                else if(deleteButton.getText().toString().toLowerCase().equals("decline request")){
                    Toast.makeText(ViewPatientProfileActivity.this, "decline ops", Toast.LENGTH_SHORT).show();
                    requestsOperation("decline");
                }
                else if(deleteButton.getText().toString().toLowerCase().equals("send sms")){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("smsto:"));
                    intent.putExtra("address", phone);
                    intent.putExtra("sms_body","Hello! I would like to contact with you.");
                    startActivity(intent);
                }
            }

        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = getIntent();

        if(intent.getStringExtra("activity").equals("PatientRequestsActivity")){
            Intent goBackIntent = new Intent(ViewPatientProfileActivity.this,PatientRequestsActivity.class);
            startActivity(goBackIntent);
            finish();

        } else if(intent.getStringExtra("activity").equals("ExplorePatientsActivity")){
            Intent goBackIntent = new Intent(ViewPatientProfileActivity.this,ExplorePatientsActivity.class);
            startActivity(goBackIntent);
            finish();
        } else if(intent.getStringExtra("activity").equals("PatientResponseActivity")) {
            Intent goBackIntent = new Intent(ViewPatientProfileActivity.this, PatientResponseActivity.class);
            startActivity(goBackIntent);
            finish();
        }
    }

    private void updateAlertDialog(final Intent intent) {

//                asking password with alertdialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientProfileActivity.this);
        builder.setMessage("Enter Password");

// Set up the input
        final EditText pass = new EditText(ViewPatientProfileActivity.this);

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

                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(ViewPatientProfileActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();;
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


    private void deleteAlertDialog(final Intent intent) {

//                asking password with alertdialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientProfileActivity.this);
        builder.setMessage("Enter Password");

// Set up the input
        final EditText pass = new EditText(ViewPatientProfileActivity.this);

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientProfileActivity.this);
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            progressBar.setVisibility(View.VISIBLE);
                            RetroInterface retroInterface = RetroInstance.getRetro();
                            Call<PatientDataModel> deletePatientRecord = retroInterface.deletePatientProfile(name, age, gender, bloodGroup, hospital, division, district, date, need, phone);
                            deletePatientRecord.enqueue(new Callback<PatientDataModel>() {
                                @Override
                                public void onResponse(Call<PatientDataModel> call, Response<PatientDataModel> response) {
                                    progressBar.setVisibility(View.GONE);
                                    if(response.body().getServerMsg().equals("Success")){
                                        Toast.makeText(ViewPatientProfileActivity.this, "Patient Record Deleted", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(ViewPatientProfileActivity.this, "Delete Failed!", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<PatientDataModel> call, Throwable t) {
                                    Toast.makeText(ViewPatientProfileActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    })
                            .setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();


                }
                else{
                    Toast.makeText(ViewPatientProfileActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();;
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

    private void passWordAlertDialog() {

//                asking password with alertdialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewPatientProfileActivity.this);
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
        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<RequestDataModel> requestFromDonor = retroInterface.sendRequest(loggedInUserPhone, findPatientName, findPatientAge, findPatientPhone, findPatientBloodGroup, "donor");
        requestFromDonor.enqueue(new Callback<RequestDataModel>() {
            @Override
            public void onResponse(Call<RequestDataModel> call, Response<RequestDataModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().getServerMsg().equals("Success")) {
                    Toast.makeText(ViewPatientProfileActivity.this, "Request Sent! Wait For Patient's Response", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(ViewPatientProfileActivity.this, response.body().getServerMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestDataModel> call, Throwable t) {
                Toast.makeText(ViewPatientProfileActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void requestsOperation(String operation) {
        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();
        //Toast.makeText(this, loggedInUserPhone+name +age +bloodGroup +phone, Toast.LENGTH_SHORT).show();
        Call<RequestDataModel> lookforRequestFromDonor = retroInterface.requestsOperation(loggedInUserPhone, name, age, bloodGroup,phone,requestedBy,operation);
        lookforRequestFromDonor.enqueue(new Callback<RequestDataModel>() {
            @Override
            public void onResponse(Call<RequestDataModel> call, Response<RequestDataModel> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    if(response.body().getServerMsg().equals("no requests")){
                        if(phone.equals(loggedInUserPhone)){
                            donateToHelpButton.setVisibility(View.GONE);
                            updateButton.setVisibility(View.VISIBLE);
                            updateButton.setText("Update Profile");
                            deleteButton.setVisibility(View.VISIBLE);
                            deleteButton.setText("Delete Profile");
                        } else if(bloodGroup.equals(loggedInUserBloodGroup)) {
                            donateToHelpButton.setVisibility(View.VISIBLE);
                            donateToHelpButton.setText("Donate to Help");
                            updateButton.setVisibility(View.GONE);
                            deleteButton.setVisibility(View.GONE);
                        }
                        else {
                            donateToHelpButton.setVisibility(View.GONE);
                            updateButton.setVisibility(View.GONE);
                            deleteButton.setVisibility(View.GONE);
                        }
                    }
                    else if(response.body().getServerMsg().equals("Pending")){
                        if(getIntent().getStringExtra("activity").equals("PatientResponseActivity")){

                            donateToHelpButton.setVisibility(View.VISIBLE);
                            donateToHelpButton.setText("Pending");
                            updateButton.setVisibility(View.GONE);
                            deleteButton.setVisibility(View.GONE);
                        } else {
                            donateToHelpButton.setVisibility(View.GONE);
                            updateButton.setVisibility(View.VISIBLE);
                            updateButton.setText("Accept Request");
                            deleteButton.setVisibility(View.VISIBLE);
                            deleteButton.setText("Decline Request");
                        }


                    }
                    else if(response.body().getServerMsg().equals("Accepted")){
                        donateToHelpButton.setVisibility(View.GONE);
                        updateButton.setVisibility(View.VISIBLE);
                        updateButton.setText("Call Patient");
                        deleteButton.setVisibility(View.VISIBLE);
                        deleteButton.setText("Send SMS");
                        phoneTextView.setText(phone);

                    }
                    else if(response.body().getServerMsg().equals("Declined")){
                        donateToHelpButton.setVisibility(View.VISIBLE);
                        donateToHelpButton.setText("Declined");
                        updateButton.setVisibility(View.GONE);
                        deleteButton.setVisibility(View.GONE);

                    }


                }

            }

            @Override
            public void onFailure(Call<RequestDataModel> call, Throwable t) {
                Toast.makeText(ViewPatientProfileActivity.this, t.toString(), Toast.LENGTH_SHORT).show();

            }
        });





    }





}
