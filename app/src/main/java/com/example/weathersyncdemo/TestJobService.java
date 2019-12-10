package com.example.weathersyncdemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.weathersyncdemo.Api.ApiClient;
import com.example.weathersyncdemo.Api.ApiInterface;
import com.example.weathersyncdemo.Api.EmptyRequest;
import com.example.weathersyncdemo.DTO.GeoResponse;
import com.example.weathersyncdemo.DTO.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TestJobService extends JobService {

    private  String message = null;
    private static final String TAG = TestJobService.class.getSimpleName();
    final static String MY_ACTION = "MY_ACTION";

    JobParameters params;
    Call<GeoResponse> call;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onStartJob(JobParameters params) {
        this.params = params;
        callApi();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        call.cancel();
        return false;
    }

    /**
     *
     */
    private void callApi() {
        if (isInternetIsConnected()) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            call = apiService.getWeatherList(new EmptyRequest());
            call.enqueue(new Callback<GeoResponse>() {
                @Override
                public void onResponse(Call<GeoResponse> call, Response<GeoResponse> response) {
                    List<Weather> weather = response.body().weatherData;
                    Log.d(TAG, "Weather: " + weather.get(0).description);
                    message = weather.get(0).description;
                    Intent intent = new Intent();
                    intent.setAction(MY_ACTION);
                    intent.putExtra("DATAPASSED", message);
                    sendBroadcast(intent);
                }

                @Override
                public void onFailure(Call<GeoResponse> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        } else {
            Toast.makeText(this, "Connect to Wifi", Toast.LENGTH_LONG).show();
        }
    }



    //Subash Check Wifi Connected
    private boolean isInternetIsConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;

            }
        } else {
            // not connected to the internet
            return false;
        }
        return false;

    }

}
