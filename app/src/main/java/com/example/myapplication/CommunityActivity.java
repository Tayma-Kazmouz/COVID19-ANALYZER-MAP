package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

public class CommunityActivity extends AppCompatActivity {

        Switch sw ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        sw = findViewById(R.id.CommunitySwitch_id);
        LinearLayout lCovid = findViewById(R.id.coronaCommunity_id);
        LinearLayout lVaccine = findViewById(R.id.vaccineCommunity_id);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    lCovid.setVisibility(View.VISIBLE);
                    lVaccine.setVisibility(View.GONE);
                }else{
                    lCovid.setVisibility(View.GONE);
                    lVaccine.setVisibility(View.VISIBLE);
                }
            }
        });

    }//end of onCreate



}//end of class