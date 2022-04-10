package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.shimmer.ShimmerFrameLayout;

public class CoronaCasesList extends AppCompatActivity {


    //Declare
    ShimmerFrameLayout shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_cases_list);

        //Define
        shimmer = findViewById(R.id.ItemShimmer_id);
        shimmer.startShimmer();




    }//end onCreate




}//end of class