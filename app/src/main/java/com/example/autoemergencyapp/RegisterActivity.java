package com.example.autoemergencyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button SignUp;
    private EditText Email;
    private  EditText Password;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        SignUp = findViewById(R.id.sign_up);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        loadingBar = new ProgressDialog(this);

        SignUp.setOnClickListener(this);

    }

    private void Register(String email, String password) {

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please Enter Your Email..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please Enter Your password..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Registration");
            loadingBar.setMessage("Please wait, while we are register your data!");
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Your Registration is Successfully ..", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                      /*  Intent RegisterER=new Intent(RegisterActivity.this,MapActivity.class);
                        startActivity(RegisterER);*/
                        Intent backToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(backToLogin);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Registration Unsuccessful, Please Try Again", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                }

            });
        }




}

    @Override
    public void onClick(View v) {
        startActivity(new Intent(RegisterActivity.this, Admin_Activity.class));
        String email = Email.getText().toString();
        String Pass = Password.getText().toString();
        Register(email,Pass);
    }
}
