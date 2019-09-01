package com.example.b9000application.Models.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.PostAsyncDao;
import com.example.b9000application.Models.PostRepository;

import java.util.LinkedList;
import java.util.List;

public class PostListViewModel extends AndroidViewModel {


    private LiveData<LinkedList<Post>> data;

    public PostListViewModel(Application application) {
        super(application);
        data = PostRepository.instance.getAllPosts();
    }

    public LiveData<LinkedList<Post>> getAllPosts() { return data;}


}