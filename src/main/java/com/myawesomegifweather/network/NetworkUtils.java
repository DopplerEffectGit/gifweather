package com.myawesomegifweather.network;

public class NetworkUtils {
    private static ApiService.WeatherApiService weatherApiService;
    private static ApiService.ConditionsApiService conditionsApiService;

    private static final String WEATHER_API_BASE_URL = "http://api.apixu.com/v1/";
    private static final String CONDITIONS_API_BASE_URL = "http://www.apixu.com/doc/";

    public static ApiService.WeatherApiService getWeatherApiInstance() {
        if (weatherApiService == null)
            weatherApiService = RetrofitAdapter.getBitDifferentInstance(WEATHER_API_BASE_URL).create(ApiService.WeatherApiService.class);
        return weatherApiService;
    }

    public static ApiService.ConditionsApiService getConditionsApiInstance() {
        if (conditionsApiService == null)
            conditionsApiService = RetrofitAdapter.getBitDifferentInstance(CONDITIONS_API_BASE_URL).create(ApiService.ConditionsApiService.class);
        return conditionsApiService;
    }
}