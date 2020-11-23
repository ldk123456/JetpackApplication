package com.example.jetpackapplication.ui.my;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jetpackapplication.R;
import com.example.libnavannotation.FragmentDestination;

@FragmentDestination(pageUrl = "main/tabs/my")
public class MyFragment extends Fragment {

    private MyViewModel myViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my, container, false);
        final TextView textView = root.findViewById(R.id.text_my);
        myViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
}
