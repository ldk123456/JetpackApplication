package com.example.jetpackapplication.ui.publish;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.jetpackapplication.R;
import com.example.libnavannotation.ActivityDestination;

@ActivityDestination(pageUrl = "main/tabs/publish")
public class PublishActivity extends AppCompatActivity {

    private PublishViewModel publishViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        publishViewModel = new ViewModelProvider(this).get(PublishViewModel.class);
        final TextView textView = findViewById(R.id.text_publish);
        publishViewModel.getText().observe(this, textView::setText);
    }
}
