package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CommunityActivity extends AppCompatActivity {

        Switch sw ;
        ImageView backArrow;
        //Vaccines
        BarChart bcPfizer,bcModerna,bcJJ,bcAstra,bcSino,bcNoneV,bcMalesV,bcFemalesV,bcTotalV;
        PieChart pcVaccines;
        // Covid
        BarChart bcPositive,bcNegative,bcContacted,bcNone,bcTotalC;
        PieChart pcPositive ,pcNegative,pcContacted,pcNone;

        private FirebaseFirestore db;
        private DocumentReference refReports;

        TextView tvTotalVaccine,tvTotalCovid;

        //Vaccines
        Long age10P,age11P,age20P,age30P,age40P,age50P,age60P,age70P,age80P,totP;//pfizer
        Long age10M,age11M,age20M,age30M,age40M,age50M,age60M,age70M,age80M,totM;//moderna
        Long age10J,age11J,age20J,age30J,age40J,age50J,age60J,age70J,age80J,totJ;//jj
        Long age10S,age11S,age20S,age30S,age40S,age50S,age60S,age70S,age80S,totS;//sino
        Long age10A,age11A,age20A,age30A,age40A,age50A,age60A,age70A,age80A,totA;//astra
        Long age10NV,age11NV,age20NV,age30NV,age40NV,age50NV,age60NV,age70NV,age80NV,totNV;//nonev
        // male , female
        Long Pm,Pf;//pfizer
        Long Mm,Mf;//moderna
        Long Jm,Jf;//jj
        Long Sm,Sf;//sino
        Long Am,Af;//astra
        Long NVm,NVf;//nonev
        Long totVacMale ,totVacFemale;

        // Covid
        Long age10Ps,age11Ps,age20Ps,age30Ps,age40Ps,age50Ps,age60Ps,age70Ps,age80Ps,totPs; // positive
        Long age10Ng,age11Ng,age20Ng,age30Ng,age40Ng,age50Ng,age60Ng,age70Ng,age80Ng,totNg; // negative
        Long age10C,age11C,age20C,age30C,age40C,age50C,age60C,age70C,age80C,totC; // contacted
        Long age10N,age11N,age20N,age30N,age40N,age50N,age60N,age70N,age80N,totN; // none
        // male , female
        Long Psm,Psf; // positive
        Long Ngm,Ngf; // negative
        Long Cm,Cf; // contacted
        Long Nm,Nf; // none

        Long F,M;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        sw = findViewById(R.id.CommunitySwitch_id);
        LinearLayout lCovid = findViewById(R.id.coronaCommunity_id);
        LinearLayout lVaccine = findViewById(R.id.vaccineCommunity_id);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    lCovid.setVisibility(View.VISIBLE);
                    lVaccine.setVisibility(View.GONE);
                }else{
                    lCovid.setVisibility(View.GONE);
                    lVaccine.setVisibility(View.VISIBLE);
                }
            }
        });

        backArrow = findViewById(R.id.ivCommunityBackArrow_id);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CommunityActivity.this,Extras.class));
            }
        });

        // connect to Firestore
        db = FirebaseFirestore.getInstance();
        // refer to aggregate doc which contains all the users collected data
        refReports = db.collection("Report").document("aggregate");

        ProgressDialog pd = new ProgressDialog(CommunityActivity.this);
        pd.setMessage("Loading...");
        pd.show();

        // read aggregate Firestore doc and set to aggregate map
        refReports.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){

                    fetchAndLoadData(documentSnapshot);
                    pd.dismiss();

                }else{
                    Toasty.error(getApplicationContext(), "Could not find data ..", Toast.LENGTH_SHORT,true).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toasty.error(getApplicationContext(), "Error Occurred, try again later", Toast.LENGTH_LONG,true).show();
                Log.e("x", "onFailure: "+ e.getMessage());
            }
        });

        tvTotalVaccine = findViewById(R.id.tv_totalV_id);
        tvTotalCovid = findViewById(R.id.tv_total_covid_id);


    }//end of onCreate

    private void fetchAndLoadData(DocumentSnapshot value) {

        // users
        M = value.getLong("males");
        F = value.getLong("females");

        // _________________________________________________________________ Vaccine Fetch Section ____________________________________________


        // Age
        age10P = value.getLong("pfizer.age10");
        age11P = value.getLong("pfizer.age11");
        age20P = value.getLong("pfizer.age20");
        age30P = value.getLong("pfizer.age30");
        age40P = value.getLong("pfizer.age40");
        age50P = value.getLong("pfizer.age50");
        age60P = value.getLong("pfizer.age60");
        age70P = value.getLong("pfizer.age70");
        age80P = value.getLong("pfizer.age80");
        totP = age10P+age11P+age20P+age30P+age40P+age50P+age60P+age70P+age80P;

        age10M = value.getLong("moderna.age10");
        age11M = value.getLong("moderna.age11");
        age20M = value.getLong("moderna.age20");
        age30M = value.getLong("moderna.age30");
        age40M = value.getLong("moderna.age40");
        age50M = value.getLong("moderna.age50");
        age60M = value.getLong("moderna.age60");
        age70M = value.getLong("moderna.age70");
        age80M = value.getLong("moderna.age80");
        totM = age10M+age11M+age20M+age30M+age40M+age50M+age60M+age70M+age80M;

        age10J = value.getLong("jj.age10");
        age11J = value.getLong("jj.age11");
        age20J = value.getLong("jj.age20");
        age30J = value.getLong("jj.age30");
        age40J = value.getLong("jj.age40");
        age50J = value.getLong("jj.age50");
        age60J = value.getLong("jj.age60");
        age70J = value.getLong("jj.age70");
        age80J = value.getLong("jj.age80");
        totJ = age10J+age11J+age20J+age30J+age40J+age50J+age60J+age70J+age80J;

        age10S = value.getLong("sino.age10");
        age11S = value.getLong("sino.age11");
        age20S = value.getLong("sino.age20");
        age30S = value.getLong("sino.age30");
        age40S = value.getLong("sino.age40");
        age50S = value.getLong("sino.age50");
        age60S = value.getLong("sino.age60");
        age70S = value.getLong("sino.age70");
        age80S = value.getLong("sino.age80");
        totS = age10S+age11S+age20S+age30S+age40S+age50S+age60S+age70S+age80S;

        age10A = value.getLong("astra.age10");
        age11A = value.getLong("astra.age11");
        age20A = value.getLong("astra.age20");
        age30A = value.getLong("astra.age30");
        age40A = value.getLong("astra.age40");
        age50A = value.getLong("astra.age50");
        age60A = value.getLong("astra.age60");
        age70A = value.getLong("astra.age70");
        age80A = value.getLong("astra.age80");
        totA = age10A+age11A+age20A+age30A+age40A+age50A+age60A+age70A+age80A;

        age10NV = value.getLong("nonev.age10");
        age11NV = value.getLong("nonev.age11");
        age20NV = value.getLong("nonev.age20");
        age30NV = value.getLong("nonev.age30");
        age40NV = value.getLong("nonev.age40");
        age50NV = value.getLong("nonev.age50");
        age60NV = value.getLong("nonev.age60");
        age70NV = value.getLong("nonev.age70");
        age80NV = value.getLong("nonev.age80");
        totNV = age10NV+age11NV+age20NV+age30NV+age40NV+age50NV+age60NV+age70NV+age80NV;

        // gender

        Pm = value.getLong("pfizer.m");
        Pf = value.getLong("pfizer.f");

        Mm = value.getLong("moderna.m");
        Mf = value.getLong("moderna.f");

        Jm = value.getLong("jj.m");
        Jf = value.getLong("jj.f");

        Sm = value.getLong("sino.m");
        Sf = value.getLong("sino.f");

        Am = value.getLong("astra.m");
        Af = value.getLong("astra.f");

        NVm = value.getLong("nonev.m");
        NVf = value.getLong("nonev.f");

        // without noneV
        totVacMale = Pm + Mm + Jm + Sm+ Am;
        totVacFemale = Pf + Mf + Jf + Sf + Af;

        // _________________________________________________________________ Corona Fetch Section ____________________________________________



        // Age
        age10Ps = value.getLong("positive.age10");
        age11Ps = value.getLong("positive.age11");
        age20Ps = value.getLong("positive.age20");
        age30Ps = value.getLong("positive.age30");
        age40Ps = value.getLong("positive.age40");
        age50Ps = value.getLong("positive.age50");
        age60Ps = value.getLong("positive.age60");
        age70Ps = value.getLong("positive.age70");
        age80Ps = value.getLong("positive.age80");
        totPs = age10Ps + age11Ps + age20Ps + age30Ps + age40Ps + age50Ps + age60Ps + age70Ps+ age80Ps;

        age10Ng = value.getLong("negative.age10");
        age11Ng = value.getLong("negative.age11");
        age20Ng = value.getLong("negative.age20");
        age30Ng = value.getLong("negative.age30");
        age40Ng = value.getLong("negative.age40");
        age50Ng = value.getLong("negative.age50");
        age60Ng = value.getLong("negative.age60");
        age70Ng = value.getLong("negative.age70");
        age80Ng = value.getLong("negative.age80");
        totNg = age10Ng + age11Ng + age20Ng + age30Ng + age40Ng + age50Ng + age60Ng + age70Ng+ age80Ng;

        age10C = value.getLong("contacted.age10");
        age11C = value.getLong("contacted.age11");
        age20C = value.getLong("contacted.age20");
        age30C = value.getLong("contacted.age30");
        age40C = value.getLong("contacted.age40");
        age50C = value.getLong("contacted.age50");
        age60C = value.getLong("contacted.age60");
        age70C = value.getLong("contacted.age70");
        age80C = value.getLong("contacted.age80");
        totC = age10C + age11C + age20C + age30C + age40C + age50C + age60C + age70C+ age80C;

        age10N = value.getLong("none.age10");
        age11N = value.getLong("none.age11");
        age20N = value.getLong("none.age20");
        age30N = value.getLong("none.age30");
        age40N = value.getLong("none.age40");
        age50N = value.getLong("none.age50");
        age60N = value.getLong("none.age60");
        age70N = value.getLong("none.age70");
        age80N = value.getLong("none.age80");
        totN = age10N + age11N + age20N + age30N + age40N + age50N + age60N + age70N+ age80N;

        // gender

        Psm = value.getLong("positive.m");
        Psf = value.getLong("positive.f");

        Ngm = value.getLong("negative.m");
        Ngf = value.getLong("negative.f");

        Cm = value.getLong("contacted.m");
        Cf = value.getLong("contacted.f");

        Nm = value.getLong("none.m");
        Nf = value.getLong("none.f");


        // Defining TextViews in Activity

        tvTotalCovid.setText("Total Users : "+String.format("%,d",F+M)); // total users are male + female
        tvTotalVaccine.setText("Total Users : "+String.format("%,d",F+M));



        Typeface fontPoppinsBlack = Typeface.createFromAsset(getAssets(),"fonts/poppins_black.ttf");

        TextView tvVaccinePieMale = findViewById(R.id.community_vaccine_males_id);
        tvVaccinePieMale.setText(String.format("%,d",totVacMale));
        tvVaccinePieMale.setTypeface(fontPoppinsBlack);

        TextView tvVaccinePieFemale = findViewById(R.id.community_vaccine_females_id);
        tvVaccinePieFemale.setTypeface(fontPoppinsBlack);
        tvVaccinePieFemale.setText(String.format("%,d",totVacFemale));

        TextView tvPositiveMale = findViewById(R.id.community_covidPositive_males_id);
        TextView tvPositiveFemale = findViewById(R.id.community_covidPositive_females_id);

        TextView tvNegativeMale = findViewById(R.id.community_covidNegative_males_id);
        TextView tvNegativeFemale = findViewById(R.id.community_covidNegative_females_id);

        TextView tvContactedMale = findViewById(R.id.community_covidContacted_males_id);
        TextView tvContactedFemale = findViewById(R.id.community_covidContacted_females_id);

        TextView tvNoneMale = findViewById(R.id.community_covidNone_males_id);
        TextView tvNoneFemale = findViewById(R.id.community_covidNone_females_id);

        tvPositiveMale.setTypeface(fontPoppinsBlack);
        tvPositiveFemale.setTypeface(fontPoppinsBlack);
        tvNegativeMale.setTypeface(fontPoppinsBlack);
        tvNegativeFemale.setTypeface(fontPoppinsBlack);
        tvContactedMale.setTypeface(fontPoppinsBlack);
        tvNoneMale.setTypeface(fontPoppinsBlack);
        tvNoneFemale.setTypeface(fontPoppinsBlack);

        tvPositiveMale.setText(String.format("%,d",Psm));
        tvPositiveFemale.setText(String.format("%,d",Psf));

        tvNegativeMale.setText(String.format("%,d",Ngm));
        tvNegativeFemale.setText(String.format("%,d",Ngf));

        tvContactedMale.setText(String.format("%,d",Cm));
        tvContactedFemale.setText(String.format("%,d",Cf));

        tvNoneMale.setText(String.format("%,d",Nm));
        tvNoneFemale.setText(String.format("%,d",Nf));


        // _________________________________________________________________ Vaccine Load Section ____________________________________________


        // total

        bcTotalV = findViewById(R.id.totalchartV_id);

        createVaccineTotalBarChart(bcTotalV,totP,totM,totJ,totS,totA,totNV);

        // Age

        bcPfizer = findViewById(R.id.pfizerchartV_id);

        createAgeBarChart(bcPfizer,"#FF3D7D",age10P,age11P,age20P,age30P,age40P,age50P,age60P,age70P,age80P);

        bcModerna = findViewById(R.id.modernachartV_id);

        createAgeBarChart(bcModerna,"#FFB957",age10M,age11M,age20M,age30M,age40M,age50M,age60M,age60M,age80M);

        bcJJ = findViewById(R.id.jjchartV_id);

        createAgeBarChart(bcJJ,"#47FF59",age10J,age11J,age20J,age30J,age40J,age50J,age60J,age70J,age80J);

        bcSino = findViewById(R.id.sinochartV_id);

        createAgeBarChart(bcSino,"#FAE04E",age10S,age11S,age20S,age30S,age40S,age50S,age60S,age70S,age80S);

        bcAstra = findViewById(R.id.astrachartV_id);

        createAgeBarChart(bcAstra,"#9960FF",age10A,age11A,age20A,age30A,age40A,age50A,age60A,age70A,age80A);

        bcNoneV = findViewById(R.id.nonechartV_id);

        createAgeBarChart(bcNoneV,"#F57600",age10NV,age11NV,age20NV,age30NV,age40NV,age50NV,age60NV,age70NV,age80NV);


        // Gender

        pcVaccines = findViewById(R.id.totalVaccinesgenderpiechart_id);

        createGenderPieChart(pcVaccines,totVacFemale,totVacMale);


        bcMalesV = findViewById(R.id.vaccine_community_males_barchart_id);

        createVaccineBarChart(bcMalesV,"#6CA0DC",Pm,Mm,Jm,Sm,Am,NVm);

        bcFemalesV = findViewById(R.id.vaccine_community_females_barchart_id);

        createVaccineBarChart(bcFemalesV,"#FFC0CB",Pf,Mf,Jf,Sf,Af,NVf);



        // _________________________________________________________________ Corona Load Section ____________________________________________


        //total

        bcTotalC = findViewById(R.id.totalchartCovid_id);

        createCovidTotalBarChart(bcTotalC,totPs,totNg,totC,totN);

        // Age

        bcPositive = findViewById(R.id.positvechart_id);

        createAgeBarChart(bcPositive,"#3AC4FF",age10Ps,age11Ps,age20Ps,age30Ps,age40Ps,age50Ps,age60Ps,age70Ps,age80Ps);

        bcNegative= findViewById(R.id.negativechart_id);

        createAgeBarChart(bcNegative,"#B0D6A6",age10Ng,age11Ng,age20Ng,age30Ng,age40Ng,age50Ng,age60Ng,age70Ng,age80Ng);

        bcContacted = findViewById(R.id.contactedchart_id);

        createAgeBarChart(bcContacted,"#FF647C",age10C,age11C,age20C,age30C,age40C,age50C,age60C,age70C,age80C);

        bcNone = findViewById(R.id.nonechart_id);

        createAgeBarChart(bcNone,"#F57600",age10N,age11N,age20N,age30N,age40N,age50N,age60N,age70N,age80N);


        // Gender

        pcPositive = findViewById(R.id.positivegenderpiechartcovid_id);

        createGenderPieChart(pcPositive,Psf,Psm);

        pcNegative = findViewById(R.id.negativegenderpiechartcovid_id);

        createGenderPieChart(pcNegative,Ngf,Ngm);

        pcContacted = findViewById(R.id.contactedgenderpiechartcovid_id);

        createGenderPieChart(pcContacted,Cf,Cm);

        pcNone = findViewById(R.id.nonegenderpiechartcovid_id);

        createGenderPieChart(pcNone,Nf,Nm);


    }//end of fetchAndLoadData()


    private void createAgeBarChart(BarChart bc , String color ,Long age10 ,Long age11,Long age20 ,Long age30,Long age40,Long age50 ,Long age60, Long age70 , Long age80){

       // customize
       bc.getDescription().setEnabled(false);
       bc.setDrawGridBackground(false);
       bc.setDrawBarShadow(false);
       bc.setFitBars(true);
       bc.setPinchZoom(false);
       bc.setNoDataText("Data Not Found");
       bc.getLegend().setEnabled(false);
       bc.animateY(1400, Easing.EaseInOutExpo);
       bc.getAxisRight().setDrawGridLines(false);
       bc.getXAxis().setDrawGridLines(false);
       bc.getAxisRight().setEnabled(false);
       bc.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
       bc.setScaleEnabled(false); // for zooming in and out


       // enables a horizontal dashed lines behind the bars
       bc.getAxisLeft().setDrawGridLines(true);
       bc.getAxisLeft().enableGridDashedLine(10f, 10f, 0f);
       bc.getAxisLeft().setGridColor(Color.parseColor("#F0EEEE"));

       // Bar Entries :
       ArrayList<BarEntry> yVals = new ArrayList<>();
       yVals.add(new BarEntry(0,age10));
       yVals.add(new BarEntry(1,age11));
       yVals.add(new BarEntry(2,age20));
       yVals.add(new BarEntry(3,age30));
       yVals.add(new BarEntry(4,age40));
       yVals.add(new BarEntry(5,age50));
       yVals.add(new BarEntry(6,age60));
       yVals.add(new BarEntry(7,age70));
       yVals.add(new BarEntry(8,age80));

       BarDataSet barchartDataSet = new BarDataSet(yVals,"Barchart Data Set");
       barchartDataSet.setColor(Color.parseColor(color));
       barchartDataSet.setDrawValues(true); // values on top of each bar
       barchartDataSet.setHighlightEnabled(true);
       barchartDataSet.setValueTextSize(7f);
       barchartDataSet.setValueTextColor(Color.parseColor("#99000000"));

       BarData barData = new BarData(barchartDataSet);


       // GIVING THE X AXIS THE NAMES ACCORDING TO The Age Group
       List<String> xAxisValues = new ArrayList<>(Arrays.asList("10-","10s","20s","30s","40s","50s","60s","70s","80+"));

       bc.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues)); // sets the x axis to the Age Group Strings created above
       bc.getXAxis().setTextSize(7f);

       YAxis leftYAxis = bc.getAxisLeft();
       leftYAxis.setEnabled(true);
       leftYAxis.setAxisMinimum(0f); // makes the bars not floating on top of x axis

       bc.setData(barData);

       bc.invalidate(); // refresh

   }//end of createAgeBarChart


    private void  createGenderPieChart(PieChart pc,Long fData,Long mData){

        pc.setDrawHoleEnabled(true); // gives donut shape
        pc.getDescription().setEnabled(false); // removing description of the pie chart


        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(fData)); // Females
        entries.add(new PieEntry(mData)); //Males


        PieDataSet pieDataSet = new PieDataSet(entries,"");
        pieDataSet.setColors(
                Color.parseColor("#FFC0CB") // Females
                ,Color.parseColor("#6CA0DC") // Males

        );

        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);

        pieData.setValueFormatter(new PercentFormatter(pc));
        pc.setUsePercentValues(true);

        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.parseColor("#99000000"));

        pc.getLegend().setEnabled(false);
        pc.setData(pieData);

        pc.animateY(1400, Easing.EaseInOutQuad);


        pc.invalidate(); // data updated.. refresh on the the screen

    }//end of createGenderPieChart


    private void createVaccineBarChart(BarChart bc , String color ,Long Pfizer ,Long Moderna,Long JJ ,Long Sino,Long Astra,Long Nonev){


        // customize
        bc.getDescription().setEnabled(false);
        bc.setDrawGridBackground(false);
        bc.setDrawBarShadow(false);
        bc.setFitBars(true);
        bc.setPinchZoom(false);
        bc.setNoDataText("Data Not Found");
        bc.getLegend().setEnabled(false);
        bc.animateY(1400, Easing.EaseInOutExpo);
        bc.getAxisRight().setDrawGridLines(false);
        bc.getXAxis().setDrawGridLines(false);
        bc.getAxisRight().setEnabled(false);
        bc.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        bc.setScaleEnabled(false); // for zooming in and out


        // enables a horizontal dashed lines behind the bars
        bc.getAxisLeft().setDrawGridLines(true);
        bc.getAxisLeft().enableGridDashedLine(10f, 10f, 0f);
        bc.getAxisLeft().setGridColor(Color.parseColor("#F0EEEE"));

        // Bar Entries :
        ArrayList<BarEntry> yVals = new ArrayList<>();
        yVals.add(new BarEntry(0,Pfizer));
        yVals.add(new BarEntry(1,Moderna));
        yVals.add(new BarEntry(2,JJ));
        yVals.add(new BarEntry(3,Sino));
        yVals.add(new BarEntry(4,Astra));
        yVals.add(new BarEntry(5,Nonev));


        BarDataSet barchartDataSet = new BarDataSet(yVals,"Barchart Data Set");
        barchartDataSet.setColor(Color.parseColor(color));
        barchartDataSet.setDrawValues(true); // values on top of each bar
        barchartDataSet.setHighlightEnabled(true);
        barchartDataSet.setValueTextSize(7f);
        barchartDataSet.setValueTextColor(Color.parseColor("#99000000"));

        BarData barData = new BarData(barchartDataSet);


        // GIVING THE X AXIS THE NAMES ACCORDING TO The Age Group
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("Pfizer","Moderna","JJ","Sinovac","Oxford","None"));

        bc.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues)); // sets the x axis to the Age Group Strings created above
        bc.getXAxis().setTextSize(7f);

        YAxis leftYAxis = bc.getAxisLeft();
        leftYAxis.setEnabled(true);
        leftYAxis.setAxisMinimum(0f); // makes the bars not floating on top of x axis

        bc.setData(barData);

        bc.invalidate(); // refresh


    }//end of createVaccineBarChart


    private void createVaccineTotalBarChart(BarChart bc ,Long Pfizer ,Long Moderna,Long JJ ,Long Sino,Long Astra,Long Nonev){


        // customize
        bc.getDescription().setEnabled(false);
        bc.setDrawGridBackground(false);
        bc.setDrawBarShadow(false);
        bc.setFitBars(true);
        bc.setPinchZoom(false);
        bc.setNoDataText("Data Not Found");
        bc.getLegend().setEnabled(false);
        bc.animateY(1400, Easing.EaseInOutExpo);
        bc.getAxisRight().setDrawGridLines(false);
        bc.getXAxis().setDrawGridLines(false);
        bc.getAxisRight().setEnabled(false);
        bc.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        bc.setScaleEnabled(false); // for zooming in and out


        // enables a horizontal dashed lines behind the bars
        bc.getAxisLeft().setDrawGridLines(true);
        bc.getAxisLeft().enableGridDashedLine(10f, 10f, 0f);
        bc.getAxisLeft().setGridColor(Color.parseColor("#F0EEEE"));

        // Bar Entries :
        ArrayList<BarEntry> yVals = new ArrayList<>();
        yVals.add(new BarEntry(0,Pfizer));
        yVals.add(new BarEntry(1,Moderna));
        yVals.add(new BarEntry(2,JJ));
        yVals.add(new BarEntry(3,Sino));
        yVals.add(new BarEntry(4,Astra));
        yVals.add(new BarEntry(5,Nonev));


        BarDataSet barchartDataSet = new BarDataSet(yVals,"Barchart Data Set");
        barchartDataSet.setColors(
                Color.parseColor("#FF3D7D") // Pfizer
                ,Color.parseColor("#FFB957") // Moderna
                ,Color.parseColor("#47FF59") // JJ
                ,Color.parseColor("#FAE04E") // Sino
                ,Color.parseColor("#9960FF") // Astra
                ,Color.parseColor("#F57600") // Nonev
        );
        barchartDataSet.setDrawValues(true); // values on top of each bar
        barchartDataSet.setHighlightEnabled(true);
        barchartDataSet.setValueTextSize(7f);
        barchartDataSet.setValueTextColor(Color.parseColor("#99000000"));

        BarData barData = new BarData(barchartDataSet);


        // GIVING THE X AXIS THE NAMES ACCORDING TO The Age Group
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("Pfizer","Moderna","JJ","Sinovac","Oxford","None"));

        bc.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues)); // sets the x axis to the Age Group Strings created above
        bc.getXAxis().setTextSize(7f);

        YAxis leftYAxis = bc.getAxisLeft();
        leftYAxis.setEnabled(true);
        leftYAxis.setAxisMinimum(0f); // makes the bars not floating on top of x axis

        bc.setData(barData);

        bc.invalidate(); // refresh


    }//end of createVaccineTotalBarChart()


    private void createCovidTotalBarChart(BarChart bc ,Long positive ,Long negative ,Long contacted,Long none){

        // customize
        bc.getDescription().setEnabled(false);
        bc.setDrawGridBackground(false);
        bc.setDrawBarShadow(false);
        bc.setFitBars(true);
        bc.setPinchZoom(false);
        bc.setNoDataText("Data Not Found");
        bc.getLegend().setEnabled(false);
        bc.animateY(1400, Easing.EaseInOutExpo);
        bc.getAxisRight().setDrawGridLines(false);
        bc.getXAxis().setDrawGridLines(false);
        bc.getAxisRight().setEnabled(false);
        bc.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        bc.setScaleEnabled(false); // for zooming in and out


        // enables a horizontal dashed lines behind the bars
        bc.getAxisLeft().setDrawGridLines(true);
        bc.getAxisLeft().enableGridDashedLine(10f, 10f, 0f);
        bc.getAxisLeft().setGridColor(Color.parseColor("#F0EEEE"));

        // Bar Entries :
        ArrayList<BarEntry> yVals = new ArrayList<>();
        yVals.add(new BarEntry(0,positive));
        yVals.add(new BarEntry(1,negative));
        yVals.add(new BarEntry(2,contacted));
        yVals.add(new BarEntry(3,none));



        BarDataSet barchartDataSet = new BarDataSet(yVals,"Barchart Data Set");
        barchartDataSet.setColors(
                Color.parseColor("#3AC4FF") // Positive
                ,Color.parseColor("#B0D6A6") // Negative
                ,Color.parseColor("#FF647C") // Contacted
                ,Color.parseColor("#F57600") // None
        );
        barchartDataSet.setDrawValues(true); // values on top of each bar
        barchartDataSet.setHighlightEnabled(true);
        barchartDataSet.setValueTextSize(7f);
        barchartDataSet.setValueTextColor(Color.parseColor("#99000000"));

        BarData barData = new BarData(barchartDataSet);


        // GIVING THE X AXIS THE NAMES ACCORDING TO The Age Group
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("Positive","Negative","Contacted","None"));

        bc.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues)); // sets the x axis to the Age Group Strings created above
        bc.getXAxis().setTextSize(7f);

        bc.getXAxis().setAxisMinimum(barData.getXMin()-.5f);
        bc.getXAxis().setAxisMaximum(barData.getXMax()+.5f);
        bc.getXAxis().setLabelCount(4);

        YAxis leftYAxis = bc.getAxisLeft();
        leftYAxis.setEnabled(true);
        leftYAxis.setAxisMinimum(0f); // makes the bars not floating on top of x axis

        bc.setData(barData);

        bc.invalidate(); // refresh


    } //end of createCovidTotalBarChart()

}//end of class