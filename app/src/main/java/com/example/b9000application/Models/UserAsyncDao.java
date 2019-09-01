package com.example.b9000application.Models;

import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.b9000application.Models.Entities.User;

import java.util.List;

@Dao
interface UserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("DELETE FROM user_table WHERE id = :userKey ")
    void deleteUser(int userKey);

    @Query("SELECT * FROM user_table")
    List<User> getAllUsers();

    @Query("SELECT * FROM user_table WHERE id = :userKey")
    User getUser(String userKey);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(List<User> users);


}

public class UserAsyncDao{

    public static void getAllUsers(final UserRepository.GetAllUsersListener listener) {
        new AsyncTask<String,String,List<User>>(){

            @Override
            protected List<User> doInBackground(String... strings) {
               List<User> list = ModelSql.db.userDao().getAllUsers();
               return list;
            }

            @Override
            protected void onPostExecute(List<User> data) {
                super.onPostExecute(data);
                listener.onResponse(data);

            }
        }.execute();

    }
    public static void insertUsers(final List<User> users)
    {
        new AsyncTask<String,String,Integer>(){

            @Override
            protected Integer doInBackground(String... strings) {
                ModelSql.db.userDao().insertUsers(users);
                Log.d("SQL", "Insert user to sql" + users.size());
                return users.size();
            }


        }.execute();
    }
    public static void insertUser(final User user)
    {
       new AsyncTask<Void, Void, Void>() {

           @Override
           protected Void doInBackground(Void... voids) {
               ModelSql.db.userDao().insert(user);
               Log.d("SQL", "Insert user to sql");
               return null;
           }


        }.execute();
    }

    public static void deleteAll()
    {
        new AsyncTask<String,String, String>(){

            @Override
            protected String doInBackground(String... strings) {
                ModelSql.db.userDao().deleteAll();
                Log.d("SQL", "Delete all users at the db");
                return null;
            }


        }.execute();
    }
    public static void getUser(final String userKey, final  UserRepository.GetUserListener listener)
    {
        new AsyncTask<String, String, User>(){

            @Override
            protected User doInBackground(String... strings) {
                return ModelSql.db.userDao().getUser(userKey);

            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                Log.d("SQL", "Get post id "+userKey);
                listener.onResponse(user);

            }
        }.execute();
    }
}