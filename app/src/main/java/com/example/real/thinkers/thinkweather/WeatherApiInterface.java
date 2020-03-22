package com.example.real.thinkers.thinkweather;


import com.example.real.thinkers.thinkweather.models.Weather;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherApiInterface {

    String BASE_URL = "https://query.yahooapis.com/v1/public/";
    @GET()
    Call<Weather> getWeather(@Url String url);

    class Factory {

        private static WeatherApiInterface service;
        public static WeatherApiInterface getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
                service = retrofit.create(WeatherApiInterface.class);
                return service;
            }
            else {
                return service;
            }
        }
    }
}