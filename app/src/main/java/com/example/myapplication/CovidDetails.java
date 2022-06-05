package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CovidDetails extends AppCompatActivity {

    //Declare

    TextView tvCountryFullName, tvCountryPopulation, tvCountryContinent;
    TextView tvTodayCases,tvTodayRecovery,tvTodayDeaths,tvMoreTests,tvMoreCritical,tvMoreActive;
    ShapeableImageView ivFlag, ivMap;
    PieChart myPieChart;
    SeekBar sbCases, sbRecovered,sbDeaths;

    private String STATIC_MAPS_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_details);

        //Define

        tvCountryFullName = findViewById(R.id.CountryFullName_id);
        tvCountryPopulation = findViewById(R.id.countryPopulation_id);
        tvCountryContinent = findViewById(R.id.countryContinent_id);

        tvTodayCases = findViewById(R.id.todayCases_id);
        tvTodayRecovery = findViewById(R.id.today_Recovered_id);
        tvTodayDeaths = findViewById(R.id.todayDeaths_id);
        tvMoreTests = findViewById(R.id.moreTests_id);
        tvMoreCritical = findViewById(R.id.moreCritical_id);
        tvMoreActive = findViewById(R.id.moreActive_id);

        sbCases = findViewById(R.id.sbCases_id);
        sbRecovered = findViewById(R.id.sbRecovered_id);
        sbDeaths = findViewById(R.id.sbDeaths_id);

        ivFlag = findViewById(R.id.ivCountryFlagProfile_id);
        ivMap = findViewById(R.id.ivMap_id);

        myPieChart = findViewById(R.id.detailedPieChart_id);

        Intent intent = getIntent();

        // getting the parcelable class object from the previous activity to access it data
        Corona coronaData = intent.getParcelableExtra("getCoronaParcelable");


        //here we use a free static map api from Jawg.io ...access token is created from a throwaway account
        // for more info : https://www.jawg.io/docs/apidocs/maps/static-maps/#access-token

        STATIC_MAPS_KEY = BuildConfig.STATIC_API_KEY;



        TextView tvTitle = findViewById(R.id.detailedTitle_id);
        tvTitle.setText(coronaData.getCountry()+" Details");





        tvCountryFullName.setText(coronaData.getCountry());
        tvCountryPopulation.setText("Population\n" + String.format("%,d",coronaData.getPopulation()));
        tvCountryContinent.setText("Continent\n" + coronaData.getContinent());
        Picasso.get().load(coronaData.getCountryInfo().getFlag()).into(ivFlag);






        //here we use a free static map api from Jawg.io ...access token is created from a throwaway account
        String mapURL = "https://api.jawg.io/static?zoom=6&center=" + coronaData.getCountryInfo().getLat() + "," + coronaData.getCountryInfo().getLong() + "&size=1500x1500&layer=jawg-terrain&format=png&access-token="+STATIC_MAPS_KEY;


        // if its one of the big countries then fetch a zoomed out static map from the API
        if (coronaData.getCountry().matches("USA|Canada|Russia|China|Brazil|Kazakhstan|Greenland"))
        {
            String mapBigURL = "https://api.jawg.io/static?zoom=4&center=" + coronaData.getCountryInfo().getLat() + "," + coronaData.getCountryInfo().getLong() + "&size=1500x1500&layer=jawg-terrain&format=png&access-token="+STATIC_MAPS_KEY;

            Picasso.get().load(mapBigURL).placeholder(R.drawable.defaultmap).into(ivMap);
        }else if (coronaData.getCountry().equals("Australia")){
            String mapAusURL = "https://api.jawg.io/static?zoom=5&center=" + coronaData.getCountryInfo().getLat() + "," + coronaData.getCountryInfo().getLong() + "&size=1500x1500&layer=jawg-terrain&format=png&access-token="+STATIC_MAPS_KEY;

            Picasso.get().load(mapAusURL).placeholder(R.drawable.defaultmap).into(ivMap);

        }
        else if (coronaData.getCountry().equals("New Zealand")){
            String mapNZURL  = "https://api.jawg.io/static?zoom=8&center=" + coronaData.getCountryInfo().getLat() + "," + coronaData.getCountryInfo().getLong() + "&size=1500x1500&layer=jawg-terrain&format=png&access-token="+STATIC_MAPS_KEY;
            Picasso.get().load(mapNZURL).placeholder(R.drawable.defaultmap).into(ivMap);
        }

        else{
            Picasso.get().load(mapURL).placeholder(R.drawable.defaultmap).into(ivMap);
        }

        /*

        Zoom = 4 for

        USA / Canada / China / Australia == 5 / Brazil / Kazakhstan / Greenland / Russia

        Else Zoom = 6

         */

        myPieChart.setDrawHoleEnabled(true); // gives donut shape
        myPieChart.getDescription().setEnabled(false); // removing description of the pie chart


        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(coronaData.getCases()));
        entries.add(new PieEntry(coronaData.getRecovered()));
        entries.add(new PieEntry(coronaData.getDeaths()));

        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(
                 Color.parseColor("#FF3AC4FF") // Cases  = Blue
                ,Color.parseColor("#FF00BFA6") // Recovered = Green
                ,Color.parseColor("#FFFF647C") // Deaths = Red
        );


        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        myPieChart.getLegend().setEnabled(false);
        myPieChart.setData(data);
        myPieChart.invalidate(); // data updated.. refresh on the the screen

        myPieChart.animateY(1400, Easing.EaseInOutQuad);



        // disabling seekbars from being dragged
        sbCases.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        sbRecovered.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        sbDeaths.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        double pCases = getPercentage(coronaData.getCases(),coronaData.getRecovered(),coronaData.getDeaths()) *100;
        double pRecovered = getPercentage(coronaData.getRecovered(),coronaData.getDeaths(),coronaData.getCases())*100;
        double pDeaths = getPercentage(coronaData.getDeaths(),coronaData.getRecovered(),coronaData.getCases())*100;

          Log.e("x", "B: "+pCases+"G: "+pRecovered+"R: "+pDeaths );


        sbCases.setProgress((int) (pCases*100));
        sbRecovered.setProgress((int) (pRecovered*100));
        sbDeaths.setProgress((int) (pDeaths*100));

        TextView tvPieCases = findViewById(R.id.tvCasesPie_id);
        TextView tvPieRecovered = findViewById(R.id.tvRecoveredPie_id);
        TextView tvPieDeaths = findViewById(R.id.tvDeathsPie_id);

        tvPieCases.setText(String.format("%,d",coronaData.getCases())+" ("+String.format("%.2f",pCases)+"%)");
        tvPieRecovered.setText(String.format("%,d",coronaData.getRecovered())+" ("+String.format("%.2f",pRecovered)+"%)");
        tvPieDeaths.setText(String.format("%,d",coronaData.getDeaths())+" ("+String.format("%.2f",pDeaths)+"%)");



        tvTodayCases.setText("+"+String.format("%,d",coronaData.getTodayCases()));
        tvTodayRecovery.setText("+"+String.format("%,d",coronaData.getTodayRecovered()));
        tvTodayDeaths.setText("+"+String.format("%,d",coronaData.getTodayDeaths()));

        tvMoreTests.setText(""+String.format("%,d",coronaData.getTests()));
        tvMoreCritical.setText(""+String.format("%,d",coronaData.getCritical()));
        tvMoreActive.setText(""+String.format("%,d",coronaData.getActive()));

        //fonts
        Typeface fontPoppinsBlack = Typeface.createFromAsset(getAssets(),"fonts/poppins_black.ttf");
        Typeface fontPoppins = Typeface.createFromAsset(getAssets(),"fonts/poppins.ttf");

        tvCountryFullName.setTypeface(fontPoppins);
        tvCountryPopulation.setTypeface(fontPoppins);
        tvCountryContinent.setTypeface(fontPoppins);

        tvPieCases.setTypeface(fontPoppins);
        tvPieRecovered.setTypeface(fontPoppins);
        tvPieDeaths.setTypeface(fontPoppins);

        tvTodayCases.setTypeface(fontPoppinsBlack);
        tvTodayRecovery.setTypeface(fontPoppinsBlack);
        tvTodayDeaths.setTypeface(fontPoppinsBlack);

        tvMoreTests.setTypeface(fontPoppinsBlack);
        tvMoreCritical.setTypeface(fontPoppinsBlack);
        tvMoreActive.setTypeface(fontPoppinsBlack);


    }//end of onCreate


    @Override
    protected void onStart() {
        super.onStart();
        // force disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }



    double getPercentage(Long x , Long y, Long z){
        double percent = (double)(x) / (x+y+z);
        return percent ;
    }

}//end of class