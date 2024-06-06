package com.iverify;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class Register extends AppCompatActivity {


    EditText usernameEditText, passwordEditText, retypePasswordEditText, emailEditText;
    ProgressBar progressBar;

    Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_register);


        usernameEditText        = findViewById(R.id.username);
        passwordEditText        = findViewById(R.id.password);
        retypePasswordEditText  = findViewById(R.id.retype_password);
        emailEditText           = findViewById(R.id.email);
        progressBar             = findViewById(R.id.progress_bar);
        registerButton          = findViewById(R.id.register_button);

        registerButton.setBackgroundColor(Color.parseColor("#12a56b"));
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username         = usernameEditText.getText().toString();
                String password         = passwordEditText.getText().toString();
                String retypePassword   = retypePasswordEditText.getText().toString();
                String email            = emailEditText.getText().toString();

                if ( username.isEmpty()  ){
                    usernameEditText.setError("Username empty!");
                    usernameEditText.requestFocus();
                    return;
                }

                if ( password.isEmpty() ){
                    passwordEditText.setError("Password empty!");
                    passwordEditText.requestFocus();
                    return;
                }

                if ( retypePassword.isEmpty()){
                    retypePasswordEditText.setError("Password empty!");
                    retypePasswordEditText.requestFocus();
                    return;
                }

                if ( email.isEmpty()){
                    emailEditText.setError("Email empty!");
                    emailEditText.requestFocus();
                    return;
                }

                if ( !password.equals(retypePassword) ){
                    passwordEditText.setError("Password mismatch!");
                    passwordEditText.requestFocus();
                    retypePasswordEditText.setError("Password mismatch!");
                    retypePasswordEditText.requestFocus();
                }

                if ( !isValidEmail(email) ){
                    emailEditText.setError("Invalid email!");
                    emailEditText.requestFocus();
                    return;
                }



            }
        });

    }


    // List of popular email domains
    private static final List<String> POPULAR_DOMAINS = Arrays.asList(
            "gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "aol.com", "icloud.com"
    );

    /**
     * Validates the given email address.
     * Checks if the email address belongs to a popular domain.
     *
     * @param email The email address to validate.
     * @return true if the email address is valid and belongs to a popular domain, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Split the email into local part and domain part
        int atIndex = email.lastIndexOf('@');
        if (atIndex < 1 || atIndex >= email.length() - 1) {
            return false;
        }

        String domain = email.substring(atIndex + 1);

        // Check if the domain is in the list of popular domains
        return POPULAR_DOMAINS.contains(domain);
    }

}