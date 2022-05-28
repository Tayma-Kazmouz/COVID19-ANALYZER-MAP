package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.firebase.firestore.FirebaseFirestore;

public class ReportStatus2 extends AppCompatActivity {

    Button gotodashboard;
    ImageView backarrow;
    RadioButton rbPositive,rbNegative,rbContacted,rbNone;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_status2);

        gotodashboard = findViewById(R.id.bt_continue_id);
        backarrow = findViewById(R.id.backarrow_id);
        rbPositive = findViewById(R.id.rb1_id);
        rbNegative = findViewById(R.id.rb2_id);
        rbContacted = findViewById(R.id.rb3_id);
        rbNone = findViewById(R.id.rb4_id);



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