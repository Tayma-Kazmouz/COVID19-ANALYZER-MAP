package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VerificationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);
    }

    public void gotoreportstatus1(View view) {
        Intent i= new Intent(VerificationPage.this, ReportStatus1.class);
        startActivity(i);
    }
}