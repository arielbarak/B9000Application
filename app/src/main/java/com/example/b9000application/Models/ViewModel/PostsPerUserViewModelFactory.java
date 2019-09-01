package com.example.b9000application.Models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PostsPerUserViewModelFactory implements ViewModelProvider.Factory  {

    private Application mApplication;
    private String userId;
    public PostsPerUserViewModelFactory(Application application, String userId) {
        this.mApplication = application;
        this.userId = userId;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PostsPerUserViewModel(mApplication, userId);

    }
}

