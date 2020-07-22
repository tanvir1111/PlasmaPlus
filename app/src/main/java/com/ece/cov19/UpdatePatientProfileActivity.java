package com.ece.cov19;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class UpdatePatientProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView aPositive, aNegative, bPositive, bNegative, oPositive, oNegative, abPositive, abNegative, selectedBldGrp;
    private TextView selectDate, labelBloodGroup, labelGender, labelDate;
    private String name, age, gender, bloodGroup, hospital, division, district, date, need, phone, newName, newAge, newBloodGroup = "not selected", newDivision, newDistrict, newHospital, newNeed, newGender, newDate;
    private ImageView genderMale, backbtn, genderFemale;
    private CheckBox needCheckbox;
    private Button updateBtn;
    private EditText nameEditText, ageEditText, hospitalEditText;
    private Spinner divisionSpinner, districtSpinner;
    public int divisionResourceIds[] = {R.array.Dhaka, R.array.Rajshahi, R.array.Rangpur, R.array.Khulna, R.array.Chittagong, R.array.Mymensingh,

            R.array.Barisal, R.array.Sylhet};
    FormFieldsFeatures formFieldsFeatures = new FormFieldsFeatures();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_patient_profile);

        updateBtn = findViewById(R.id.update_patient_request_btn);
        backbtn = findViewById(R.id.update_patient_back_button);
//      editTexts
        nameEditText = findViewById(R.id.update_patient_name_edittext);
        ageEditText = findViewById(R.id.update_patient_age_edittext);
        hospitalEditText = findViewById(R.id.update_patient_hospital_edittext);

        //        spinners
        divisionSpinner = findViewById(R.id.update_patient_division_spinner);
        districtSpinner = findViewById(R.id.update_patient_district_spinner);


        needCheckbox = findViewById(R.id.update_patient_plasma_checkbox);

//      Date section
        labelDate = findViewById(R.id.update_patient_label_date);
        selectDate = findViewById(R.id.update_patient_date_textview);
        /*blood group textviews*/
        labelBloodGroup = findViewById(R.id.update_patient_label_blood_grp);
        aPositive = findViewById(R.id.update_patient_bld_a_positive);
        bPositive = findViewById(R.id.update_patient_bld_b_positive);
        oPositive = findViewById(R.id.update_patient_bld_o_positive);
        abPositive = findViewById(R.id.update_patient_bld_ab_positive);
        aNegative = findViewById(R.id.update_patient_bld_a_negative);
        bNegative = findViewById(R.id.update_patient_bld_b_negative);
        oNegative = findViewById(R.id.update_patient_bld_o_negative);
        abNegative = findViewById(R.id.update_patient_bld_ab_negative);

        /*        Gender Imageviews*/
        labelGender = findViewById(R.id.update_patient_label_gender);
        genderMale = findViewById(R.id.update_patient_male_icon);
        genderFemale = findViewById(R.id.update_patient_female_icon);




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

        if(need.toLowerCase().equals("plasma")){
            needCheckbox.setChecked(true);
        }
        ArrayAdapter arrayAdapter = (ArrayAdapter) divisionSpinner.getAdapter();

        divisionSpinner.setSelection(arrayAdapter.getPosition(division));


        selectDate.setText(date);

        newGender = gender;
        newDate = date;

        nameEditText.setText(name);
        ageEditText.setText(age);
        hospitalEditText.setText(hospital);

        if(gender.toLowerCase().equals("male")) {
            genderFemale.setImageResource(R.drawable.female_icon);
            genderMale.setImageResource(R.drawable.male_icon_selected);
        }
        else {
            genderFemale.setImageResource(R.drawable.female_icon_selected);
            genderMale.setImageResource(R.drawable.male_icon);
        }

        setPreviousBloodGroup();



        //      Districts spinner as per selected division

        divisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<String> adpter = new ArrayAdapter<String>(
                        UpdatePatientProfileActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(divisionResourceIds[position]));
                districtSpinner.setAdapter(adpter);
                districtSpinner.setSelection(adpter.getPosition(district));
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
        updateBtn.setOnClickListener(this);
        backbtn.setOnClickListener(this);
    }

    private void setPreviousBloodGroup() {
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
        selectedBldGrp.setBackgroundResource(R.drawable.blood_grp_selected);
        selectedBldGrp.setTextColor(Color.WHITE);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.update_patient_male_icon:
                genderFemale.setImageResource(R.drawable.female_icon);
                genderMale.setImageResource(R.drawable.male_icon_selected);
                newGender = "male";
                break;
            case R.id.update_patient_female_icon:
                genderFemale.setImageResource(R.drawable.female_icon_selected);
                genderMale.setImageResource(R.drawable.male_icon);
                newGender = "female";
                break;
            case R.id.update_patient_bld_a_positive:
            case R.id.update_patient_bld_b_positive:
            case R.id.update_patient_bld_o_positive:
            case R.id.update_patient_bld_ab_positive:
            case R.id.update_patient_bld_a_negative:
            case R.id.update_patient_bld_b_negative:
            case R.id.update_patient_bld_o_negative:
            case R.id.update_patient_bld_ab_negative:

                selectedBldGrp = formFieldsFeatures.bloodGroupSelection(this, (TextView) v, selectedBldGrp);


                break;
            case R.id.update_patient_request_btn:
                verifydata();
                break;
            case R.id.update_patient_back_button:
                finish();
                break;
        }
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
                        newDate = sdf.format(calendar.getTime());

                        selectDate.setText(newDate); // set the date
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


    private void verifydata() {

        String emptyfield = "";
        boolean emptyfieldChecker = true;

        newName = nameEditText.getText().toString();
        newAge = ageEditText.getText().toString();
        newDivision = divisionSpinner.getSelectedItem().toString();
        newDistrict = districtSpinner.getSelectedItem().toString();
        newHospital = hospitalEditText.getText().toString();

        if (selectedBldGrp != null) {
            newBloodGroup = selectedBldGrp.getText().toString();
        }
        phone = loggedInUserPhone;

        if (needCheckbox.isChecked()) {
            newNeed = "Plasma";
        } else {
            newNeed = "Blood";
        }




//        checking empty Fields

        if (newName.isEmpty()) {
            emptyfieldChecker = false;
            emptyfield += getResources().getString(R.string.label_name) + " ";
            nameEditText.setError(getResources().getString(R.string.label_name)+" "+getResources().getString(R.string.is_required_txt));
        }
        if (newHospital.isEmpty()) {
            emptyfieldChecker = false;
            emptyfield += getResources().getString(R.string.label_hospital) + " ";
            hospitalEditText.setError(getResources().getString(R.string.label_hospital)+" "+getResources().getString(R.string.is_required_txt));
        }
        if (newAge.isEmpty()) {
            emptyfieldChecker = false;
            emptyfield += getResources().getString(R.string.label_age_1) + " ";
            ageEditText.setError(getResources().getString(R.string.label_age_1)+" "+getResources().getString(R.string.is_required_txt));
        }
        if (newDivision.isEmpty()) {
            emptyfieldChecker = false;
            emptyfield += getResources().getString(R.string.label_division) + " ";
        }
        if (newDistrict.isEmpty()) {
            emptyfieldChecker = false;
            emptyfield += getResources().getString(R.string.label_district) + " ";
        }

        if(emptyfieldChecker == true){
            updatePatient(name, age, bloodGroup, phone, newName, newAge, newGender, newBloodGroup, newHospital, newDivision, newDistrict, newDate, newNeed);

        }
        else{
            emptyfield += getResources().getString(R.string.is_required_txt);
            ToastCreator.toastCreatorRed(this, emptyfield);
        }

        /*if (!formFieldsFeatures.checkIfEmpty(nameEditText) && !formFieldsFeatures.checkIfEmpty(ageEditText)
                && !formFieldsFeatures.checkIfEmpty(hospitalEditText) && !formFieldsFeatures.checkIfEmpty(this, labelBloodGroup, bloodGroup)
                && !formFieldsFeatures.checkIfEmpty(this, labelGender, gender)) {
            updatePatient(name, age, bloodGroup, phone, newName, newAge, newGender, newBloodGroup, newHospital, newDivision, newDistrict, newDate, newNeed)

        }

         */
//

/*



            //updatePatient(name, age, gender, bloodGroup, hospital, division, district, date, need, phone, newName, newAge, newGender, newBloodGroup, newHospital, newDivision, newDistrict, newDate, newNeed);
        }

*/

    }


    //    database operations
    private void updatePatient(String name, String age, String bloodGroup, String phone, String newName, String newAge, String newGender, String newBloodGroup, String newHospital, String newDivision, String newDistrict, String newDate, String newNeed) {
        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<PatientDataModel> sendingData = retroInterface.updatePatientProfile(name, age, bloodGroup, phone, newName, newAge, newGender, newBloodGroup, newHospital, newDivision, newDistrict, newDate, newNeed);
        sendingData.enqueue(new Callback<PatientDataModel>() {
            @Override
            public void onResponse(Call<PatientDataModel> call, Response<PatientDataModel> response) {
                if (response.body().getServerMsg().toLowerCase().equals("success")) {
                    updateAlertDialog();

                } else {
                    ToastCreator.toastCreatorRed(UpdatePatientProfileActivity.this, getResources().getString(R.string.failed_to_update_error));
                }
            }

            @Override
            public void onFailure(Call<PatientDataModel> call, Throwable t) {
                ToastCreator.toastCreatorRed(UpdatePatientProfileActivity.this, getResources().getString(R.string.error_occured));

            }
        });

    }


private void updateAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePatientProfileActivity.this);
        builder.setMessage(getResources().getString(R.string.confirm_update_txt));
        builder.setPositiveButton(getString(R.string.confirm_update_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(UpdatePatientProfileActivity.this, ViewPatientProfileActivity.class);
                intent.putExtra("name",newName);
                intent.putExtra("age",newAge);
                intent.putExtra("gender",newGender);
                intent.putExtra("blood_group",newBloodGroup);
                intent.putExtra("hospital",newHospital);
                intent.putExtra("division",newDivision);
                intent.putExtra("district",newDistrict);
                intent.putExtra("date",newDate);
                intent.putExtra("need",newNeed);
                intent.putExtra("phone",phone);
                intent.putExtra("activity","UpdatePatientProfileActivity");
                ToastCreator.toastCreatorGreen(UpdatePatientProfileActivity.this, getResources().getString(R.string.update_patient_activity_update_successful));
                startActivity(intent);
                finish();
            }
        })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }


}



