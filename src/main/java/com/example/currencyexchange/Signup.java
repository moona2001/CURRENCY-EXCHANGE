package com.example.currencyexchange;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;




public class Signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText editTextEmailSignup, editTextPasswordSignup, editTextReEnterPasswordSignup;
    private Button buttonSignup;
    private TextView textViewAlreadyHaveAccount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        // Initialize views
        editTextEmailSignup = findViewById(R.id.editTextEmailSignup);
        editTextPasswordSignup = findViewById(R.id.editTextPasswordSignup);
        editTextReEnterPasswordSignup = findViewById(R.id.editTextReEnterPasswordSignup);
        buttonSignup = findViewById(R.id.buttonSignup); // Initialize buttonSignup
        textViewAlreadyHaveAccount = findViewById(R.id.textViewAlreadyHaveAccount);

        textViewAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });

        // Set click listener for signup button
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call signup function
                signup();
            }
        });
    }

    // Function to handle signup process
    private void signup() {
        String email = editTextEmailSignup.getText().toString().trim();
        String password = editTextPasswordSignup.getText().toString().trim();
        String reEnterPassword = editTextReEnterPasswordSignup.getText().toString().trim();

        // Email validation
        if (email.isEmpty()) {
            editTextEmailSignup.setError("Email is required");
            editTextEmailSignup.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailSignup.setError("Enter a valid email address");
            editTextEmailSignup.requestFocus();
            return;
        }

        // Password validation
        if (password.isEmpty()) {
            editTextPasswordSignup.setError("Password is required");
            editTextPasswordSignup.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextPasswordSignup.setError("Password must be at least 6 characters long");
            editTextPasswordSignup.requestFocus();
            return;
        }
        if (!password.matches(".*\\d.*")) {
            editTextPasswordSignup.setError("Password must contain at least one digit");
            editTextPasswordSignup.requestFocus();
            return;
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            editTextPasswordSignup.setError("Password must contain at least one letter");
            editTextPasswordSignup.requestFocus();
            return;
        }

        // Re-enter password validation
        if (reEnterPassword.isEmpty()) {
            editTextReEnterPasswordSignup.setError("Please re-enter password");
            editTextReEnterPasswordSignup.requestFocus();
            return;
        }
        if (!reEnterPassword.equals(password)) {
            editTextReEnterPasswordSignup.setError("Passwords do not match");
            editTextReEnterPasswordSignup.requestFocus();
            return;
        }

        // Add your signup logic here
        signUpUser(email,password);


    }
    private void signUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Signup.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Signup.this, MainActivity.class));
                            finish();
                        } else {
                            Log.w("SignupActivity", "createUserWithEmailAndPassword:failure", task.getException());
                            Toast.makeText(Signup.this, "Sign up failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
