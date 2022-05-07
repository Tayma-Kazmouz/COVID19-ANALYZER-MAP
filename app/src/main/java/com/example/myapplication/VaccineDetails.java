package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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

public class VaccineDetails extends AppCompatActivity {

    String COUNTRY_NAME;

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

    LineChart myLineChart;

    RequestQueue rq;

    TextView tvCountryFullName, tvCountryPopulation, tvCountryContinent;
    ShapeableImageView ivFlag, ivMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_details);

        rq = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        myLineChart = findViewById(R.id.detailedLineChartVaccine_id);

        tvCountryFullName = findViewById(R.id.CountryFullNameVaccine_id);
        tvCountryPopulation = findViewById(R.id.countryPopulationVaccine_id);
        tvCountryContinent = findViewById(R.id.countryContinentVaccine_id);
        ivFlag = findViewById(R.id.ivFlagVaccine_id);
        ivMap = findViewById(R.id.ivMapVaccine_id);


        Intent intent = getIntent();

        // getting the country name which is clicked from the vaccine list
        COUNTRY_NAME = intent.getStringExtra("getVaccineCountry");

        // getting the parcelable class object from the previous activity to access it data
        SimpleCountry countryData = intent.getParcelableExtra("getSimpleCountryInfo");

        TextView tvTitle = findViewById(R.id.detailedTitleVaccine_id);
        tvTitle.setText(COUNTRY_NAME + " Details");


        tvCountryFullName.setText(COUNTRY_NAME);

        switch (COUNTRY_NAME) {

            case "Guernsey":
                tvCountryPopulation.setText("Population\n" + 62792);
                tvCountryContinent.setText("Continent\n" +"Europe" );
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Flag_of_Guernsey.svg/200px-Flag_of_Guernsey.svg.png").into(ivFlag);

                String mapURLGG = "https://api.jawg.io/static?zoom=12&center=" + "49.448196" + "," + "-2.589490" + "&size=1500x1500&layer=jawg-terrain&format=png&access-token=***REMOVED***";
                Picasso.get().load(mapURLGG).placeholder(R.drawable.defaultmap).into(ivMap);

                break;
            case "Pitcairn":
                tvCountryPopulation.setText("Population\n" + 47);
                tvCountryContinent.setText("Continent\n" +"Oceania" );
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/8/88/Flag_of_the_Pitcairn_Islands.svg/200px-Flag_of_the_Pitcairn_Islands.svg.png").into(ivFlag);

                String mapURLPN = "https://api.jawg.io/static?zoom=15&center=-25.066668,-130.100006&size=1500x1500&layer=jawg-terrain&format=png&access-token=***REMOVED***";
                Picasso.get().load(mapURLPN).placeholder(R.drawable.defaultmap).into(ivMap);

                break;
            case "Tokelau":
                tvCountryPopulation.setText("Population\n" + 1499);
                tvCountryContinent.setText("Continent\n" +"Oceania" );
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/Flag_of_Tokelau.svg/200px-Flag_of_Tokelau.svg.png").into(ivFlag);

                String mapURLTK = "https://api.jawg.io/static?zoom=13&center=-9.1668337,-171.8182111&size=1500x1500&layer=jawg-terrain&format=png&access-token=***REMOVED***";
                Picasso.get().load(mapURLTK).placeholder(R.drawable.defaultmap).into(ivMap);

                break;
            case "Turkmenistan":
                tvCountryPopulation.setText("Population\n" + 6193576);
                tvCountryContinent.setText("Continent\n" +"Asia" );
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Flag_of_Turkmenistan.svg/200px-Flag_of_Turkmenistan.svg.png").into(ivFlag);

                String mapURLTM = "https://api.jawg.io/static?zoom=6&center=" + "39" + "," + "59" + "&size=1500x1500&layer=jawg-terrain&format=png&access-token=***REMOVED***";
                Picasso.get().load(mapURLTM).placeholder(R.drawable.defaultmap).into(ivMap);


                break;
            case "Tuvalu":
                tvCountryPopulation.setText("Population\n" + 12057);
                tvCountryContinent.setText("Continent\n" +"Oceania" );
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Flag_of_Tuvalu.svg/200px-Flag_of_Tuvalu.svg.png").into(ivFlag);

                String mapURLTV = "https://api.jawg.io/static?zoom=14&center=-8.521147,179.196198&size=1500x1500&layer=jawg-terrain&format=png&access-token=***REMOVED***";
                Picasso.get().load(mapURLTV).placeholder(R.drawable.defaultmap).into(ivMap);

                break;

            default:

        tvCountryPopulation.setText("Population\n" + countryData.getPopulation());
        tvCountryContinent.setText("Continent\n" + countryData.getContinent());
        Picasso.get().load(countryData.getCountryInfo().getFlag()).into(ivFlag);


        //here we use a free static map api from Jawg.io ...access token is created from a throwaway account ... use another access token in case of request failure
        String mapURL = "https://api.jawg.io/static?zoom=6&center=" + countryData.getCountryInfo().getLat() + "," + countryData.getCountryInfo().getLong() + "&size=1500x1500&layer=jawg-terrain&format=png&access-token=***REMOVED***";


        // if its one of the big countries then fetch a zoomed out static map from the API
        if (countryData.getCountry().matches("USA|Canada|Russia|China|Brazil|Kazakhstan|Greenland"))
        {
            String mapBigURL = "https://api.jawg.io/static?zoom=4&center=" + countryData.getCountryInfo().getLat() + "," + countryData.getCountryInfo().getLong() + "&size=1500x1500&layer=jawg-terrain&format=png&access-token=***REMOVED***";

            Picasso.get().load(mapBigURL).placeholder(R.drawable.defaultmap).into(ivMap);
        }else if (countryData.getCountry().equals("Australia")){
            String mapAusURL = "https://api.jawg.io/static?zoom=5&center=" + countryData.getCountryInfo().getLat() + "," + countryData.getCountryInfo().getLong() + "&size=1500x1500&layer=jawg-terrain&format=png&access-token=***REMOVED***";

            Picasso.get().load(mapAusURL).placeholder(R.drawable.defaultmap).into(ivMap);
        }else{
            Picasso.get().load(mapURL).placeholder(R.drawable.defaultmap).into(ivMap);
        }
        break;

        }//end of switch




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

        // Current Month ((5 Days Ago... Since Today's Data May Not Be Updated Yet))
        Calendar c = Calendar.getInstance();
        c.setTime(dateCurrentMonth);
        c.add(Calendar.DATE, -5);
        String M = dateFormatMonth.format(dateCurrentMonth);
        nameM = mapMonths.get(M);


        //Last Month
        Calendar c1 = Calendar.getInstance();
        c1.setTime(dateCurrentMonth);
        c1.add(Calendar.MONTH, -1);
        String M1 = dateFormatMonth.format(new Date(c1.getTimeInMillis()));
        nameM1 = mapMonths.get(M1);


        // 2 Months Ago
        Calendar c2 = Calendar.getInstance();
        c2.setTime(dateCurrentMonth);
        c2.add(Calendar.MONTH, -2);
        String M2 = dateFormatMonth.format(new Date(c2.getTimeInMillis()));
        nameM2 = mapMonths.get(M2);


        // 3 Months Ago
        Calendar c3 = Calendar.getInstance();
        c3.setTime(dateCurrentMonth);
        c3.add(Calendar.MONTH, -3);
        String M3 = dateFormatMonth.format(new Date(c3.getTimeInMillis()));
        nameM3 = mapMonths.get(M3);



        // 4 Months Ago
        Calendar c4 = Calendar.getInstance();
        c4.setTime(dateCurrentMonth);
        c4.add(Calendar.MONTH, -4);
        String M4 = dateFormatMonth.format(new Date(c4.getTimeInMillis()));
        nameM4 = mapMonths.get(M4);


        //5 Months Ago
        Calendar c5 = Calendar.getInstance();
        c5.setTime(dateCurrentMonth);
        c5.add(Calendar.MONTH, -5);
        String M5 = dateFormatMonth.format(new Date(c5.getTimeInMillis()));
        nameM5 = mapMonths.get(M5);


            getVaccineTimeline(new TimelineVolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(VaccineDetails.this,message,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(JSONObject timelineDS) {

                    try {

                        JSONArray ja = timelineDS.getJSONArray("timeline");

                        Log.e("x", "onResponse: ja first object Date "+ja.getJSONObject(0).getString("date") );

                        // Setting up the Today CV with data of the fifth to last object in the array as today's data may be not updated yet...
                        TextView tvDosesToday = findViewById(R.id.todayDosesVaccine_id);
                        TextView tvDosesMill = findViewById(R.id.todayDosesPerMillionVaccine_id);
                        tvDosesToday.setText("+"+ja.optJSONObject(ja.length()-5).getString("daily"));
                        tvDosesMill.setText("+"+ja.optJSONObject(ja.length()-5).getString("dailyPerMillion"));


                        // Setting up the vaccine line chart ...


                        // for the current month
                        long data0 = ja.optJSONObject(ja.length()-2).getLong("total");
                        // for the -1 month
                        long data1 = ja.optJSONObject(ja.length()-30).getLong("total");
                        // for the -2 month
                        long data2 = ja.optJSONObject(ja.length()-60).getLong("total");
                        // for the -3 month
                        long data3 = ja.optJSONObject(ja.length()-90).getLong("total");
                        // for the -4 month
                        long data4 = ja.optJSONObject(ja.length()-120).getLong("total");
                        // for the -5 month
                        long data5 = ja.optJSONObject(ja.length()-180).getLong("total");

                        ///// ------------------------------PLOT THE DATA ABOVE------------------------------------------


                        // Step 1 -- Array List with Entries
                        ArrayList<Entry> activeCasesEntries = new ArrayList<Entry>();
                        activeCasesEntries.add(new Entry(0,data5));
                        activeCasesEntries.add(new Entry(1,data4));
                        activeCasesEntries.add(new Entry(2,data3));
                        activeCasesEntries.add(new Entry(3,data2));
                        activeCasesEntries.add(new Entry(4,data1));
                        activeCasesEntries.add(new Entry(5,data0));

                        //Step 2 DataSet

                        LineDataSet lineDataSetCases = new LineDataSet(activeCasesEntries,"Vaccine Statistics");

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


                        LineData Data = new LineData(lineDataSetCases);

                        //Disables the y Axis
                        YAxis rightYAxis = myLineChart.getAxisRight();
                        rightYAxis.setEnabled(false);
                        YAxis leftYAxis = myLineChart.getAxisLeft();
                        leftYAxis.setEnabled(true);
                        leftYAxis.setValueFormatter(new LargeValueFormatter());


                        myLineChart.setData(Data);
                        myLineChart.invalidate();


                    } catch (JSONException e) {
                        e.printStackTrace(); }
                }//end of on Response
            });



    }//end of onCreate

    //Fetching Json Data by country by changing the suffix after '/countries'
    //https://disease.sh/v3/covid-19/vaccine/coverage/countries/{COUNTRY_NAME}?lastdays=200&fullData=true

    private interface TimelineVolleyResponseListener {
        void onError(String message);

        void onResponse(JSONObject timelineDS);
    }

    private void getVaccineTimeline(VaccineDetails.TimelineVolleyResponseListener timelineVolleyResponseListener){

        String urlCountry = "https://disease.sh/v3/covid-19/vaccine/coverage/countries/"+COUNTRY_NAME+"?lastdays=200&fullData=true";

        ProgressDialog pd = new ProgressDialog(VaccineDetails.this);
        pd.setMessage("Loading...");
        pd.show();


        JsonObjectRequest timelineRequest = new JsonObjectRequest(Request.Method.GET, urlCountry, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.dismiss();
            timelineVolleyResponseListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                timelineVolleyResponseListener.onError("Error Fetching Timeline Data");
                Log.e("x", "onErrorResponse: "+error.getMessage());
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(timelineRequest);




    }//end of fetch method





}//end of class