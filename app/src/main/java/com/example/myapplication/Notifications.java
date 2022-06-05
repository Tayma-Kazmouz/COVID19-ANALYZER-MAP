package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blongho.country_data.Country;
import com.blongho.country_data.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Notifications extends AppCompatActivity {

        // declare

        Switch sw;
        Button confirm;
        Spinner spCountries;
        ImageView ivBack;
        CountryPickerAdapter adapter;
        List<Country> libCountriesList;
        String [] countryNames;
        PendingIntent pendingIntent;
        AlarmManager alarmManager;
        boolean swState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        makeNotificationChannel();



        confirm = findViewById(R.id.btn_notifications_confirm_id);
        spCountries = findViewById(R.id.spinnerNotifications_id);
        ivBack = findViewById(R.id.ivNotificationsBackArrow_id);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Notifications.this,Extras.class));
            }
        });

            //default state
        spCountries.setEnabled(false);


        sw = findViewById(R.id.NotificationsSwitch_id);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    confirm.setEnabled(true);
                    spCountries.setEnabled(true);
                }else{
                    confirm.setEnabled(false);
                    spCountries.setEnabled(false);
                    swState =false;

                    if (pendingIntent != null && alarmManager != null){

                        alarmManager.cancel(pendingIntent);
                        Toasty.info(getApplicationContext(),"Disable The Notification...",Toast.LENGTH_SHORT,true).show();

                        SharedPreferences.Editor editor = getSharedPreferences("SP_Notifications",MODE_PRIVATE).edit();
                        editor.putString("country","Canceled");
                        editor.apply();
                    }

                }
            }
        });


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
        spCountries.setAdapter(adapter);


        // set up activity state

        SharedPreferences readSP = getSharedPreferences("SP_Notifications",MODE_PRIVATE);

        sw.setChecked(readSP.getBoolean("enabled",false));

        int countrySpinnerPosition = adapter.getPosition(readSP.getString("country","Afghanistan (AF)"));
        spCountries.setSelection(countrySpinnerPosition);


    }//end of onCreate


    public void confirmNotifications(View view) throws IOException {

        // full country name
        String countryFull = spCountries.getSelectedItem().toString().trim();



        // get country iso2 from spinner
        String selectedCountry = spCountries.getSelectedItem().toString().trim();
        selectedCountry = selectedCountry.substring(selectedCountry.lastIndexOf("(")+1,selectedCountry.lastIndexOf(")"));

        if (getSharedPreferences("SP_Notifications",MODE_PRIVATE).getString("country","-1").equals(countryFull)){

            Toasty.warning(getApplicationContext(),"Notification for ("+selectedCountry+") is already set",Toasty.LENGTH_LONG,true).show();
            return;
        }

        // read txt file from raw folder containing iso2 list of countries from api and compare with spinner value
        InputStream inputStream = getBaseContext().getResources().openRawResource(R.raw.available_countries_data);

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        boolean found = false;
        String x = br.readLine();
        while (x != null){
            Log.e("x", " NOW " + x);
            if (selectedCountry.equals(x)){
                found = true;
                br.close();
                break;
            }

            x = br.readLine();
        }


        // if the spinner value wasnt found show msg
        if (!found){
            Toasty.warning(getApplicationContext(), "Country Data Not Available", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Notifications.this,NotificationsBroadcast.class);
        String title = countryFull;
        String message = selectedCountry;

        intent.putExtra(NotificationsBroadcast.titleExtra, title);
        intent.putExtra(NotificationsBroadcast.messageExtra, message); // will send iso2 code to disease.sh url to fetch country data


        pendingIntent = PendingIntent.getBroadcast(Notifications.this,NotificationsBroadcast.NotificationID,intent,PendingIntent.FLAG_UPDATE_CURRENT);



        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        // at Hour 22:30
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,22);
        calendar.set(Calendar.MINUTE,36);
        calendar.set(Calendar.SECOND,00);

        if (Calendar.getInstance().after(calendar)){
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }


        // write an sp to set activity switch and spinner states
        SharedPreferences SP = getSharedPreferences("SP_Notifications",MODE_PRIVATE);

        // Connect the Editor with our SP obj above ...
        SharedPreferences.Editor editor = SP.edit();


        // Write/Store Data in SP file ...
        editor.putBoolean("enabled",true);
        editor.putString("country",countryFull);

        editor.apply();

        Toasty.success(getApplicationContext(), "Notification is set for 22:30 everyday", Toast.LENGTH_SHORT).show();


    }//end of method

// make notification channel for Android O and above...
    void makeNotificationChannel(){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel NChannel = new NotificationChannel
                        (NotificationsBroadcast.channelID,
                                "Country Data",
                                NotificationManager.IMPORTANCE_HIGH
                        );
                NChannel.setDescription("daily Covid 19 data for a selected country ");

                NotificationManager NManager = getSystemService(NotificationManager.class);
                NManager.createNotificationChannel(NChannel);


            }
    }


    @Override
    protected void onStart() {
        super.onStart();
        // force disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }



}//end of Activity class