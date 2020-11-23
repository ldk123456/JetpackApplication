package com.example.jetpackapplication.ui.publish;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PublishViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public PublishViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is PublishActivity");
    }

    public MutableLiveData<String> getText() {
        return mText;
    }
}
