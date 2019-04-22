package com.example.autoemergencyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Admin_Activity extends AppCompatActivity implements View.OnClickListener {

    TextView add, delete, edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_);

        add = findViewById(R.id.add);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);

        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add :
                break;
            case R.id.edit :
                break;
            case R.id.delete :
                break;
        }
    }
}
