package com.example.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static volatile VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context ctx;

    //private constructor which take context to make a single entire app global request queue
    private VolleySingleton(Context context) {
        ctx = context;
        mRequestQueue = getRequestQueue();

    }

    // guarantee the singularity of the this class instantiation via a public method named getInstance()
    public static  VolleySingleton getInstance(Context context){
       // check if its first time that class object is instantiated
        // the create a Double Checked Locking Mechanism for MultiThreaded Scenarios...
        if (mInstance == null){
            synchronized (VolleySingleton.class){
                if (mInstance == null){
                    mInstance = new VolleySingleton(context);
                }
            }
        }
        return mInstance;
    }

    // a getter for for our requestQueue
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }



}//end of class
