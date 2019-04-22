package com.example.autoemergencyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class welcomActivity extends AppCompatActivity implements View.OnClickListener {

    private Button ImEmergencyResponder  , ImCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);

       ImEmergencyResponder = findViewById(R.id.EmRes);
        ImCustomer  = findViewById(R.id.Customer);

        ImEmergencyResponder.setOnClickListener(this);
        ImCustomer.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.EmRes: {
                Intent loginER = new Intent(welcomActivity.this, LoginActivity.class);
                startActivity(loginER);
            }
                break;
            case R.id.Customer:
            {
                Intent Services =new Intent(welcomActivity.this,ServicesActivity.class);
                startActivity(Services);
            }
                break;
        }
    }
}
