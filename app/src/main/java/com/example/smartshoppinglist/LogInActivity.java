package com.example.smartshoppinglist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private TextView alreadyHaveAccountTextView;
    private TextView changeSignupModeTextView;
    private Boolean signUpModeActive = true;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        alreadyHaveAccountTextView = findViewById(R.id.alreadyHaveAccountTextView);
        changeSignupModeTextView = findViewById(R.id.changeSignupModeTextView);

        mAuth = FirebaseAuth.getInstance();

        changeSignupModeTextView.setOnClickListener(this);
    }

    //Changes text of button and text views depending on whether the user want to log in or sign up.
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.changeSignupModeTextView){

            if(signUpModeActive){
                signUpModeActive = false;
                signUpButton.setText("Log in");

                changeSignupModeTextView.setText("Sign up");
                alreadyHaveAccountTextView.setText("Don't have an account?");
            } else {
                signUpModeActive = true;
                signUpButton.setText("Sign up");

                changeSignupModeTextView.setText("Log in");
                alreadyHaveAccountTextView.setText("Already have an account?");
            }}}

    public void signUp(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(signUpModeActive == true){
    if(email.isEmpty() || password.isEmpty()){
        Toast.makeText(this, "Username and password required", Toast.LENGTH_SHORT).show();
    } else {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LogInActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogInActivity.this, MainActivity.class));
                } else{
                    Toast.makeText(LogInActivity.this, "Registration error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }}});}
    }else{
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Username and password required", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LogInActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LogInActivity.this, MainActivity.class));
                        } else{
                            Toast.makeText(LogInActivity.this, "Registration error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }}});}
    }
    }
}