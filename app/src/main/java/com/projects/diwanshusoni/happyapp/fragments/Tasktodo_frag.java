package com.projects.diwanshusoni.happyapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.diwanshusoni.happyapp.R;

/**
 * Created by Diwanshu Soni on 01-10-2017.
 */

public class Tasktodo_frag extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Nullable
    @Override
    public View getView() {
        View view = ((LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.tasktodo_layout_dash, null);
        return view;
    }
}
