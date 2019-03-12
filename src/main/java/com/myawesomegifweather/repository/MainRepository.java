package com.myawesomegifweather.repository;

import android.content.SharedPreferences;
import android.util.Log;

import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.MediaType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GPHApi;
import com.giphy.sdk.core.network.api.GPHApiClient;
import com.giphy.sdk.core.network.response.MediaResponse;
import com.myawesomegifweather.Contract;
import com.myawesomegifweather.models.CurrentWeatherModel;
import com.myawesomegifweather.network.Callback;
import com.myawesomegifweather.network.NetworkUtils;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainRepository implements Contract.Repository {

    private GPHApi mClient;
    private String defaultCity;

    private final String GIPHGY_CLIENT_KEY = "1UVj608uL9SzQjkKjZHHw30BQV3VqyQB";
    private final String APIXU_CLIENT_KEY = "d609d261dd14469d86b132351192402";

    private CurrentWeatherModel model;
    private String currentGifUrl;

    private SharedPreferences mPreferences;
    SharedPreferences.Editor editor;


    public MainRepository(SharedPreferences preferences) {
        mClient = new GPHApiClient(GIPHGY_CLIENT_KEY);
        mPreferences = preferences;
        editor = mPreferences.edit();
    }

    @Override
    public void getCurrentWeather(final com.myawesomegifweather.network.Callback<CurrentWeatherModel> callback) {

        NetworkUtils.getWeatherApiInstance()
                .getCurrentWeather(APIXU_CLIENT_KEY, defaultCity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        model = new CurrentWeatherModel(s);
                        callback.returnResult(model);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.returnError("error");
                    }

                    @Override
                    public void onComplete() {
                        Log.v("retro", "complete");
                    }
                });
    }

    @Override
    public void getGif(final Callback<String> callback, CurrentWeatherModel model) {
        String gifSearchText = "";

        if(addCustimPrefix()) {
            gifSearchText = getSearchPrefix() + " ";
        }

        if(addCountryToSearchPrefix()) {
            gifSearchText = gifSearchText.concat(model.getCountry() + " ");
        }

        if(addLocationToSearchPrefix()) {
            gifSearchText = gifSearchText.concat(defaultCity + " ");
        }

        gifSearchText = gifSearchText.concat(" " + model.getConditionText());

        mClient.random(gifSearchText, MediaType.gif, null, new CompletionHandler<MediaResponse>() {
            @Override
            public void onComplete(MediaResponse result, Throwable e) {
                if (result == null) {
                    callback.returnError("No results found");
                } else {
                    if (result.getData() != null) {
                        Media gif = result.getData();
                        currentGifUrl = gif.getImages().getOriginal().getGifUrl();
                        callback.returnResult(currentGifUrl);
                    } else {
                        callback.returnError("No results found");
                    }
                }
            }
        });
    }

    @Override
    public void setLocation(String location) {
        defaultCity = location;
    }

    @Override
    public CurrentWeatherModel getModel() {
        return model;
    }

    @Override
    public String getSavedGifUri() {
        return currentGifUrl;
    }

    @Override
    public HashMap<String, Boolean> getOptions() {

        HashMap<String, Boolean> map = new HashMap<>();
        map.put("country", mPreferences.getBoolean("COUNTRY", false));
        map.put("location", mPreferences.getBoolean("LOCATION", false));
        map.put("customPrefix", mPreferences.getBoolean("CUSTOM_PREFIX", false));

        return map;
    }

    @Override
    public void setOptions(HashMap<String, Boolean> map) {

        editor.putBoolean("COUNTRY", map.get("country"));
        editor.putBoolean("LOCATION", map.get("location"));
        editor.putBoolean("CUSTOM_PREFIX", map.get("customPrefix"));
        editor.commit();
    }

    @Override
    public void setCustomSearchPrefix(String prefix) {
        editor.putString("STRING_CUSTOM_PREFIX", prefix);
        editor.commit();
    }

    @Override
    public String getCustomSearchPrefix() {
        return getSearchPrefix();
    }

    public boolean addCountryToSearchPrefix() {
        return mPreferences.getBoolean("COUNTRY", false);
    }

    boolean addLocationToSearchPrefix() {
        return mPreferences.getBoolean("LOCATION", false);
    }

    boolean addCustimPrefix() {
        return mPreferences.getBoolean("CUSTOM_PREFIX", false);
    }

    String getSearchPrefix() {
        return mPreferences.getString("STRING_CUSTOM_PREFIX", "");
    }
}
