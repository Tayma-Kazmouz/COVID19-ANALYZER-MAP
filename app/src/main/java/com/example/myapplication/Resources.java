package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Resources extends AppCompatActivity {


    TextView hyperlink1;
    TextView hyperlink2;
    TextView hyperlink3;
    TextView hyperlink4;
    TextView hyperlink5;
    TextView hyperlink6;
    TextView hyperlink7;
    TextView hyperlink8;
    TextView hyperlink9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);



        //backarrow
        ImageView backArrow = findViewById(R.id.ivResourcesBackArrow_id);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Resources.this,Extras.class));
                finish();
            }
        });




        //hyperlinks

        hyperlink1 = findViewById(R.id.resource1);
        hyperlink1.setMovementMethod(LinkMovementMethod.getInstance());

        hyperlink2 = findViewById(R.id.resource2);
        hyperlink2.setMovementMethod(LinkMovementMethod.getInstance());

        hyperlink3 = findViewById(R.id.resource3);
        hyperlink3.setMovementMethod(LinkMovementMethod.getInstance());

        hyperlink4 = findViewById(R.id.resource4);
        hyperlink4.setMovementMethod(LinkMovementMethod.getInstance());

        hyperlink5 = findViewById(R.id.resource5);
        hyperlink5.setMovementMethod(LinkMovementMethod.getInstance());

        hyperlink6 = findViewById(R.id.resource6);
        hyperlink6.setMovementMethod(LinkMovementMethod.getInstance());

        hyperlink7 = findViewById(R.id.resource7);
        hyperlink7.setMovementMethod(LinkMovementMethod.getInstance());

        hyperlink8 = findViewById(R.id.resource8);
        hyperlink8.setMovementMethod(LinkMovementMethod.getInstance());

        hyperlink9 = findViewById(R.id.resource9);
        hyperlink9.setMovementMethod(LinkMovementMethod.getInstance());



    }


    @Override
    protected void onStart() {
        super.onStart();
        // force disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

}