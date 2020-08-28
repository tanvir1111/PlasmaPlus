package com.ece.cov19;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.Functions.FormFieldsFeatures;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView aPositive, aNegative, bPositive, bNegative, oPositive, oNegative, abPositive, abNegative, selectedBldGrp;
    private TextView labelGender, labelBloodGroup;
    private EditText nameEditText, ageEditText,  passwordEditText, confPasswordEditText;
    private String gender = "not selected", donorInfo = "None";
    private ImageView genderMale, genderFemale, backbtn;
    private Button singUp;
    private Spinner divisionSpinner, districtSpinner,thanaSpinner;
    private RadioGroup donorRoleRadioGrp;
    private ProgressBar progressBar;
    FormFieldsFeatures formFieldsFeatures = new FormFieldsFeatures();
    public int divisionResourceIds[] = {R.array.Dhaka, R.array.Rajshahi, R.array.Rangpur, R.array.Khulna, R.array.Chittagong, R.array.Mymensingh,

            R.array.Barisal, R.array.Sylhet};

    boolean eligible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        donorRoleRadioGrp = findViewById(R.id.reg_donor_role_radio_group);


//        all editTexts
        nameEditText = findViewById(R.id.reg_name_edittext);
        ageEditText = findViewById(R.id.reg_age_edittext);

        passwordEditText = findViewById(R.id.reg_password_edittext);
        confPasswordEditText = findViewById(R.id.reg_confirm_password_edittext);
        progressBar = findViewById(R.id.reg_progress_bar);


//      buttons
        backbtn = findViewById(R.id.reg_back_button);
        singUp = findViewById(R.id.reg_sign_up_btn);

//        spinners
        divisionSpinner = findViewById(R.id.reg_division_spinner);
        districtSpinner = findViewById(R.id.reg_district_spinner);
        thanaSpinner = findViewById(R.id.reg_thana_spinner);

        /*blood group textviews*/
        labelBloodGroup = findViewById(R.id.reg_label_blood_grp);
        aPositive = findViewById(R.id.reg_bld_a_positive);
        bPositive = findViewById(R.id.reg_bld_b_positive);
        oPositive = findViewById(R.id.reg_bld_o_positive);
        abPositive = findViewById(R.id.reg_bld_ab_positive);
        aNegative = findViewById(R.id.reg_bld_a_negative);
        bNegative = findViewById(R.id.reg_bld_b_negative);
        oNegative = findViewById(R.id.reg_bld_o_negative);
        abNegative = findViewById(R.id.reg_bld_ab_negative);

        /*        Gender Imageviews*/
        labelGender = findViewById(R.id.reg_label_gender);
        genderMale = findViewById(R.id.reg_male_icon);
        genderFemale = findViewById(R.id.reg_female_icon);

        /* just a random one to avoid nullPoint Exception*/


//        all OnclickListeners
        aPositive.setOnClickListener(this);
        bPositive.setOnClickListener(this);
        oPositive.setOnClickListener(this);
        abPositive.setOnClickListener(this);
        aNegative.setOnClickListener(this);
        bNegative.setOnClickListener(this);
        oNegative.setOnClickListener(this);
        abNegative.setOnClickListener(this);
        genderMale.setOnClickListener(this);
        genderFemale.setOnClickListener(this);
        singUp.setOnClickListener(this);
        backbtn.setOnClickListener(this);


//      Districts spinner as per selected division

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adpter = new ArrayAdapter<String>(
                        RegistrationActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(divisionResourceIds[position]));
                districtSpinner.setAdapter(adpter);
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
                        RegistrationActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(i));
                thanaSpinner.setAdapter(adpter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
      //thana ops   getResources().getStringArray(i));


//        donorcheckbox setting donor info blood/plasma/na
        donorRoleRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                RadioButton radioButton = findViewById(checkedID);
                if (checkedID == R.id.reg_donor_role_radio_group_plasma || checkedID == R.id.reg_donor_role_radio_group_plasma_and_blood) {

                    if (radioButton.isChecked()) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setCancelable(false);

                        LayoutInflater inflater = LayoutInflater.from(RegistrationActivity.this);
                        View plasmaDialog = inflater.inflate(R.layout.plasma_dialog_new, null);
                        TextView ok = plasmaDialog.findViewById(R.id.plasma_dialog_ok_textView);
                        TextView cancel = plasmaDialog.findViewById(R.id.plasma_dialog_cancel_textView);
                        CheckBox confirmCheckBox = plasmaDialog.findViewById(R.id.plasma_dialog_checkbox);
                        TextView header = plasmaDialog.findViewById(R.id.plasma_dialog_confirm_header);
                        TextView eligibility = plasmaDialog.findViewById(R.id.plasma_dialog_confirm_eligibility_text);
                        TextView confirm_ok = plasmaDialog.findViewById(R.id.plasma_dialog_confirm_ok_textView);
                        TextView q1Txt = plasmaDialog.findViewById(R.id.plasma_dialog_q1);
                        TextView q2Txt = plasmaDialog.findViewById(R.id.plasma_dialog_q2);
                        TextView q3Txt = plasmaDialog.findViewById(R.id.plasma_dialog_q3);
                        TextView q4Txt = plasmaDialog.findViewById(R.id.plasma_dialog_q4);


                        RadioGroup q1 = plasmaDialog.findViewById(R.id.q1);
                        RadioGroup q2 = plasmaDialog.findViewById(R.id.q2);
                        RadioGroup q3 = plasmaDialog.findViewById(R.id.q3);
                        RadioGroup q4 = plasmaDialog.findViewById(R.id.q4);

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
                                    if (q1.getCheckedRadioButtonId() != -1 && q2.getCheckedRadioButtonId() != -1 &&
                                            q3.getCheckedRadioButtonId() != -1 && q4.getCheckedRadioButtonId() != -1) {
                                        ok.setEnabled(true);

                                        ok.setTextColor(getResources().getColor(R.color.colorAccent));
                                    } else {


                                        if (q4.getCheckedRadioButtonId() == -1) {
                                            q4Txt.setError(getString(R.string.answer_this_question_error));
                                            q4Txt.requestFocus();
                                        }

                                        if (q3.getCheckedRadioButtonId() == -1) {
                                            q3Txt.setError(getString(R.string.answer_this_question_error));
                                            q3Txt.requestFocus();

                                        }

                                        if (q2.getCheckedRadioButtonId() == -1) {
                                            q2Txt.setError(getString(R.string.answer_this_question_error));
                                            q2Txt.requestFocus();

                                        }

                                        if (q1.getCheckedRadioButtonId() == -1) {
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

                                if (q1_yes.isChecked() && q2_greater.isChecked() && q3_greater.isChecked() && q4_no.isChecked()) {
                                    eligible = true;
                                    eligibility.setText(getResources().getString(R.string.plasma_eligible));
                                }
                            }

                        });

                        confirm_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (eligible == true) {
                                    radioButton.setChecked(true);
                                    donorInfo = radioButton.getText().toString();
                                } else {

                                    radioGroup.clearCheck();
                                }
                                alertDialog.dismiss();
                            }
                        });


                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                radioGroup.clearCheck();

                                alertDialog.cancel();
                            }
                        });


                    }
                }


            }
        });


    }

    //  onclickListening
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_male_icon:
                genderFemale.setImageResource(R.drawable.female_icon);
                genderMale.setImageResource(R.drawable.male_icon_selected);
                gender = "male";
                break;
            case R.id.reg_female_icon:
                genderFemale.setImageResource(R.drawable.female_icon_selected);
                genderMale.setImageResource(R.drawable.male_icon);
                gender = "female";
                break;
            case R.id.reg_bld_a_positive:
            case R.id.reg_bld_b_positive:
            case R.id.reg_bld_o_positive:
            case R.id.reg_bld_ab_positive:
            case R.id.reg_bld_a_negative:
            case R.id.reg_bld_b_negative:
            case R.id.reg_bld_o_negative:
            case R.id.reg_bld_ab_negative:

                selectedBldGrp = formFieldsFeatures.bloodGroupSelection(this, (TextView) v, selectedBldGrp);

                break;

            case R.id.reg_back_button:
                finish();
                break;

            case R.id.reg_sign_up_btn:

                verifyData();

                break;

        }
    }


    //    checking valid data or empty fields
    private void verifyData() {
        String name, phone, division, bloodGroup = "not selected", district, thana, age, password;
        name = nameEditText.getText().toString();
        thana = thanaSpinner.getSelectedItem().toString();
        age = ageEditText.getText().toString();
        password = passwordEditText.getText().toString();
        division = divisionSpinner.getSelectedItem().toString();
        district = districtSpinner.getSelectedItem().toString();
        if (selectedBldGrp != null) {
            bloodGroup = selectedBldGrp.getText().toString();
        }

        RadioButton selectedRadiobtn = findViewById(donorRoleRadioGrp.getCheckedRadioButtonId());
        switch (selectedRadiobtn.getText().toString()) {
            case "ব্লাড":
                donorInfo = "Blood";
                break;
            case "প্লাজমা":
                donorInfo = "Plasma";
                break;
            case "ব্লাড এবং প্লাজমা":
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
        Intent i = getIntent();
        phone = i.getExtras().get("phone").toString();


//        checking empty Fields


        if (!formFieldsFeatures.checkIfEmpty(nameEditText) && !formFieldsFeatures.checkIfEmpty(ageEditText)
                && !formFieldsFeatures.checkIfEmpty(this, labelGender, gender)
                && !formFieldsFeatures.checkIfEmpty(this, labelBloodGroup, bloodGroup)
                && !formFieldsFeatures.checkIfEmpty(passwordEditText)) {

            if (password.length() < 6) {
                ToastCreator.toastCreatorRed(RegistrationActivity.this, getResources().getString(R.string.reg_activity_password_length));
            } else {
                if (password.equals(confPasswordEditText.getText().toString())) {
//            retro operations

                    registerUser(name, phone, division, district, bloodGroup, thana, age, password);


                } else {
                    confPasswordEditText.setError(getResources().getString(R.string.reg_activity_password_no_match));
                    confPasswordEditText.requestFocus();
                }
            }
        }


    }

//    database operations

    private void registerUser(String name, String phone, String division, String district, String bloodGroup, String thana, String age, String password) {
        progressBar.setVisibility(View.VISIBLE);
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<UserDataModel> sendingData = retroInterface.registerRetroMethod(name, phone, gender, bloodGroup, division, district, thana, age, donorInfo, password);
        sendingData.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {
                if (response.body().getServerMsg().toLowerCase().equals("success")) {
                    progressBar.setVisibility(View.GONE);
                    ToastCreator.toastCreatorGreen(RegistrationActivity.this, getResources().getString(R.string.reg_activity_registration_success));

//              going to login activity
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    ToastCreator.toastCreatorRed(RegistrationActivity.this, getResources().getString(R.string.connection_failed_try_again));
                }

            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ToastCreator.toastCreatorRed(RegistrationActivity.this, getResources().getString(R.string.reg_activity_registration_failed));
            }
        });

    }
}
