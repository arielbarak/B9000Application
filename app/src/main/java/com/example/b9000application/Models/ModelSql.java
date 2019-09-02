package com.example.b9000application.Models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.b9000application.Adapters.MyApplication;
import com.example.b9000application.Models.Entities.Comment;
import com.example.b9000application.Models.Entities.Converters;
import com.example.b9000application.Models.Entities.Post;
import com.example.b9000application.Models.Entities.User;

import java.util.ArrayList;
import java.util.List;


@Database(entities = {Post.class, User.class, Comment.class}, version = 31, exportSchema = false)
@TypeConverters({Converters.class})
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract UserDao userDao();
    public abstract CommentDao commentDao();
    private static volatile AppLocalDbRepository INSTANCE;

    static AppLocalDbRepository getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppLocalDbRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(MyApplication.context,
                            AppLocalDbRepository.class, "database.db")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                }
            };


}

public class ModelSql {
        static AppLocalDbRepository db = AppLocalDbRepository.getDatabase(MyApplication.getContext());


}

