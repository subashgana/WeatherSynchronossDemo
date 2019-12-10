package com.example.weathersyncdemo.Api;

import com.example.weathersyncdemo.DTO.GeoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("weather?q=Bangalore&appid=eede1279d52630223701026aa9913946")
    Call<GeoResponse> getWeatherList(@Body EmptyRequest request);

}
