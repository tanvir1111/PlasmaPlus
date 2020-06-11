package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashBoard extends AppCompatActivity {
    private Button profileBtn,seeRequestBtn,findDonorBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        profileBtn=findViewById(R.id.dashboard_profile_btn);
        seeRequestBtn=findViewById(R.id.dashboard_see_requests_btn);
        findDonorBtn=findViewById(R.id.dashboard_find_donor_btn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent=new Intent(DashBoard.this,ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        seeRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seeRequstsIntent=new Intent(DashBoard.this,RequestsActivity.class);
                startActivity(seeRequstsIntent);
            }
        });
        findDonorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchDonorIntent=new Intent(DashBoard.this,SearchDonorActivity.class);
                startActivity(searchDonorIntent);
            }
        });
    }
}
