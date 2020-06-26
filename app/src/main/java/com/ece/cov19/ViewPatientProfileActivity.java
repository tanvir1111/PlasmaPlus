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

import com.ece.cov19.DataModels.LoggedInUserData;
import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPass;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class ViewPatientProfileActivity extends AppCompatActivity {

    private TextView nameTextView, phoneTextView, bloodGroupTextView, hospitalTextView, ageTextView, needTextView;
    private ImageView genderImageView,backbtn;
    Button updateButton, deleteButton;
    String name, age, gender, bloodGroup, hospital, division, district, date, need, phone;

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


        if(intent.getStringExtra("gender").equals("male")){
            genderImageView.setImageResource(R.drawable.profile_icon_male);
        }
        else {
            genderImageView.setImageResource(R.drawable.profile_icon_female);

        }

        if(intent.getStringExtra("phone").equals(LoggedInUserData.loggedInUserPhone)){
            updateButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewPatientProfileActivity.this, UpdatePatientProfileActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("age",age);
                intent.putExtra("gender",gender);
                intent.putExtra("blood_group",bloodGroup);
                intent.putExtra("hospital",hospital);
                intent.putExtra("division",division);
                intent.putExtra("district",district);
                intent.putExtra("date",date);
                intent.putExtra("need",need);
                intent.putExtra("phone",phone);
                updateAlertDialog(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewPatientProfileActivity.this, ExplorePatientsActivity.class);
                deleteAlertDialog(intent);
            }
        });


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
                            RetroInterface retroInterface = RetroInstance.getRetro();
                            Call<PatientDataModel> deletePatientRecord = retroInterface.deletePatientProfile(name, age, gender, bloodGroup, hospital, division, district, date, need, phone);
                            deletePatientRecord.enqueue(new Callback<PatientDataModel>() {
                                @Override
                                public void onResponse(Call<PatientDataModel> call, Response<PatientDataModel> response) {
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
    
}
