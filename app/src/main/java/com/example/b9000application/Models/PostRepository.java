package com.example.b9000application.Models;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.b9000application.Models.Entities.Post;

import java.util.LinkedList;
import java.util.List;

public class PostRepository {
    final public static PostRepository instance = new PostRepository();
    private PostDao mPostDao;
    ModelFirebasePost modelFirebase = ModelFirebasePost.instance;
    private PostListLiveData postListLiveData = new PostListLiveData();
    public interface ExistPostListener {
        void onExist();
        void onNotExist();
        void onOffline();
        void onError(Exception e);
    }
    public void ifPostExists(String postKey, ExistPostListener listener) {
        modelFirebase.ifPostExists(postKey, listener);
    }

    public LiveData<LinkedList<Post>> getAllPosts()
    {
        return postListLiveData;
    }

    private PostRepository() {
        AppLocalDbRepository db = ModelSql.db;

    }
    class PostListLiveData extends MutableLiveData<LinkedList<Post>> {
        @Override
        protected void onActive() {
            super.onActive();
            modelFirebase.GetAllPosts(new PostRepository.GetAllPostsListener() {
                @Override
                public void onResponse(LinkedList<Post> list) {
                    Log.d("TAG","FB data = " + list.size() );

                    setValue(list);
                    PostAsyncDao.deleteAll();
                    PostAsyncDao.insertPosts(list);

                }
                public void onError()
                {
                    getAllPostsDao(new PostRepository.GetAllPostsListener() {
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
            Log.d("TAG","cancellGetAllStudents");
        }
        public PostListLiveData() {
            super();
            getAllPostsDao(new PostRepository.GetAllPostsListener() {
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

    public void updatePost (Post p, InsertPostListener listener)
    {
        modelFirebase.updatePost(p,listener);
    }
    public interface GetPostListener{
        void onResponse(Post p);
        void onError();
    }
    public interface GetAllPostsListener{
        void onResponse(LinkedList<Post> list);
        void onError();
    }

    public void getPostFirebase(String postKey, GetPostListener listener)
    {
        modelFirebase.getPost(postKey , listener);
    }
    public void getPostDao(String postKey, GetPostListener listener)
    {
        PostAsyncDao.getPost(postKey, listener);
    }



    public void getAllPostsDao(GetAllPostsListener listener)
    {
        PostAsyncDao.getAllPosts(listener);
    }

    public void activatePostPerUserFirebaseListener(String userId, GetAllPostsListener listener)
    {
        modelFirebase.activatePostsPerUserListener(userId,listener);
    }

    public void disActivatePostsPerUserListener()
    {
        modelFirebase.removePostsPerUserListener();
    }
    public void getPostsPerUserDao(String userId, GetAllPostsListener listener)
    {
        PostAsyncDao.getPostsPerUser(userId, listener);
    }


    public interface InsertPostListener{
        void onComplete(boolean success);
        void onError(Exception e);
        void onOffline();
    }
    public void insert (final Post post, final InsertPostListener listener) {
       modelFirebase.addPost(post, new InsertPostListener() {
           @Override
           public void onComplete(boolean success) {
               PostAsyncDao.insertPost(post);
               listener.onComplete(success);
           }

           @Override
           public void onError(Exception e) {
listener.onError(e);
           }

           @Override
           public void onOffline() {
listener.onOffline();
           }
       });

    }
    public interface SaveImageListener{
        void onComplete(String uri);
        void onOffline();
        void onError(Exception e);
    }
    public void saveBlogImage(Uri imageBitmap, SaveImageListener listener) {
        modelFirebase.saveBlogImage(imageBitmap, listener);
    }

}

