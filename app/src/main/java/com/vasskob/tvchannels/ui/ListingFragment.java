package com.vasskob.tvchannels.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasskob.tvchannels.R;


public class ListingFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int sectionNumber;
    View rootView;

    public ListingFragment() {
    }

    public static Fragment newInstance(int sectionNumber) {
        ListingFragment fragment = new ListingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout, container, false);
//
//        sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
//        TextView textView = (TextView) rootView.findViewById(R.id.listing);
//        textView.setText("TAB " + (sectionNumber -1));

        return rootView;
    }
}
