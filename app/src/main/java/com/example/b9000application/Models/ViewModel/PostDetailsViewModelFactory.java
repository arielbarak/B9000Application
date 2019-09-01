package com.example.b9000application.Models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PostDetailsViewModelFactory  implements ViewModelProvider.Factory  {

    private Application mApplication;
    private String postKey;
    public PostDetailsViewModelFactory(Application application, String postKey) {
        this.mApplication = application;
        this.postKey = postKey;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PostDetailsViewModel(mApplication, postKey);

    }
}

