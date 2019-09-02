package com.example.b9000application.Models;

import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.Entities.PostAllComments;
import com.example.b9000application.Models.ViewModel.PostsPerUserViewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Dao
interface
PostDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Query("DELETE FROM post_table")
    void deleteAll();

    @Query("DELETE FROM POST_TABLE WHERE id = :postKey ")
    void deletePost(int postKey);

    @Query("SELECT * FROM POST_TABLE WHERE deleted = 0 order by timestamp DESC")
    List<Post> getAllPosts();

    @Query("SELECT * FROM POST_TABLE WHERE post_table.id = :postKey ")
    Post getPost(String postKey);

    @Query("SELECT * FROM POST_TABLE WHERE id = :postKey")
    @Transaction
    PostAllComments getPostAllComments(String postKey);

    @Query("SELECT * FROM POST_TABLE WHERE deleted = 0 and userId = :userId order by timestamp DESC")
    List<Post>  getPostsPerUser(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPosts(List<Post> posts);


}

public class PostAsyncDao{

    public static void getAllPosts(final PostRepository.GetAllPostsListener listener) {
        new AsyncTask<String,String,LinkedList<Post>>(){

            @Override
            protected LinkedList<Post> doInBackground(String... strings) {
                LinkedList<Post> list = new LinkedList<Post>(ModelSql.db.postDao().getAllPosts());
               return list;
            }

            @Override
            protected void onPostExecute(LinkedList<Post> data) {
                super.onPostExecute(data);
                if (data!=null)
                    listener.onResponse(data);

            }
        }.execute();

    }

    public static void getPostsPerUser(final String userId, final PostRepository.GetAllPostsListener listener) {
        new AsyncTask<String,String,LinkedList<Post>>(){

            @Override
            protected LinkedList<Post> doInBackground(String... strings) {
                LinkedList<Post> list = new LinkedList<Post>(ModelSql.db.postDao().getPostsPerUser(userId));
                return list;
            }

            @Override
            protected void onPostExecute(LinkedList<Post> data) {
                super.onPostExecute(data);
                if (data!=null)
                    listener.onResponse(data);

            }
        }.execute();

    }
    public static void insertPosts(final List<Post> posts)
    {
        new AsyncTask<String,String,Integer>(){

            @Override
            protected Integer doInBackground(String... strings) {
                ModelSql.db.postDao().insertPosts(posts);
                return posts.size();
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                Log.d("Insert posts", "Insert posts "+integer);
            }
        }.execute();
    }
    public static void insertPost(final Post post)
    {
       new AsyncTask<Void, Void, Void>() {

           @Override
           protected Void doInBackground(Void... voids) {
               ModelSql.db.postDao().insert(post);
               Log.d("SQL", "Insert post to sql");
               return null;
           }


        }.execute();
    }

    public static void deleteAll()
    {
        new AsyncTask<String,String, String>(){

            @Override
            protected String doInBackground(String... strings) {
                ModelSql.db.postDao().deleteAll();
                Log.d("SQL", "Delete all the db");
                return null;
            }


        }.execute();
    }
    public static void getPost(final String postKey, final  PostRepository.GetPostListener listener)
    {
        new AsyncTask<String, String, Post>(){

            @Override
            protected Post doInBackground(String... strings) {
                return ModelSql.db.postDao().getPost(postKey);
            }

            @Override
            protected void onPostExecute(Post post) {
                super.onPostExecute(post);
                if (post!=null)
                {
                    Log.d("SQL", "Get post id "+postKey);
                    listener.onResponse(post);
                }
                else
                {
                    listener.onError();
                }

            }
        }.execute();
    }
}