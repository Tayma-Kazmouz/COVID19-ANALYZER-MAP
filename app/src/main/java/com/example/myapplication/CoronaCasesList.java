package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

public class CoronaCasesList extends AppCompatActivity {


    //Declare
    ShimmerFrameLayout shimmer;
    RequestQueue rq; // for Volley
    ListView lv;
    BaseAdapter ba;
    Corona[] dS; // dS containing Corona class objects
    Gson gson; // imported to fill up dS POJOs with Json data


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_cases_list);


        //Define
        shimmer = findViewById(R.id.ItemShimmer_id);



        lv = findViewById(R.id.lvCases_id);

        rq = VolleySingleton.getInstance(this).getRequestQueue();


        gson = new GsonBuilder().create();

        enableShimmer();


        ImageView back = findViewById(R.id.imageViewCasesBack_id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoronaCasesList.this,DashBoard.class));
                finish();
            }
        });






        getJsonData(new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Corona[] dS) {

                disableShimmer();


                ba = new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return dS.length;
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
                        TextView countryCases = view.findViewById(R.id.countryCasesDoses_id);
                        TextView countryDeaths = view.findViewById(R.id.countryDeathsCoverage_id);


                        String flagUrl = dS[i].getCountryInfo().getFlag();
                        String countryNameVal = dS[i].getCountryInfo().getIso2();
                        String countryTotalCasesVal = dS[i].getCases().toString();
                        String countryTotalDeathsVal = dS[i].getDeaths().toString();
                        String countryFullName = dS[i].getCountry().toString();


                        Picasso.get().load(flagUrl).into(flag);
                        countryName.setText(countryNameVal);
                        countryName.setTextSize(14);
                        countryCases.setText(countryTotalCasesVal);
                        countryDeaths.setText(countryTotalDeathsVal);

                        // The Following are Cruise Ships which only have a full name attribute and no Iso names

                        if (countryFullName.equals("Diamond Princess")){
                            countryName.setText("Diamond\nPrincess");
                            countryName.setTextSize(5);

                            // since it belongs to the UK
                            Picasso.get().load("https://disease.sh/assets/img/flags/gb.png").into(flag);

                        }

                        if (countryFullName.equals("MS Zaandam")){
                            countryName.setText("MS\nZaandam");
                            countryName.setTextSize(5);

                            // since it belongs to the Netherlands
                            Picasso.get().load("https://disease.sh/assets/img/flags/nl.png").into(flag);
                        }


                        return view;
                    }
                };

                lv.setAdapter(ba);

            }//end of onResponse
        });

    }//end onCreate

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
                if ( !shimmer.isShimmerVisible())
                    shimmer.setVisibility(View.VISIBLE);
                return view;
            }
        };

        lv.setAdapter(ba);

    }

    public void disableShimmer() {
        Log.e("TAG", "disableShimmer: " + shimmer.isShimmerStarted()+ " "+shimmer.isShimmerVisible());

        if ( shimmer.isShimmerStarted()){
            shimmer.stopShimmer();
            shimmer.hideShimmer();
            shimmer.setEnabled(false);
        }

    }

    private interface VolleyResponseListener {
        void onError(String message);

        void onResponse(Corona[] dS);
    }


    private void getJsonData(VolleyResponseListener volleyResponseListener) {

        String url = "https://disease.sh/v3/covid-19/countries";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dS = gson.fromJson(response, Corona[].class);
                Log.e("x", "onResponse Corona Data Source length :" + dS.length);
                volleyResponseListener.onResponse(dS);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyResponseListener.onError("Error Fetching Data "+error.getMessage());
            }
        });

        rq.add(stringRequest);

    }//end of getJsonData


}//end of class