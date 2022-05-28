package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
//import com.example.myapplication.UserInput;
import com.blongho.country_data.Country;
import com.blongho.country_data.World;

import java.util.List;



import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class UserInput extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    Button gotoreport;


    //Country Picker func.
    private Spinner spinner;

    CountryPickerAdapter adapter;

    List<Country> libCountriesList;
    String [] countryNames;

    Calendar dob,today;

    RadioButton rbMale,rbFemale;
    String DOB;
    String Gender;
    String Country;
    Long Age;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);


        gotoreport= findViewById(R.id.submitpersonalinfo_id);
        rbMale = findViewById(R.id.rbmale_id);
        rbFemale = findViewById(R.id.rbfemale_id);




        //Date&Time
        initDatePicker();
        dateButton = findViewById(R.id.dobbtn_id);
        dateButton.setText(getTodaysDate());

        // fill in array with country names and iso2 from library
        libCountriesList = World.getAllCountries();
        countryNames = new String[libCountriesList.size()];
        int i =0;
        for (Country c: libCountriesList){
            countryNames[i] = c.getName() +" ("+c.getAlpha2()+")";
            i++;
        }

        //Country Spinner
        spinner = findViewById(R.id.spinnerid);
        adapter = new CountryPickerAdapter(this,countryNames);
        spinner.setAdapter(adapter);


        gotoreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (!rbMale.isChecked() && !rbFemale.isChecked()){
                        Toasty.warning(UserInput.this,"Please Select Gender", Toast.LENGTH_LONG).show();
                        return;
                    }

                    DOB = makeDateString(datePickerDialog.getDatePicker().getDayOfMonth(),datePickerDialog.getDatePicker().getMonth()+1,datePickerDialog.getDatePicker().getYear());

                    Country = spinner.getSelectedItem().toString().trim();
                    Country = Country.substring(Country.lastIndexOf("(")+1,Country.lastIndexOf(")"));

                    if (rbMale.isChecked())
                        Gender = "M";
                    else
                        Gender = "F";


                    today = Calendar.getInstance();
                    dob = Calendar.getInstance();

                    dob.set(datePickerDialog.getDatePicker().getYear(), datePickerDialog.getDatePicker().getMonth()+1, datePickerDialog.getDatePicker().getDayOfMonth());

                        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
                            age--;
                        }

                        Age = (long) age;

                        if (Age == -1)
                            Age = 0L ;

                    user = User.getInstance();

                    user.setDob(DOB.trim());
                    user.setAge(Age);
                    user.setGender(Gender.trim());
                    user.setCountry(Country.trim());

                Intent i = new Intent(UserInput.this,ReportStatus1.class);
                startActivity(i);

            }
        });

    }//end of ONCreate

    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override 
            public void onDateSet (DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1; //because android starts months from 0
                String date = makeDateString(day, month, year);
                dateButton.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

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

    public void openDatePicker(View view) {

        datePickerDialog.show();

    }




    @Override
    public void onBackPressed() {
        Toasty.info(getApplicationContext(), "Please enter your details!", Toast.LENGTH_SHORT, true).show();
    }



}//end of class




