package com.example.konsttest2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class ChooseThemeFragment extends Fragment {

    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";
    public static final String KEY_THEME = "density";

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_choose_theme_page, container, false);

        final SharedPreferences defaultSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String themeName = defaultSharedPreferences.getString(KEY_THEME, THEME_LIGHT);

        final RadioButton lightThemeRadioButton = rootView.findViewById(R.id.light_theme);
        final RadioButton darkThemeRadioButton = rootView.findViewById(R.id.dark_theme);
        if (THEME_LIGHT.equals(themeName)) {
            lightThemeRadioButton.setChecked(true);
            darkThemeRadioButton.setChecked(false);
        } else {
            lightThemeRadioButton.setChecked(false);
            darkThemeRadioButton.setChecked(true);
        }

        return rootView;
    }
}
