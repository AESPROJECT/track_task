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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button Login;
    private EditText Email;
    private EditText Password;
    private TextView SignUp;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Login = findViewById(R.id.login);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        SignUp = findViewById(R.id.sign_up);
        loadingBar = new ProgressDialog(this);

        Login.setOnClickListener(this);
        SignUp.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login: {
                String email = Email.getText().toString();
                String Pass = Password.getText().toString();



                Login(email, Pass);
                Intent RegisterER = new Intent(LoginActivity.this, MapActivity.class);
                startActivity(RegisterER);
            }
            break;
            case R.id.sign_up: {
                Intent RegisterER = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(RegisterER);
            }
            break;
        }
    }

    private void Login(String email, String password) {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please Enter Your Email..", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please Enter Your password..", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login");
            loadingBar.setMessage("Please wait, while we are cheaking your credientials");
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Your logged in Successfully ..", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    } else
                        Toast.makeText(getApplicationContext(), "login Unsuccessful ", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            });
        }
    }


}
