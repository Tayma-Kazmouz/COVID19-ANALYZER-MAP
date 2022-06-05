package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blongho.country_data.World;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class Extras extends AppCompatActivity {

    //Declare
    private FirebaseAuth firebaseAuth;
    private FirebaseUser fbUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extras);

        //Define
        World.init(getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();
        fbUser = firebaseAuth.getCurrentUser();


        ImageView ivBack = findViewById(R.id.ivExtrasBackArrow_id);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Extras.this,DashBoard.class));
            }
        });


        ImageView ivLogout = findViewById(R.id.logout_id);
        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fbUser == null){
                    Toasty.error(getApplicationContext(), "No User Signed In", Toast.LENGTH_SHORT,true).show();
                    return;
                }else{
                        logOutUser();
                }


            }
        });


        MaterialCardView mCVSymptoms = findViewById(R.id.mCVsympsandprecs_id);
        mCVSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(Extras.this,NewLottie.class);
                x.putExtra("src","extra");
                startActivity(x);
            }
        });


        MaterialCardView myprofile = findViewById(R.id.myprofilecard); //user profile page
        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fbUser == null){
                    Toasty.error(getApplicationContext(), "No User Signed In", Toast.LENGTH_SHORT,true).show();
                    return;
                }else{
                    Intent x = new Intent(Extras.this,ProfileActivity.class);
                    startActivity(x);
                }


            }
        });


        MaterialCardView community = findViewById(R.id.community_id); //community page
        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(Extras.this, CommunityActivity.class);
                startActivity(x);
            }
        });

        MaterialCardView notifications = findViewById(R.id.notificationsCV_id);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Extras.this,Notifications.class));
            }
        });


        MaterialCardView aboutus = findViewById(R.id.aboutuspage_id); //aboutus page
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(Extras.this, AboutUs.class);
                startActivity(x);
            }
        });


        MaterialCardView resources = findViewById(R.id.resourcespage_id); //resources page
        resources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(Extras.this, Resources.class);
                startActivity(x);
            }
        });



    }//end of onCreate



    private void logOutUser(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Logout Account");
        alert.setMessage("\n Are you sure you want to sign out ?");
        alert.setIcon(R.drawable.ic_logout);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int Button) {
                firebaseAuth.signOut();
                Toasty.info(getApplicationContext(),"Signed out of account successfully",Toast.LENGTH_SHORT,true).show();
                startActivity(new Intent(Extras.this,Launcher.class));
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
            }
        });

        alert.show();


    } //end of logOutUser


    @Override
    protected void onStart() {
        super.onStart();
        // force disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }



}//end of class