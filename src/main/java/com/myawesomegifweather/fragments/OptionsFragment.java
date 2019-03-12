package com.myawesomegifweather.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.myawesomegifweather.Presenter;
import com.myawesomegifweather.R;

import java.util.HashMap;


public class OptionsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Presenter mPresenter;

    private HashMap<String, Boolean> optionsMap;

    private Switch countrySwitch;
    private Switch locationPrefixSwitch;
    private Switch customPrefixSwitch;
    private EditText editTextSearchPrefix;
    private LinearLayout prefixLayout;
    private View rootView;

    public OptionsFragment() {

    }

    @SuppressLint("ValidFragment")
    public OptionsFragment(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_options, container, false);

        optionsMap = mPresenter.getOptions();

        countrySwitch = rootView.findViewById(R.id.switchCountry);
        locationPrefixSwitch = rootView.findViewById(R.id.switchLocationPrefix);
        customPrefixSwitch = rootView.findViewById(R.id.switchCustomPrefix);
        editTextSearchPrefix = rootView.findViewById(R.id.editTextSearchPrefix);

        countrySwitch.setChecked(optionsMap.get("country"));
        locationPrefixSwitch.setChecked(optionsMap.get("location"));
        customPrefixSwitch.setChecked(optionsMap.get("customPrefix"));

        editTextSearchPrefix.setText(mPresenter.getCustomSearchPrefix());

        Button button = rootView.findViewById(R.id.confirmButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

        prefixLayout = rootView.findViewById(R.id.prefixLayout);
        prefixLayout.setVisibility(customPrefixSwitch.isChecked() ? View.VISIBLE : View.GONE);

        customPrefixSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    hideKeyboard();
                }

                TransitionManager.beginDelayedTransition((ViewGroup) rootView);
                prefixLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        return rootView;
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
        saveOptions();
    }

    private void saveOptions() {

        optionsMap.put("country", countrySwitch.isChecked());
        optionsMap.put("location", locationPrefixSwitch.isChecked());
        optionsMap.put("customPrefix", customPrefixSwitch.isChecked());

        mPresenter.setOptions(optionsMap);

        if(customPrefixSwitch.isChecked()) { //&& !String.valueOf(editTextSearchPrefix.getText()).equals("")
            mPresenter.setCustomSearchPrefix(String.valueOf(editTextSearchPrefix.getText()));
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
