package com.myawesomegifweather;


import com.myawesomegifweather.fragments.WeatherFragment;
import com.myawesomegifweather.models.CurrentWeatherModel;
import com.myawesomegifweather.network.Callback;

import java.util.HashMap;

public interface Contract {

    interface ViewWeatherFragment {
        void updateWeatherData(CurrentWeatherModel model);
        void updateGif(String gifUrl);
        void showError();
    }

    interface Presenter {
        void refresh();
        void setWeatherFragment(WeatherFragment weatherFragment);
        void setLocation(String location);

        CurrentWeatherModel getModel();
        String getSavedGifUri();

        HashMap<String, Boolean> getOptions();
        void setOptions(HashMap<String, Boolean> map);
        void setCustomSearchPrefix(String prefix);
        String getCustomSearchPrefix();
    }


    interface Repository {
        //for WeatherFragment
        void getCurrentWeather(Callback<CurrentWeatherModel> callback);
        void getGif(Callback<String> callback, CurrentWeatherModel model);
        void setLocation(String location);
        CurrentWeatherModel getModel();
        String getSavedGifUri();

        //for OptionsFragment
        HashMap<String, Boolean> getOptions();
        void setOptions(HashMap<String, Boolean> map);
        void setCustomSearchPrefix(String prefix);
        String getCustomSearchPrefix();
    }
}
