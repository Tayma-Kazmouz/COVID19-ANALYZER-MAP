package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blongho.country_data.World;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class SignInPage extends AppCompatActivity {

    EditText email, password;
    Button signin;

    FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    boolean passwordVisible;
    TextView gotoregisterpage;
    TextView goToForgotPassword;
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

        goToForgotPassword = findViewById(R.id.forgotPassword_id);
        goToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInPage.this,ForgotPassword.class));
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

    /*
     here we know email is verified and it'll check if the account has been correctly
     set up in the Users collection and transfer the user accordingly to an activity
     */
                                    db = FirebaseFirestore.getInstance();
                                    db.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()){
                                                Intent i= new Intent(SignInPage.this, DashBoard.class);
                                                startActivity(i);
                                                finish();
                                            }else{
                                                Toasty.info(getApplicationContext(),"Please enter your information..", Toast.LENGTH_LONG,true).show();
                                                User.getInstance().setEmail(email.getText().toString().trim());
                                                askForName();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toasty.error(SignInPage.this, "Error Occurred, try again later", Toast.LENGTH_LONG,true).show();
                                            Log.e("x", "onFailure: " + e.getMessage() );
                                        }
                                    });


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



    }//end of onCreate

    private void askForName(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Setup Account");
        alert.setMessage("Enter Your Full Name");
        alert.setIcon(R.drawable.ic_myprofile);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(50, 20, 50, 20);


        final EditText fullName = new EditText(this);
        fullName.setBackgroundResource(R.drawable.edittext_newlook);
        fullName.setHint("  Full Name");
        // set max chars of 30 for the name
        fullName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});

        ll.addView(fullName, layoutParams);

        alert.setView(ll);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int Button) {
                User.getInstance().setName(fullName.getText().toString().trim());
                startActivity(new Intent(SignInPage.this,UserInput.class));
                finish();
            }
        });
        alert.show();
    }




}//end of class