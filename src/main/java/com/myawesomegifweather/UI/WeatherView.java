package com.myawesomegifweather.UI;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.myawesomegifweather.R;
import com.myawesomegifweather.fragments.WeatherFragment;
import com.myawesomegifweather.models.CurrentWeatherModel;

import java.io.File;

public class WeatherView extends RelativeLayout {

    private TextView tempS;
    private TextView wind;
    private TextView cloud;
    private TextView humidity;
    private Context mContext;
    private ImageView weatherGifView;
    private ImageView ivCloud;
    private View rootView;

    WeatherFragment.WeatherViewListener weatherViewListener;

    public WeatherView(Context context, WeatherFragment.WeatherViewListener weatherViewListener) {
        super(context);
        this.weatherViewListener = weatherViewListener;
        init(context);
    }

    private void init(Context context){
        mContext = context;
        rootView = inflate(context, R.layout.weather_view, this);

        tempS = findViewById(R.id.temp);
        wind = findViewById(R.id.wind);
        cloud = findViewById(R.id.cloud);
        humidity = findViewById(R.id.humidity);

        weatherGifView = findViewById(R.id.weatherGifView);

        ivCloud = findViewById(R.id.ivCloud);


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.no_signal_1);

        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .asGif()
                .load(R.drawable.no_signal_1)
                .into(weatherGifView);
    }

    public void setData(CurrentWeatherModel model) {
        tempS.setText(String.valueOf(model.getTemp()) + getResources().getString(R.string.weatherview_temp_postfix));
        wind.setText(String.valueOf(model.getWindSpeed()) + " " + getResources().getString(R.string.weatherview_wind_postfix));
        cloud.setText(model.getCloudCoverPercent() + getResources().getString(R.string.weatherview_cloud_postfix));
        humidity.setText(model.getHumidity() + getResources().getString(R.string.weatherview_cloud_postfix));

        if(!model.isDay()) {
            ivCloud.setImageResource(R.drawable.ic_cloud_night);
        }
    }

    public void setGif(String gifUrl, Context context) {
        mContext = context;
        if (mContext != null) {
            Glide.with(mContext)
                    .download(gifUrl)
                    .listener(new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                            setImage(Uri.fromFile(resource));
                            return false;
                        }
                    })
                    .submit();
        }

    }

    private void setImage(Uri uri) {
        Glide.with(mContext)
                .setDefaultRequestOptions(new RequestOptions())
                .asGif()
                .load(uri)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        weatherViewListener.widgetLoaded();
                        return false;
                    }
                })
                .into(weatherGifView);
    }

    public void showError() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.no_signal_3);

        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .asGif()
                .load(R.drawable.no_signal_3)
                .into(weatherGifView);
    }
}
