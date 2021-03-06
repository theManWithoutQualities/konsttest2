package com.example.konsttest2.welcome;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.konsttest2.R;

import static com.example.konsttest2.settings.SettingsUtils.DENSITY_STANDARD;
import static com.example.konsttest2.settings.SettingsUtils.KEY_DENSITY;

public class ChooseDensityFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_choose_density_page, container, false);
        final SharedPreferences defaultSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        final String density = defaultSharedPreferences
                .getString(KEY_DENSITY, DENSITY_STANDARD);

        final RadioButton standardDensityRadioButton = rootView
                .findViewById(R.id.standard_density);
        final RadioButton highDensityRadioButton = rootView.findViewById(R.id.high_density);
        if (DENSITY_STANDARD.equals(density)) {
            standardDensityRadioButton.setChecked(true);
            highDensityRadioButton.setChecked(false);
        } else {
            standardDensityRadioButton.setChecked(false);
            highDensityRadioButton.setChecked(true);
        }

        return rootView;
    }
}
