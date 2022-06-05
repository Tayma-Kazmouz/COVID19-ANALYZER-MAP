package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ReportStatus2 extends AppCompatActivity {

    Button gotodashboard;
    ImageView backarrow;
    RadioButton rbPositive,rbNegative,rbContacted,rbNone;
    String Condition;

    User user;
    private String uid; // unique user id
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private DocumentReference refUsers;
    private DocumentReference refReports;

    boolean correctlySetup;
    String ageGroup;
    String gender;
    Map<String,Object> aggregateMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_status2);

        gotodashboard = findViewById(R.id.bt_continue_id);
        backarrow = findViewById(R.id.backarrow_id);
        rbPositive = findViewById(R.id.rb1_id);
        rbPositive.setTag("positive");
        rbNegative = findViewById(R.id.rb2_id);
        rbNegative.setTag("negative");
        rbContacted = findViewById(R.id.rb3_id);
        rbContacted.setTag("contacted");
        rbNone = findViewById(R.id.rb4_id);
        rbNone.setTag("none");

        correctlySetup = false;

        user = User.getInstance();

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportStatus2.this,ReportStatus1.class));
            }
        });


        gotodashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check if no option was selected
                if (!rbPositive.isChecked() && !rbNegative.isChecked() && !rbContacted.isChecked() && !rbNone.isChecked()){
                    Toasty.warning(ReportStatus2.this,"Please Select an Option!", Toast.LENGTH_LONG).show();
                }else{

                    // search in array of rbs for checked option and set its tag string to Condition
                    RadioButton[] rbs = {rbPositive,rbNegative,rbContacted,rbNone};
                    for (RadioButton rb:rbs) {
                        if (rb.isChecked()){
                            Condition = rb.getTag().toString().trim();
                        } }

                    user.setCovid(Condition);


                   // connect to Firestore
                    db = FirebaseFirestore.getInstance();
                    firebaseAuth = FirebaseAuth.getInstance();
                    uid = firebaseAuth.getCurrentUser().getUid();

                    // store user data in Firebase Firestore
                    makeBatchWrite();

                }
            }
        });





    }

    private String getAgeGroup() {
        Long userAge = user.getAge();
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

    //atomically (all or nothing approach) write to Users and Update Reports Collections
    private void makeBatchWrite(){

        //these will be used to refer to the correct fields in "aggregate" document
        ageGroup = getAgeGroup().trim();
        gender = user.getGender().toLowerCase().trim();

        // ____________________________ Aggregate Vaccines Data ____________________________________

        // if user got no vaccines... increment "nonev" in aggregate document
        if (user.getVaccines().isEmpty()){
            aggregateMap.put("nonev."+ageGroup, FieldValue.increment(1));
            aggregateMap.put("nonev."+gender, FieldValue.increment(1));
        }else {

            // iterate of user's vaccine map keys to increment them in Firestore in the code below
            for (String vaccine : user.getVaccines().keySet()){
                aggregateMap.put(vaccine.toLowerCase().trim()+"."+ageGroup, FieldValue.increment(1));
                aggregateMap.put(vaccine.toLowerCase().trim()+"."+gender, FieldValue.increment(1));
            }
        }
        // ____________________________ Aggregate Covid Condition Data ____________________________________

        aggregateMap.put(Condition+"."+ageGroup, FieldValue.increment(1));
        aggregateMap.put(Condition+"."+gender, FieldValue.increment(1));

        // ____________________________ Aggregate males/females fields  ____________________________________
        if (gender.equals("m")){
            aggregateMap.put("males", FieldValue.increment(1));
        }else {
            aggregateMap.put("females", FieldValue.increment(1));
        }

        // make a user write batch
        WriteBatch userWriteBatch = db.batch();

        // start a document in Firestore with the user's unique id and set the values as user object
        refUsers = db.collection("Users").document(uid);

        // refer to aggregate doc which contains all the users collected data
        refReports = db.collection("Report").document("aggregate");

        refUsers.set(user);

        refReports.update(aggregateMap);


        // commit the Users Write, Reports Update batch
        userWriteBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                    successDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), "Storing details failed! try again later..", Toast.LENGTH_SHORT, true).show();
            }
        });

    }// end of db write function


    private void successDialog() {

        correctlySetup = true;

        Dialog dialog=new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.setup_success);
        dialog.show();

        new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub


                startActivity(new Intent(ReportStatus2.this,DashBoard.class));
                finish();

            }
        }.start();

    }//end of successDialog

    @Override
    public void onBackPressed() {
        if (correctlySetup){
            return;
        }else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // force disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

}//end of class