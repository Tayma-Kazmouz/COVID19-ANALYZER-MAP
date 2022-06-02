package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Resources extends AppCompatActivity {


    TextView hyperlink;
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

        hyperlink = findViewById(R.id.resource1);
        hyperlink.setMovementMethod(LinkMovementMethod.getInstance());







    }
}