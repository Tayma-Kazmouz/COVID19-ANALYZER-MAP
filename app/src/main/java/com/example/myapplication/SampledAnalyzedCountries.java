package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

public class SampledAnalyzedCountries extends AppCompatActivity {


    //Declare
    PieChart pFR,pGE,pUSA,pUA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampled_analyzed_countries);

   //Define
    pFR = findViewById(R.id.piechartFR_id);
    pGE = findViewById(R.id.piechartGE_id);
    pUSA = findViewById(R.id.piechartUSA_id);
    pUA = findViewById(R.id.piechartUA_id);

        pFR.setDrawHoleEnabled(true); // gives donut shape
        pFR.getDescription().setEnabled(false); // removing description of the pie chart


        ArrayList<PieEntry> entriesFR = new ArrayList<>();
        entriesFR.add(new PieEntry(77.04f)); // Pfizer
        entriesFR.add(new PieEntry(0.77f)); //J&J
        entriesFR.add(new PieEntry(16.63f)); // Moderna
        entriesFR.add(new PieEntry(5.56f)); // Oxford

        PieDataSet dataSetFR = new PieDataSet(entriesFR,"");
        dataSetFR.setColors(
                Color.parseColor("#FF3D7D") // Pfizer
                ,Color.parseColor("#47FF59") // J&J
                ,Color.parseColor("#FFB957") // Moderna
                ,Color.parseColor("#9960FF") // Oxford
        );

        PieData dataFR = new PieData(dataSetFR);
        dataFR.setDrawValues(true);

        dataFR.setValueFormatter(new PercentFormatter(pFR));
        pFR.setUsePercentValues(true);

        dataFR.setValueTextSize(12f);
        dataFR.setValueTextColor(Color.BLACK);

        pFR.getLegend().setEnabled(false);
        pFR.setData(dataFR);



        pFR.invalidate(); // data updated.. refresh on the the screen

        pFR.animateY(1400, Easing.EaseInOutQuad);

        // Germany

        pGE.setDrawHoleEnabled(true); // gives donut shape
        pGE.getDescription().setEnabled(false); // removing description of the pie chart


        ArrayList<PieEntry> entriesGE = new ArrayList<>();
        entriesGE.add(new PieEntry(73.26f)); // Pfizer
        entriesGE.add(new PieEntry(2.12f)); //J&J
        entriesGE.add(new PieEntry(17.13f)); // Moderna
        entriesGE.add(new PieEntry(7.46f)); // Oxford
        entriesGE.add(new PieEntry(0.32f)); // NovaVax

        PieDataSet dataSetGE = new PieDataSet(entriesGE,"");
        dataSetGE.setColors(
                Color.parseColor("#FF3D7D") // Pfizer
                ,Color.parseColor("#47FF59") // J&J
                ,Color.parseColor("#FFB957") // Moderna
                ,Color.parseColor("#9960FF") // Oxford
                ,Color.parseColor("#38B2FF") // NovaVax
        );

        PieData dataGE = new PieData(dataSetGE);
        dataFR.setDrawValues(true);

        dataGE.setValueFormatter(new PercentFormatter(pGE));
        pGE.setUsePercentValues(true);

        dataGE.setValueTextSize(12f);
        dataGE.setValueTextColor(Color.BLACK);

        pGE.getLegend().setEnabled(false);
        pGE.setData(dataGE);



        pGE.invalidate(); // data updated.. refresh on the the screen

        pGE.animateY(1400, Easing.EaseInOutQuad);


        // USA

        pUSA.setDrawHoleEnabled(true); // gives donut shape
        pUSA.getDescription().setEnabled(false); // removing description of the pie chart


        ArrayList<PieEntry> entriesUSA = new ArrayList<>();
        entriesUSA.add(new PieEntry(58.97f)); // Pfizer
        entriesUSA.add(new PieEntry(3.33f)); //J&J
        entriesUSA.add(new PieEntry(37.61f)); // Moderna

        PieDataSet dataSetUSA = new PieDataSet(entriesUSA,"");
        dataSetUSA.setColors(
                Color.parseColor("#FF3D7D") // Pfizer
                ,Color.parseColor("#47FF59") // J&J
                ,Color.parseColor("#FFB957") // Moderna
        );

        PieData dataUSA = new PieData(dataSetUSA);
        dataUSA.setDrawValues(true);

        dataUSA.setValueFormatter(new PercentFormatter(pUSA));
        pUSA.setUsePercentValues(true);

        dataUSA.setValueTextSize(12f);
        dataUSA.setValueTextColor(Color.BLACK);

        pUSA.getLegend().setEnabled(false);
        pUSA.setData(dataUSA);



        pUSA.invalidate(); // data updated.. refresh on the the screen

        pUSA.animateY(1400, Easing.EaseInOutQuad);

        // Ukraine

        pUA.setDrawHoleEnabled(true); // gives donut shape
        pUA.getDescription().setEnabled(false); // removing description of the pie chart


        ArrayList<PieEntry> entriesUA = new ArrayList<>();
        entriesUA.add(new PieEntry(46.63f)); // Pfizer
        entriesUA.add(new PieEntry(0.07f)); //J&J
        entriesUA.add(new PieEntry(9.61f)); // Moderna
        entriesUA.add(new PieEntry(12.76f)); // Oxford
        entriesUA.add(new PieEntry(30.94f)); // Sinovac

        PieDataSet dataSetUA = new PieDataSet(entriesUA,"");
        dataSetUA.setColors(
                Color.parseColor("#FF3D7D") // Pfizer
                ,Color.parseColor("#47FF59") // J&J
                ,Color.parseColor("#FFB957") // Moderna
                ,Color.parseColor("#9960FF") // Oxford
                ,Color.parseColor("#FAE04E") // Sinovac
        );

        PieData dataUA = new PieData(dataSetUA);
        dataUA.setDrawValues(true);

        dataUA.setValueFormatter(new PercentFormatter(pUA));
        pUA.setUsePercentValues(true);

        dataUA.setValueTextSize(12f);
        dataUA.setValueTextColor(Color.BLACK);

        pUA.getLegend().setEnabled(false);
        pUA.setData(dataUA);



        pUA.invalidate(); // data updated.. refresh on the the screen

        pUA.animateY(1400, Easing.EaseInOutQuad);

    }//end of onCreate




}//end of class
