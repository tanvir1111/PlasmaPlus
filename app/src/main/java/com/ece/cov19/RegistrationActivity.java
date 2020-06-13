package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView aPositive, aNegative, bPositive, bNegative, oPositive, oNegative, abPositive, abNegative, selectedBldGrp;
    private EditText nameEditText,ageEditText,thanaEditText,passwordEditText,confPasswordEditText;

    private String  bloodGroup, gender,donorInfo;
    private ImageView genderMale, genderFemale;
    private Button singUp;

    private CheckBox isPlasmaDonor,isDonorBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

//        all editTexts
        nameEditText=findViewById(R.id.reg_name_edittext);
        ageEditText=findViewById(R.id.reg_age_edittext);
        thanaEditText=findViewById(R.id.reg_thana_edittext);
        passwordEditText=findViewById(R.id.reg_password_edittext);
        confPasswordEditText=findViewById(R.id.reg_confirm_password_edittext);

//      buttons
        singUp=findViewById(R.id.reg_sign_up_btn);
        isDonorBtn=findViewById(R.id.reg_donor_checkbox);
        isPlasmaDonor=findViewById(R.id.reg_plasma_checkbox);

        /*blood group textviews*/
        aPositive = findViewById(R.id.reg_bld_a_positive);
        bPositive = findViewById(R.id.reg_bld_b_positive);
        oPositive = findViewById(R.id.reg_bld_o_positive);
        abPositive = findViewById(R.id.reg_bld_ab_positive);
        aNegative = findViewById(R.id.reg_bld_a_negative);
        bNegative = findViewById(R.id.reg_bld_b_negative);
        oNegative = findViewById(R.id.reg_bld_o_negative);
        abNegative = findViewById(R.id.reg_bld_ab_negative);

        /*        Gender Imageviews*/
        genderMale = findViewById(R.id.reg_male_icon);
        genderFemale = findViewById(R.id.reg_female_icon);

        /* just a random one to avoid nullPoint Exception*/
        selectedBldGrp = aPositive;


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

//        donorcheckbox setting donor info blood/plasma/na
        isDonorBtn.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            donorInfo="Blood";
                            isPlasmaDonor.setVisibility(View.VISIBLE);
                            isPlasmaDonor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked){
                                        donorInfo="Plasma";

                                    }
                                    else {
                                        donorInfo="Blood";
                                    }
                                    Toast.makeText(RegistrationActivity.this, donorInfo, Toast.LENGTH_SHORT).show();


                                }
                            });

                        }
                        else
                        {
                            donorInfo="na";
                            isPlasmaDonor.setVisibility(View.INVISIBLE);
                        }
                        Toast.makeText(RegistrationActivity.this, donorInfo, Toast.LENGTH_SHORT).show();
                    }
                });






    }



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
                selectedBldGrp.setBackgroundResource(R.drawable.blood_grp_not_selected);
                selectedBldGrp.setTextColor(getColor(R.color.textColorGrey));
                selectedBldGrp = findViewById(v.getId());
                selectedBldGrp.setBackgroundResource(R.drawable.blood_grp_selected);
                selectedBldGrp.setTextColor(Color.WHITE);
                bloodGroup = selectedBldGrp.getText().toString();
                break;

            case R.id.reg_sign_up_btn:

                verifyData();
                Intent login=new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(login);
                break;
        }
    }

    private void verifyData() {
        String name,phone,division,district,thana,age,password;
        name=nameEditText.getText().toString();
        thana=thanaEditText.getText().toString();
        age=ageEditText.getText().toString();
        password=passwordEditText.getText().toString();
        division="";
        district="";
        Intent i=getIntent();
        phone=i.getExtras().get("phone").toString();
        if(password==confPasswordEditText.getText().toString()){
//            retro operations

        }
        else {
            Toast.makeText(this, "password doesn't match", Toast.LENGTH_SHORT).show();
        }



    }
}
