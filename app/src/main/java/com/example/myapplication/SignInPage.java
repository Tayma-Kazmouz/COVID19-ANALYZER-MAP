package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blongho.country_data.World;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class SignInPage extends AppCompatActivity {

    TextView goToDashBoard;
    EditText email, password;
    Button signin;
    FirebaseAuth firebaseAuth;
    boolean passwordVisible;
    TextView gotoregisterpage;
    TextView gotolottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);


        email = findViewById(R.id.et_email_id);
        password = findViewById(R.id.et_password_id);
        firebaseAuth = FirebaseAuth.getInstance();
        signin = findViewById(R.id.bt_signin_id);


        //takes you back to register page
        gotoregisterpage = findViewById(R.id.register_id);
        gotoregisterpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInPage.this, RegisterPage.class));
            }
        });

        //Firebase login
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().trim().isEmpty()){
                    Toasty.warning(getApplicationContext(),"Required Field is Empty!",Toast.LENGTH_LONG,true).show();
                    email.requestFocus();
                }
                else if (password.getText().toString().trim().isEmpty()){
                    Toasty.warning(getApplicationContext(),"Required Field is Empty!",Toast.LENGTH_LONG,true).show();
                    password.requestFocus();
                }else{



                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if (firebaseAuth.getCurrentUser().isEmailVerified()){
//                                if ("ACCOUNT SET UP CORRECTLY"){
                                    World.init(getApplicationContext()); // Initializes the countries library and loads all data
                                    Intent i= new Intent(SignInPage.this, DashBoard.class);
                                    startActivity(i);
                                    finish();
//                                }else{
//                                    Toasty.info(SignInPage.this,"Please enter your information..", Toast.LENGTH_LONG,true).show();
//                                    startActivity(new Intent(SignInPage.this,UserInput.class));
//                                }
                                }else{
                                    Toasty.info(SignInPage.this,"Please verify your email!", Toast.LENGTH_LONG,true).show();
                                    startActivity(new Intent(SignInPage.this,VerificationPage.class));
                                    finish();
                                }
                            }else{
                                Toasty.error(SignInPage.this, task.getException().getMessage(), Toast.LENGTH_LONG,true).show();
                            }
                        }
                    });


                }



            }
        });



        //password icon functionality

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if (event.getAction()==MotionEvent.ACTION_UP) {
                    if(event.getRawX()>=password.getRight()-password.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection=password.getSelectionEnd();
                        if(passwordVisible) {
                            //set drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.passwordicon, 0);
                            //for hide password
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }else {
                            //set drawable image here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.showpasswordicon, 0);
                            //for hide password
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        password.setSelection(selection);
                        return true;

                    }
                }
                return false;
            }
        });



    }
}