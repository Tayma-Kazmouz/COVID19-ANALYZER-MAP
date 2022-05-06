package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
//import com.example.myapplication.UserInput;
import java.util.ArrayList;
import java.util.List;



import java.util.Calendar;

public class UserInput extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    Button gotoreport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        gotoreport= findViewById(R.id.submitpersonalinfo_id);


        gotoreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(UserInput.this,ReportStatus1.class);
                startActivity(i);
                finish();

            }
        });


        //Date&Time
        initDatePicker();
        dateButton = findViewById(R.id.dobbtn_id);
        dateButton.setText(getTodaysDate());


        //Country Spinner
        spinner = findViewById(R.id.spinnerid);
        adapter = new customadapter(this,names,images);
        spinner.setAdapter(adapter);


    }//end of ONCreate

    private String getTodaysDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override 
            public void onDateSet (DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1; //because android starts months from 0
                String date = makeDateString(day, month, year);
                dateButton.setText(date);

            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = android.R.style.ThemeOverlay_Material_Dialog; //THIS CHANGED IT


        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year) {

        return getMonthFormat(month) + "/" + day + "/" + year;
    }





    private String getMonthFormat(int month) {

        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";


//Default should never happen
        return "JAN";


    }

    public void openDatePicker(View view) {

        datePickerDialog.show();

    }


    //Country Picker func.
    private Spinner spinner;

    customadapter adapter;

    String [] names = {"Afghanistan (AF)","Aland Island (AX)","Albania(AL)","Algeria (DZ)","American Samoa (AS)","Andorra(AD)","Angola (AO)","Anguilla (Al)","Antartica(AQ)","Antigua and Barbuda (AG)","Aregentina(AG)","Armenia (AM)","Aruba (AW)","Australia (AU)","Austria (AT)","Azerbaijan (AZ)","Bahamas (BS)","Bahrain (BH)","Bangladesh (BD)","Barbados (BB)","Belarus (BY)","belgium (BE)","Belize (BZ)","Benin (BJ)","Bermuda (BM)","Bhutan (BT)",

            "Bolivia, Pleurinational State of (BO)","Bosnia and Herzegovina (BA)","Botswana (BW)","Brazil (BR)","British Indian Ocean Territory (IO)","British Virgin Islands (VG)","Brunei Darussalam (BN)","Bulgaria ( BG)","Burkina Faso (BF)","Burundi (BI)","Cambodia (KH)","Cameroon (CM)","Canada (CA)","Cape Verde (CV)","Cayman Islands (KY)","Central African Republic (CF)","Chad (TD)","Chile (CL)","China (CN)","Christmas Island (CX)","Cocos(kelling) Islands (CC)","Colombia (CO)","Comoros (KM)","Congo (CG)","Congo, The Democratic Republic Of The (CD)","Cook Islands (CK)","Costa Rica (CR)","Cote divoire (CL)","Croatia (HR)","Cuba (CU)","Curacao(CW)","Cyprus (CY)","Czech Republic (CZ)","Denmark (DK)","Dijibouti (DJ)","Dominica (DM)","Dominican Republic (DO)","Ecuador (EC)","Egypt (EG)","El Salvador (SV)","Equatorial Guiena(GQ)","Eritrea (ER)","Estonia (EE)","Ethioia (ET)","Falkland Islands (malvinas) (FK)","Faroe Islands (FO)","Fiji (FJ)","Finland (FL)","France (FR)","French Guyana (GF)","French Polynesia (PF)","Gabon (GA)","Gambia (GM)","Georgia (GE)","Germany (DE)","Ghana (GH)","Gibralter (Gl)","Greece (GR)","Greenland (GL)","Grenada (GD)","Guadeloupe (GP)","Guam (GU)","Guatemala (GT)","Guemsey (GG)","Guinea (GN)","Guinea-bissau (GW)","Guyana (GY)","Haiti (HT)","Holy See (Vatican City State) (VA)","Honduras (HN)","Hong Kong (HK)","Hungary (HU)","Iceland (IS)","India (IN)","Indonesia (ID)","Iran, Islamic Republic Of (IR)","Iraq (IQ)","Ireland (IE)","Isle Of Man (IM)","Israel (IL)","Italy (IT)","Jamaica (JM)","Japan (JP)","Jersey (JE)","Jordan (JO)","Kazakhstan (KZ)","Kenya (KE)","Kiribati (Kl)","Kosovo (XK)","Kuwait (KW)","Kyrgyzstan (KG)","Lao People's Democratic Republic (LA)","Latvia (LV)","Lebanon (LB)","Lesotho (LS)","Liberia (LR)","Libya (LY)","Liechtenstein (LI)","Lithuania (LT)","Luxembourg (LU)","Macau (MO)","Macedonia (FYROM) (MK)","Madagascar (MG)","Malawi (MW)","Malaysia (MY)","Maldives (MV)","Mali (ML)","Malta (ML)","Marshall Islands (MH)","Martinique (MQ)","Mauritania (MR)","Mauritius (MU)","Mayotte (YT)","Maxico (MX)","Micronesia, Federated States Of  (FM)","Moldova, Republic Of (MD)","Monaco (MC)","Mangolia (MN)","Montenegro (ME)","Montserrat (MS)","Morocco (MA)","Mozambique (MZ)","Myanmar (MM)","Namibia (NA)","Nauru (NR)","Nepal (Np)","Netherlands (NL)","New Caledonia (NC)","New Zealand (NZ)","Nicaragua (NI)","Niger (NE)","Nigeria (NG)","Niue (NU)","Norfolk islands (NF)","North Korea (KP)","Northern Mariana islands (MP)","Norway (NO)","Oman (OM)","Pakistan (PK)","Palau (PW)","Palestine (PS)","Panama (PA)","Papua New Guinea (PG)","Paraguay (PY)","Peru (PE)","Philipines (PH)","Pitcaim Islands (PN)","Poland (PL)","Portugal (PT)","Puerto Rico (PR)","Qatar (QA)","Reunion (RE)","Romania (RO)","Russian Federation (RU)","Rawanda (RW)","Saint Barthelelemy (BL)","Saint Helena,Ascension And Tristan Da Cunha(SH)","Saint Kitts and Nevis (KN)","Saint Lucia (LC)","Saint Martin (MF)","Saint Pierre And Miquelon (PM)","Saint Vincent & The Grenadines (VC)","Samoa (WS)","San Marino (SM)","Sao Tome And Principe (ST)","Saudi Arabia (SA)","Senegal (SN)","Serbia (RS)","Seychelles (SC)","Sierra Leone (SL)","Singapore (SG)","Sint Marten (SX)","Slovakia (SK)","Slovenia (SL)","Solomon Islands (SB)","Somalia (SO)","South Africa (ZA)","South Korea (KR)","South Sudan (SS)","Spain (ES)","Sri Lanka (LK)","Sudan (SD)","Suriname (SR)","Swaziland (SZ)","Sweden (SE)","Switzerland (CH)","Syrian Arab Republic (SY)","Taiwan (TW)","Tajikistan (TJ)","Tanzania, United Republic Of (TZ)","Thailand (TH)","Timor-leste (TL)","Togo (TG)"," Tokelau(TK)","Tonga (TO)","Trinidad & Tabago (TT)","Tunisisa (TN)","Turkey (TR)","Turkmenistan (TM)","Turks and Caicos Islands (TC)","Tuvalu (TV)","Uganda (UG)","Ukraine (UA)","United Arab Emirates (UAE) (AE)","United Kingdom (GB)","United States (US)","Urguay (UY)","US Virgin Islands (VI)","Uzbekistan (UZ)","Vanuatu (VU)","Venezuela, Bolivarian Republic Of (VE)","Vietnam (VN)","Wallis And Futuna (WF)","Yemen (YE)","Zambia (ZM)","Zimbabwe (ZW)"};

    int [] images = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j,R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o,R.drawable.p,R.drawable.q,R.drawable.r,R.drawable.s,R.drawable.t,R.drawable.u,R.drawable.v,R.drawable.w,R.drawable.x,R.drawable.y,R.drawable.z,

            R.drawable.za,R.drawable.zb,R.drawable.zc,R.drawable.zd,R.drawable.bra,R.drawable.ze,R.drawable.zf,R.drawable.zg,R.drawable.zh,R.drawable.zi,R.drawable.zj,R.drawable.zk,R.drawable.zl,R.drawable.zm,R.drawable.zn,R.drawable.zo,R.drawable.zp,R.drawable.zq,R.drawable.zr,R.drawable.zs,R.drawable.zt,R.drawable.zu,R.drawable.zv,R.drawable.zw,R.drawable.zx,R.drawable.zy,R.drawable.zz,R.drawable.zzc,R.drawable.zzd,R.drawable.zze,R.drawable.zzf,R.drawable.zzg,R.drawable.zzh,R.drawable.zzi,R.drawable.zzj,R.drawable.zzk,R.drawable.zzl,R.drawable.zzm,R.drawable.zzn,R.drawable.zzo,R.drawable.zzp,R.drawable.zzq,R.drawable.zzr,R.drawable.zzs,R.drawable.zzt,R.drawable.zzu,R.drawable.zzv,R.drawable.zzw,R.drawable.zzx,R.drawable.zzy,R.drawable.zzz,R.drawable.zzza,R.drawable.zzzb,R.drawable.zzzc,R.drawable.zzzd,R.drawable.zzze,R.drawable.zzzf,R.drawable.zzzg,R.drawable.zzzh,R.drawable.zzzi,R.drawable.zzzj,R.drawable.zzzk,R.drawable.zzzl,R.drawable.zzzm,R.drawable.zzzn,R.drawable.zzzo,R.drawable.zzzp,R.drawable.zzzq,R.drawable.zzzr,R.drawable.zzzs,R.drawable.zzzt,R.drawable.zzzu,R.drawable.zzzv,R.drawable.zzzw,R.drawable.zzzx,R.drawable.zzzy,R.drawable.zzzz,R.drawable.zzzza,R.drawable.zzzzb,R.drawable.zzzzc,R.drawable.zzzzd,R.drawable.zzzze,R.drawable.zzzzf,R.drawable.zzzzg,R.drawable.zzzzh,R.drawable.zzzzi,R.drawable.zzzzj,R.drawable.zzzzk,R.drawable.zzzzl,R.drawable.zzzzm,R.drawable.zzzzn,R.drawable.zzzzo,R.drawable.zzzzp,R.drawable.zzzzq,R.drawable.zzzzr,R.drawable.zzzzs,R.drawable.zzzzt,R.drawable.zzzzu,R.drawable.zzzzv,R.drawable.zzzzw,R.drawable.zzzzx,R.drawable.zzzzy,R.drawable.zzzzz,R.drawable.zzzzza,R.drawable.zzzzzb,R.drawable.zzzzzc,R.drawable.zzzzzd,R.drawable.zzzzze,R.drawable.zzzzzf,R.drawable.zzzzzg,R.drawable.zzzzzh,R.drawable.zzzzzi,R.drawable.zzzzzj,R.drawable.zzzzzk,R.drawable.zzzzzl,R.drawable.zzzzzm,R.drawable.zzzzzn,R.drawable.zzzzzo,R.drawable.zzzzzp,R.drawable.zzzzzq,R.drawable.zzzzzr,R.drawable.zzzzzs,R.drawable.zzzzzt,R.drawable.zzzzzu,R.drawable.zzzzzv,R.drawable.zzzzzw,R.drawable.zzzzzx,R.drawable.zzzzzy,R.drawable.zzzzzz,R.drawable.zzzzzza,R.drawable.zzzzzzb,R.drawable.zzzzzzc,R.drawable.zzzzzzd,R.drawable.zzzzzze,R.drawable.zzzzzzf,R.drawable.zzzzzzg,R.drawable.zzzzzzh,R.drawable.zzzzzzi,R.drawable.zzzzzzj,R.drawable.zzzzzzk,R.drawable.zzzzzzl,R.drawable.zzzzzzm,R.drawable.zzzzzzn,R.drawable.zzzzzzo,R.drawable.zzzzzzp,R.drawable.zzzzzzq,R.drawable.zzzzzzr,R.drawable.zzzzzzs,R.drawable.zzzzzzt,R.drawable.zzzzzzu,R.drawable.zzzzzzv,R.drawable.zzzzzzw,R.drawable.zzzzzzx,R.drawable.zzzzzzy,R.drawable.zzzzzzz,R.drawable.brow,R.drawable.zzzzzzza,R.drawable.zzzzzzzb,R.drawable.zzzzzzzc,R.drawable.zzzzzzzd,R.drawable.zzzzzzze,R.drawable.zzzzzzzf,R.drawable.zzzzzzzg,R.drawable.zzzzzzzh,R.drawable.zzzzzzzi,R.drawable.zzzzzzzj,R.drawable.zzzzzzzk,R.drawable.zzzzzzzl,R.drawable.zzzzzzzm,R.drawable.zzzzzzzn,R.drawable.zzzzzzzo,R.drawable.zzzzzzzp,R.drawable.zzzzzzzq,R.drawable.zzzzzzzr,R.drawable.zzzzzzzs,R.drawable.zzzzzzzt,R.drawable.zzzzzzzu,R.drawable.zzzzzzzv,R.drawable.zzzzzzzw,R.drawable.zzzzzzzx,R.drawable.zzzzzzzy,R.drawable.zzzzzzzz,R.drawable.zzzzzzzza,R.drawable.zzzzzzzzb,R.drawable.zzzzzzzzc,R.drawable.zzzzzzzzd,R.drawable.zzzzzzzze,R.drawable.zzzzzzzzf,R.drawable.zzzzzzzzg,R.drawable.zzzzzzzzh,R.drawable.zzzzzzzzi,R.drawable.zzzzzzzzj,R.drawable.zzzzzzzzk,R.drawable.zzzzzzzzl,R.drawable.zzzzzzzzm,R.drawable.zzzzzzzzn,R.drawable.zzzzzzzzo,R.drawable.zzzzzzzzp,R.drawable.zzzzzzzzq,R.drawable.zzzzzzzzr,R.drawable.zzzzzzzzs,R.drawable.zzzzzzzzt,R.drawable.zzzzzzzzu,R.drawable.zzzzzzzzv,R.drawable.zzzzzzzzw,R.drawable.zzzzzzzzx,R.drawable.abba,R.drawable.aas,R.drawable.zzzzzzzzza,R.drawable.zzzzzzzzzb,R.drawable.zzzzzzzzzc,R.drawable.zzzzzzzzzd,R.drawable.zzzzzzzzze,R.drawable.zzzzzzzzzf,R.drawable.zzzzzzzzzg,R.drawable.zzzzzzzzzh};











}




