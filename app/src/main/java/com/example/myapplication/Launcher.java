package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Launcher extends AppCompatActivity {


    TextView gotosigninpage;
    TextView gotoregisterpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);









        gotoregisterpage = findViewById(R.id.bt_register_id);
        gotoregisterpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Launcher.this,NewLottie.class));
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