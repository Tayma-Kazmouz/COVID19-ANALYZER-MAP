package com.example.myapplication;

import android.content.Context;

import android.content.res.Resources;

import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import android.widget.ImageView;

import android.widget.TextView;



import androidx.annotation.NonNull;

import androidx.annotation.Nullable;

import com.blongho.country_data.World;


public class CountryPickerAdapter extends ArrayAdapter<String>

{

    Context c;

    String[] names;

    int[] images;





    public CountryPickerAdapter(Context context, String[] names) {

        super(context, R.layout.spinner_item,names);

        this.c = context;

        this.names = names;

    }



    @Override

    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {



        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.spinner_item,null);

        TextView t1 = (TextView)row.findViewById(R.id.text);

        ImageView i1 = (ImageView) row.findViewById(R.id.imge);



        t1.setText(names[position]);


        i1.setImageResource(World.getFlagOf(names[position].toString()
                .substring(names[position].toString().lastIndexOf("(") + 1
                        ,names[position].toString().lastIndexOf(")")).trim()));



        return row;

    }



    @NonNull

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.spinner_item,null);

        TextView t1 = (TextView)row.findViewById(R.id.text);

        ImageView i1 = (ImageView) row.findViewById(R.id.imge);



        t1.setText(names[position]);


        i1.setImageResource(World.getFlagOf(names[position].toString()
                .substring(names[position].toString().lastIndexOf("(") + 1
                        ,names[position].toString().lastIndexOf(")")).trim()));



        return row;

    }

}