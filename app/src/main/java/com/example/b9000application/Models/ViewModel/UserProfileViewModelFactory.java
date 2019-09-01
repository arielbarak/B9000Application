package com.example.b9000application.Models.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UserProfileViewModelFactory implements ViewModelProvider.Factory  {

    private Application mApplication;
    private String userId;
    public UserProfileViewModelFactory(Application application, String userId) {
        this.mApplication = application;
        this.userId = userId;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserProfileViewModel(mApplication, userId);

    }
}

