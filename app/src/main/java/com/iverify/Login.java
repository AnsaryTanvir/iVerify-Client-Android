package com.iverify;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import okhttp3.FormBody;

public class Login extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    static ProgressBar progressBar;
    Button loginButton;
    TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.login_username);
        passwordEditText = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progress_bar);
        loginButton = findViewById(R.id.login_button);
        registerTextView = findViewById(R.id.register_text);

        loginButton.setBackgroundColor(Color.parseColor("#12a56b"));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty()) {
                    usernameEditText.setError("Username empty");
                    usernameEditText.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordEditText.setError("Password empty");
                    passwordEditText.requestFocus();
                    return;
                }

                Context context = getApplicationContext();

                FormBody requestBody = new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .build();

                loginButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                Network network = new Network(context, Constants.LOGIN_URL, requestBody, progressBar, loginButton, Login.this);
                network.execute();
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Finish the Login activity
    }
}
