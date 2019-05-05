package com.example.retrofit.model;

public class Temperature {

    private String temp;
    private String ph;

    public Temperature(String temp, String ph) {
        this.temp = temp;
        this.ph = ph;
    }

    public Temperature() {
    }

    public String getTemp() {
        return temp;
    }

    public String getPh() {
        return ph;
    }
}
