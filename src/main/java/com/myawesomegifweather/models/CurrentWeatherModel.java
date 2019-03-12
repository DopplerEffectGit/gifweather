package com.myawesomegifweather.models;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrentWeatherModel {

    private int temp, conditionCode,isDay, windSpeed, cloudCoverPercent, humidity;
    private String conditionText, conditionImageUrl, windDirectionCompass, country, defaultCity;
    private double lastUpdatedEpoch;

    public CurrentWeatherModel(String currentWeather) {
        try {
            parseFromJson(currentWeather);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseFromJson(String s) throws JSONException {

        JSONObject mainObject = new JSONObject(s);

        JSONObject locationObject = mainObject.getJSONObject("location");
        JSONObject currentObject = mainObject.getJSONObject("current");

        defaultCity = locationObject.getString("name");
        country = locationObject.getString("country");

        lastUpdatedEpoch = Double.parseDouble(currentObject.getString("last_updated_epoch"));
        temp = Math.round(Float.parseFloat(currentObject.getString("temp_c")));
        isDay = currentObject.getInt("is_day");
        humidity  = currentObject.getInt("humidity");
        windSpeed = currentObject.getInt("wind_kph");
        cloudCoverPercent = currentObject.getInt("cloud"); //Cloud cover as percentage
        windDirectionCompass = currentObject.getString("wind_dir");

        JSONObject conditionObject = currentObject.getJSONObject("condition");
        conditionText = conditionObject.getString("text");
        conditionImageUrl = conditionObject.getString("icon").substring(2);
        conditionCode = conditionObject.getInt("code");
    }

    public int getTemp() {
        return temp;
    }

    public boolean isDay() {

        if(isDay == 1)
            return true;
        else
            return false;

    }

    public int getHumidity() {
        return humidity;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getCloudCoverPercent() {
        return cloudCoverPercent;
    }

    public String getWindDirectionCompass() {
        return windDirectionCompass;
    }

    public String getConditionText() {
        return conditionText;
    }

    public String getConditionImageUrl() {
        return conditionImageUrl;
    }

    public int getConditionCode() {
        return conditionCode;
    }

    public double getLastUpdatedEpoch() {
        return lastUpdatedEpoch;
    }

    public String getDefaultCity() {
        return defaultCity;
    }

    public String getCountry() {
        return country;
    }

}
