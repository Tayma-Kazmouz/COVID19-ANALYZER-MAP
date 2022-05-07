//-----------------------------------com.example.myapplication.CountryInfo.java-----------------------------------
package com.example.myapplication;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CountryInfo implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private Long id;
    @SerializedName("iso2")
    @Expose
    private String iso2;
    @SerializedName("iso3")
    @Expose
    private String iso3;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("long")
    @Expose
    private Double _long;
    @SerializedName("flag")
    @Expose
    private String flag;
    public final static Creator<CountryInfo> CREATOR = new Creator<CountryInfo>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CountryInfo createFromParcel(android.os.Parcel in) {
            return new CountryInfo(in);
        }

        public CountryInfo[] newArray(int size) {
            return (new CountryInfo[size]);
        }

    }
            ;

    protected CountryInfo(android.os.Parcel in) {
        this.id = ((Long) in.readValue((Long.class.getClassLoader())));
        this.iso2 = ((String) in.readValue((String.class.getClassLoader())));
        this.iso3 = ((String) in.readValue((String.class.getClassLoader())));
        this.lat = ((Double) in.readValue((Double.class.getClassLoader())));
        this._long = ((Double) in.readValue((Double.class.getClassLoader())));
        this.flag = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CountryInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(iso2);
        dest.writeValue(iso3);
        dest.writeValue(lat);
        dest.writeValue(_long);
        dest.writeValue(flag);
    }

    public int describeContents() {
        return 0;
    }

}