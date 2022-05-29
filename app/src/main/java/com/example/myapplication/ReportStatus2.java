package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

        User user = User.getInstance();
        Log.e("x", "onCreate: " + user.getName() +" "+user.getEmail()+" "+user.getCountry() +" "+user.getGender()+" "+user.getDob() +" "+ user.getVaccines().toString());

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

                    // start a document in Firestore with the user's unique id and set the values as user object
                    db.collection("Users").document(uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            successDialog();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(getApplicationContext(), "Storing details failed!", Toast.LENGTH_SHORT, true).show();
                        }
                    });

                }
            }
        });





    }

    private void successDialog() {

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
    }

}