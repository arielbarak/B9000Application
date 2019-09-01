package com.example.b9000application.Models.ViewModel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;

import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.PostRepository;

public class PostInsertViewModel extends AndroidViewModel {
    public PostInsertViewModel(Application application) {
        super(application);
    }
    public void insert(Post post, PostRepository.InsertPostListener listener) { PostRepository.instance.insert(post,listener); }

    public void saveBlogImage(Uri imageBitmap, PostRepository.SaveImageListener listener) {
        PostRepository.instance.saveBlogImage(imageBitmap,listener);
    }
}
