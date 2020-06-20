package com.ece.cov19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RequestsActivity extends AppCompatActivity {
    private Button addRequestsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        addRequestsBtn=findViewById(R.id.requsts_add_requests_btn);
        addRequestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bldReqIntent=new Intent(RequestsActivity.this, BloodRequestFormActivity.class);
                startActivity(bldReqIntent);
            }
        });
    }
}
