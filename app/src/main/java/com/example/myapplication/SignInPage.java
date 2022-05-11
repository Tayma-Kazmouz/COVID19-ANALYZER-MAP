package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
                startActivity(new Intent(SignInPage.this,MainActivity.class));
            }
        });

        //Firebase login
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if (firebaseAuth.getCurrentUser().isEmailVerified()){
                                Intent i= new Intent(SignInPage.this, UserInput.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(SignInPage.this,"Please verify your email!", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(SignInPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


//    //going to lottie test
//    gotolottie= findViewById(R.id.tv_donthaveacc_id);
//    gotolottie.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            startActivity(new Intent(SignInPage.this,NewLottie.class));
//        }
//    });
















//        //takes you to dashboard
//        goToDashBoard = findViewById(R.id.bt_signin_id);
//        goToDashBoard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(SignInPage.this,DashBoard.class));
//            }
//        });


        //password icon fucntionality
        email = findViewById(R.id.et_email_id);
        password = findViewById(R.id.et_password_id);

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