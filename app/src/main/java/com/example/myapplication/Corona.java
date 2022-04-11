//-----------------------------------com.example.myapplication.Corona.java-----------------------------------
package com.example.myapplication;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Corona implements Parcelable
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
    @SerializedName("cases")
    @Expose
    private Long cases;
    @SerializedName("todayCases")
    @Expose
    private Long todayCases;
    @SerializedName("deaths")
    @Expose
    private Long deaths;
    @SerializedName("todayDeaths")
    @Expose
    private Long todayDeaths;
    @SerializedName("recovered")
    @Expose
    private Long recovered;
    @SerializedName("todayRecovered")
    @Expose
    private Long todayRecovered;
    @SerializedName("active")
    @Expose
    private Long active;
    @SerializedName("critical")
    @Expose
    private Long critical;
    @SerializedName("casesPerOneMillion")
    @Expose
    private Long casesPerOneMillion;
    @SerializedName("deathsPerOneMillion")
    @Expose
    private Long deathsPerOneMillion;
    @SerializedName("tests")
    @Expose
    private Long tests;
    @SerializedName("testsPerOneMillion")
    @Expose
    private Long testsPerOneMillion;
    @SerializedName("population")
    @Expose
    private Long population;
    @SerializedName("continent")
    @Expose
    private String continent;
    @SerializedName("oneCasePerPeople")
    @Expose
    private Long oneCasePerPeople;
    @SerializedName("oneDeathPerPeople")
    @Expose
    private Long oneDeathPerPeople;
    @SerializedName("oneTestPerPeople")
    @Expose
    private Long oneTestPerPeople;
    @SerializedName("activePerOneMillion")
    @Expose
    private Double activePerOneMillion;
    @SerializedName("recoveredPerOneMillion")
    @Expose
    private Double recoveredPerOneMillion;
    @SerializedName("criticalPerOneMillion")
    @Expose
    private Double criticalPerOneMillion;
    public final static Creator<Corona> CREATOR = new Creator<Corona>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Corona createFromParcel(android.os.Parcel in) {
            return new Corona(in);
        }

        public Corona[] newArray(int size) {
            return (new Corona[size]);
        }

    }
            ;

    protected Corona(android.os.Parcel in) {
        this.updated = ((Long) in.readValue((Long.class.getClassLoader())));
        this.country = ((String) in.readValue((String.class.getClassLoader())));
        this.countryInfo = ((CountryInfo) in.readValue((CountryInfo.class.getClassLoader())));
        this.cases = ((Long) in.readValue((Long.class.getClassLoader())));
        this.todayCases = ((Long) in.readValue((Long.class.getClassLoader())));
        this.deaths = ((Long) in.readValue((Long.class.getClassLoader())));
        this.todayDeaths = ((Long) in.readValue((Long.class.getClassLoader())));
        this.recovered = ((Long) in.readValue((Long.class.getClassLoader())));
        this.todayRecovered = ((Long) in.readValue((Long.class.getClassLoader())));
        this.active = ((Long) in.readValue((Long.class.getClassLoader())));
        this.critical = ((Long) in.readValue((Long.class.getClassLoader())));
        this.casesPerOneMillion = ((Long) in.readValue((Long.class.getClassLoader())));
        this.deathsPerOneMillion = ((Long) in.readValue((Long.class.getClassLoader())));
        this.tests = ((Long) in.readValue((Long.class.getClassLoader())));
        this.testsPerOneMillion = ((Long) in.readValue((Long.class.getClassLoader())));
        this.population = ((Long) in.readValue((Long.class.getClassLoader())));
        this.continent = ((String) in.readValue((String.class.getClassLoader())));
        this.oneCasePerPeople = ((Long) in.readValue((Long.class.getClassLoader())));
        this.oneDeathPerPeople = ((Long) in.readValue((Long.class.getClassLoader())));
        this.oneTestPerPeople = ((Long) in.readValue((Long.class.getClassLoader())));
        this.activePerOneMillion = ((Double) in.readValue((Double.class.getClassLoader())));
        this.recoveredPerOneMillion = ((Double) in.readValue((Double.class.getClassLoader())));
        this.criticalPerOneMillion = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    public Corona() {
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

    public Long getCases() {
        return cases;
    }

    public void setCases(Long cases) {
        this.cases = cases;
    }

    public Long getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(Long todayCases) {
        this.todayCases = todayCases;
    }

    public Long getDeaths() {
        return deaths;
    }

    public void setDeaths(Long deaths) {
        this.deaths = deaths;
    }

    public Long getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(Long todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public Long getRecovered() {
        return recovered;
    }

    public void setRecovered(Long recovered) {
        this.recovered = recovered;
    }

    public Long getTodayRecovered() {
        return todayRecovered;
    }

    public void setTodayRecovered(Long todayRecovered) {
        this.todayRecovered = todayRecovered;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public Long getCritical() {
        return critical;
    }

    public void setCritical(Long critical) {
        this.critical = critical;
    }

    public Long getCasesPerOneMillion() {
        return casesPerOneMillion;
    }

    public void setCasesPerOneMillion(Long casesPerOneMillion) {
        this.casesPerOneMillion = casesPerOneMillion;
    }

    public Long getDeathsPerOneMillion() {
        return deathsPerOneMillion;
    }

    public void setDeathsPerOneMillion(Long deathsPerOneMillion) {
        this.deathsPerOneMillion = deathsPerOneMillion;
    }

    public Long getTests() {
        return tests;
    }

    public void setTests(Long tests) {
        this.tests = tests;
    }

    public Long getTestsPerOneMillion() {
        return testsPerOneMillion;
    }

    public void setTestsPerOneMillion(Long testsPerOneMillion) {
        this.testsPerOneMillion = testsPerOneMillion;
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

    public Long getOneCasePerPeople() {
        return oneCasePerPeople;
    }

    public void setOneCasePerPeople(Long oneCasePerPeople) {
        this.oneCasePerPeople = oneCasePerPeople;
    }

    public Long getOneDeathPerPeople() {
        return oneDeathPerPeople;
    }

    public void setOneDeathPerPeople(Long oneDeathPerPeople) {
        this.oneDeathPerPeople = oneDeathPerPeople;
    }

    public Long getOneTestPerPeople() {
        return oneTestPerPeople;
    }

    public void setOneTestPerPeople(Long oneTestPerPeople) {
        this.oneTestPerPeople = oneTestPerPeople;
    }

    public Double getActivePerOneMillion() {
        return activePerOneMillion;
    }

    public void setActivePerOneMillion(Double activePerOneMillion) {
        this.activePerOneMillion = activePerOneMillion;
    }

    public Double getRecoveredPerOneMillion() {
        return recoveredPerOneMillion;
    }

    public void setRecoveredPerOneMillion(Double recoveredPerOneMillion) {
        this.recoveredPerOneMillion = recoveredPerOneMillion;
    }

    public Double getCriticalPerOneMillion() {
        return criticalPerOneMillion;
    }

    public void setCriticalPerOneMillion(Double criticalPerOneMillion) {
        this.criticalPerOneMillion = criticalPerOneMillion;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(updated);
        dest.writeValue(country);
        dest.writeValue(countryInfo);
        dest.writeValue(cases);
        dest.writeValue(todayCases);
        dest.writeValue(deaths);
        dest.writeValue(todayDeaths);
        dest.writeValue(recovered);
        dest.writeValue(todayRecovered);
        dest.writeValue(active);
        dest.writeValue(critical);
        dest.writeValue(casesPerOneMillion);
        dest.writeValue(deathsPerOneMillion);
        dest.writeValue(tests);
        dest.writeValue(testsPerOneMillion);
        dest.writeValue(population);
        dest.writeValue(continent);
        dest.writeValue(oneCasePerPeople);
        dest.writeValue(oneDeathPerPeople);
        dest.writeValue(oneTestPerPeople);
        dest.writeValue(activePerOneMillion);
        dest.writeValue(recoveredPerOneMillion);
        dest.writeValue(criticalPerOneMillion);
    }

    public int describeContents() {
        return 0;
    }

}