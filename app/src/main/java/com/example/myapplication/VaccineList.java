package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VaccineList extends AppCompatActivity {


    //Declare
    ShimmerFrameLayout shimmer;
    RequestQueue rq; // for Volley
    ListView lv;
    BaseAdapter ba;
    SimpleCountry[] countryDS; // dS containing SimpleCountry class objects
    Map<String, SimpleCountry> mapNameSimpleCountry = new HashMap<>();
    Gson gson; // imported to fill up dS POJOs with Json data
    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_list);


        //Define
        shimmer = findViewById(R.id.ItemShimmer_id);

        TextView tvTitle = findViewById(R.id.vaccineTitle_id);

        lv = findViewById(R.id.lvVaccines_id);

        rq = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        gson = new GsonBuilder().create();


        sw = findViewById(R.id.vaccineSwitch_id);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked){
                    tvTitle.setText("Analyzed Data");
                    lv.setVisibility(View.GONE);
                }else{

                    tvTitle.setText("Vaccine Data");
                    lv.setVisibility(View.VISIBLE);
                }


            }
        });

        ImageView back = findViewById(R.id.imageViewBack_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VaccineList.this,DashBoard.class));
                finish();
            }
        });





        enableShimmer();

        getJsonData(new CountryVolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(SimpleCountry[] countryDS) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < countryDS.length; i++) {
                            mapNameSimpleCountry.put(countryDS[i].getCountry(), countryDS[i]);
                        }

                    }
                });


            }
        }, new VaccineVolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(JSONArray vaccineDS) {

                disableShimmer();

                Date dateCurrentMonth = new Date();
                // Set up the date format  to match that of the json file
                SimpleDateFormat sdJsonDateFormat = new SimpleDateFormat("M/d/yy");
                // Current Data
                Calendar c = Calendar.getInstance();
                c.setTime(dateCurrentMonth);
                c.add(Calendar.DATE, -2);
                String jsonFetchM = sdJsonDateFormat.format(new Date(c.getTimeInMillis()));


                ba = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return vaccineDS.length();
                    }

                    @Override
                    public Object getItem(int i) {
                        return null;
                    }

                    @Override
                    public long getItemId(int i) {
                        return 0;
                    }

                    @Override
                    public View getView(int i, View view, ViewGroup viewGroup) {

                        if (view == null) {
                            view = getLayoutInflater().inflate(R.layout.item, null);
                        }

                        ImageView flag = view.findViewById(R.id.flag_id);
                        TextView countryName = view.findViewById(R.id.countryName_id);
                        TextView countryDoses = view.findViewById(R.id.countryCasesDoses_id);
                        TextView countryCoverage = view.findViewById(R.id.countryDeathsCoverage_id);


                        try {

                            String strCountryFlagURL = mapNameSimpleCountry.containsKey(vaccineDS.getJSONObject(i).getString("country")) ? mapNameSimpleCountry.get(vaccineDS.getJSONObject(i).getString("country")).getCountryInfo().getFlag() : "https://disease.sh/assets/img/flags/unknown.png";


                            String strCountryName = mapNameSimpleCountry.containsKey(vaccineDS.getJSONObject(i).getString("country")) ? mapNameSimpleCountry.get(vaccineDS.getJSONObject(i).getString("country")).getCountryInfo().getIso2() : "XX";


                            String strCountryDoses = vaccineDS.getJSONObject(i).getJSONObject("timeline").getString(jsonFetchM) ;

                            String strCountryCoverage;
                            if (mapNameSimpleCountry.containsKey(vaccineDS.getJSONObject(i).getString("country"))) {
                                strCountryCoverage = String.format("%.0f",((Double.valueOf(strCountryDoses) / (mapNameSimpleCountry.get(vaccineDS.getJSONObject(i).getString("country")).getPopulation()))*100));
                            } else {
                                strCountryCoverage = "-1.0 ";
                            }



                            switch (vaccineDS.getJSONObject(i).getString("country")) {

                                case "Guernsey":
                                    strCountryName = "GG";
                                    strCountryFlagURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Flag_of_Guernsey.svg/200px-Flag_of_Guernsey.svg.png";
                                    strCountryCoverage = String.format("%.0f",((Double.valueOf(strCountryDoses) / 62792)*100));
                                    break;
                                case "Pitcairn":
                                    strCountryName = "PN";
                                    strCountryFlagURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/88/Flag_of_the_Pitcairn_Islands.svg/200px-Flag_of_the_Pitcairn_Islands.svg.png";
                                    strCountryCoverage = String.format("%.0f",((Double.valueOf(strCountryDoses) / 47)*100));
                                    break;
                                case "Tokelau":
                                    strCountryName = "TK";
                                    strCountryFlagURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/Flag_of_Tokelau.svg/200px-Flag_of_Tokelau.svg.png";
                                    strCountryCoverage = String.format("%.0f",((Double.valueOf(strCountryDoses) / 1499)*100));
                                    break;
                                case "Turkmenistan":
                                    strCountryName = "TM";
                                    strCountryFlagURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Flag_of_Turkmenistan.svg/200px-Flag_of_Turkmenistan.svg.png";
                                    strCountryCoverage = String.format("%.0f",((Double.valueOf(strCountryDoses) / 6193576)*100));
                                    break;
                                case "Tuvalu":
                                    strCountryName = "TV";
                                    strCountryFlagURL = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Flag_of_Tuvalu.svg/200px-Flag_of_Tuvalu.svg.png";
                                    strCountryCoverage = String.format("%.0f",((Double.valueOf(strCountryDoses) / 12057)*100));
                                    break;
                            }


                            Picasso.get().load(strCountryFlagURL).into(flag);
                            countryName.setText(strCountryName);
                            countryDoses.setText(strCountryDoses);
                            countryCoverage.setText(strCountryCoverage);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return view;
                    }
                };

                lv.setAdapter(ba);

            }
        });


    }//end of onCreate

    private void enableShimmer() {

        ba = new BaseAdapter() {
            @Override
            public int getCount() {
                return 50;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                if (view == null) {
                    view = getLayoutInflater().inflate(R.layout.itemshimmereffect, null);
                }

                shimmer = view.findViewById(R.id.ItemShimmer_id);
                shimmer.setEnabled(true);
                shimmer.startShimmer();
                if (!shimmer.isShimmerVisible())
                    shimmer.setVisibility(View.VISIBLE);
                return view;
            }
        };

        lv.setAdapter(ba);
    }

    public void disableShimmer() {
        Log.e("TAG", "disableShimmer: " + shimmer.isShimmerStarted() + " " + shimmer.isShimmerVisible());

        if (shimmer.isShimmerStarted()) {
            shimmer.stopShimmer();
            shimmer.hideShimmer();
            shimmer.setEnabled(false);
        }

    }


    private interface VaccineVolleyResponseListener {
        void onError(String message);

        void onResponse(JSONArray vaccineDS);
    }

    private interface CountryVolleyResponseListener {
        void onError(String message);

        void onResponse(SimpleCountry[] countryDS);
    }


    public void getJsonData(VaccineList.CountryVolleyResponseListener countryVolleyResponseListener, VaccineList.VaccineVolleyResponseListener vaccineVolleyResponseListener) {


        String urlCountry = "https://disease.sh/v3/covid-19/countries";

        StringRequest countryStringRequest = new StringRequest(Request.Method.GET, urlCountry, new Response.Listener<String>() {

            public Request.Priority getPriority() {
                return Request.Priority.IMMEDIATE;
            }


            @Override
            public void onResponse(String response) {

                countryDS = gson.fromJson(response, SimpleCountry[].class);
                Log.e("x", "onResponse country Data Source length :" + countryDS.length);
                countryVolleyResponseListener.onResponse(countryDS);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                countryVolleyResponseListener.onError("Error Fetching Country Data " + error.getMessage());
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(countryStringRequest);


//______________________________________________________________________________________________________
        // Request #2 for our Vaccine Data


        String url = "https://disease.sh/v3/covid-19/vaccine/coverage/countries?lastdays=10&fullData=false";

        JsonArrayRequest vaccineRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            public Request.Priority getPriority() {
                return Request.Priority.LOW;
            }

            @Override
            public void onResponse(JSONArray response) {
                vaccineVolleyResponseListener.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                vaccineVolleyResponseListener.onError("Error Fetching Vaccine Date " + error.getMessage());
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(vaccineRequest);


    }//end of getJsonData


}//end of class