package com.example.konsttest2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DescriptionFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_description_page, container, false);

        return rootView;
    }
}
