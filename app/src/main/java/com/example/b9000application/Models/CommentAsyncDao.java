package com.example.b9000application.Models;

import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.b9000application.Models.Entities.Comment;

import java.util.List;

@Dao
interface CommentDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment comment);

    @Query("DELETE FROM comment_table WHERE postId = :postKey")
    void deleteAll(String postKey);

    @Query("DELETE FROM COMMENT_TABLE WHERE commentKey = :commentKey ")
    void deleteComment(int commentKey);

    @Query("SELECT * FROM COMMENT_TABLE WHERE postId = :postKey ORDER BY timestamp DESC")
    List<Comment> getAllComments(String postKey);

    @Query("SELECT * FROM COMMENT_TABLE WHERE comment_table.commentKey = :commentKey ")
    Comment getComment(String commentKey);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComments(List<Comment> comments);


}

public class CommentAsyncDao{

    public static void getAllComments(final String postKey, final CommentRepository.GetAllCommentsListener listener) {
        new AsyncTask<String,String,List<Comment>>(){

            @Override
            protected List<Comment> doInBackground(String... strings) {
                return ModelSql.db.commentDao().getAllComments(postKey);
            }

            @Override
            protected void onPostExecute(List<Comment> data) {
                super.onPostExecute(data);
                if (data!=null)
                    listener.onResponse(data);

            }
        }.execute();

    }
    public static void insertComments(final List<Comment> posts)
    {
        new AsyncTask<String,String,Integer>(){

            @Override
            protected Integer doInBackground(String... strings) {
                ModelSql.db.commentDao().insertComments(posts);
                return posts.size();
            }


        }.execute();
    }
    public static void insertComment(final Comment comment)
    {
       new AsyncTask<Void, Void, Void>() {

           @Override
           protected Void doInBackground(Void... voids) {
               ModelSql.db.commentDao().insert(comment);
               Log.d("SQL", "Insert comment to sql");
               return null;
           }


        }.execute();
    }

    public static void deleteAll(final String postKey)
    {
        new AsyncTask<String,String, String>(){

            @Override
            protected String doInBackground(String... strings) {
                ModelSql.db.commentDao().deleteAll(postKey);
                Log.d("SQL", "Delete all the comments of post "+ postKey + " from the db");
                return null;
            }


        }.execute();
    }
    public static void getComment(final String commentKey, final  CommentRepository.GetCommentListener listener)
    {
        new AsyncTask<String, String, Comment>(){

            @Override
            protected Comment doInBackground(String... strings) {
                return ModelSql.db.commentDao().getComment(commentKey);
            }

            @Override
            protected void onPostExecute(Comment comment) {
                super.onPostExecute(comment);
                if (comment!=null)
                {
                    Log.d("SQL", "Get comment commentKey "+commentKey);
                    listener.onResponse(comment);
                }
                else
                {
                    listener.onError();
                }

            }
        }.execute();
    }
}