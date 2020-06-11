package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BloodRequestForm extends AppCompatActivity implements View.OnClickListener {
    private TextView aPositive, aNegative, bPositive, bNegative, oPositive, oNegative, abPositive, abNegative, selectedBldGrp;
    private String bloodGroup, gender;
    private ImageView genderMale, genderFemale;
    private Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request_form);

        submitBtn=findViewById(R.id.bld_req_request_btn);

        /*blood group textviews*/
        aPositive = findViewById(R.id.bld_req_bld_a_positive);
        bPositive = findViewById(R.id.bld_req_bld_b_positive);
        oPositive = findViewById(R.id.bld_req_bld_o_positive);
        abPositive = findViewById(R.id.bld_req_bld_ab_positive);
        aNegative = findViewById(R.id.bld_req_bld_a_negative);
        bNegative = findViewById(R.id.bld_req_bld_b_negative);
        oNegative = findViewById(R.id.bld_req_bld_o_negative);
        abNegative = findViewById(R.id.bld_req_bld_ab_negative);

        /*        Gender Imageviews*/
        genderMale = findViewById(R.id.bld_req_male_icon);
        genderFemale = findViewById(R.id.bld_req_female_icon);

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
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
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
                selectedBldGrp.setBackgroundResource(R.drawable.blood_grp_not_selected);
                selectedBldGrp.setTextColor(getColor(R.color.textColorGrey));
                selectedBldGrp = findViewById(v.getId());
                selectedBldGrp.setBackgroundResource(R.drawable.blood_grp_selected);
                selectedBldGrp.setTextColor(Color.WHITE);
                bloodGroup = selectedBldGrp.getText().toString();
                break;

            case R.id.bld_req_request_btn:
                Intent dashboardIntent=new Intent(BloodRequestForm.this,DashBoard.class);
                startActivity(dashboardIntent);
        }
    }
}



