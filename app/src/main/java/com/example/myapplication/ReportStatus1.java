package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.mcdev.quantitizerlibrary.AnimationStyle;
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer;
import com.mcdev.quantitizerlibrary.QuantitizerListener;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class ReportStatus1 extends AppCompatActivity {

        //declare
    Button gotoreporttwo;
    ImageView backarrow;

    CheckBox cbPfizer,cbModerna,cbJJ,cbSinovac,cbAstra,cbNone;
    HorizontalQuantitizer pfizerCounter,modernaCounter,jjCounter,sinovacCounter,astraCounter;
    Map<String, Integer> Vaccines = new HashMap<>();
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_status1);

        //define
        gotoreporttwo = findViewById(R.id.bt_submit_id);
        backarrow = findViewById(R.id.backarrow_id);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportStatus1.this,UserInput.class));
            }
        });


        cbPfizer = findViewById(R.id.checkBox1);
        cbModerna = findViewById(R.id.checkBox2);
        cbJJ = findViewById(R.id.checkBox3);
        cbSinovac = findViewById(R.id.checkBox4);
        cbAstra = findViewById(R.id.checkBox5);
        cbNone = findViewById(R.id.checkBox6);

        pfizerCounter = findViewById(R.id.biontechVal_id);
        pfizerCounter.setTag("pfizer");
        pfizerCounter.setMinusIconBackgroundColor("#75FF647C");
        pfizerCounter.setTextAnimationStyle(AnimationStyle.FALL_IN);
        pfizerCounter.setAnimationDuration(300);
        pfizerCounter.setValueTextColor("#7C7C7C");
        pfizerCounter.setMinusIconColor("#FF0000");
        pfizerCounter.setReadOnly(true);//doesn't allow keyboard entry
        pfizerCounter.setMaxValue(6);
        pfizerCounter.setMinValue(1);
        pfizerCounter.setVisibility(View.INVISIBLE);

        modernaCounter = findViewById(R.id.modernaVal_id);
        modernaCounter.setTag("moderna");
        modernaCounter.setMinusIconBackgroundColor("#75FF647C");
        modernaCounter.setTextAnimationStyle(AnimationStyle.FALL_IN);
        modernaCounter.setAnimationDuration(300);
        modernaCounter.setValueTextColor("#7C7C7C");
        modernaCounter.setMinusIconColor("#FF0000");
        modernaCounter.setReadOnly(true);//doesn't allow keyboard entry
        modernaCounter.setMaxValue(6);
        modernaCounter.setMinValue(1);
        modernaCounter.setVisibility(View.INVISIBLE);

        jjCounter = findViewById(R.id.jjVal_id);
        jjCounter.setTag("jj");
        jjCounter.setMinusIconBackgroundColor("#75FF647C");
        jjCounter.setTextAnimationStyle(AnimationStyle.FALL_IN);
        jjCounter.setAnimationDuration(300);
        jjCounter.setValueTextColor("#7C7C7C");
        jjCounter.setMinusIconColor("#FF0000");
        jjCounter.setReadOnly(true);//doesn't allow keyboard entry
        jjCounter.setMaxValue(6);
        jjCounter.setMinValue(1);
        jjCounter.setVisibility(View.INVISIBLE);

        sinovacCounter = findViewById(R.id.sinovacVal_id);
        sinovacCounter.setTag("sinovac");
        sinovacCounter.setMinusIconBackgroundColor("#75FF647C");
        sinovacCounter.setTextAnimationStyle(AnimationStyle.FALL_IN);
        sinovacCounter.setAnimationDuration(300);
        sinovacCounter.setValueTextColor("#7C7C7C");
        sinovacCounter.setMinusIconColor("#FF0000");
        sinovacCounter.setReadOnly(true);//doesn't allow keyboard entry
        sinovacCounter.setMaxValue(6);
        sinovacCounter.setMinValue(1);
        sinovacCounter.setVisibility(View.INVISIBLE);

        astraCounter = findViewById(R.id.astraVal_id);
        astraCounter.setTag("astra");
        astraCounter.setMinusIconBackgroundColor("#75FF647C");
        astraCounter.setTextAnimationStyle(AnimationStyle.FALL_IN);
        astraCounter.setAnimationDuration(300);
        astraCounter.setValueTextColor("#7C7C7C");
        astraCounter.setMinusIconColor("#FF0000");
        astraCounter.setReadOnly(true);//doesn't allow keyboard entry
        astraCounter.setMaxValue(6);
        astraCounter.setMinValue(1);
        astraCounter.setVisibility(View.INVISIBLE);


        //disable/enable all options based on click checkbox
        cbNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cbPfizer.setEnabled(false);
                    cbPfizer.setChecked(false);

                    cbModerna.setEnabled(false);
                    cbModerna.setChecked(false);

                    cbJJ.setEnabled(false);
                    cbJJ.setChecked(false);

                    cbAstra.setEnabled(false);
                    cbAstra.setChecked(false);

                    cbSinovac.setEnabled(false);
                    cbSinovac.setChecked(false);
                }else{
                    cbPfizer.setEnabled(true);
                    cbModerna.setEnabled(true);
                    cbJJ.setEnabled(true);
                    cbAstra.setEnabled(true);
                    cbSinovac.setEnabled(true);
                }
            }
        });


        // listeners for the check boxes
        cbPfizer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    pfizerCounter.setVisibility(View.VISIBLE);
                }else{
                    pfizerCounter.setVisibility(View.INVISIBLE);
                }

            }
        });

        cbModerna.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    modernaCounter.setVisibility(View.VISIBLE);
                }else{
                    modernaCounter.setVisibility(View.INVISIBLE);
                }

            }
        });

        cbJJ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    jjCounter.setVisibility(View.VISIBLE);
                }else{
                    jjCounter.setVisibility(View.INVISIBLE);
                }

            }
        });

        cbSinovac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    sinovacCounter.setVisibility(View.VISIBLE);
                }else{
                    sinovacCounter.setVisibility(View.INVISIBLE);
                }

            }
        });

        cbAstra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    astraCounter.setVisibility(View.VISIBLE);
                }else{
                    astraCounter.setVisibility(View.INVISIBLE);
                }

            }
        });


        // Listeners for the quantities

        pfizerCounter.setQuantitizerListener(new QuantitizerListener() {
            @Override
            public void onIncrease() {
                if (pfizerCounter.getValue() == 6){
                    pfizerCounter.setValue(5);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Only 5 Doses Are Allowed For Each Vaccine", Snackbar.LENGTH_LONG);
                    snackbar.show(); }
            }
            @Override
            public void onDecrease() {
            }

            @Override
            public void onValueChanged(int i) {
            }
        });

        modernaCounter.setQuantitizerListener(new QuantitizerListener() {
            @Override
            public void onIncrease() {
                if (modernaCounter.getValue() == 6){
                    modernaCounter.setValue(5);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Only 5 Doses Are Allowed For Each Vaccine", Snackbar.LENGTH_LONG);
                    snackbar.show(); }
            }
            @Override
            public void onDecrease() {
            }

            @Override
            public void onValueChanged(int i) {
            }
        });

        jjCounter.setQuantitizerListener(new QuantitizerListener() {
            @Override
            public void onIncrease() {
                if (jjCounter.getValue() == 6){
                    jjCounter.setValue(5);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Only 5 Doses Are Allowed For Each Vaccine", Snackbar.LENGTH_LONG);
                    snackbar.show(); }

            }
            @Override
            public void onDecrease() {
            }

            @Override
            public void onValueChanged(int i) {
            }
        });

        sinovacCounter.setQuantitizerListener(new QuantitizerListener() {
            @Override
            public void onIncrease() {
                if (sinovacCounter.getValue() == 6){
                    sinovacCounter.setValue(5);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Only 5 Doses Are Allowed For Each Vaccine", Snackbar.LENGTH_LONG);
                    snackbar.show(); }
            }
            @Override
            public void onDecrease() {
            }

            @Override
            public void onValueChanged(int i) {
            }
        });

        astraCounter.setQuantitizerListener(new QuantitizerListener() {
            @Override
            public void onIncrease() {
                if (astraCounter.getValue() == 6) {
                    astraCounter.setValue(5);
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Only 5 Doses Are Allowed For Each Vaccine", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
            @Override
            public void onDecrease() {
            }

            @Override
            public void onValueChanged(int i) {
            }
        });


        gotoreporttwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    // check if no option was selected
                if (!cbPfizer.isChecked() && !cbModerna.isChecked() && !cbJJ.isChecked() && !cbSinovac.isChecked() && !cbAstra.isChecked() && !cbNone.isChecked()){
                    Toasty.warning(ReportStatus1.this,"Please Select an Option!", Toast.LENGTH_LONG).show();
                }else{

                    user = User.getInstance();
                    HorizontalQuantitizer[] counters = {pfizerCounter, modernaCounter, jjCounter, sinovacCounter, astraCounter};

                    // search in array called counters for visible ones and set the visible one's tags string attribute to Vaccines Map along with the corresponding values
                    for (HorizontalQuantitizer c : counters){
                        if (c.getVisibility() == View.VISIBLE){
                            Vaccines.put(c.getTag().toString(),c.getValue());
                        }
                    }

                    user.setVaccines(Vaccines);

                    startActivity(new Intent(ReportStatus1.this,ReportStatus2.class));
                }

            }
        });


    }//end of onCreate


}//end of class