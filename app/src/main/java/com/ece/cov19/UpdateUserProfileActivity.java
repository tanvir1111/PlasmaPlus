package com.ece.cov19;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.ToastCreator;
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

public class UpdateUserProfileActivity extends AppCompatActivity {

    private EditText nameEditText, ageEditText;
    private RadioGroup bloodRadioGroup;
    private Spinner divisionSpinner, districtSpinner,thanaSpinner;
    private String donorInfo;
    private ImageView backbtn;
    private boolean dischanged=false;
    private int divisionResourceIds[] = {R.array.Dhaka, R.array.Rajshahi, R.array.Rangpur, R.array.Khulna, R.array.Chittagong, R.array.Mymensingh,
            R.array.Barisal, R.array.Sylhet};
    private Button updateBtn;
    boolean eligible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);
        nameEditText = findViewById(R.id.update_name_edittext);
        thanaSpinner = findViewById(R.id.update_thana_spinner);
        ageEditText = findViewById(R.id.update_age_edittext);

        divisionSpinner = findViewById(R.id.update_division_spinner);
        districtSpinner = findViewById(R.id.update_district_spinner);
        bloodRadioGroup = findViewById(R.id.update_donor_role_radio_group);
        backbtn=findViewById(R.id.update_back_button);
        updateBtn=findViewById(R.id.update_update_btn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//     setting up current data
        nameEditText.setText(loggedInUserName);

        ageEditText.setText(loggedInUserAge);
        ArrayAdapter arrayAdapter = (ArrayAdapter) divisionSpinner.getAdapter();

        divisionSpinner.setSelection(arrayAdapter.getPosition(loggedInUserDivision));


//       setting donor info radio button
        int index;
        if (loggedInUserDonorInfo.toLowerCase().equals("blood")) {
            index = 0;
        } else if (loggedInUserDonorInfo.toLowerCase().equals("plasma")) {
            index = 1;
        }
        else if(loggedInUserDonorInfo.toLowerCase().equals("blood and plasma")){
            index=2;
        }

        else {
            index = 3;
        }
        RadioButton bld = (RadioButton) bloodRadioGroup.getChildAt(index);
        bld.setChecked(true);
        donorInfo=bld.getText().toString();

        bloodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                if(bld.getId()==R.id.update_donor_role_radio_group_none|| bld.getId()==R.id.update_donor_role_radio_group_blood){
                    RadioButton radioButton = findViewById(checkedID);
                    if(checkedID==R.id.update_donor_role_radio_group_plasma|| checkedID==R.id.update_donor_role_radio_group_blood_and_plasma) {

                        if (radioButton.isChecked()) {

                            final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserProfileActivity.this);
                            builder.setCancelable(false);

                            LayoutInflater inflater = LayoutInflater.from(UpdateUserProfileActivity.this);
                            View plasmaDialog = inflater.inflate(R.layout.plasma_dialog_new, null);
                            TextView ok = plasmaDialog.findViewById(R.id.plasma_dialog_ok_textView);
                            TextView cancel = plasmaDialog.findViewById(R.id.plasma_dialog_cancel_textView);
                            CheckBox confirmCheckBox = plasmaDialog.findViewById(R.id.plasma_dialog_checkbox);
                            TextView header = plasmaDialog.findViewById(R.id.plasma_dialog_confirm_header);
                            TextView eligibility = plasmaDialog.findViewById(R.id.plasma_dialog_confirm_eligibility_text);
                            TextView confirm_ok = plasmaDialog.findViewById(R.id.plasma_dialog_confirm_ok_textView);
                            TextView q1Txt=plasmaDialog.findViewById(R.id.plasma_dialog_q1);
                            TextView q2Txt=plasmaDialog.findViewById(R.id.plasma_dialog_q2);
                            TextView q3Txt=plasmaDialog.findViewById(R.id.plasma_dialog_q3);
                            TextView q4Txt=plasmaDialog.findViewById(R.id.plasma_dialog_q4);



                            RadioGroup q1=plasmaDialog.findViewById(R.id.q1);
                            RadioGroup q2=plasmaDialog.findViewById(R.id.q2);
                            RadioGroup q3=plasmaDialog.findViewById(R.id.q3);
                            RadioGroup q4=plasmaDialog.findViewById(R.id.q4);

                            RadioButton q1_yes = plasmaDialog.findViewById(R.id.q1_yes);
                            RadioButton q1_no = plasmaDialog.findViewById(R.id.q1_no);
                            RadioButton q2_less = plasmaDialog.findViewById(R.id.q2_less);
                            RadioButton q2_greater = plasmaDialog.findViewById(R.id.q2_greater);
                            RadioButton q3_less = plasmaDialog.findViewById(R.id.q3_less);
                            RadioButton q3_greater = plasmaDialog.findViewById(R.id.q3_greater);
                            RadioButton q4_yes = plasmaDialog.findViewById(R.id.q4_yes);
                            RadioButton q4_no = plasmaDialog.findViewById(R.id.q4_no);

                            ScrollView scrollView = plasmaDialog.findViewById(R.id.plasma_dialog_scrollView);

                            header.setVisibility(View.GONE);
                            eligibility.setVisibility(View.GONE);
                            confirm_ok.setVisibility(View.GONE);

                            builder.setCancelable(false);
                            builder.setView(plasmaDialog);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            ok.setEnabled(false);
                            ok.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled));
                            confirmCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                    if (isChecked) {
                                        if(q1.getCheckedRadioButtonId()!=-1 && q2.getCheckedRadioButtonId()!=-1 &&
                                                q3.getCheckedRadioButtonId()!=-1 && q4.getCheckedRadioButtonId()!=-1) {
                                            ok.setEnabled(true);

                                            ok.setTextColor(getResources().getColor(R.color.colorAccent));
                                        }
                                        else{


                                            if(q4.getCheckedRadioButtonId()==-1){
                                                q4Txt.setError(getString(R.string.answer_this_question_error));
                                                q4Txt.requestFocus();
                                            }

                                            if (q3.getCheckedRadioButtonId()==-1){
                                                q3Txt.setError(getString(R.string.answer_this_question_error));
                                                q3Txt.requestFocus();

                                            }

                                            if(q2.getCheckedRadioButtonId()==-1){
                                                q2Txt.setError(getString(R.string.answer_this_question_error));
                                                q2Txt.requestFocus();

                                            }

                                            if(q1.getCheckedRadioButtonId()==-1){
                                                q1Txt.setError(getString(R.string.answer_this_question_error));
                                                q1Txt.requestFocus();
                                            }


                                            confirmCheckBox.toggle();
                                        }
                                    } else {
                                        ok.setEnabled(false);

                                        ok.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled));
                                    }

                                }
                            });

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    scrollView.setVisibility(View.GONE);
                                    header.setVisibility(View.VISIBLE);
                                    eligibility.setVisibility(View.VISIBLE);
                                    confirm_ok.setVisibility(View.VISIBLE);

                                    if(q1_yes.isChecked() && q2_greater.isChecked() && q3_greater.isChecked() && q4_no.isChecked()){
                                        eligible = true;
                                        eligibility.setText(getResources().getString(R.string.plasma_eligible));
                                    }
                                }

                            });

                            confirm_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(eligible == true){
                                        radioButton.setChecked(true);
                                        donorInfo=radioButton.getText().toString();
                                    }
                                    else{

                                        radioGroup.check(bld.getId());
                                    }
                                    alertDialog.dismiss();
                                }
                            });

                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    radioGroup.check(bld.getId());
                                    alertDialog.cancel();

                                }
                            });
                        }
                    }
                }
            }
        });


        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        UpdateUserProfileActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(divisionResourceIds[position]));
                        districtSpinner.setAdapter(adapter);

                    districtSpinner.setSelection(adapter.getPosition(loggedInUserDistrict));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                int i= getResources().getIdentifier(districtSpinner.getSelectedItem().toString()+"_t","array",getPackageName());
                ArrayAdapter<String> adpter = new ArrayAdapter<String>(
                        UpdateUserProfileActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(i));
                thanaSpinner.setAdapter(adpter);
                thanaSpinner.setSelection(adpter.getPosition(loggedInUserThana));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
// all current data set
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBtn.setEnabled(false);
                updateAlertDialog();
            }
        });



    }

    private void verifyData() {
        String name,  division, district, thana, age, emptyfield="";
        Boolean emptyfieldChecker=true;
        name = nameEditText.getText().toString();
        thana = thanaSpinner.getSelectedItem().toString();
        age = ageEditText.getText().toString();

        division = divisionSpinner.getSelectedItem().toString();
        district = districtSpinner.getSelectedItem().toString();
        RadioButton selectedRadiobtn= findViewById(bloodRadioGroup.getCheckedRadioButtonId());
        switch (selectedRadiobtn.getText().toString()){
            case "রক্ত":
                donorInfo = "Blood";
                break;
            case "প্লাজমা":
                donorInfo = "Plasma";
                break;
            case "রক্ত এবং প্লাজমা":
                donorInfo = "Blood and Plasma";
                break;
            case "সাধারণ ব্যবহারকারী/গ্রাহক":
            case "General User/Acceptor":
                donorInfo = "None";
                break;
            default:
                donorInfo = selectedRadiobtn.getText().toString();
                break;
        }



//        checking empty Fields

        if (name.isEmpty()) {
            emptyfieldChecker = false;
            emptyfield += getResources().getString(R.string.label_name) + " ";
            nameEditText.setError(getResources().getString(R.string.label_name)+" "+getResources().getString(R.string.is_required_txt));
            updateBtn.setEnabled(true);
        }

        if (age.isEmpty()) {
            emptyfieldChecker = false;
            emptyfield += getResources().getString(R.string.label_age_1) + " ";
            ageEditText.setError(getResources().getString(R.string.label_age_1)+" "+getResources().getString(R.string.is_required_txt));
            updateBtn.setEnabled(true);
        }
        if (division.isEmpty()) {
            emptyfieldChecker = false;
            emptyfield += getResources().getString(R.string.label_division) + " ";
            updateBtn.setEnabled(true);
        }
        if (district.isEmpty()) {
            emptyfieldChecker = false;
            emptyfield += getResources().getString(R.string.label_district) + " ";
            updateBtn.setEnabled(true);
        }


        if (emptyfieldChecker == true) {

//            retro operations
             updateUserInfo(name, division, district, thana, age,donorInfo);
        } else {
            emptyfield += getResources().getString(R.string.is_required_txt);
            updateBtn.setEnabled(true);
            ToastCreator.toastCreatorRed(this,emptyfield);
        }


    }

    private void updateUserInfo(final String name, final String division, final String district, final String thana,final String age, final String donorInfo) {
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<UserDataModel> sendingData = retroInterface.updateUser(loggedInUserPhone,name, division, district, thana, age, donorInfo);
        sendingData.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                updateBtn.setEnabled(true);
                if (response.body().getServerMsg().toLowerCase().equals("success")) {

                    ToastCreator.toastCreatorGreen(UpdateUserProfileActivity.this, getResources().getString(R.string.update_activity_update_successful));
//                    update logged in Data
                    loggedInUserName=name;
                    loggedInUserDivision=division;
                    loggedInUserDistrict=district;
                    loggedInUserThana=thana;
                    loggedInUserAge=age;
                    loggedInUserDonorInfo=donorInfo;



//              going to login activity
                    Intent intent = new Intent(UpdateUserProfileActivity.this, DashboardActivity.class);
                    finishAffinity();
                    startActivity(intent);
                }
                else {
                    ToastCreator.toastCreatorRed(UpdateUserProfileActivity.this,getResources().getString(R.string.failed_to_update_error));
                }

            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                updateBtn.setEnabled(true);
                ToastCreator.toastCreatorRed(UpdateUserProfileActivity.this,   getResources().getString(R.string.connection_error));
            }
        });



    }

    private void updateAlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.confirm_update_txt));
        builder.setCancelable(false);

        builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               verifyData();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                updateBtn.setEnabled(true);
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
}