package com.myawesomegifweather.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myawesomegifweather.Contract;
import com.myawesomegifweather.Presenter;
import com.myawesomegifweather.R;
import com.myawesomegifweather.UI.WeatherView;
import com.myawesomegifweather.models.CurrentWeatherModel;

public class WeatherFragment extends Fragment implements Contract.ViewWeatherFragment {

    private OnFragmentInteractionListener mListener;
    private WeatherView mWeatherView;
    private Presenter mPresenter;
    private SwipeRefreshLayout mSwipeContainer;
    private TextView defaultCity;


    public interface WeatherViewListener {
        void widgetLoaded();
    }

    public WeatherFragment() {

    }
    @SuppressLint("ValidFragment")
    public WeatherFragment(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        defaultCity = view.findViewById(R.id.defaultCity);
        defaultCity.setSelected(true);

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh();
            }
        });

        mSwipeContainer.setRefreshing(true);

        RelativeLayout currentWeatherRoot = (RelativeLayout)view.findViewById(R.id.currentWeatherRoot);

        mWeatherView = new WeatherView(getContext(), new WeatherViewListener() {
            @Override
            public void widgetLoaded() {
                mSwipeContainer.setRefreshing(false);
            }
        });

        currentWeatherRoot.addView(mWeatherView);

        CurrentWeatherModel model = mPresenter.getModel();
        String savedGifUri = mPresenter.getSavedGifUri();

        if(model != null && savedGifUri != null) {
            mWeatherView.setData(model);
            mWeatherView.setGif(savedGifUri, getContext());

            defaultCity.setText(model.getDefaultCity() + ", " + model.getConditionText());
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void updateGif(String gifUrl) {
        mWeatherView.setGif(gifUrl, getContext());
    }

    @Override
    public void showError() {
        mSwipeContainer.setRefreshing(false);
        mWeatherView.showError();
    }

    @Override
    public void updateWeatherData(CurrentWeatherModel model) {
        mWeatherView.setData(model);
        defaultCity.setText(model.getDefaultCity() + ", " + model.getConditionText());

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
