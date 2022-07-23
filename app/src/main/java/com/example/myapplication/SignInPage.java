package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SignInPage extends AppCompatActivity {

    EditText email, password;
    Button signin;

    FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private DocumentReference refUsers;
    private DocumentReference refReports;

    boolean passwordVisible;
    TextView gotoregisterpage;
    TextView goToForgotPassword;


    String  mainKeyAlias;
    SharedPreferences encSharedPreferences;
    SharedPreferences.Editor enEdit;
    String ENCRYPTED_SHARED_PREFERENCE = "Encrypted_User_SP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);


        email = findViewById(R.id.et_email_id);
        password = findViewById(R.id.et_password_id);
        firebaseAuth = FirebaseAuth.getInstance();
        signin = findViewById(R.id.bt_signin_id);


        db = FirebaseFirestore.getInstance();
        refReports = db.collection("Report").document("aggregate");


        try {
            mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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

                                    /*
                                     an encrypted Shared Preference File is used for 2 reasons:
                                        1: check if user has correctly set up account and entered all their details from UserInput class onwards ... this is done from SP to reduce number of reads to FB
                                        2: check and DOB with current date and check if user's ageGroup has changed and write to Firebase accordingly ... this is done to keep the data always up to date
                                     */



                                    // Read from EN_SP to load password
                                    try {
                                        mainKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
                                        // We Pass The name & keyAlias defined above + Context , key encryption , value encryption .
                                        encSharedPreferences = EncryptedSharedPreferences.create(
                                                ENCRYPTED_SHARED_PREFERENCE,
                                                mainKeyAlias,
                                                getApplicationContext(),
                                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    if (encSharedPreferences.contains("DOB")){ // if it contains DOB val it means we already signed in before / collected user input correctly

                                        Log.e("x", "Shared Preferences Found" );

                                        String sp_DOB = encSharedPreferences.getString("DOB","");
                                        String sp_AGE = encSharedPreferences.getString("AGE","");

                                        Log.e("x", "sp_DOB = "+sp_DOB + " sp_AGE = "+sp_AGE);

                                        findCurrentAgeSPExists(sp_DOB,sp_AGE); // send to find current age function to decide to update age or not


                                    }else { // if it doesnt contain the sp file then read from Firebase DB if doc exits -> make sp and find current age // doc !exist -> transfer user to enter details

                                        Log.e("x", "Shared Preferences NOT Found" );

                                        refUsers =db.collection("Users").document(firebaseAuth.getCurrentUser().getUid());

                                        refUsers.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.contains("gender")){


                                                    String DOB = documentSnapshot.getString("dob");
                                                    String AGE = (String.valueOf(documentSnapshot.getLong("age")));
                                                    String COVID = documentSnapshot.getString("covid");
                                                    Map<String, Long> VaccinesMap = (Map<String, Long>) documentSnapshot.get("vaccines");


                                                    // ENCRYPTED USER SP

                                                    try {


                                                        // We Pass The name & keyAlias defined above + Context , key encryption , value encryption .
                                                        encSharedPreferences = EncryptedSharedPreferences.create(
                                                                ENCRYPTED_SHARED_PREFERENCE,
                                                                mainKeyAlias,
                                                                getApplicationContext(),
                                                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);

                                                        enEdit = encSharedPreferences.edit();
                                                        // Edit/Write the user's Password shared preferences...
                                                        enEdit.putString("DOB",DOB);
                                                        enEdit.putString("AGE",AGE);
                                                        enEdit.apply();

                                                        findCurrentAge(DOB,AGE,COVID,VaccinesMap);

                                                    } catch (Exception e) {
                                                        e.printStackTrace(); }




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


                                    }


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


    void findCurrentAge(String DOB, String AGE, String COVID, Map<String, Long> vaccinesMap){


        String Year = DOB.substring(DOB.lastIndexOf("/")+1);
        String Month = DOB.substring(DOB.indexOf("/")+1,DOB.lastIndexOf("/"));
        String Day = DOB.substring(0,DOB.indexOf("/"));

        int year = Integer.valueOf(Year.trim());
        int month = getReverseMonthFormat(Month.trim());
        int day = Integer.valueOf(Day.trim());

        Calendar today = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Long newAge = (long) age;

        if (newAge == -1)
            newAge = 0L ;


        Long oldAge = Long.valueOf(AGE);

        // if the group of the new age calculated above does not match the old stored value then write to Firestore with new values
        String oldAgeGroup =getAgeGroup(oldAge) ;
        String newAgeGroup = getAgeGroup(newAge) ;
        if (!oldAgeGroup.equals(newAgeGroup)){

            Log.e("x", "Shared Preferences NOT Found : findCurrentAge -> age groups are different!" );

            writeNewDetails(newAge,oldAgeGroup,newAgeGroup,COVID,vaccinesMap);


        }else {
            Log.e("x", "Shared Preferences NOT Found : findCurrentAge -> age groups are same , oldAgeGroup: "+oldAgeGroup+" newAgeGroup: "+newAgeGroup +" oldAge: "+oldAge + " newAge "+ newAge);
         Intent i= new Intent(SignInPage.this, DashBoard.class);
         startActivity(i);
         finish();
        }

    }//end of findCurrentAge()


    void findCurrentAgeSPExists(String DOB, String AGE){


        String Year = DOB.substring(DOB.lastIndexOf("/")+1);
        String Month = DOB.substring(DOB.indexOf("/")+1,DOB.lastIndexOf("/"));
        String Day = DOB.substring(0,DOB.indexOf("/"));

        int year = Integer.valueOf(Year.trim());
        int month = getReverseMonthFormat(Month.trim());
        int day = Integer.valueOf(Day.trim());

        Calendar today = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Long newAge = (long) age;

        if (newAge == -1)
            newAge = 0L ;


        Long oldAge = Long.valueOf(AGE);

        // if the group of the new age calculated above does not match the old stored value then write to Firestore with new values
        String oldAgeGroup =getAgeGroup(oldAge) ;
        String newAgeGroup = getAgeGroup(newAge) ;
        if (!oldAgeGroup.equals(newAgeGroup)){

            Log.e("x", "Shared Preferences Found : findCurrentAge -> age groups are different! ... read for condition and vaccines now" );

            refUsers =db.collection("Users").document(firebaseAuth.getCurrentUser().getUid());

            Long finalNewAge = newAge;
            refUsers.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String COVID = documentSnapshot.getString("covid");
                    Map<String, Long> VaccinesMap = (Map<String, Long>) documentSnapshot.get("vaccines");

                    writeNewDetails(finalNewAge,oldAgeGroup,newAgeGroup,COVID,VaccinesMap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toasty.error(SignInPage.this, "Error Occurred, try again later", Toast.LENGTH_LONG,true).show();
                    Log.e("x", "onFailure: " + e.getMessage() );
                }
            });


        }else {
            Log.e("x", "Shared Preferences Found : findCurrentAge -> age groups are same , oldAgeGroup: "+oldAgeGroup+" newAgeGroup: "+newAgeGroup +" oldAge: "+oldAge + " newAge "+ newAge);
            Intent i= new Intent(SignInPage.this, DashBoard.class);
            startActivity(i);
            finish();
        }

    }//end of findCurrentAgeSPExists()


    private void writeNewDetails( Long newAge, String oldAgeGroup, String newAgeGroup, String COVID, Map<String, Long> vaccinesMap) {


        Map<String,Object> deleteOldData = new HashMap<>(); // decrements

        Map<String, Object> aggregateMap = new HashMap<>(); // increments


//         ____________________________ Reset Vaccines Data (Decrement) ____________________________________

        // if user got no vaccines... decrement "nonev" in aggregate document

        if (vaccinesMap.isEmpty()){
            deleteOldData.put("nonev."+oldAgeGroup, FieldValue.increment(-1));
        }else {

            // iterate of user's  old vaccine map keys to decrement them in Firestore in the code below

            for (String vaccine : vaccinesMap.keySet()){
                deleteOldData.put(vaccine.toLowerCase().trim()+"."+oldAgeGroup, FieldValue.increment(-1));
            }
        }
        // ____________________________ Reset Covid Condition Data (Decrement) ____________________________________

        deleteOldData.put(COVID+"."+oldAgeGroup, FieldValue.increment(-1));

        // ____________________________ New Vaccines Data (Increment) ____________________________________

        // if user got no vaccines... Increment "nonev" in aggregate document

        if (vaccinesMap.isEmpty()){
            aggregateMap.put("nonev."+newAgeGroup, FieldValue.increment(1));
        }else {

            // iterate of user's  old vaccine map keys to increment them in Firestore in the code below
            for (String vaccine : vaccinesMap.keySet()){
                aggregateMap.put(vaccine.toLowerCase().trim()+"."+newAgeGroup, FieldValue.increment(1));
            }
        }
        // ____________________________ New Covid Condition Data (Increment) ____________________________________

        aggregateMap.put(COVID+"."+newAgeGroup, FieldValue.increment(1));


        // make a user write batch
        WriteBatch writeBatchEdit = db.batch();

        refUsers =db.collection("Users").document(firebaseAuth.getCurrentUser().getUid());

        refUsers.update("age",newAge);


        refReports.update(deleteOldData);
        refReports.update(aggregateMap);


        // commit the Users Write, Reports Update batch
        writeBatchEdit.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toasty.success(getApplicationContext(), "User age has changed... updated details.", Toast.LENGTH_SHORT, true).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), "Updating user age failed! try again later..", Toast.LENGTH_SHORT, true).show();
            }
        });



    }//end of writeNewDetails




    private String getAgeGroup(Long userAge) {
        if (userAge <= 10){
            return "age10";
        }else if (userAge < 20){
            return "age11";
        }else if (userAge <30){
            return  "age20";
        }else  if (userAge <40){
            return "age30";
        } else  if (userAge <50){
            return  "age40";
        }else  if (userAge <60){
            return  "age50";
        }else if (userAge <70){
            return  "age60";
        }else if (userAge <80){
            return "age70";
        }else {
            return "age80";
        }
    }//end of getAgeGroup



    private int getReverseMonthFormat(String month) {

        int monthNum;
        switch (month){

            case "JAN" :
                monthNum =1;
                break;
            case "FEB" :
                monthNum = 2;
                break;
            case "MAR" :
                monthNum = 3;
                break;
            case "APR" :
                monthNum = 4;
                break;
            case "MAY" :
                monthNum = 5;
                break;
            case "JUN" :
                monthNum = 6;
                break;
            case "JUL" :
                monthNum = 7;
                break;
            case "AUG" :
                monthNum = 8;
                break;
            case "SEP" :
                monthNum = 9;
                break;
            case "OCT" :
                monthNum = 10;
                break;
            case "NOV" :
                monthNum = 11;
                break;
            case "DEC" :
                monthNum = 12;
                break;
            default:
                monthNum = 1;
                break;
            //Default should never happen
        }
        return monthNum;
    }//end of getReverseMonth



    @Override
    protected void onStart() {
        super.onStart();
        // force disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }


}//end of class