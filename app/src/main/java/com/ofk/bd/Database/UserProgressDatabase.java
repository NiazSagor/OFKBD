package com.ofk.bd.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ofk.bd.Dao.UserProgressDao;
import com.ofk.bd.HelperClass.UserProgressClass;

@Database(entities = UserProgressClass.class, version = 1)
public abstract class UserProgressDatabase extends RoomDatabase {
    private static final String TAG = "UserProgressDatabase";
    private static UserProgressDatabase instance;

    public abstract UserProgressDao userProgressDao();

    public static synchronized UserProgressDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    UserProgressDatabase.class, "user_progress")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
