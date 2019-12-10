package com.example.weathersyncdemo.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoResponse {


    /**
     */
    @SerializedName("weather")
    public List<Weather> weatherData;

}
