package com.example.konsttest2.welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konsttest2.R;

public class DescriptionSecondFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_description_second_page, container, false);

        return rootView;
    }
}
