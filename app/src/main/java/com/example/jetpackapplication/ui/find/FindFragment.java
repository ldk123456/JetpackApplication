package com.example.jetpackapplication.ui.find;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jetpackapplication.R;
import com.example.libnavannotation.FragmentDestination;

@FragmentDestination(pageUrl = "main/tabs/find")
public class FindFragment extends Fragment {

    private FindViewModel findViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        findViewModel = new ViewModelProvider(this).get(FindViewModel.class);
        View root = inflater.inflate(R.layout.fragment_find, container, false);
        final TextView textView = root.findViewById(R.id.text_find);
        findViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}