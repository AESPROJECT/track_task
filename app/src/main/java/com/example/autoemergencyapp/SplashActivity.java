package com.example.autoemergencyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread =new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(5000);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext()," error ",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                finally {
                    Intent welcomIntent=new Intent(SplashActivity.this,welcomActivity.class);
                    startActivity(welcomIntent);

                }


            }

        };
        thread.start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
