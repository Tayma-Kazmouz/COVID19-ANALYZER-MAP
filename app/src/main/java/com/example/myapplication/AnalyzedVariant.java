package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalyzedVariant extends AppCompatActivity {



    //Declare
    Intent nData;
    String variantName,variantFD,variantDR;
    float vSDJJ,vSDModerna,vSDNovanax,vSDOxford,vSDPfizer;
    float vInfJJ,vInfModerna,vInfNovanax,vInfOxford,vInfPfizer;
    ImageView iv;
    BarChart barchartSD , barchartInf ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzed_variant);

        //Define

        nData = getIntent();
        variantName = nData.getStringExtra("variantName");
        variantFD = nData.getStringExtra("FD");
        variantDR = nData.getStringExtra("DR");

        vSDJJ = nData.getFloatExtra("SDJJ",0.0f);
        vSDModerna = nData.getFloatExtra("SDModerna",0.0f);
        vSDNovanax = nData.getFloatExtra("SDNovavax",0.0f);
        vSDOxford = nData.getFloatExtra("SDOxford",0.0f);
        vSDPfizer = nData.getFloatExtra("SDPfizer",0.0f);

        vInfJJ = nData.getFloatExtra("InfJJ",0.0f);
        vInfModerna = nData.getFloatExtra("InfModerna",0.0f);
        vInfNovanax = nData.getFloatExtra("InfNovavax",0.0f);
        vInfOxford = nData.getFloatExtra("InfOxford",0.0f);
        vInfPfizer = nData.getFloatExtra("InfPfizer",0.0f);

        iv = findViewById(R.id.ivVariant_id);

     switch (variantName){

         case "Ancestral":
             iv.setImageResource(R.drawable.ic_ancestrialvector);
             break;
         case "Alpha":
             iv.setImageResource(R.drawable.ic_alphavector);
             break;
         case "Beta":
             iv.setImageResource(R.drawable.ic_betavector);
             break;
         case "Gamma":
             iv.setImageResource(R.drawable.ic_gammavector);
             break;
         case "Delta":
             iv.setImageResource(R.drawable.ic_deltavector);
             break;
         case "Omicron":
             iv.setImageResource(R.drawable.ic_omicronvector);
             break;
     }

     TextView tvTitle = findViewById(R.id.varientTitle_id);
     tvTitle.setText(variantName+" Varient Details");

     TextView tvName = findViewById(R.id.vairiantNameAV_id);
     tvName.setText(variantName);

     TextView tvFD = findViewById(R.id.FirstDetectedAV_id);
     tvFD.setText("First Detected:\n"+variantFD);

     TextView tvDR = findViewById(R.id.DateReportedAV_id);
     tvDR.setText("Date Reported:\n"+variantDR);






     barchartSD = findViewById(R.id.analyzedBarChartSD_id);

        // customize
        barchartSD.getDescription().setEnabled(false);
        barchartSD.setDrawGridBackground(false);
        barchartSD.setDrawBarShadow(false);
        barchartSD.setFitBars(true);
        barchartSD.setPinchZoom(false);
        barchartSD.setNoDataText("Data Not Found");
        barchartSD.getLegend().setEnabled(false);
        barchartSD.animateY(1400, Easing.EaseInOutExpo);
        barchartSD.getAxisRight().setDrawGridLines(false);
        barchartSD.getXAxis().setDrawGridLines(false);
        barchartSD.getAxisRight().setEnabled(false);
        barchartSD.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barchartSD.setExtraLeftOffset(5);
        barchartSD.setExtraRightOffset(5);
        barchartSD.setScaleEnabled(false); // for zooming in and out

        // enables a horizontal dashed lines behind the bars
        barchartSD.getAxisLeft().setDrawGridLines(true);
        barchartSD.getAxisLeft().enableGridDashedLine(10f, 10f, 0f);
        barchartSD.getAxisLeft().setGridColor(Color.parseColor("#F0EEEE"));

        // bar Severe Disease Entries :
        ArrayList<BarEntry> yValsSD = new ArrayList<>();
        yValsSD.add(new BarEntry(0,vSDJJ*100));
        yValsSD.add(new BarEntry(1,vSDModerna*100));
        yValsSD.add(new BarEntry(2,vSDNovanax*100));
        yValsSD.add(new BarEntry(3,vSDOxford*100));
        yValsSD.add(new BarEntry(4,vSDPfizer*100));

        BarDataSet barSetSD = new BarDataSet(yValsSD,"Data Set SD");
        barSetSD.setColor(Color.parseColor("#FF647C"));
        barSetSD.setDrawValues(true); // values on top of each bar
        barSetSD.setHighlightEnabled(true);
        barSetSD.setValueTextSize(10);
        barSetSD.setValueTextColor(Color.parseColor("#99000000"));
        barSetSD.setValueFormatter(new PercentFormatter());

        BarData dataSD = new BarData(barSetSD);


        // GIVING THE X AXIS THE NAMES ACCORDING TO The Vaccine Name
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("J&J","Moderna","NovaVax","Oxford","Pfizer"));

        barchartSD.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues)); // sets the x axis to the Vaccine Names Strings created above
        barchartSD.getXAxis().setTextSize(8);
        barchartSD.getAxisLeft().setAxisMaximum(100.0f);//makes %100 the max value


        YAxis leftYAxis = barchartSD.getAxisLeft();
        leftYAxis.setEnabled(true);
        leftYAxis.setValueFormatter(new PercentFormatter());


        barchartSD.setData(dataSD);
        barchartSD.invalidate(); // refresh

        //------------------------------------------------------------------------------------------------------- 2nd Barchart -----------------------------------------------------------------------

        barchartInf = findViewById(R.id.analyzedBarChartInf_id);

        // customize
        barchartInf.getDescription().setEnabled(false);
        barchartInf.setDrawGridBackground(false);
        barchartInf.setDrawBarShadow(false);
        barchartInf.setFitBars(true);
        barchartInf.setPinchZoom(false);
        barchartInf.setNoDataText("Data Not Found");
        barchartInf.getLegend().setEnabled(false);
        barchartInf.animateY(1400, Easing.EaseInOutExpo);
        barchartInf.getAxisRight().setDrawGridLines(false);
        barchartInf.getXAxis().setDrawGridLines(false);
        barchartInf.getAxisRight().setEnabled(false);
        barchartInf.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barchartInf.setExtraLeftOffset(5);
        barchartInf.setExtraRightOffset(5);
        barchartInf.setScaleEnabled(false); // for zooming in and out

        // enables a horizontal dashed lines behind the bars
        barchartInf.getAxisLeft().setDrawGridLines(true);
        barchartInf.getAxisLeft().enableGridDashedLine(10f, 10f, 0f);
        barchartInf.getAxisLeft().setGridColor(Color.parseColor("#F0EEEE"));

        // bar Infection Entries :
        ArrayList<BarEntry> yValsInfection = new ArrayList<>();
        yValsInfection.add(new BarEntry(0,vInfJJ*100));
        yValsInfection.add(new BarEntry(1,vInfModerna*100));
        yValsInfection.add(new BarEntry(2,vInfNovanax*100));
        yValsInfection.add(new BarEntry(3,vInfOxford*100));
        yValsInfection.add(new BarEntry(4,vInfPfizer*100));

        BarDataSet barSetInfection = new BarDataSet(yValsInfection,"Data Set Infection");
        barSetInfection.setColor(Color.parseColor("#71CCFF"));
        barSetInfection.setDrawValues(true); // values on top of each bar
        barSetInfection.setHighlightEnabled(true);
        barSetInfection.setValueTextSize(10);
        barSetInfection.setValueTextColor(Color.parseColor("#99000000"));
        barSetInfection.setValueFormatter(new PercentFormatter());


        BarData dataInf = new BarData(barSetInfection);


      //   GIVING THE X AXIS THE NAMES ACCORDING TO The Vaccine Name

        barchartInf.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues)); // sets the x axis to the Vaccine Names Strings created above
        barchartInf.getXAxis().setTextSize(8);
        barchartInf.getAxisLeft().setAxisMaximum(100.0f);//makes %100 the max value


        YAxis leftYAxisInf = barchartInf.getAxisLeft();
        leftYAxisInf.setEnabled(true);
        leftYAxisInf.setValueFormatter(new PercentFormatter());


        barchartInf.setData(dataInf);
        barchartInf.invalidate(); // refresh


    }//end of onCreate


    @Override
    protected void onStart() {
        super.onStart();
        // force disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }



}//end of class