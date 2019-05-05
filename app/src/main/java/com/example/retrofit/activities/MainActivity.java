package com.example.retrofit.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofit.model.DefaultResponse;
import com.example.retrofit.R;
import com.example.retrofit.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import storage.SharedPrefManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText,passwordEditText,nameEditText;
    private TextView loginText;
    private Button signUpButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailEditText=findViewById(R.id.email_edittext);
        passwordEditText=findViewById(R.id.password_edittext);
        nameEditText=findViewById(R.id.name_edittext);
        progressBar=findViewById(R.id.progressBar);
        loginText=findViewById(R.id.login_text);
        signUpButton=findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(this);
        loginText.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).loggedIn()){
            Intent intent=new Intent(this,ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.sign_up_button){
            userSignUp();
        }
        if (v.getId()==R.id.login_text){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
    }

    private void userSignUp() {
        String email=emailEditText.getText().toString().trim();
        String password=passwordEditText.getText().toString().trim();
        String name=nameEditText.getText().toString().trim();

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


        if (name.isEmpty()){
            nameEditText.setError("Enter a Name");
            nameEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Call<DefaultResponse> call= RetrofitClient.getInstance().getApi().createUser(email,password,name);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.code()==201){
                    DefaultResponse defaultResponse=response.body();
                    Toast.makeText(MainActivity.this, defaultResponse.getMsg(), Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(MainActivity.this, "User Already Exits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });

        emailEditText.setText("");
        passwordEditText.setText("");
        nameEditText.setText("");
    }

}
