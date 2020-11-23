package com.example.jetpackapplication.ui.my;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("this is MyFragment");
    }

    public MutableLiveData<String> getText() {
        return mText;
    }
}
