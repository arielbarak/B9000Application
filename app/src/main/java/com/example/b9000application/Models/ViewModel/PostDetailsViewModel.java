package com.example.b9000application.Models.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.PostAsyncDao;
import com.example.b9000application.Models.PostRepository;
import com.example.b9000application.Models.UserAsyncDao;

import java.util.List;

public class PostDetailsViewModel extends AndroidViewModel {

    private PostDetailLiveData data;
    PostDetailsViewModel(@NonNull Application application, String postKey) {
        super(application);
        data = new PostDetailLiveData(application,postKey);
    }
    class PostDetailLiveData extends MutableLiveData<Post> {

        private final String postKey;
        PostDetailLiveData(Application application, final String postKey) {
            this.postKey = postKey;
            if (this.postKey!=null) {
                PostRepository.instance.getPostDao(postKey, new PostRepository.GetPostListener() {
                    @Override
                    public void onResponse(Post p) {
                        Log.d("TAG", "post received from room" + postKey);
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
            if(this.postKey!=null){
                PostRepository.instance.getPostFirebase(this.postKey, new PostRepository.GetPostListener() {
                    @Override
                    public void onResponse(Post p) {
                        Log.d("TAG","post received from firebase" + postKey );
                        setValue(p);
                        PostAsyncDao.insertPost(p);
                    }

                    @Override
                    public void onError() {
                        PostAsyncDao.getPost(postKey, new PostRepository.GetPostListener() {
                            @Override
                            public void onResponse(Post p) {
                                Log.d("TAG","post received from room" + postKey );
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
            Log.d("TAG","cancellGetPost");
        }
    }
    public void refresh()
    {
        this.data.onActive();
    }
    public LiveData<Post> getPost()
    {
       return data;
    }
}
