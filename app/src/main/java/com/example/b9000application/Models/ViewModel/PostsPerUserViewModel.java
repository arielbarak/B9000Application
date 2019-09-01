package com.example.b9000application.Models.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.PostAsyncDao;
import com.example.b9000application.Models.PostRepository;

import java.util.LinkedList;

public class PostsPerUserViewModel extends AndroidViewModel {

    private PostsPerUserLiveData data;

    PostsPerUserViewModel(@NonNull Application application, String userId) {
        super(application);
        data = new PostsPerUserLiveData(userId);
    }
    class PostsPerUserLiveData extends MutableLiveData<LinkedList<Post>>{
        private  String userId;
        @Override
        protected void onActive() {
            super.onActive();
            PostRepository.instance.activatePostPerUserFirebaseListener(userId,new PostRepository.GetAllPostsListener() {
                @Override
                public void onResponse(LinkedList<Post> list) {
                    Log.d("GetPostsPerUser","FB data = " + list.size() );
                    setValue(list);

                }
                public void onError()
                {
                    PostRepository.instance.getPostsPerUserDao(userId,new PostRepository.GetAllPostsListener() {
                        @Override
                        public void onResponse(LinkedList<Post> list) {
                            setValue(list);
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
            });

        }
        @Override
        protected void onInactive() {
            super.onInactive();
            PostRepository.instance.disActivatePostsPerUserListener();
            Log.d("TAG","cancellGetPostsPerUser");
        }
        public PostsPerUserLiveData(String userId) {
            super();
            this.userId = userId;
            PostRepository.instance.getPostsPerUserDao(userId,new PostRepository.GetAllPostsListener() {
                @Override
                public void onResponse(LinkedList<Post> list) {
                    setValue(list);
                }

                @Override
                public void onError() {

                }
            });
        }
    }
    public void refresh()
    {
        this.data.onActive();
    }
    public LiveData<LinkedList<Post>> getPosts()
    {
       return data;
    }
}
