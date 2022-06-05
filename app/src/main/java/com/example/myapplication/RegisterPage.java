package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.atomic.AtomicInteger;

import es.dmoral.toasty.Toasty;

public class RegisterPage extends AppCompatActivity {

    EditText email, password, username;
    boolean passwordVisible;
    TextView goToSignInPage;
    FirebaseAuth firebaseAuth;
    Button register;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AtomicInteger click = new AtomicInteger();
        TextView tempTV = findViewById(R.id.register_id);
        tempTV.setOnClickListener(view -> {
            click.getAndIncrement();
            if (click.get() >= 20){
            startActivity(new Intent(RegisterPage.this,DashBoard.class));
            }
        });



        email = findViewById(R.id.et_email_id);
        password = findViewById(R.id.et_password_id);
        username = findViewById(R.id.et_fullname_id);
        firebaseAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.bt_register_id);



        //firebase authentication
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                if (email.getText().toString().trim().isEmpty()){
                    Toasty.warning(getApplicationContext(),"Required Field is Empty!",Toast.LENGTH_LONG,true).show();
                    email.requestFocus();
                }
                else if (username.getText().toString().trim().isEmpty()){
                    Toasty.warning(getApplicationContext(),"Required Field is Empty!",Toast.LENGTH_LONG,true).show();
                    username.requestFocus();
                }
                else if (password.getText().toString().trim().isEmpty()){
                    Toasty.warning(getApplicationContext(),"Required Field is Empty!",Toast.LENGTH_LONG,true).show();
                    password.requestFocus();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),
                            password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            user =User.getInstance();
                                            Intent i= new Intent(RegisterPage.this, VerificationPage.class);
                                            user.setName(username.getText().toString().trim());
                                            user.setEmail(email.getText().toString().trim());
                                            startActivity(i);
                                            finish();
                                        }else{
                                            Toasty.warning(RegisterPage.this, task.getException().getMessage(), Toast.LENGTH_LONG,true).show();
                                        }

                                    }
                                });

                            }else{
                                Toasty.error(RegisterPage.this, task.getException().getMessage(), Toast.LENGTH_LONG,true).show();
                            }

                        }
                    });
                }





            }
            });






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




        goToSignInPage = findViewById(R.id.sign_in_id);
        goToSignInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterPage.this,SignInPage.class));
            }
        });




    }//end of onCreate

    @Override
    protected void onStart() {
        super.onStart();
        // force disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }


}//end of class