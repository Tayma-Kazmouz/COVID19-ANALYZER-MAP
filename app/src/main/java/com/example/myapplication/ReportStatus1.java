package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ReportStatus1 extends AppCompatActivity {


    Button gotoreporttwo;
    ImageView backarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_status1);

        gotoreporttwo = findViewById(R.id.bt_continue_id);
        backarrow = findViewById(R.id.backarrow_id);


        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportStatus1.this,UserInput.class));
            }
        });



        gotoreporttwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportStatus1.this,ReportStatus2.class));
            }
        });







    }
}