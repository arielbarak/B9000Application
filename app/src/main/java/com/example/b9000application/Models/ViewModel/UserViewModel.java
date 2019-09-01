package com.example.b9000application.Models.ViewModel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.b9000application.Models.Entities.User;
import com.example.b9000application.Models.UserRepository;

public class UserViewModel extends AndroidViewModel {
    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public void signUp(String email, String password, final UserRepository.SignUpListener listener){
       UserRepository.instance.signUp(email,password, listener);
    }
    public void updateUserInfo(final String userName, Uri pickerImgUri, UserRepository.UpdateUserInfoListener listener)
    {
        UserRepository.instance.updateUserInfo(userName,pickerImgUri,listener);
    }

    public void signIn(String email, String password, UserRepository.SignInListener listener) {
        UserRepository.instance.signIn(email,password,listener);
    }

    public boolean isSigned(){
        return UserRepository.instance.isSigned();
    }

    public Uri getUserImageUrl() {return UserRepository.instance.getUserImageUrl();}
    public User getCurrentUser() {return UserRepository.instance.getCurrentUser();}
    public String getUid() {return UserRepository.instance.getUid();}
    public String getDisplayName() {return UserRepository.instance.getDisplayName();}
    public void getUser(String userId, UserRepository.GetUserListener listener){
        UserRepository.instance.getUser(userId,listener);
    }
}
