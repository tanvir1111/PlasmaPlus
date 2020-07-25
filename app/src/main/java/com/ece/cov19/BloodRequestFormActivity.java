package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.Functions.FormFieldsFeatures;
import com.ece.cov19.Functions.ToastCreator;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserAge;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserBloodGroup;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserGender;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserName;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class BloodRequestFormActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView aPositive, aNegative, bPositive, bNegative, oPositive, oNegative, abPositive, abNegative, selectedBldGrp;
    private TextView selectDate, labelBloodGroup, labelGender, labelDate;
    private String gender = "not selected", date = "not selected";
    private ImageView genderMale, backbtn, genderFemale;
    private CheckBox needCheckbox;
    private Button submitBtn, forMyselfBtn,othersBtn;
    private EditText nameEditText, ageEditText, hospitalEditText;
    private Spinner divisionSpinner, districtSpinner;
    public int divisionResourceIds[] = {R.array.Dhaka, R.array.Rajshahi, R.array.Rangpur, R.array.Khulna, R.array.Chittagong, R.array.Mymensingh,

            R.array.Barisal, R.array.Sylhet};
    FormFieldsFeatures formFieldsFeatures = new FormFieldsFeatures();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request_form);

        submitBtn = findViewById(R.id.bld_req_request_btn);
        backbtn = findViewById(R.id.bld_req_back_button);
        forMyselfBtn = findViewById(R.id.bld_req_for_myself);
        othersBtn=findViewById(R.id.bld_req_others_btn);
//      editTexts
        nameEditText = findViewById(R.id.bld_req_name_edittext);
        ageEditText = findViewById(R.id.bld_req_age_edittext);
        hospitalEditText = findViewById(R.id.bld_req_hospital_edittext);

        //        spinners
        divisionSpinner = findViewById(R.id.bld_req_division_spinner);
        districtSpinner = findViewById(R.id.bld_req_district_spinner);


        needCheckbox = findViewById(R.id.bld_req_plasma_checkbox);

//      Date section
        labelDate = findViewById(R.id.bld_req_label_date);
        selectDate = findViewById(R.id.bld_req_date_textview);
        /*blood group textviews*/
        labelBloodGroup = findViewById(R.id.bld_req_label_blood_grp);
        aPositive = findViewById(R.id.bld_req_bld_a_positive);
        bPositive = findViewById(R.id.bld_req_bld_b_positive);
        oPositive = findViewById(R.id.bld_req_bld_o_positive);
        abPositive = findViewById(R.id.bld_req_bld_ab_positive);
        aNegative = findViewById(R.id.bld_req_bld_a_negative);
        bNegative = findViewById(R.id.bld_req_bld_b_negative);
        oNegative = findViewById(R.id.bld_req_bld_o_negative);
        abNegative = findViewById(R.id.bld_req_bld_ab_negative);

        /*        Gender Imageviews*/
        labelGender = findViewById(R.id.bld_req_label_gender);
        genderMale = findViewById(R.id.bld_req_male_icon);
        genderFemale = findViewById(R.id.bld_req_female_icon);


        //      Districts spinner as per selected division

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adpter = new ArrayAdapter<String>(
                        BloodRequestFormActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(divisionResourceIds[position]));
                districtSpinner.setAdapter(adpter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        DatePicker
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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
        submitBtn.setOnClickListener(this);
        backbtn.setOnClickListener(this);
        forMyselfBtn.setOnClickListener(this);
        othersBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    datepicker for choosing date

    private void chooseDate() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker view, final int year, final int month,
                                          final int dayOfMonth) {

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        calendar.set(year, month, dayOfMonth);
                        date = sdf.format(calendar.getTime());

                        selectDate.setText(date); // set the date
                    }
                }, year, month, day); // set date picker to current date

        datePicker.show();

        datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(final DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.bld_req_for_myself:
                forMyselfBtn.setBackgroundResource(R.drawable.button_style_white);
                forMyselfBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                forMyselfBtn.setEnabled(false);
                othersBtn.setBackgroundResource(R.drawable.button_style_colored);
                othersBtn.setTextColor(getColor(R.color.textColorWhite));
                othersBtn.setEnabled(true);
                    fillAvailableInfo();
                break;
            case R.id.bld_req_others_btn:
                othersBtn.setBackgroundResource(R.drawable.button_style_white);
                othersBtn.setTextColor(getResources().getColor(R.color.textColorGrey));
                othersBtn.setEnabled(false);
                forMyselfBtn.setBackgroundResource(R.drawable.button_style_colored);
                forMyselfBtn.setTextColor(getColor(R.color.textColorWhite));
                forMyselfBtn.setEnabled(true);
                genderFemale.setImageResource(R.drawable.female_icon);
                genderMale.setImageResource(R.drawable.male_icon);
                selectedBldGrp.setBackgroundResource(R.drawable.blood_grp_not_selected);
                selectedBldGrp.setTextColor(ContextCompat.getColor(this,R.color.textColorGrey));
                nameEditText.setText("");
                ageEditText.setText("");


                break;

            case R.id.bld_req_male_icon:
                genderFemale.setImageResource(R.drawable.female_icon);
                genderMale.setImageResource(R.drawable.male_icon_selected);
                gender = "male";
                break;

            case R.id.bld_req_female_icon:
                genderFemale.setImageResource(R.drawable.female_icon_selected);
                genderMale.setImageResource(R.drawable.male_icon);
                gender = "female";
                break;

            case R.id.bld_req_bld_a_positive:
            case R.id.bld_req_bld_b_positive:
            case R.id.bld_req_bld_o_positive:
            case R.id.bld_req_bld_ab_positive:
            case R.id.bld_req_bld_a_negative:
            case R.id.bld_req_bld_b_negative:
            case R.id.bld_req_bld_o_negative:
            case R.id.bld_req_bld_ab_negative:

                selectedBldGrp = formFieldsFeatures.bloodGroupSelection(this, (TextView) v, selectedBldGrp);


                break;


            case R.id.bld_req_request_btn:
                verifydata();
                break;
            case R.id.bld_req_back_button:
                finish();
                break;


        }
    }

    private void fillAvailableInfo() {
        nameEditText.setText(loggedInUserName);
        ageEditText.setText(loggedInUserAge);
        String bloodGroup = loggedInUserBloodGroup;
        String gender = loggedInUserGender;
        if (gender.toLowerCase().toLowerCase().equals("male")) {
            genderFemale.setImageResource(R.drawable.female_icon);
            genderMale.setImageResource(R.drawable.male_icon_selected);
        } else {
            genderFemale.setImageResource(R.drawable.female_icon_selected);
            genderMale.setImageResource(R.drawable.male_icon);
        }
        TextView oldSelection;

        
            oldSelection = selectedBldGrp;
        switch (bloodGroup) {
            case "A+":
                selectedBldGrp = aPositive;
                break;
            case "A-":
                selectedBldGrp = aNegative;
                break;
            case "AB+":
                selectedBldGrp = abPositive;
                break;
            case "AB-":
                selectedBldGrp = abNegative;
                break;
            case "B+":
                selectedBldGrp = bPositive;
                break;
            case "B-":
                selectedBldGrp = bNegative;
                break;
            case "O+":
                selectedBldGrp = oPositive;
                break;
            case "O-":
                selectedBldGrp = oNegative;
                break;


        }
        selectedBldGrp = formFieldsFeatures.bloodGroupSelection(this,selectedBldGrp ,oldSelection );
//        selectedBldGrp.setBackgroundResource(R.drawable.blood_grp_selected);
//        selectedBldGrp.setTextColor(Color.WHITE);


    }

    private void verifydata() {
        String name, phone, division, district, bloodGroup = "not selected", hospital, need, age;
        name = nameEditText.getText().toString();
        age = ageEditText.getText().toString();
        division = divisionSpinner.getSelectedItem().toString();
        district = districtSpinner.getSelectedItem().toString();
        hospital = hospitalEditText.getText().toString();
        if (selectedBldGrp != null) {
            bloodGroup = selectedBldGrp.getText().toString();
        }
        phone = loggedInUserPhone;

        if (needCheckbox.isChecked()) {
            need = "Plasma";
        } else {
            need = "Blood";
        }

//


        if (!formFieldsFeatures.checkIfEmpty(nameEditText) && !formFieldsFeatures.checkIfEmpty(ageEditText)
                && !formFieldsFeatures.checkIfEmpty(hospitalEditText) && !formFieldsFeatures.checkIfEmpty(this, labelBloodGroup, bloodGroup)
                && !formFieldsFeatures.checkIfEmpty(this, labelGender, gender) && !formFieldsFeatures.checkIfEmpty(this, labelDate, date)) {

            registerPatient(name, age, gender, bloodGroup, hospital, division, district, date, need, phone);
        }


    }


    //    database operations
    private void registerPatient(String name, String age, String gender, String bloodGroup, String hospital, String division, String district, String date, String need, String phone) {
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<PatientDataModel> sendingData = retroInterface.registerPatientRetro(name, age, gender, bloodGroup, hospital, division, district, date, need, phone);
        sendingData.enqueue(new Callback<PatientDataModel>() {
            @Override
            public void onResponse(Call<PatientDataModel> call, Response<PatientDataModel> response) {
                if (response.body().getServerMsg().toLowerCase().equals("success")) {
                    ToastCreator.toastCreatorGreen(BloodRequestFormActivity.this, getResources().getString(R.string.bld_req_activity_patient_added));
                    Intent intent = new Intent(BloodRequestFormActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    ToastCreator.toastCreatorRed(BloodRequestFormActivity.this, getResources().getString(R.string.connection_failed_try_again));
                }
            }

            @Override
            public void onFailure(Call<PatientDataModel> call, Throwable t) {

                ToastCreator.toastCreatorRed(BloodRequestFormActivity.this, getResources().getString(R.string.connection_error));

            }
        });

    }


}


