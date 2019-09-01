package com.example.b9000application.Models.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.b9000application.Models.Entities.User;
import com.example.b9000application.Models.UserAsyncDao;
import com.example.b9000application.Models.UserRepository;

public class UserProfileViewModel extends AndroidViewModel {

    private UserProfileLiveData data;
    UserProfileViewModel(@NonNull Application application, String userId) {
        super(application);
        data = new UserProfileLiveData(application,userId);
    }
    class UserProfileLiveData extends MutableLiveData<User> {

        private final String userId;
        UserProfileLiveData(Application application, final String userId) {
            this.userId = userId;
            if (this.userId!=null) {
                UserRepository.instance.getUserDao(userId, new UserRepository.GetUserListener() {
                    @Override
                    public void onResponse(User p) {
                        Log.d("TAG", "user received from room" + userId);
                        setValue(p);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        }



        @Override
        protected void onActive() {
            super.onActive();
            if(this.userId!=null){
                UserRepository.instance.getUserFirebase(this.userId, new UserRepository.GetUserListener() {
                    @Override
                    public void onResponse(User p) {
                        Log.d("TAG","user received from firebase" + p.getName() );
                        setValue(p);
                        UserAsyncDao.insertUser(p);
                    }

                    @Override
                    public void onError() {
                        UserAsyncDao.getUser(userId, new UserRepository.GetUserListener() {
                            @Override
                            public void onResponse(User p) {
                                Log.d("TAG","user received from room" + userId );
                                setValue(p);
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
            }

        }

        @Override
        protected void onInactive() {
            super.onInactive();
            Log.d("TAG","cancellGetUser");
        }
    }
    public void refresh()
    {
        this.data.onActive();
    }
    public LiveData<User> getUser()
    {
       return data;
    }
}
