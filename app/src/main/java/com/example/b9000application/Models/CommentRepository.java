package com.example.b9000application.Models;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.b9000application.Models.Entities.Comment;

import java.util.List;

public class CommentRepository {
    final public static CommentRepository instance = new CommentRepository();
    private CommentDao mCommentDao;
    private ModelFirebaseComment modelFirebase = ModelFirebaseComment.instance;

    private CommentRepository() {
        AppLocalDbRepository db = ModelSql.db;

    }

    public void remove(Comment c, CommentRepository.RemoveCommentListener listener) {
        modelFirebase.removeComment(c.getCommentKey(),c.getPostId(), listener);
    }

    public void insertback(Comment c, InsertCommentListener listener) {
        modelFirebase.addbackComment(c,listener);
    }


    public interface GetCommentListener{
        void onResponse(Comment p);
        void onError();
    }
    public interface GetAllCommentsListener{
        void onResponse(List<Comment> list);
        void onError();
    }

    public void getAllCommentsFirebase(String postKey, GetAllCommentsListener listener)
    {
        modelFirebase.GetAllComments(listener,postKey);
    }
    public void getAllCommentsDao(String postKey, GetAllCommentsListener listener)
    {
        CommentAsyncDao.getAllComments(postKey,listener);
    }


    public interface InsertCommentListener{
        void onComplete(boolean success);
        void onError(Exception e);
        void onOffline();
    }
    public void insert (final Comment comment, final InsertCommentListener listener) {
        modelFirebase.addComment(comment, new InsertCommentListener() {
            @Override
            public void onComplete(boolean success) {
                CommentAsyncDao.insertComment(comment);
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

    public interface RemoveCommentListener {
        void onRemove();
        void onError(Exception x);
        void onOffline();
    }
}

