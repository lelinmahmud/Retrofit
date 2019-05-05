package com.example.retrofit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.retrofit.R;
import com.example.retrofit.api.RetrofitClient;
import com.example.retrofit.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import storage.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText,passwordEditText;
    Button login;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText=findViewById(R.id.email_login);
        passwordEditText=findViewById(R.id.password_login);
        progressBar=findViewById(R.id.progressBar);
        login=findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).loggedIn()){
            Intent intent=new Intent(LoginActivity.this,ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    private void userLogin() {
        String email=emailEditText.getText().toString().trim();
        String password=passwordEditText.getText().toString().trim();

        if (email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Invalid Email Address");
            emailEditText.requestFocus();
            return;
        }
        if (password.isEmpty()){
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 4){
            passwordEditText.setError("Password must be 5 digit");
            passwordEditText.requestFocus();
            return;

        }

        progressBar.setVisibility(View.VISIBLE);

        Call<LoginResponse> call= RetrofitClient
                .getInstance().getApi().userLogin(email,password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                LoginResponse loginResponse=response.body();
                if (!loginResponse.getError()){
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    SharedPrefManager.getInstance(LoginActivity.this).saveUser(loginResponse.getUser());
                    Intent intent=new Intent(LoginActivity.this,ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                else{
                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
