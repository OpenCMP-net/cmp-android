package com.example.inappcmp.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.inappcmp.R;
import com.opencmp.inapplib.OpenCmp;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeTab extends Fragment {
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_home, container, false);

        Button openCmpProgrammaticallyButton = root.findViewById(R.id.showCmpProgrammatically);
        openCmpProgrammaticallyButton.setOnClickListener(v -> OpenCmp.show());

        return root;
    }
}