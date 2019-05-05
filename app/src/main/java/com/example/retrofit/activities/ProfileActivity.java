package com.example.retrofit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.retrofit.R;
import com.example.retrofit.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import fragment.HomeFragment;
import fragment.SettingFragment;
import fragment.UserFragment;
import storage.SharedPrefManager;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

   BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        navigationView=findViewById(R.id.bottomNavigationView);

       User user= SharedPrefManager.getInstance(this).getUser();

       navigationView.setOnNavigationItemSelectedListener(this);

       if (savedInstanceState==null){
               getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).commit();
       }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(this).loggedIn()){
            Intent intent=new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.user_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).commit();
                break;

            case R.id.user:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new UserFragment()).commit();
                break;
            case R.id.setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new SettingFragment()).commit();
                break;
        }
        return false;
    }
}
