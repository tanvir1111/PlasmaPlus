package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserAge;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDistrict;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDivision;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserDonorInfo;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserName;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserThana;

public class UpdateInfoActivity extends AppCompatActivity {

    private EditText nameEditText, thanaEditText, ageEditText;
    private RadioGroup bloodRadioGroup;
    private Spinner divisionSpinner, districtSpinner;
    private String donorInfo;
    private boolean dischanged=false;
    private int divisionResourceIds[] = {R.array.Dhaka, R.array.Rajshahi, R.array.Rangpur, R.array.Khulna, R.array.Chittagong, R.array.Mymensingh,
            R.array.Barisal, R.array.Sylhet};
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        nameEditText = findViewById(R.id.update_name_edittext);
        thanaEditText = findViewById(R.id.update_thana_edittext);
        ageEditText = findViewById(R.id.update_age_edittext);

        divisionSpinner = findViewById(R.id.update_division_spinner);
        districtSpinner = findViewById(R.id.update_district_spinner);
        bloodRadioGroup = findViewById(R.id.update_donor_role_radio_group);
        updateBtn=findViewById(R.id.update_update_btn);

//     setting up current data
        nameEditText.setText(loggedInUserName);
        thanaEditText.setText(loggedInUserThana);
        ageEditText.setText(loggedInUserAge);
        ArrayAdapter arrayAdapter = (ArrayAdapter) divisionSpinner.getAdapter();

        divisionSpinner.setSelection(arrayAdapter.getPosition(loggedInUserDivision));


//       setting donor info radio button
        int index;
        if (loggedInUserDonorInfo.equals("Blood")) {
            index = 0;
        } else if (loggedInUserDonorInfo.equals("Plasma")) {
            index = 1;
        }

        else {
            index = 2;
        }
        RadioButton bld = (RadioButton) bloodRadioGroup.getChildAt(index);
        bld.setChecked(true);
        donorInfo=bld.getText().toString();

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adpter = new ArrayAdapter<String>(
                        UpdateInfoActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(divisionResourceIds[position]));
                districtSpinner.setAdapter(adpter);

                if(!dischanged) {
                    districtSpinner.setSelection(adpter.getPosition(loggedInUserDistrict));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

// all current data set
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyData();
            }
        });



    }

    private void verifyData() {
        String name,  division, district, thana, age,  emptyfield = "all ok";
        name = nameEditText.getText().toString();
        thana = thanaEditText.getText().toString();
        age = ageEditText.getText().toString();

        division = divisionSpinner.getSelectedItem().toString();
        district = districtSpinner.getSelectedItem().toString();
        RadioButton selectedRadiobtn= findViewById(bloodRadioGroup.getCheckedRadioButtonId());
        donorInfo=selectedRadiobtn.getText().toString();
        emptyfield="all ok";

        if(donorInfo.equals("none")){
            donorInfo="na";
        }


//        checking empty Fields

        if (name.isEmpty()) {
            emptyfield = "name";
        } else if (thana.isEmpty()) {
            emptyfield = "thana";
        } else if (age.isEmpty()) {
            emptyfield = "age";
        } else if (division.isEmpty()) {
            emptyfield = "division";
        } else if (district.isEmpty()) {
            emptyfield = "district";
        }


        if (emptyfield.equals("all ok")) {

//            retro operations
             updateUserInfo(name, division, district, thana, age,donorInfo);
        } else {
            Toast.makeText(this, "Enter " + emptyfield, Toast.LENGTH_SHORT).show();
        }


    }

    private void updateUserInfo(final String name, final String division, final String district, final String thana,final String age, final String donorInfo) {
        RetroInterface retroinstance = RetroInstance.getRetro();
        Call<UserDataModel> sendingData = retroinstance.updateUser(loggedInUserPhone,name, division, district, thana, age, donorInfo);
        sendingData.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                if (response.body().getServerMsg().equals("Success")) {
                    Toast.makeText(UpdateInfoActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
//                    update logged in Data
                    loggedInUserName=name;
                    loggedInUserDivision=division;
                    loggedInUserDistrict=district;
                    loggedInUserThana=thana;
                    loggedInUserAge=age;
                    loggedInUserDonorInfo=donorInfo;



//              going to login activity
                    Intent intent = new Intent(UpdateInfoActivity.this, DashBoardActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(UpdateInfoActivity.this, response.body().getServerMsg(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                Toast.makeText(UpdateInfoActivity.this, "failed to update! Check your connection and try again.", Toast.LENGTH_SHORT).show();
            }
        });



    }
}