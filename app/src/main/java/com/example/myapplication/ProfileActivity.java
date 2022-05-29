package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ToggleButton;

public class ProfileActivity extends AppCompatActivity {

    //Declare

        ToggleButton edit;
        Button resetPass,save;
        EditText etName,etEmail;
        RadioButton rbMale,rbFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Define
            resetPass = findViewById(R.id.bt_profile_passReset_id);
            save = findViewById(R.id.btn_profile_save_id);
            etName = findViewById(R.id.et_profile_name_id);
            etEmail = findViewById(R.id.et_profile_email_id);
            rbMale = findViewById(R.id.rbmale_profile_id);
            rbFemale = findViewById(R.id.rbfemale_profile_id);

            edit = findViewById(R.id.toggleEdit_id);
            edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        resetPass.setEnabled(true);
                        save.setVisibility(View.VISIBLE);
                        etName.setEnabled(true);
                        etEmail.setEnabled(true);
                        rbFemale.setEnabled(true);
                        rbMale.setEnabled(true);
                    }else{

                        resetPass.setEnabled(false);
                        save.setVisibility(View.GONE);
                        etName.setEnabled(false);
                        etEmail.setEnabled(false);
                        rbFemale.setEnabled(false);
                        rbMale.setEnabled(false);

                    }
                }
            });

    }//end of onCreate


}//end of class