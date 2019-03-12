package com.myawesomegifweather.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    interface WeatherApiService {

        @GET("current.json")
        Observable<String> getCurrentWeather(@Query("key") String key, @Query("q") String city);
    }

    interface ConditionsApiService {

        @GET("conditions.json")
        Observable<String> getConditions();
    }

}
