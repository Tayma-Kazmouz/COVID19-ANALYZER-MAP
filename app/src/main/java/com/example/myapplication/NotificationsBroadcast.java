package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationsBroadcast extends BroadcastReceiver {


    public static final int NotificationID = 1;
    public static final String channelID = "country_data";
    public static final String titleExtra = "titleExtra";
    public static final String messageExtra = "messageExtra";



    @Override
    public void onReceive(Context context, Intent intent) {


        Gson gson = new GsonBuilder().create();
        RequestQueue rq = VolleySingleton.getInstance(context).getRequestQueue();

        String url = "https://disease.sh/v3/covid-19/countries/"+intent.getStringExtra(messageExtra);

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                  String Cases =  response.getString("cases");
                  String Deaths = response.getString("deaths");


                    NotificationCompat.Builder CountryDataNotification = new NotificationCompat.Builder(context,channelID)
                            .setSmallIcon(R.drawable.ic_coronacasesiconsvg)
                            .setColor(Color.parseColor("#FFC839"))
                            .setContentTitle(intent.getStringExtra(titleExtra))
                            .setContentText("Cases: "+Cases + " Deaths: "+Deaths)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true);

                    NotificationManagerCompat notifyManage = NotificationManagerCompat.from(context);

                    notifyManage.notify(NotificationID,CountryDataNotification.build());



                } catch (JSONException e) {
                    e.printStackTrace(); }

            }//end of on response
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NotificationCompat.Builder CountryDataNotification = new NotificationCompat.Builder(context,channelID)
                        .setSmallIcon(R.drawable.ic_coronacasesiconsvg)
                        .setColor(Color.parseColor("#FFC839"))
                        .setContentTitle(intent.getStringExtra(titleExtra))
                        .setContentText("No Data Available")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

                NotificationManagerCompat notifyManage = NotificationManagerCompat.from(context);

                notifyManage.notify(NotificationID,CountryDataNotification.build());


            }
        });

        rq.add(jsonReq);

    }



}//end of class
