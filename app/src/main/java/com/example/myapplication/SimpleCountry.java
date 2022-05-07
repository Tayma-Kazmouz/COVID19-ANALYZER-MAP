package com.example.myapplication;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SimpleCountry implements Parcelable
{

    @SerializedName("updated")
    @Expose
    private Long updated;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("countryInfo")
    @Expose
    private CountryInfo countryInfo;
    @SerializedName("population")
    @Expose
    private Long population;
    @SerializedName("continent")
    @Expose
    private String continent;
    public final static Creator<SimpleCountry> CREATOR = new Creator<SimpleCountry>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SimpleCountry createFromParcel(android.os.Parcel in) {
            return new SimpleCountry(in);
        }

        public SimpleCountry[] newArray(int size) {
            return (new SimpleCountry[size]);
        }

    }
            ;

    protected SimpleCountry(android.os.Parcel in) {
        this.updated = ((Long) in.readValue((Long.class.getClassLoader())));
        this.country = ((String) in.readValue((String.class.getClassLoader())));
        this.countryInfo = ((CountryInfo) in.readValue((CountryInfo.class.getClassLoader())));
        this.population = ((Long) in.readValue((Long.class.getClassLoader())));
        this.continent = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SimpleCountry() {
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public CountryInfo getCountryInfo() {
        return countryInfo;
    }

    public void setCountryInfo(CountryInfo countryInfo) {
        this.countryInfo = countryInfo;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(updated);
        dest.writeValue(country);
        dest.writeValue(countryInfo);
        dest.writeValue(population);
        dest.writeValue(continent);
    }

    public int describeContents() {
        return 0;
    }

}