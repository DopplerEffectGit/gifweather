package com.myawesomegifweather;
import android.content.SharedPreferences;
import android.util.Log;

import com.myawesomegifweather.fragments.WeatherFragment;
import com.myawesomegifweather.models.CurrentWeatherModel;
import com.myawesomegifweather.network.Callback;
import com.myawesomegifweather.repository.MainRepository;

import java.util.HashMap;

public class Presenter implements Contract.Presenter{

    private static final String TAG = "MainPresenter";

    private Contract.ViewWeatherFragment mViewWeatherFragment;
    private Contract.Repository mRepository;

    public Presenter(SharedPreferences preferences) {

        this.mRepository = new MainRepository(preferences);
        Log.d(TAG, "Constructor");
    }

    @Override
    public void refresh() {

        mRepository.getCurrentWeather(new Callback<CurrentWeatherModel>() {
            @Override
            public void returnResult(final CurrentWeatherModel currentWeatherModel) {

                mViewWeatherFragment.updateWeatherData(currentWeatherModel);
                mRepository.getGif(new Callback<String>() {
                    @Override
                    public void returnResult(String gifUrl) {
                        mViewWeatherFragment.updateGif(gifUrl);
                    }

                    @Override
                    public void returnError(String message) {
                        mViewWeatherFragment.showError();
                    }
                }, currentWeatherModel);
            }

            @Override
            public void returnError(String message) {
                mViewWeatherFragment.showError();
            }
        });
    }

    @Override
    public void setWeatherFragment(WeatherFragment weatherFragment) {
        mViewWeatherFragment = weatherFragment;
    }

    @Override
    public void setLocation(String location) {
        mRepository.setLocation(location);
    }

    @Override
    public CurrentWeatherModel getModel() {
        return mRepository.getModel();
    }

    @Override
    public String getSavedGifUri() {
        return mRepository.getSavedGifUri();
    }

    @Override
    public HashMap<String, Boolean> getOptions() {
        return mRepository.getOptions();
    }

    @Override
    public void setOptions(HashMap<String, Boolean> map) {
        mRepository.setOptions(map);
    }

    @Override
    public void setCustomSearchPrefix(String prefix) {
        mRepository.setCustomSearchPrefix(prefix);
    }

    @Override
    public String getCustomSearchPrefix() {
        return mRepository.getCustomSearchPrefix();
    }

}