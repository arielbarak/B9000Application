package com.example.b9000application.Models.ViewModel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;

import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.PostRepository;
import com.example.b9000application.Models.UserRepository;

public class UserUpdateViewModel extends AndroidViewModel {
    public UserUpdateViewModel(Application application) {
        super(application);
    }

    public void changePass(final String pass, final UserRepository.ChangePassListener listener)
    {
        UserRepository.instance.changePass(pass, listener);
    }
    public void changeMail(final String mail, final UserRepository.ChangeMailListener listener)
    {
        UserRepository.instance.changeMail(mail, listener);
    }
    public void updateUserInfo(final String userName, Uri pickerImgUri, final UserRepository.UpdateUserInfoListener listener) {
        UserRepository.instance.updateUserInfo(userName,pickerImgUri,listener);
    }

}
