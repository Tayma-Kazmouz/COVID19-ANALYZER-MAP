package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.blongho.country_data.World;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DashBoard extends AppCompatActivity {




    //Declare
    LineChart myLineChart;



    // For Cases
    private static final String SP_CASES = "SHARED_PREFERENCES_CASES";
    private static final String SP_VALUE_M = "M";
    private static final String SP_VALUE_M1 = "M1";
    private static final String SP_VALUE_M2 = "M2";
    private static final String SP_VALUE_M3 = "M3";
    private static final String SP_VALUE_M4 = "M4";
    private static final String SP_VALUE_M5 = "M5";

    // For Deaths
    private static final String SP_Deaths = "SHARED_PREFERENCES_DEATHS";
    private static final String SP_VALUE_D = "D";
    private static final String SP_VALUE_D1 = "D1";
    private static final String SP_VALUE_D2 = "D2";
    private static final String SP_VALUE_D3 = "D3";
    private static final String SP_VALUE_D4 = "D4";
    private static final String SP_VALUE_D5 = "D5";



    //Declare
    RequestQueue rq;        // Using Volley Library for Cases


    JSONObject dSCases = new JSONObject();

    JSONObject dSDeaths = new JSONObject();

    // To get the dates for the json request exactly like formatted in the API
    public String jsonFetchM;
    public String jsonFetchM1;
    public String jsonFetchM2;
    public String jsonFetchM3;
    public String jsonFetchM4;
    public String jsonFetchM5;


    String nameM;
    String nameM1;
    String nameM2;
    String nameM3;
    String nameM4;
    String nameM5;


    // For Cases
    String valMx;
    String valMx1;
    String valMx2;
    String valMx3;
    String valMx4;
    String valMx5;

    long valM;
    long valM1;
    long valM2;
    long valM3;
    long valM4;
    long valM5;


    // For Deaths

    String valDx;
    String valDx1;
    String valDx2;
    String valDx3;
    String valDx4;
    String valDx5;

    long valD;
    long valD1;
    long valD2;
    long valD3;
    long valD4;
    long valD5;


    MaterialCardView coronaCasesCv;
    MaterialCardView vaccineCV;
    MaterialCardView mapsCV;

    ImageView ivExtras;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        coronaCasesCv = findViewById(R.id.coronaCasesCV_id);
        coronaCasesCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this,CoronaCasesList.class));
            }
        });
        vaccineCV = findViewById(R.id.VaccinesCV_id);
        vaccineCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this,VaccineList.class));
            }
        });

        mapsCV = findViewById(R.id.mapsCV_id);
        mapsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this,MapsActivity.class));
            }
        });

        ivExtras = findViewById(R.id.extras_id);
        ivExtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoard.this,Extras.class));
            }
        });







        //Define

        rq = VolleySingleton.getInstance(this).getRequestQueue();




        myLineChart = findViewById(R.id.myLineChartJsonCases_id);


        // get the Month dates from current time

        DateFormat dateFormatMonth = new SimpleDateFormat("M");
        Date dateCurrentMonth = new Date();

        //matching numbers with names
        Map<String, String> mapMonths = new HashMap<>();
        mapMonths.put("1","Jan");
        mapMonths.put("2","Feb");
        mapMonths.put("3","Mar");
        mapMonths.put("4","Apr");
        mapMonths.put("5","May");
        mapMonths.put("6","Jun");
        mapMonths.put("7","Jul");
        mapMonths.put("8","Aug");
        mapMonths.put("9","Sep");
        mapMonths.put("10","Oct");
        mapMonths.put("11","Nov");
        mapMonths.put("12","Dec");

        // Set up the date format  to match that of the json file
        SimpleDateFormat sdJsonDateFormat = new SimpleDateFormat("M/d/yy");

        // Current Month ((5 Days Ago... Since Today's Data May Not Be Updated Yet))
        Calendar c = Calendar.getInstance();
        c.setTime(dateCurrentMonth);
        c.add(Calendar.DATE, -5);
        String M = dateFormatMonth.format(dateCurrentMonth);
        nameM = mapMonths.get(M);
        jsonFetchM = sdJsonDateFormat.format(new Date(c.getTimeInMillis()));

        //Last Month
        Calendar c1 = Calendar.getInstance();
        c1.setTime(dateCurrentMonth);
        c1.add(Calendar.MONTH, -1);
        String M1 = dateFormatMonth.format(new Date(c1.getTimeInMillis()));
        nameM1 = mapMonths.get(M1);
        jsonFetchM1 = sdJsonDateFormat.format(new Date(c1.getTimeInMillis()));


        // 2 Months Ago
        Calendar c2 = Calendar.getInstance();
        c2.setTime(dateCurrentMonth);
        c2.add(Calendar.MONTH, -2);
        String M2 = dateFormatMonth.format(new Date(c2.getTimeInMillis()));
        nameM2 = mapMonths.get(M2);
        jsonFetchM2 = sdJsonDateFormat.format(new Date(c2.getTimeInMillis()));


        // 3 Months Ago
        Calendar c3 = Calendar.getInstance();
        c3.setTime(dateCurrentMonth);
        c3.add(Calendar.MONTH, -3);
        String M3 = dateFormatMonth.format(new Date(c3.getTimeInMillis()));
        nameM3 = mapMonths.get(M3);
        jsonFetchM3 = sdJsonDateFormat.format(new Date(c3.getTimeInMillis()));


        // Log.e("3 Months ago",""+ );

        // 4 Months Ago
        Calendar c4 = Calendar.getInstance();
        c4.setTime(dateCurrentMonth);
        c4.add(Calendar.MONTH, -4);
        String M4 = dateFormatMonth.format(new Date(c4.getTimeInMillis()));
        nameM4 = mapMonths.get(M4);
        jsonFetchM4 = sdJsonDateFormat.format(new Date(c4.getTimeInMillis()));


        //5 Months Ago
        Calendar c5 = Calendar.getInstance();
        c5.setTime(dateCurrentMonth);
        c5.add(Calendar.MONTH, -5);
        String M5 = dateFormatMonth.format(new Date(c5.getTimeInMillis()));
        nameM5 = mapMonths.get(M5);
        jsonFetchM5 = sdJsonDateFormat.format(new Date(c5.getTimeInMillis()));






//
//
//        //get the data from the api


        getCasesDeathsData();




    }//end of onCreate



    @Override
    protected void onStart() {
        super.onStart();

        plotData();


    }// end of onStart


    void plotData(){



        //Read  getType(NAME_OF_SP_KEY,Default Value If not Found)
        SharedPreferences read = getSharedPreferences(SP_CASES,MODE_PRIVATE);



        // Step 1 -- Array List with Entries
        ArrayList<Entry> activeCasesEntries = new ArrayList<Entry>();
        activeCasesEntries.add(new Entry(0,read.getLong(SP_VALUE_M5,251)));
        activeCasesEntries.add(new Entry(1,read.getLong(SP_VALUE_M4,268)));
        activeCasesEntries.add(new Entry(2,read.getLong(SP_VALUE_M3,308)));
        activeCasesEntries.add(new Entry(3,read.getLong(SP_VALUE_M2,403)));
        activeCasesEntries.add(new Entry(4,read.getLong(SP_VALUE_M1,451)));
        activeCasesEntries.add(new Entry(5,read.getLong(SP_VALUE_M,492)));

        //Step 2 DataSet

        LineDataSet lineDataSetCases = new LineDataSet(activeCasesEntries,"Worldwide Case Statistics");

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSetCases);

        // Step3 link Data to obj
        LineData lineData = new LineData((iLineDataSets));
        myLineChart.setData(lineData);
        myLineChart.invalidate();

        // Customize
        myLineChart.setNoDataText("Data Not Found");
        myLineChart.getLegend().setEnabled(false);
        myLineChart.getDescription().setEnabled(false);
        myLineChart.animateY(1400, Easing.EaseInOutExpo);
        myLineChart.getAxisLeft().setDrawGridLines(false);
        myLineChart.getAxisRight().setDrawGridLines(false);
        myLineChart.getXAxis().setDrawGridLines(false);
        myLineChart.getAxisRight().setEnabled(false);
        myLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        myLineChart.getXAxis().setGranularity(1f);
        myLineChart.setExtraLeftOffset(15);
        myLineChart.setExtraRightOffset(15);
        myLineChart.setScaleEnabled(false); // for zooming in and out


        // GIVING THE X AXIS THE NAMES ACCORDING TO OUR CURRENT MONTH DYNAMICALLY
        List<String> xAxisValues = new ArrayList<>(Arrays.asList(nameM5,nameM4,nameM3,nameM2,nameM1,nameM));

        myLineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues)); // sets the x axis to the Month Strings created above

        myLineChart.getLineData().setValueFormatter(new LargeValueFormatter()); // Makes the large values appear with m for millions

        lineDataSetCases.setLineWidth(5);
        lineDataSetCases.setValueTextSize(10);
        lineDataSetCases.setValueTextColor(Color.parseColor("#99000000"));
        lineDataSetCases.setMode(LineDataSet.Mode.CUBIC_BEZIER);// Makes The Line Plotting have curvature



        //Read  getType(NAME_OF_SP_KEY,Default Value If not Found)
        SharedPreferences readDeaths = getSharedPreferences(SP_Deaths,MODE_PRIVATE);




// Another List of Deaths Data On The Same Line Graph ...
        ArrayList<Entry> DeathsEntries = new ArrayList<Entry>();
        DeathsEntries.add(new Entry(0,readDeaths.getLong(SP_VALUE_D5,5)));
        DeathsEntries.add(new Entry(1,readDeaths.getLong(SP_VALUE_D4,5)));
        DeathsEntries.add(new Entry(2,readDeaths.getLong(SP_VALUE_D3,5)));
        DeathsEntries.add(new Entry(3,readDeaths.getLong(SP_VALUE_D2,5)));
        DeathsEntries.add(new Entry(4,readDeaths.getLong(SP_VALUE_D1,6)));
        DeathsEntries.add(new Entry(5,readDeaths.getLong(SP_VALUE_D,6)));


        LineDataSet deathsDataSet = new LineDataSet(DeathsEntries,"Worldwide Statistics Death");
        ArrayList<ILineDataSet> deathsIDataSets = new ArrayList<>();
        deathsIDataSets.add(deathsDataSet);


        deathsDataSet.setLineWidth(5);
        deathsDataSet.setValueTextSize(10);
        deathsDataSet.setValueTextColor(Color.parseColor("#99000000"));
        deathsDataSet.setColor(Color.parseColor("#FF0000"));
        deathsDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Makes The Line Plotting have curvature
        deathsDataSet.setCircleColor(Color.parseColor("#FF0000")); // changes the dot value color
        deathsDataSet.setValueFormatter(new LargeValueFormatter()); // makes large values show millions as m

        LineData combineData = new LineData(lineDataSetCases ,deathsDataSet);

        //Disables the y Axis
        YAxis rightYAxis = myLineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        YAxis leftYAxis = myLineChart.getAxisLeft();
        leftYAxis.setEnabled(false);



        myLineChart.setData(combineData);
        myLineChart.invalidate();






    }//end of plotData



    private synchronized void getCasesDeathsData(){


        String url = "https://disease.sh/v3/covid-19/historical/all?lastdays=200";

        ProgressDialog pd = new ProgressDialog(DashBoard.this);
        pd.setMessage("Loading...");
        pd.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override       // WHEN RESPONSE IS SUCCESSFUL
            public void onResponse(JSONObject response) {
                pd.dismiss();
                // Note: 'response' obj found in parameter is the whole json {object} found in the url

                // we fill up the json arrays we initialized in our main class above
                try {
                    dSCases = response.getJSONObject("cases");
                    dSDeaths = response.getJSONObject("deaths");

                    // parse the JsonObjects outside...
                    parseJsonObjs(dSCases,dSDeaths);



                    // This Causes Our Issue If The Data Is Not Found For Today Yet...
                    valMx = dSCases.getString(jsonFetchM);
                    valM = Long.valueOf(valMx);
                    valMx1 = dSCases.getString(jsonFetchM1);
                    valM1 =  Long.valueOf(valMx1);
                    valMx2 = dSCases.getString(jsonFetchM2);
                    valM2 =  Long.valueOf(valMx2);
                    valMx3 = dSCases.getString(jsonFetchM3);
                    valM3 =  Long.valueOf(valMx3);
                    valMx4 = dSCases.getString(jsonFetchM4);
                    valM4 =  Long.valueOf(valMx4);
                    valMx5 = dSCases.getString(jsonFetchM5);
                    valM5 =  Long.valueOf(valMx5);

//                    Do The Same for Death Values

                    valDx = dSDeaths.getString(jsonFetchM);
                    valD = Long.valueOf(valDx);
                    valDx1 = dSDeaths.getString(jsonFetchM1);
                    valD1 = Long.valueOf(valDx1);
                    valDx2 = dSDeaths.getString(jsonFetchM2);
                    valD2 = Long.valueOf(valDx2);
                    valDx3 = dSDeaths.getString(jsonFetchM3);
                    valD3 = Long.valueOf(valDx3);
                    valDx4 = dSDeaths.getString(jsonFetchM4);
                    valD4 = Long.valueOf(valDx4);
                    valDx5 = dSDeaths.getString(jsonFetchM5);
                    valD5 = Long.valueOf(valDx5);


                    SharedPreferences sharedPreferences = getSharedPreferences(SP_CASES,MODE_PRIVATE);

                    // Connect the Editor with our SP obj above ...
                    SharedPreferences.Editor editor = sharedPreferences.edit();


                    // Write/Store Data in SP file ...
                    editor.putLong(SP_VALUE_M,valM);
                    editor.putLong(SP_VALUE_M1,valM1);
                    editor.putLong(SP_VALUE_M2,valM2);
                    editor.putLong(SP_VALUE_M3,valM3);
                    editor.putLong(SP_VALUE_M4,valM4);
                    editor.putLong(SP_VALUE_M5,valM5);

                    editor.apply();




                    SharedPreferences deathsSharedPreferences = getSharedPreferences(SP_Deaths,MODE_PRIVATE);

                    // Connect the Editor with our SP obj above ...
                    SharedPreferences.Editor deathsEditor = deathsSharedPreferences.edit();


                    // Write/Store Data in SP file ...
                    deathsEditor.putLong(SP_VALUE_D,valD);
                    deathsEditor.putLong(SP_VALUE_D1,valD1);
                    deathsEditor.putLong(SP_VALUE_D2,valD2);
                    deathsEditor.putLong(SP_VALUE_D3,valD3);
                    deathsEditor.putLong(SP_VALUE_D4,valD4);
                    deathsEditor.putLong(SP_VALUE_D5,valD5);

                    deathsEditor.apply();



                } catch (JSONException e) {

                    Log.e("x", "onResponse Failed: " +e );
                }


            }
        }, new Response.ErrorListener() {
            @Override                       // WHEN AN ERROR OCCURS
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toasty.error(getApplicationContext(), "Cant Retrieve Data", Toast.LENGTH_LONG,true).show();
                error.printStackTrace();
            }
        });

        rq.add(request);


    }//end of getCasesData

    private void parseJsonObjs(JSONObject dSCases, JSONObject dSDeaths) throws JSONException {

        valMx = dSCases.getString(jsonFetchM);
        valM = Long.valueOf(valMx);
        valMx1 = dSCases.getString(jsonFetchM1);
        valM1 =  Long.valueOf(valMx1);
        valMx2 = dSCases.getString(jsonFetchM2);
        valM2 =  Long.valueOf(valMx2);
        valMx3 = dSCases.getString(jsonFetchM3);
        valM3 =  Long.valueOf(valMx3);
        valMx4 = dSCases.getString(jsonFetchM4);
        valM4 =  Long.valueOf(valMx4);
        valMx5 = dSCases.getString(jsonFetchM5);
        valM5 =  Long.valueOf(valMx5);

        valDx = dSDeaths.getString(jsonFetchM);
        valD = Long.valueOf(valDx);
        valDx1 = dSDeaths.getString(jsonFetchM1);
        valD1 = Long.valueOf(valDx1);
        valDx2 = dSDeaths.getString(jsonFetchM2);
        valD2 = Long.valueOf(valDx2);
        valDx3 = dSDeaths.getString(jsonFetchM3);
        valD3 = Long.valueOf(valDx3);
        valDx4 = dSDeaths.getString(jsonFetchM4);
        valD4 = Long.valueOf(valDx4);
        valDx5 = dSDeaths.getString(jsonFetchM5);
        valD5 = Long.valueOf(valDx5);


        ///// ------------------------------PLOT THE DATA ABOVE------------------------------------------


        // Step 1 -- Array List with Entries
        ArrayList<Entry> activeCasesEntries = new ArrayList<Entry>();
        activeCasesEntries.add(new Entry(0,valM5));
        activeCasesEntries.add(new Entry(1,valM4));
        activeCasesEntries.add(new Entry(2,valM3));
        activeCasesEntries.add(new Entry(3,valM2));
        activeCasesEntries.add(new Entry(4,valM1));
        activeCasesEntries.add(new Entry(5,valM));

        //Step 2 DataSet

        LineDataSet lineDataSetCases = new LineDataSet(activeCasesEntries,"Worldwide Case Statistics");

        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSetCases);

        // Step3 link Data to obj
        LineData lineData = new LineData((iLineDataSets));
        myLineChart.setData(lineData);
        myLineChart.invalidate();

        // Customize
        myLineChart.setNoDataText("Data Not Found");
        myLineChart.getLegend().setEnabled(false);
        myLineChart.getDescription().setEnabled(false);
        myLineChart.animateY(1400, Easing.EaseInOutExpo);
        myLineChart.getAxisLeft().setDrawGridLines(false);
        myLineChart.getAxisRight().setDrawGridLines(false);
        myLineChart.getXAxis().setDrawGridLines(false);
        myLineChart.getAxisRight().setEnabled(false);
        myLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        myLineChart.getXAxis().setGranularity(1f);
        myLineChart.setExtraLeftOffset(15);
        myLineChart.setExtraRightOffset(15);
        myLineChart.setScaleEnabled(false); // for zooming in and out


        // GIVING THE X AXIS THE NAMES ACCORDING TO OUR CURRENT MONTH DYNAMICALLY
        List<String> xAxisValues = new ArrayList<>(Arrays.asList(nameM5,nameM4,nameM3,nameM2,nameM1,nameM));

        myLineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues)); // sets the x axis to the Month Strings created above

        myLineChart.getLineData().setValueFormatter(new LargeValueFormatter()); // Makes the large values appear with m for millions

        lineDataSetCases.setLineWidth(5);
        lineDataSetCases.setValueTextSize(10);
        lineDataSetCases.setValueTextColor(Color.parseColor("#99000000"));
        lineDataSetCases.setMode(LineDataSet.Mode.CUBIC_BEZIER);// Makes The Line Plotting have curvature




// Another List of Deaths Data On The Same Line Graph ...
        ArrayList<Entry> DeathsEntries = new ArrayList<Entry>();
        DeathsEntries.add(new Entry(0,valD5));
        DeathsEntries.add(new Entry(1,valD4));
        DeathsEntries.add(new Entry(2,valD3));
        DeathsEntries.add(new Entry(3,valD2));
        DeathsEntries.add(new Entry(4,valD1));
        DeathsEntries.add(new Entry(5,valD));


        LineDataSet deathsDataSet = new LineDataSet(DeathsEntries,"Worldwide Statistics Death");
        ArrayList<ILineDataSet> deathsIDataSets = new ArrayList<>();
        deathsIDataSets.add(deathsDataSet);


        deathsDataSet.setLineWidth(5);
        deathsDataSet.setValueTextSize(10);
        deathsDataSet.setValueTextColor(Color.parseColor("#99000000"));
        deathsDataSet.setColor(Color.parseColor("#FF0000"));
        deathsDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Makes The Line Plotting have curvature
        deathsDataSet.setCircleColor(Color.parseColor("#FF0000")); // changes the dot value color
        deathsDataSet.setValueFormatter(new LargeValueFormatter()); // makes large values show millions as m

        LineData combineData = new LineData(lineDataSetCases ,deathsDataSet);

        //Disables the y Axis
        YAxis rightYAxis = myLineChart.getAxisRight();
        rightYAxis.setEnabled(false);
        YAxis leftYAxis = myLineChart.getAxisLeft();
        leftYAxis.setEnabled(false);



        myLineChart.setData(combineData);
        myLineChart.invalidate();




    }//end of parseJsonObjs


}//end of class