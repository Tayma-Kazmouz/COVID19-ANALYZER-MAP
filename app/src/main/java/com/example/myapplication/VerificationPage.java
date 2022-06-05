package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class VerificationPage extends AppCompatActivity {


    //declare

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);

        //define
        firebaseAuth = FirebaseAuth.getInstance();


    }//end of onCreate

    public void gotosigninpage(View view) {


        user = firebaseAuth.getCurrentUser();
        assert user != null;
        user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                boolean userVerified = firebaseAuth.getCurrentUser().isEmailVerified();

                if (userVerified){
                    Toasty.success(getApplicationContext(), "Email address verified!", Toast.LENGTH_SHORT, true).show();
                    Intent i= new Intent(VerificationPage.this, UserInput.class);
                    startActivity(i);
                    finish();

                } else{
                    Toasty.info(getApplicationContext(), "Please verify your email!", Toast.LENGTH_SHORT, true).show();
                }

            }
        });


    }//end of function

    @Override
    public void onBackPressed() {
        Toasty.info(getApplicationContext(), "Please verify your email!", Toast.LENGTH_SHORT, true).show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        // force disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

}//end of class