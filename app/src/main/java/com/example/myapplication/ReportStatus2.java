package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ReportStatus2 extends AppCompatActivity {

    Button gotodashboard;
    ImageView backarrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_status2);

        gotodashboard = findViewById(R.id.bt_continue_id);
        backarrow = findViewById(R.id.backarrow_id);



        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportStatus2.this,ReportStatus1.class));
            }
        });


        gotodashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportStatus2.this,DashBoard.class));
            }
        });




    }
}