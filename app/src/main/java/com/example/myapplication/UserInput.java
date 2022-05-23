package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.Spinner;
//import com.example.myapplication.UserInput;
import com.blongho.country_data.Country;
import com.blongho.country_data.World;

import java.util.List;



import java.util.Calendar;

public class UserInput extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    Button gotoreport;


    //Country Picker func.
    private Spinner spinner;

    CountryPickerAdapter adapter;

    List<Country> libCountriesList;
    String [] countryNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);




        gotoreport= findViewById(R.id.submitpersonalinfo_id);


        gotoreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(UserInput.this,ReportStatus1.class);
                startActivity(i);
                finish();

            }
        });


        //Date&Time
        initDatePicker();
        dateButton = findViewById(R.id.dobbtn_id);
        dateButton.setText(getTodaysDate());

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

        return getMonthFormat(month) + "/" + day + "/" + year;
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








}//end of class




