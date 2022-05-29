package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class ForgotPassword extends AppCompatActivity {


    Button reset;
    EditText Email;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Email = findViewById(R.id.et_fp_email_id);

        reset = findViewById(R.id.btn_forgotpassword_id);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString().trim();

                if (email.isEmpty()){
                    Toasty.warning(getApplicationContext(),"Required Field is Empty!", Toast.LENGTH_LONG,true).show();
                    Email.requestFocus();
                }else{
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toasty.success(getApplicationContext(),"A reset password email is sent", Toast.LENGTH_LONG,true).show();
                            startActivity(new Intent(ForgotPassword.this,SignInPage.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG,true).show();
                        }
                    });

                }

            }
        });



    }//end of onCreate



}//end of class