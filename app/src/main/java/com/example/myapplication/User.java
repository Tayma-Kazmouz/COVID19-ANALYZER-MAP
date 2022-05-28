package com.example.myapplication;

import java.util.Map;

public class User {
    private static volatile User user = null;
    private String name;
    private String email;
    private String dob;
    private Long age;
    private String gender;
    private String country;
    private String covid;
    private Map<String,Object> vaccines;
    private Boolean set;

    private User(){
        // empty constructor for firebase
        // private to prevent instantiations outside of getInstance()
    }

    // guarantee the singularity of the this class instantiation via a public method named getInstance()
    public static  User getInstance(){
        // check if its first time that class object is instantiated
        // the create a Double Checked Locking Mechanism for MultiThreaded Scenarios...
        if (user == null){
            synchronized (User.class){
                if (user == null){
                    user = new User();
                }
            }
        }
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCovid() {
        return covid;
    }

    public void setCovid(String covid) {
        this.covid = covid;
    }

    public Map<String, Object> getVaccines() {
        return vaccines;
    }

    public void setVaccines(Map<String, Object> vaccines) {
        this.vaccines = vaccines;
    }

    public Boolean getSet() {
        return set;
    }

    public void setSet(Boolean set) {
        this.set = set;
    }
}//end of class
