package com.example.b9000application.Models.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.b9000application.Models.Entities.User;
import com.example.b9000application.Models.UserRepository;


import java.util.List;

public class UserListViewModel extends AndroidViewModel {


    private LiveData<List<User>> data;

    public UserListViewModel(Application application) {
        super(application);
        data = UserRepository.instance.getmAllUsers();
    }

    public LiveData<List<User>> getAllUsers() { return data;}


}