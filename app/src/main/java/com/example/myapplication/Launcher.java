package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blongho.country_data.World;

public class Launcher extends AppCompatActivity {


    TextView gotosigninpage;
    TextView gotoregisterpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        World.init(getApplicationContext()); // Initializes the countries library and loads all data

        // force disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        TextView gotoUserInput = findViewById(R.id.launchtitle_id);
        gotoUserInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Launcher.this,UserInput.class));
            }
        });

        gotoregisterpage = findViewById(R.id.bt_register_id);
        gotoregisterpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(Launcher.this,NewLottie.class);
                reg.putExtra("src","reg");
                startActivity(reg);
            }
        });


        gotosigninpage = findViewById(R.id.bt_Signin_id);
        gotosigninpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Launcher.this,SignInPage.class));
            }
        });





















    }
}