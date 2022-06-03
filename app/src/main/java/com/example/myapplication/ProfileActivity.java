package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.blongho.country_data.Country;
import com.blongho.country_data.World;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.WriteBatch;
import com.mcdev.quantitizerlibrary.AnimationStyle;
import com.mcdev.quantitizerlibrary.HorizontalQuantitizer;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity {

    //Declare

        ToggleButton edit;
        Button resetPass,save,btnDOB;
        EditText etName,etEmail;
        RadioButton rbMale,rbFemale;
        boolean m,f;
        ConstraintLayout dobSelectCl;
        LinearLayout countryProfileLL;
        TextView tvDOB,tvAge,tvCountry,tvCondition,tvVaccine;
        Spinner profileCountrySpinner;
        Spinner profileConditionSpinner;
        ImageView editVacc,ivFlag;

        TextView tvEditedVaccines;


        String uFullName;
        String uEmail;
        String uDOB;
        Long uAge;
        Long newAge;
        String uCountry;
        String uGender;
        String uCovid;
        String uVaccines;
        Map<String, Long> uVaccinesMap;
        Map<String, Long> newVaccinesMap;

        private FirebaseAuth firebaseAuth;
        private FirebaseUser fbUser;
        private FirebaseFirestore db;
        private DocumentReference refUsers;
        private DocumentReference refReports;
        String uid;

        private DatePickerDialog datePickerDialog;
        CountryPickerAdapter adapter;
        ArrayAdapter<String> conditionsAdapter;

        List<Country> libCountriesList;
        String [] countryNames;

        User user;

        Calendar dob,today;

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
            dobSelectCl = findViewById(R.id.dateSelector_profile_id);
            btnDOB = findViewById(R.id.dobbtn_profile_id);
            countryProfileLL = findViewById(R.id.profileCountry_id);
            tvDOB = findViewById(R.id.tvdobProfile_id);
            tvAge = findViewById(R.id.tvageProfile_id);
            profileCountrySpinner = findViewById(R.id.spinnerProfile_id);
            profileConditionSpinner = findViewById(R.id.spinner_Condition_Profile_id);
            editVacc = findViewById(R.id.btnEditVaccProfile_id);
            ivFlag = findViewById(R.id.ivCountryFlagProfile_id);
            tvCountry = findViewById(R.id.tvCountryProfile_id);
            tvVaccine = findViewById(R.id.vaccinesDataProfile_id);
            tvCondition = findViewById(R.id.profile_covidCondition_id);
            tvEditedVaccines = findViewById(R.id.vaccinesDataEditedProfile_id);


            db = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            fbUser = firebaseAuth.getCurrentUser();
            uid = fbUser.getUid();

            // initialize as null compare in save onClick later...
            newVaccinesMap =  null;

            //what happens when user clicks on edit toggle button... switch to edit mode
            edit = findViewById(R.id.toggleEdit_id);
            edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        resetPass.setEnabled(true);
                        save.setVisibility(View.VISIBLE);
                        etName.setEnabled(true);
//                        etEmail.setEnabled(true);
                        rbFemale.setEnabled(true);
                        rbMale.setEnabled(true);
                        dobSelectCl.setVisibility(View.VISIBLE);
                        tvDOB.setVisibility(View.GONE);
                        tvAge.setVisibility(View.GONE);
                        tvCondition.setVisibility(View.GONE);
                        profileCountrySpinner.setVisibility(View.VISIBLE);
                        profileConditionSpinner.setVisibility(View.VISIBLE);
                        countryProfileLL.setVisibility(View.GONE);
                        editVacc.setVisibility(View.VISIBLE);
                        tvVaccine.setVisibility(View.GONE);
                        tvEditedVaccines.setVisibility(View.VISIBLE);
                    }else{
                        resetPass.setEnabled(false);
                        save.setVisibility(View.GONE);
                        etName.setEnabled(false);
                        etEmail.setEnabled(false);
                        rbFemale.setEnabled(false);
                        rbMale.setEnabled(false);
                        rbMale.setChecked(m);
                        rbFemale.setChecked(f);
                        dobSelectCl.setVisibility(View.GONE);
                        tvDOB.setVisibility(View.VISIBLE);
                        tvAge.setVisibility(View.VISIBLE);
                        tvCondition.setVisibility(View.VISIBLE);
                        profileCountrySpinner.setVisibility(View.GONE);
                        profileConditionSpinner.setVisibility(View.GONE);
                        countryProfileLL.setVisibility(View.VISIBLE);
                        editVacc.setVisibility(View.GONE);
                        tvVaccine.setVisibility(View.VISIBLE);
                        tvEditedVaccines.setVisibility(View.GONE);

                    }
                }
            });

                // when date picker is clicked
            btnDOB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datePickerDialog.show();
                }
            });

            // when vaccines edit iv is clicked
            editVacc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editVaccClicked();
                }
            });


            // setting up the Covid Conditions Spinner
        String[] covidConditions ={"Positive","Negative","Contacted","None"};
        conditionsAdapter = new ArrayAdapter<String>(ProfileActivity.this,
        android.R.layout.simple_spinner_dropdown_item,
        covidConditions);


        profileConditionSpinner.setAdapter(conditionsAdapter);
        profileConditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#999999"));
                ((TextView) adapterView.getChildAt(0)).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveClicked();
            }
        });


        ImageView backArrow = findViewById(R.id.ivProfileBackArrow_id);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,Extras.class));
            }
        });


    }//end of onCreate

    private void onSaveClicked() {
        String Name = etName.getText().toString().trim();
        String DOB = makeDateString(datePickerDialog.getDatePicker().getDayOfMonth(),datePickerDialog.getDatePicker().getMonth()+1,datePickerDialog.getDatePicker().getYear());
        String Country = profileCountrySpinner.getSelectedItem().toString().trim();
        Country = Country.substring(Country.lastIndexOf("(")+1,Country.lastIndexOf(")"));
        String Gender = rbMale.isChecked() ?"M" : "F";
        String Condition = profileConditionSpinner.getSelectedItem().toString().trim().toLowerCase();


        // check if old data and new data are identical...
        if (uFullName.equals(Name) &&
            uDOB.equals(DOB) &&
            uCountry.equals(Country) &&
            uGender.equals(Gender) &&
            uCovid.equals(Condition) && ( hashMapsEqualIgnoreOrder(uVaccinesMap,newVaccinesMap)|| newVaccinesMap == null)
           )
        {
            Toasty.warning(this, "No Data Has Changed!", Toast.LENGTH_SHORT,true).show();
        }
        else{



            user = User.getInstance();

            user.setName(Name);
            user.setEmail(uEmail);
            user.setDob(DOB);
            user.setCountry(Country);
            user.setGender(Gender);
            user.setCovid(Condition);

            today = Calendar.getInstance();
            dob = Calendar.getInstance();

            dob.set(datePickerDialog.getDatePicker().getYear(), datePickerDialog.getDatePicker().getMonth()+1, datePickerDialog.getDatePicker().getDayOfMonth());

            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                age--;
            }

            newAge = (long) age;

            if (newAge == -1)
                newAge = 0L ;


            user.setAge(newAge);

            // if vaccines didnt change set old vaccine map else the new map
            if (newVaccinesMap == null){
                user.setVaccines(uVaccinesMap);

                    newVaccinesMap = new HashMap<>();
                for (Map.Entry<String,Long> v : uVaccinesMap.entrySet()) {
                    newVaccinesMap.put(v.getKey(),v.getValue());
                }

            }else{
                user.setVaccines(newVaccinesMap);
            }


            String oldAgeGroup = getAgeGroup(uAge).trim();
            String newAgeGroup = getAgeGroup(newAge).trim();
            String oldGender = uGender.toLowerCase().trim();
            String newGender = Gender.toLowerCase().trim();
            String oldCondition = uCovid.toLowerCase().trim();
            String newCondition = Condition.toLowerCase().trim();


            makeBatchEdit(oldAgeGroup,newAgeGroup,oldGender,newGender,oldCondition,newCondition);

        }


    }//end of onSave Clicked


    //atomically (all or nothing approach) write to Users and Update Reports Collections
    private void makeBatchEdit(String oldAgeGroup,String newAgeGroup,String oldGender,String newGender,String oldCondition,String newCondition){

        Log.e("x", "makeBatchEdit: oldGender : "+oldGender+" New Gender: "+newGender +" oldAge : "+oldAgeGroup + " new AgeGroup : "+ newAgeGroup );



        Map<String,Object> deleteOldData = new HashMap<>(); // decrements


        Map<String, Object> aggregateMap = new HashMap<>(); // increments



//         ____________________________ Reset Vaccines Data (Decrement) ____________________________________

        // if user got no vaccines... decrement "nonev" in aggregate document

        if (uVaccinesMap.isEmpty()){
            deleteOldData.put("nonev."+oldAgeGroup, FieldValue.increment(-1));
            deleteOldData.put("nonev."+oldGender, FieldValue.increment(-1));
        }else {

            // iterate of user's  old vaccine map keys to decrement them in Firestore in the code below

            for (String vaccine : uVaccinesMap.keySet()){
                deleteOldData.put(vaccine.toLowerCase().trim()+"."+oldAgeGroup, FieldValue.increment(-1));
                deleteOldData.put(vaccine.toLowerCase().trim()+"."+oldGender, FieldValue.increment(-1));
            }
        }
        // ____________________________ Reset Covid Condition Data (Decrement) ____________________________________

        deleteOldData.put(oldCondition+"."+oldAgeGroup, FieldValue.increment(-1));
        deleteOldData.put(oldCondition+"."+oldGender, FieldValue.increment(-1));

        // ____________________________ Reset males/females fields (Decrement)  ____________________________________
        if (oldGender.equals("m")){
            deleteOldData.put("males", FieldValue.increment(-1));
        }else {
            deleteOldData.put("females", FieldValue.increment(-1));
        }


        // ____________________________ New Vaccines Data (Increment) ____________________________________

        // if user got no vaccines... Increment "nonev" in aggregate document

        if (newVaccinesMap.isEmpty()){
            aggregateMap.put("nonev."+newAgeGroup, FieldValue.increment(1));
            aggregateMap.put("nonev."+newGender, FieldValue.increment(1));
        }else {

            // iterate of user's  old vaccine map keys to increment them in Firestore in the code below
            for (String vaccine : newVaccinesMap.keySet()){
                aggregateMap.put(vaccine.toLowerCase().trim()+"."+newAgeGroup, FieldValue.increment(1));
                aggregateMap.put(vaccine.toLowerCase().trim()+"."+newGender, FieldValue.increment(1));
            }
        }
        // ____________________________ New Covid Condition Data (Increment) ____________________________________

        aggregateMap.put(newCondition+"."+newAgeGroup, FieldValue.increment(1));
        aggregateMap.put(newCondition+"."+newGender, FieldValue.increment(1));

        // ____________________________ New males/females fields (Increment)  ____________________________________
        if (newGender.equals("m")){
            aggregateMap.put("males", FieldValue.increment(1));
        }else {
            aggregateMap.put("females", FieldValue.increment(1));
        }




        // make a user write batch
        WriteBatch writeBatchEdit = db.batch();

        // start a document in Firestore with the user's unique id and set the values as user object
        refUsers = db.collection("Users").document(uid);

        // refer to aggregate doc which contains all the users collected data
        refReports = db.collection("Report").document("aggregate");

        refUsers.set(user);


        refReports.update(deleteOldData);
        refReports.update(aggregateMap);


        // commit the Users Write, Reports Update batch
        writeBatchEdit.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toasty.success(getApplicationContext(), "Profile Edited Successfully", Toast.LENGTH_SHORT, true).show();
                edit.setChecked(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), "Updating details failed! try again later..", Toast.LENGTH_SHORT, true).show();
            }
        });


    }// end of db write function




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


    @Override
    protected void onStart() {
        super.onStart();
        World.init(getApplicationContext());
        loadFirebaseUserData();
    }

    // used in onStart for listening to uid document in Users collection
    private void loadFirebaseUserData(){


        refUsers = db.collection("Users").document(uid);

        // "this" key word ensures that the listener will stop when activity is closed to improve efficiency.
        refUsers.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                // when error exists
                if (error != null){
                    Toasty.error(getApplicationContext(),"Couldn't retrieve user data, try again later.", Toast.LENGTH_LONG,true).show();
                    Log.e("x", "onEvent: "+ error.getMessage());
                    return;
                }

                if (value.exists()){


                    uFullName = value.getString("name");

                    uEmail = value.getString("email");

                    uDOB = value.getString("dob");

                    uAge = value.getLong("age");

                    uCountry = value.getString("country");

                    uGender = value.getString("gender");

                    uCovid = value.getString("covid");


                    uVaccinesMap = (Map<String, Long>)  value.get("vaccines");

                    uVaccines = "";
                    if (uVaccinesMap.isEmpty()){
                        uVaccines = "None";
                    }else{
                        // iterate of user's vaccine map and append to Vaccines string
                        for (Map.Entry<String,Long> vaccine : uVaccinesMap.entrySet()){
                            uVaccines += getFullVaccName(vaccine.getKey()) +" : "+ vaccine.getValue()+"\n";
                        }
                    }


                    etName.setText(uFullName);
                    etEmail.setText(uEmail);
                    tvDOB.setText(uDOB);
                    tvAge.setText("Age : "+uAge);
                    if (uGender.equals("M")){
                        rbMale.setChecked(true);
                        m = true;
                    }
                    else{
                        rbFemale.setChecked(true);
                        f = true;
                    }
                    tvCountry.setText(uCountry);
                    ivFlag.setImageResource(World.getFlagOf(uCountry));
                    tvCondition.setText(uCovid.substring(0, 1).toUpperCase() + uCovid.substring(1));
                    tvVaccine.setText(uVaccines.trim());
                    tvEditedVaccines.setText(uVaccines.trim());

                    // gives the country spinner its values and sets up the date picker for the DOB button
                    setUpDateNCountrySpinner();
                    fillUpConditionNCountryNDatePicker();

                }else {
                    Toasty.error(getApplicationContext(),"Couldn't retrieve user data, try again later.", Toast.LENGTH_LONG,true).show();
                }


            }
        });

    }

    private void fillUpConditionNCountryNDatePicker() {
        // make conditions spinner display user's retrieved covid condition as default.
        String condition = uCovid.substring(0, 1).toUpperCase() + uCovid.substring(1);
        int conditionSpinnerPosition = conditionsAdapter.getPosition(condition);
        profileConditionSpinner.setSelection(conditionSpinnerPosition);

        // set default country on spinner as user country
        String fullCountry = World.getCountryFrom(uCountry).getName().trim() +" ("+uCountry+")";
        int countrySpinnerPosition = adapter.getPosition(fullCountry);
        profileCountrySpinner.setSelection(countrySpinnerPosition);

        // set default date as user's dob
        btnDOB.setText(uDOB);

    }


    private void setUpDateNCountrySpinner(){
        //Date&Time
        initProfileDatePicker();
        btnDOB.setText(getTodaysDate());

        // fill in array with country names and iso2 from library
        libCountriesList = World.getAllCountries();
        countryNames = new String[libCountriesList.size()];
        int i =0;
        for (Country c: libCountriesList){
            countryNames[i] = c.getName() +" ("+c.getAlpha2()+")";
            i++;
        }

        //Country Spinner
        adapter = new CountryPickerAdapter(this,countryNames);
        profileCountrySpinner.setAdapter(adapter);
    }


    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initProfileDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet (DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1; //because android starts months from 0
                String date = makeDateString(day, month, year);
                btnDOB.setText(date);

            }
        };


        String yearx = uDOB.substring(uDOB.lastIndexOf("/")+1);
        String monthx = uDOB.substring(uDOB.indexOf("/")+1,uDOB.lastIndexOf("/"));
        String dayx = uDOB.substring(0,uDOB.indexOf("/"));


        int year = Integer.valueOf(yearx.trim());
        int month = getReverseMonthFormat(monthx.trim());
        int day = Integer.valueOf(dayx.trim());

//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH);
//        int day = cal.get(Calendar.DAY_OF_MONTH);

        Log.e("x", "initProfileDatePicker: year : "+year +" yearx : "+yearx +" month "+month + " monthx"+monthx + " day "+day+ " dayx "+ dayx );

        int style = android.R.style.ThemeOverlay_Material_Dialog; //THIS CHANGED IT


        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {

        return  day+ "/" + getMonthFormat(month) + "/" + year;
    }





    private String getMonthFormat(int month) {

        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";


//Default should never happen
        return "JAN";
    }

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
    }

    private boolean hashMapsEqualIgnoreOrder(Map<String,Long> mapX,Map<String,Long> mapY){

        // if the map is null it means the user didnt access vaccine edit page so true is passed + ensure no crash occurs
        if (mapX == null || mapY == null ){
            return true;
        }

        String [] mapXKeys = new String[mapX.keySet().size()];
        Long [] mapXVals = new Long[mapX.keySet().size()];
        String [] mapYKeys = new String[mapY.keySet().size()];
        Long [] mapYVals = new Long[mapY.keySet().size()];

        int X = 0;
        for (Map.Entry<String,Long> x : mapX.entrySet()){
            mapXKeys[X] = x.getKey();
            mapXVals[X] = x.getValue();
            X++;
        }

        int Y = 0;
        for (Map.Entry<String,Long> y : mapY.entrySet()){
            mapYKeys[Y] = y.getKey();
            mapYVals[Y] = y.getValue();
            Y++;
        }
        Arrays.sort(mapXKeys);
        Arrays.sort(mapYKeys);
        Arrays.sort(mapXVals);
        Arrays.sort(mapYVals);

        if (Arrays.equals(mapXKeys,mapYKeys)  && Arrays.equals(mapXVals,mapYVals)){

            return true;
        }
        else {
            return false;
        }

    }



    private void editVaccClicked() {


            Dialog d = new Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            d.setContentView(R.layout.activity_report_status1);
            d.show();

            CheckBox cbPfizer,cbModerna,cbJJ,cbSinovac,cbAstra,cbNone;
            HorizontalQuantitizer pfizerCounter,modernaCounter,jjCounter,sinovacCounter,astraCounter;
            Button vaccineSubmit;

            ImageView bArrow = d.findViewById(R.id.backarrow_id);
            bArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    d.dismiss();
                }
            });

        vaccineSubmit = d.findViewById(R.id.bt_submit_id);

        cbPfizer = d.findViewById(R.id.checkBox1);
        cbModerna = d.findViewById(R.id.checkBox2);
        cbJJ = d.findViewById(R.id.checkBox3);
        cbSinovac = d.findViewById(R.id.checkBox4);
        cbAstra = d.findViewById(R.id.checkBox5);
        cbNone = d.findViewById(R.id.checkBox6);

        pfizerCounter = d.findViewById(R.id.biontechVal_id);
        pfizerCounter.setTag("pfizer");
        pfizerCounter.setMinusIconBackgroundColor("#75FF647C");
        pfizerCounter.setTextAnimationStyle(AnimationStyle.FALL_IN);
        pfizerCounter.setAnimationDuration(300);
        pfizerCounter.setValueTextColor("#7C7C7C");
        pfizerCounter.setMinusIconColor("#FF0000");
        pfizerCounter.setReadOnly(true);//doesn't allow keyboard entry
        pfizerCounter.setMaxValue(5);
        pfizerCounter.setMinValue(1);
        pfizerCounter.setVisibility(View.INVISIBLE);

        modernaCounter = d.findViewById(R.id.modernaVal_id);
        modernaCounter.setTag("moderna");
        modernaCounter.setMinusIconBackgroundColor("#75FF647C");
        modernaCounter.setTextAnimationStyle(AnimationStyle.FALL_IN);
        modernaCounter.setAnimationDuration(300);
        modernaCounter.setValueTextColor("#7C7C7C");
        modernaCounter.setMinusIconColor("#FF0000");
        modernaCounter.setReadOnly(true);//doesn't allow keyboard entry
        modernaCounter.setMaxValue(5);
        modernaCounter.setMinValue(1);
        modernaCounter.setVisibility(View.INVISIBLE);

        jjCounter = d.findViewById(R.id.jjVal_id);
        jjCounter.setTag("jj");
        jjCounter.setMinusIconBackgroundColor("#75FF647C");
        jjCounter.setTextAnimationStyle(AnimationStyle.FALL_IN);
        jjCounter.setAnimationDuration(300);
        jjCounter.setValueTextColor("#7C7C7C");
        jjCounter.setMinusIconColor("#FF0000");
        jjCounter.setReadOnly(true);//doesn't allow keyboard entry
        jjCounter.setMaxValue(5);
        jjCounter.setMinValue(1);
        jjCounter.setVisibility(View.INVISIBLE);

        sinovacCounter = d.findViewById(R.id.sinovacVal_id);
        sinovacCounter.setTag("sino");
        sinovacCounter.setMinusIconBackgroundColor("#75FF647C");
        sinovacCounter.setTextAnimationStyle(AnimationStyle.FALL_IN);
        sinovacCounter.setAnimationDuration(300);
        sinovacCounter.setValueTextColor("#7C7C7C");
        sinovacCounter.setMinusIconColor("#FF0000");
        sinovacCounter.setReadOnly(true);//doesn't allow keyboard entry
        sinovacCounter.setMaxValue(5);
        sinovacCounter.setMinValue(1);
        sinovacCounter.setVisibility(View.INVISIBLE);

        astraCounter = d.findViewById(R.id.astraVal_id);
        astraCounter.setTag("astra");
        astraCounter.setMinusIconBackgroundColor("#75FF647C");
        astraCounter.setTextAnimationStyle(AnimationStyle.FALL_IN);
        astraCounter.setAnimationDuration(300);
        astraCounter.setValueTextColor("#7C7C7C");
        astraCounter.setMinusIconColor("#FF0000");
        astraCounter.setReadOnly(true);//doesn't allow keyboard entry
        astraCounter.setMaxValue(5);
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


        vaccineSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                // check if no option was selected
                if (!cbPfizer.isChecked() && !cbModerna.isChecked() && !cbJJ.isChecked() && !cbSinovac.isChecked() && !cbAstra.isChecked() && !cbNone.isChecked()){
                    Toasty.warning(getApplicationContext(),"Please Select an Option!", Toast.LENGTH_LONG).show();
                }else {

                    user = User.getInstance();
                    HorizontalQuantitizer[] counters = {pfizerCounter, modernaCounter, jjCounter, sinovacCounter, astraCounter};
                        newVaccinesMap = new HashMap<>();
                    // search in array called counters for visible ones and set the visible one's tags string attribute to Vaccines Map along with the corresponding values
                    for (HorizontalQuantitizer c : counters) {
                        if (c.getVisibility() == View.VISIBLE) {
                            newVaccinesMap.put(c.getTag().toString(), (long) c.getValue());
                        }
                    }


                    if (hashMapsEqualIgnoreOrder(uVaccinesMap,newVaccinesMap)){
                        Toasty.warning(getApplicationContext(), "No Change Occurred!", Toast.LENGTH_SHORT,true).show();
                    }else {

                        // iterate of user's vaccine map and append to EditedVaccines string
                        String editedtVaccines = "";
                        for (Map.Entry<String,Long> vaccine : newVaccinesMap.entrySet()){
                            editedtVaccines += getFullVaccName(vaccine.getKey()) +" : "+ vaccine.getValue()+"\n";
                        }
                        tvEditedVaccines.setText(editedtVaccines.trim());
                        user.setVaccines(newVaccinesMap);

                        Toasty.info(getApplicationContext(), "Vaccine Options Selected", Toast.LENGTH_SHORT,true).show();
                        d.dismiss();
                    }

                }

            }
        });



    }//end of vaccedit


    public void resetPassProfile(View view) {

        if (fbUser != null) {
            String email = fbUser.getEmail();
            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toasty.success(getApplicationContext(),"A reset password email is sent to "+email, Toast.LENGTH_LONG,true).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toasty.error(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG,true).show();
                }
            });

        } else {
            Toasty.error(getApplicationContext(), "No User Signed In", Toast.LENGTH_SHORT,true).show();
        }
    }

    private String getFullVaccName(String vacc){
        if (vacc.equals("pfizer"))
            return "Pfizer/BioNTech";
        if (vacc.equals("astra"))
            return "Oxford/AstraZeneca";
        if (vacc.equals("jj"))
            return "Johnson & Johnson";
        if (vacc.equals("sino"))
            return "Sinovac/CoronaVac";
        if (vacc.equals("moderna"))
            return "Moderna/Spikevax";
        else
            return "";
    }

}//end of class