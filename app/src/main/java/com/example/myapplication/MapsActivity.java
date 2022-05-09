package com.example.myapplication;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.myapplication.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    RequestQueue rq ; // for volley
    Corona[] dS; // dS containing Corona class objects
    Gson gson; // imported to fill up dS POJOs with Json data

    Map<Circle, Corona> dataCircleMap =  new HashMap();
    Map<Circle,Double>mapCircleRadius = new HashMap<>();
    double maxCases;
    double factor;
    Dialog infoBox;
    //Initialize to a non-valid zoom value
    private float previousZoomLevel = -1.0f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rq = VolleySingleton.getInstance(this).getRequestQueue();

        gson = new GsonBuilder().create();



        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        infoBox = new Dialog(this);
        // set up the theme of the map
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.custom_map_style));


        getJsonData(new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(MapsActivity.this,"Cant Retrieve Data",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Corona[] dS) {


                maxCases = getMax(dS); // find the country with the most cases to use for scaling reference
                for (Corona data: dS ) {

                   factor = getFactor(data); // get a factor from 0.0 to 1.0 for each country
                    double radius = 2000000 * factor;
//                     since some counties circle are too small ... set a min size and scale up
                        if (radius<100000){ // if smaller than 100 km scale up 2 times
                            radius = radius *2;
                            if (radius<20000)
                                radius = 40000;
                            else if (radius<30000)
                                radius = 50000;
                            else if (radius<40000)
                                radius = 60000;
                            else if (radius<50000)
                                radius = 70000; }



//                    Log.e(data.getCountry(), "    list "+data.getCountry()+ " :  radius   "+radius+" factor: "+factor  +"    cases: "+data.getCases());
                    // add circles to the map according to the lat and long values of each country
                    CircleOptions cCountryOptions = new CircleOptions()
                            .center(new LatLng(data.getCountryInfo().getLat(), data.getCountryInfo().getLong()))
                            .fillColor(Color.parseColor("#95FF7575"))
                            .strokeColor(Color.argb(99,255,255,255))
                            .strokeWidth(2)
                            .clickable(true)
                            .radius(radius);// set by meters
                    Circle cCountry = mMap.addCircle(cCountryOptions);
                    cCountry.setTag(data.getCountry());
                    dataCircleMap.put(cCountry,data);// add circles to map matched with pojo data
                    mapCircleRadius.put(cCountry,radius); // keeps track of original radius of each circle
                }


            }//end of onResponse
        }); //  end of getJsonData



        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(@NonNull CameraPosition position) {
                for (Map.Entry<Circle,Double> entry:mapCircleRadius.entrySet()) {
                    double newRadius;
                    double INITIAL_RADIUS = entry.getValue();

                    // when max zoom out
                    if (position.zoom == mMap.getMinZoomLevel()){
                        newRadius = INITIAL_RADIUS;
                    }
                    // when zoom out
                    else if (position.zoom < previousZoomLevel && Math.abs(position.zoom - previousZoomLevel)>0.15){
                        newRadius = Math.min(INITIAL_RADIUS,entry.getKey().getRadius() * Math.pow(1.2, position.zoom));
                    }
                    //when zoom in
                    else if (position.zoom > previousZoomLevel) {
                        newRadius = Math.min(INITIAL_RADIUS,INITIAL_RADIUS/ Math.pow(1.5, position.zoom)) ;
                        if (newRadius<20000)
                            newRadius = 40000;
                        else if (newRadius<10000)
                            newRadius = 20000;

                        // when no zoom occurs but map is being dragged
                    }else{
                        return;
                    }
                    Log.e("x", " Previous Zoom = "+previousZoomLevel+"   positionZoom = "+position.zoom+"   newRadius = "+newRadius);
                    entry.getKey().setRadius(newRadius);


                }

                previousZoomLevel = position.zoom;
            }
        });




        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
            @Override
            public void onCircleClick(@NonNull Circle circle) {
                String nameTag = (String) circle.getTag();

                // Create a Pop Up View
                infoBox.setContentView(R.layout.mapinfobox);
                infoBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Window window = infoBox.getWindow();
                window.setGravity(Gravity.CENTER);
                window.getAttributes().windowAnimations = R.style.MapInfoBoxAnimation;

                // Declare and Define
                TextView countryFullNameTv = infoBox.findViewById(R.id.mapinfoFullName_id);
                TextView countryCasesTv = infoBox.findViewById(R.id.mapinfoCases_id);
                TextView countryActiveTv = infoBox.findViewById(R.id.mapinfoActive_id);
                TextView countryRecoveredTv = infoBox.findViewById(R.id.mapinfoRecovered_id);
                TextView countryDeathsTv = infoBox.findViewById(R.id.mapinfoDeaths_id);
                ShapeableImageView countyFlag = infoBox.findViewById(R.id.ivflagmapinfo_id);

                // Set Values
                Picasso.get().load(dataCircleMap.get(circle).getCountryInfo().getFlag()).placeholder(R.drawable.ic_sampleflagsvg).into(countyFlag);
                countryFullNameTv.setText(nameTag);
                countryFullNameTv.setTypeface(ResourcesCompat.getFont(getApplicationContext(),R.font.poppins));
                countryCasesTv.setText("+"+dataCircleMap.get(circle).getCases());
                countryCasesTv.setTypeface(ResourcesCompat.getFont(getApplicationContext(),R.font.poppins_black));
                countryActiveTv.setText("+"+dataCircleMap.get(circle).getActive());
                countryActiveTv.setTypeface(ResourcesCompat.getFont(getApplicationContext(),R.font.poppins_black));
                countryRecoveredTv.setText("+"+dataCircleMap.get(circle).getRecovered());
                countryRecoveredTv.setTypeface(ResourcesCompat.getFont(getApplicationContext(),R.font.poppins_black));
                countryDeathsTv.setText("+"+dataCircleMap.get(circle).getDeaths());
                countryDeathsTv.setTypeface(ResourcesCompat.getFont(getApplicationContext(),R.font.poppins_black));


                // change font

                TextView tvCases = infoBox.findViewById(R.id.mapinfocasestext_id);
                TextView tvActive = infoBox.findViewById(R.id.mapinfoactivetext_id);
                TextView tvRecovered = infoBox.findViewById(R.id.mapinforecoveredtext_id);
                TextView tvDeaths = infoBox.findViewById(R.id.mapinfodeathstext_id);

                tvCases.setTypeface(ResourcesCompat.getFont(getApplicationContext(),R.font.poppins));
                tvActive.setTypeface(ResourcesCompat.getFont(getApplicationContext(),R.font.poppins));
                tvRecovered.setTypeface(ResourcesCompat.getFont(getApplicationContext(),R.font.poppins));
                tvDeaths.setTypeface(ResourcesCompat.getFont(getApplicationContext(),R.font.poppins));

                infoBox.show();

            }
        });





    }//end of onMapReady

    private double getFactor(Corona data) {
        double factor = data.getCases() / maxCases;
        return factor ;
    }

    private double getMax(Corona[] myData) {
        double max = myData[0].getCases();
        for (int i =0 ; i<myData.length ;i++){
            if (max < myData[i].getCases()) {
                max = myData[i].getCases();
            }
        }
        return max;
    }




    private interface VolleyResponseListener {
        void onError(String message);

        void onResponse(Corona[] dS);
    }


    private void getJsonData(MapsActivity.VolleyResponseListener volleyResponseListener) {

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
                volleyResponseListener.onError("Error Fetching Data ");
                Log.e("x", "onErrorResponse: "+error.getMessage());
            }
        });

        rq.add(stringRequest);

    }//end of getJsonData





}//end of class