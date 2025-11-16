package com.example.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment; // ✅ đúng
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentB extends Fragment {
    public FragmentB() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_fragment_b, container, false);
    }
}
