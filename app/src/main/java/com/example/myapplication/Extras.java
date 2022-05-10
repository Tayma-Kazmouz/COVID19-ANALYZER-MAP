package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Extras extends AppCompatActivity {

    //Declare

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extras);

        //Define

        ImageView ivBack = findViewById(R.id.ivExtrasBackArrow_id);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Extras.this,DashBoard.class));
            }
        });




    }//end of onCreate



}//end of class